package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SimpleOrderQueryDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public SimpleOrderQueryDto(Order order) {
        orderId = order.getId();
        orderDate = order.getOrderDate();
        name = order.getMember().getName(); //Lazy 초기화
        address = order.getDelivery().getAddress(); //Lazy 초기화
        orderStatus = order.getStatus();
    }


    public SimpleOrderQueryDto(Long id, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
        this.orderId = id;
        this.orderDate = orderDate;
        this.name = name;
        this.address = address;
        this.orderStatus = orderStatus;
    }

}
