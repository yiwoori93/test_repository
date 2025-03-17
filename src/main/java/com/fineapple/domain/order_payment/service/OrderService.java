package com.fineapple.domain.order_payment.service;


import com.fineapple.Infrastructure.common.CommonCodeService;
import com.fineapple.domain.logistics_inventory.repository.StockMapper;
import com.fineapple.domain.order_payment.dto.OrderInfoDto;
import com.fineapple.domain.order_payment.dto.OrderItemRequest;
import com.fineapple.domain.order_payment.entity.OrderItemDetail;
import com.fineapple.domain.order_payment.entity.Orders;
import com.fineapple.domain.order_payment.entity.Payment;
import com.fineapple.domain.order_payment.repository.OrderItemDetailMapper;
import com.fineapple.domain.order_payment.repository.OrdersMapper;
import com.fineapple.domain.order_payment.repository.PaymentMapper;
import com.fineapple.domain.user.repository.CartMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrdersMapper ordersMapper;
    private final StockMapper stockMapper;
    private final OrderItemDetailMapper orderItemDetailMapper;
    private final PaymentMapper paymentMapper;
    private final CartMapper cartMapper;
    private final CommonCodeService commonCodeService;


    /**
     * 주문 단건 조회 (주문 ID 기반)
     * @param id 주문ID
     */
    public OrderInfoDto selectOrderById(Long id) {
        return toOrderInfoDto(ordersMapper.selectOrderById(id));
    }

    /**
     * 장바구니 정보 기반 주문 생성
     * @param userId          사용자 ID
     * @param orderItems      주문할 상품 목록(상품ID, 수량, 가격)
     */
    @Transactional
    public Long createOrder(Long userId,
                            List<OrderItemRequest> orderItems) {
        // 1) 재고 확인
        for (OrderItemRequest item : orderItems) {
            Integer stock = stockMapper.selectStockByProductId(item.productId());
            if (stock == null || stock < item.quantity()) {
                throw new RuntimeException("재고 부족으로 주문을 진행할 수 없습니다. productId=" + item.productId());
            }
        }

        // 2) Order 생성 (주문 상태 = 주문접수)
        Orders orders = Orders.builder()
                .userId(userId)
                .orderStatus(commonCodeService.getCommonCode("ORDER_PLACED").getName())
                .createdAt(LocalDateTime.now())
                .build();
        ordersMapper.insertOrder(orders);

        //    OrderItemDetail 생성
        //    장바구니에서 가져온 각 아이템을 주문 상세로 변환
        List<OrderItemDetail> orderItemDetails = new ArrayList<>();
        for (OrderItemRequest item : orderItems) {
            // 재고 차감
            stockMapper.decreaseStock(item.productId(), item.quantity());
            OrderItemDetail detail = OrderItemDetail.builder()
                    .orderId(orders.getOrderId())
                    .productId(item.productId())
                    .quantity(item.quantity())
                    .price(BigDecimal.valueOf(item.price()))
                    .itemStatus(commonCodeService.getCommonCode("ORDER_PLACED").getDescription())
                    .build();
            orderItemDetails.add(detail);

        }
        orderItemDetailMapper.insertOrderItemDetailsList(orderItemDetails);

        //    Payment 생성 (결제 대기 상태)
        //    금액은 간단히 모든 상품 가격 * 수량 합산 값으로 계산, 실제론 쿠폰/할인 적용여부 고민중..
        //    장바구니 삭제
        BigDecimal totalAmount = BigDecimal.valueOf(orderItems.stream()
                .mapToInt(i -> i.price() * i.quantity())
                .sum());

        Payment payment = Payment.builder()
                .orderId(orders.getOrderId())
                .totalAmount(totalAmount)
                .paymentMethod(commonCodeService.getCommonCode("CARD").getDescription())
                .paymentStatus(commonCodeService.getCommonCode("PAYMENT_PENDING").getDescription())
                .paidAt(LocalDateTime.now())
                .build();
        paymentMapper.insertPayment(payment);

        cartMapper.deleteCartByUserId(userId);

        return orders.getOrderId();
    }

    public OrderInfoDto toOrderInfoDto(Orders orders) {
        return new OrderInfoDto(orders.getOrderId(), orders.getOrderCode(), orders.getTotalPrice(),
                orders.getDiscountPrice(), orders.getOrderStatus(), orders.getPaymentMethod());
    }


}
