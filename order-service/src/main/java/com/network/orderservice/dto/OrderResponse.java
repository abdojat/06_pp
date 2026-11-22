
package com.network.orderservice.dto;

public class OrderResponse {

    private Long orderId;
    private Long userId;
    private String userName;
    private String productName;
    private Integer quantity;
    private String status;

    public OrderResponse() {
    }

    public OrderResponse(
            Long orderId,
            Long userId,
            String userName,
            String productName,
            Integer quantity,
            String status) {

        this.orderId = orderId;
        this.userId = userId;
        this.userName = userName;
        this.productName = productName;
        this.quantity = quantity;
        this.status = status;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
