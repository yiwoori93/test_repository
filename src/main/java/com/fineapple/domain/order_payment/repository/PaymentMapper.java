package com.fineapple.domain.order_payment.repository;


import com.fineapple.domain.order_payment.entity.Payment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper {

    void insertPayment(Payment payment);

    Payment selectPaymentById(Long paymentId);

    void updatePayment(Payment payment);

    void deletePayment(Long paymentId);
}
