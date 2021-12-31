/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kalsym.email.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.kalsym.email.service.model.order.*;
import java.util.List;

/**
 *
 * @author saros
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class OrderEmailBodyContent {

    public enum OrderStatus {
        DELIVERED_TO_CUSTOMER("Order Delivered"),
        PAYMENT_CONFIRMED("Payment Confirmed"),
        BEING_DELIVERED("Order on its way!"),
        CANCELED_BY_CUSTOMER("Order cancelation confirmed"),
        CANCELED_BY_MERCHANT("Order cancel by merchant"),
        READY_FOR_DELIVERY("Order is processed and awating delivery"),
        RECEIVED_AT_STORE("Order received at store"),
        REFUNDED("Refund confirmation"),
        REQUESTING_DELIVERY_FAILED("Dlivery failed"),
        AWAITING_PICKUP("Order is awaiting pickup"),
        BEING_PREPARED("Order is being processed"),
        REJECTED_BY_STORE("Order was not accepted by store"),
        FAILED("Order palcement failed");

        public final String label;

        private OrderStatus(String label) {
            this.label = label;
        }
    }
    private String title;
    private String logoUrl;
    private String customerTrackingUrl;
    private String merchantTrackingUrl;
    private String storeContact;
    private String currency;
    private String storeName;
    private String storeAddress;

    private OrderStatus orderStatus;

    private String receiverName;
    private String deliveryAddress;
    private String deliveryCity;

    private List<OrderItem> orderItems;

    private String invoiceId;
    private String deliveryCharges;
    private String total;
}
