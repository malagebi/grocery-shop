package com.grocery.groceryshop.service.impl;

import com.grocery.groceryshop.base.CommonPageInfo;
import com.grocery.groceryshop.base.CustomerException;
import com.grocery.groceryshop.base.PaymentErrorCode;
import com.grocery.groceryshop.base.req.PayReq;
import com.grocery.groceryshop.base.req.PaymentListReq;
import com.grocery.groceryshop.entity.PaymentRecord;
import com.grocery.groceryshop.enums.OrderStatus;
import com.grocery.groceryshop.enums.PayMethod;
import com.grocery.groceryshop.enums.PaymentStatus;
import com.grocery.groceryshop.service.OrderService;
import com.grocery.groceryshop.service.PaymentService;
import com.grocery.groceryshop.vo.OrderVO;
import com.grocery.groceryshop.vo.PaymentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Resource
    private OrderService orderService;

    private final Map<String, PaymentRecord> paymentStore = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1L);

    private static final Random RANDOM = new Random();
    /** 专用线程池，隔离网关回调模拟，避免阻塞业务线程 */
    private static final ExecutorService GATEWAY_EXECUTOR =
            Executors.newCachedThreadPool(r -> {
                Thread t = new Thread(r, "gateway-callback");
                t.setDaemon(true);
                return t;
            });

    @Override
    public PaymentVO pay(PayReq req) {
        OrderVO order = orderService.findByOrderNo(req.getOrderNo());
        if (order == null) {
            throw new CustomerException(PaymentErrorCode.ORDER_NOT_FOUND);
        }
        if (!OrderStatus.UNPAID.getCode().equals(order.getStatus())) {
            throw new CustomerException(PaymentErrorCode.ORDER_NOT_UNPAID);
        }

        Long id = idGenerator.getAndIncrement();
        String paymentNo = "PAY" + System.currentTimeMillis() + id;
        Date now = new Date();

        PaymentRecord record = new PaymentRecord();
        record.setId(id);
        record.setPaymentNo(paymentNo);
        record.setOrderNo(req.getOrderNo());
        record.setUserId(order.getUserId());
        record.setAmount(order.getTotalAmount());
        record.setPayMethod(req.getPayMethod());
        record.setStatus(PaymentStatus.PROCESSING.getCode());
        record.setCreateTime(now);
        record.setUpdateTime(now);
        paymentStore.put(paymentNo, record);

        log.info("[发起支付] paymentNo={}, orderNo={}, amount={}, method={}",
                paymentNo, req.getOrderNo(), record.getAmount(), PayMethod.descOf(req.getPayMethod()));

        simulateGatewayCallback(paymentNo, req.getOrderNo());
        return toVO(record);
    }

    /**
     * 模拟支付网关的异步回调：延迟 1-3 秒后以 90% 概率通知支付成功。
     * 真实场景中，这里对应网关主动 POST 到商户的 notify_url。
     */
    private void simulateGatewayCallback(String paymentNo, String orderNo) {
        CompletableFuture.runAsync(() -> {
            try {
                int delayMs = 1000 + RANDOM.nextInt(2000);
                Thread.sleep(delayMs);

                PaymentRecord record = paymentStore.get(paymentNo);
                if (record == null) return;

                boolean success = RANDOM.nextInt(10) < 9;
                record.setUpdateTime(new Date());

                if (success) {
                    record.setStatus(PaymentStatus.SUCCESS.getCode());
                    record.setGatewayTradeNo("GW" + System.currentTimeMillis());
                    orderService.markOrderPaid(orderNo);
                    log.info("[网关回调] paymentNo={} 支付成功，耗时约 {}ms", paymentNo, delayMs);
                } else {
                    record.setStatus(PaymentStatus.FAILED.getCode());
                    log.info("[网关回调] paymentNo={} 支付失败（模拟），耗时约 {}ms", paymentNo, delayMs);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("[网关回调] 线程中断, paymentNo={}", paymentNo);
            }
        }, GATEWAY_EXECUTOR);
    }

    @Override
    public PaymentVO refund(String paymentNo) {
        PaymentRecord record = paymentStore.get(paymentNo);
        if (record == null) {
            throw new CustomerException(PaymentErrorCode.PAYMENT_NOT_FOUND);
        }
        if (PaymentStatus.REFUNDED.getCode().equals(record.getStatus())) {
            throw new CustomerException(PaymentErrorCode.ALREADY_REFUNDED);
        }
        if (!PaymentStatus.SUCCESS.getCode().equals(record.getStatus())) {
            throw new CustomerException(PaymentErrorCode.PAYMENT_NOT_SUCCESS);
        }

        OrderVO order = orderService.findByOrderNo(record.getOrderNo());
        if (order != null) {
            Integer status = order.getStatus();
            if (OrderStatus.SHIPPED.getCode().equals(status) || OrderStatus.COMPLETED.getCode().equals(status)) {
                throw new CustomerException(PaymentErrorCode.ORDER_STATUS_NOT_REFUNDABLE);
            }
        }

        record.setStatus(PaymentStatus.REFUNDED.getCode());
        record.setUpdateTime(new Date());
        orderService.markOrderRefunded(record.getOrderNo());

        log.info("[申请退款] paymentNo={}, orderNo={}", paymentNo, record.getOrderNo());
        return toVO(record);
    }

    @Override
    public PaymentVO getByPaymentNo(String paymentNo) {
        PaymentRecord record = paymentStore.get(paymentNo);
        if (record == null) {
            throw new CustomerException(PaymentErrorCode.PAYMENT_NOT_FOUND);
        }
        return toVO(record);
    }

    @Override
    public CommonPageInfo<PaymentVO> listPayments(PaymentListReq req) {
        int page = req.getPageNum() == null || req.getPageNum() < 1 ? 1 : req.getPageNum();
        int size = req.getPageSize() == null || req.getPageSize() < 1 ? 10 : req.getPageSize();

        List<PaymentRecord> filtered = paymentStore.values().stream()
                .filter(r -> req.getUserId() == null || Objects.equals(r.getUserId(), req.getUserId()))
                .filter(r -> req.getStatus() == null || Objects.equals(r.getStatus(), req.getStatus()))
                .sorted(Comparator.comparing(PaymentRecord::getCreateTime).reversed())
                .collect(Collectors.toList());

        int total = filtered.size();
        int fromIndex = Math.min((page - 1) * size, total);
        int toIndex = Math.min(fromIndex + size, total);
        List<PaymentVO> pageList = filtered.subList(fromIndex, toIndex).stream()
                .map(this::toVO)
                .collect(Collectors.toList());

        CommonPageInfo<PaymentVO> result = new CommonPageInfo<>();
        result.setPageNum(page);
        result.setPageSize(size);
        result.setTotal(total);
        result.setPages((int) Math.ceil((double) total / size));
        result.setList(pageList);
        return result;
    }

    private PaymentVO toVO(PaymentRecord record) {
        PaymentVO vo = new PaymentVO();
        BeanUtils.copyProperties(record, vo);
        vo.setPayMethodDesc(PayMethod.descOf(record.getPayMethod()));
        vo.setStatusDesc(PaymentStatus.descOf(record.getStatus()));
        return vo;
    }
}
