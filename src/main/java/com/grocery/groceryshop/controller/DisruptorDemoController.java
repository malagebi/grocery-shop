package com.grocery.groceryshop.controller;

import com.grocery.groceryshop.base.CommonResult;
import com.grocery.groceryshop.disruptor.OrderEventBus;
import com.grocery.groceryshop.disruptor.event.OrderEventType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Tag(name = "Disruptor 演示")
@RestController
@RequestMapping("/disruptor")
@RequiredArgsConstructor
public class DisruptorDemoController {

    private final OrderEventBus orderEventBus;

    @Operation(summary ="查看各类订单事件的累计统计")
    @GetMapping("/stats")
    public CommonResult<Map<String, Long>> stats() {
        Map<String, Long> result = new LinkedHashMap<>();
        orderEventBus.getStatHandler().getCounters()
                .forEach((type, counter) -> result.put(type.getDesc(), counter.get()));
        return CommonResult.success(result);
    }

    @Operation(summary ="手动发布测试事件（压测 / 演示用）")
    @PostMapping("/test-event")
    public CommonResult<String> testEvent(
            @Parameter(description ="订单号") @RequestParam(defaultValue = "TEST-001") String orderNo,
            @Parameter(description ="用户ID") @RequestParam(defaultValue = "1") Long userId,
            @Parameter(description ="事件类型: CREATE PAY CANCEL COMPLETE REFUND") @RequestParam(defaultValue = "CREATE") String eventType,
            @Parameter(description ="金额") @RequestParam(defaultValue = "99.00") BigDecimal amount,
            @Parameter(description ="发布次数") @RequestParam(defaultValue = "1") int times) {

        OrderEventType type;
        try {
            type = OrderEventType.valueOf(eventType.toUpperCase());
        } catch (IllegalArgumentException e) {
            return CommonResult.error("非法事件类型，可选: CREATE PAY CANCEL COMPLETE REFUND");
        }

        for (int i = 0; i < times; i++) {
            orderEventBus.publish(orderNo + "-" + i, userId, type, amount);
        }
        return CommonResult.success("已发布 " + times + " 条 [" + type.getDesc() + "] 事件");
    }

    @Operation(summary ="并发压测：多线程同时向 RingBuffer 发布事件")
    @PostMapping("/benchmark")
    public CommonResult<String> benchmark(
            @Parameter(description ="线程数") @RequestParam(defaultValue = "4") int threads,
            @Parameter(description ="每线程发布次数") @RequestParam(defaultValue = "1000") int perThread) throws InterruptedException {

        long start = System.currentTimeMillis();
        AtomicLong total = new AtomicLong(0);
        Thread[] workers = new Thread[threads];

        for (int t = 0; t < threads; t++) {
            final int tid = t;
            workers[t] = new Thread(() -> {
                for (int i = 0; i < perThread; i++) {
                    orderEventBus.publish("BENCH-" + tid + "-" + i, (long) tid,
                            OrderEventType.CREATE, BigDecimal.TEN);
                    total.incrementAndGet();
                }
            }, "bench-producer-" + t);
        }

        for (Thread w : workers) w.start();
        for (Thread w : workers) w.join();

        long elapsed = System.currentTimeMillis() - start;
        long tps = elapsed > 0 ? total.get() * 1000 / elapsed : total.get();
        return CommonResult.success(
                String.format("共发布 %d 条事件，耗时 %d ms，吞吐量约 %d 事件/秒", total.get(), elapsed, tps));
    }
}
