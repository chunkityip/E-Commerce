package com.example.E_Commerce.service;

import com.example.E_Commerce.dto.OrderRequest;
import com.example.E_Commerce.dto.Response;
import com.example.E_Commerce.enums.OrderStatus;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface OrderItemService {

    Response placeOrder(OrderRequest orderRequest);
    Response updateOrderItemStatus(Long orderItemId , String status);
    Response filterOrderItems(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate, Long itemId, Pageable pageable);

}
