package com.grocery.groceryshop.service;

import com.grocery.groceryshop.base.CommonPageInfo;
import com.grocery.groceryshop.base.req.PayReq;
import com.grocery.groceryshop.base.req.PaymentListReq;
import com.grocery.groceryshop.vo.PaymentVO;

public interface PaymentService {

    /**
     * 发起支付，返回处理中的支付记录。
     * 网关回调通过异步线程模拟，1-3 秒后更新支付和订单状态。
     */
    PaymentVO pay(PayReq req);

    /** 按支付单号申请退款 */
    PaymentVO refund(String paymentNo);

    /** 按支付单号查询支付记录 */
    PaymentVO getByPaymentNo(String paymentNo);

    /** 分页查询支付记录 */
    CommonPageInfo<PaymentVO> listPayments(PaymentListReq req);
}
