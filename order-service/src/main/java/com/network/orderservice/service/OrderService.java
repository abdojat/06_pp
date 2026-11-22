
package com.network.orderservice.service;

import com.network.orderservice.client.UserGrpcClient;
import com.network.orderservice.dto.OrderRequest;
import com.network.orderservice.dto.OrderResponse;
import com.network.orderservice.entity.Order;
import com.network.orderservice.repository.OrderRepository;
import com.network.userservice.grpc.UserResponse;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserGrpcClient userGrpcClient;

    public OrderService(
            OrderRepository orderRepository,
            UserGrpcClient userGrpcClient) {

        this.orderRepository = orderRepository;
        this.userGrpcClient = userGrpcClient;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {

        // Step 1: Verify user through gRPC
        UserResponse user;

        try {
            user = userGrpcClient.getUser(request.getUserId());

        } catch (StatusRuntimeException e) {

            if (e.getStatus().getCode() == Status.Code.NOT_FOUND) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found");
            }

            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "User service is unavailable",
                    e);
        }

        // Step 2: User must exist
        if (!user.getExists()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User not found");
        }

        // Step 3: Create order only after verification
        Order order = new Order();

        order.setUserId(request.getUserId());
        order.setUserName(user.getUserName());
        order.setProductName(request.getProductName());
        order.setQuantity(request.getQuantity());
        order.setStatus("CREATED");

        // Step 4: Save into H2 database
        Order savedOrder = orderRepository.save(order);

        // Step 5: Build REST response
        return new OrderResponse(
                savedOrder.getOrderId(),
                savedOrder.getUserId(),
                savedOrder.getUserName(),
                savedOrder.getProductName(),
                savedOrder.getQuantity(),
                savedOrder.getStatus()
        );
    }
}
