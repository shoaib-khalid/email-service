/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kalsym.email.service.model.order;

import com.kalsym.email.service.model.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author saros
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class OrderItem {

    private String orderId;
    private String productId;
    private Float price;
    private Float productPrice;
    private Float weight;
    private String SKU;
    private int quantity;
    private String itemCode;
    private String productName;
}
