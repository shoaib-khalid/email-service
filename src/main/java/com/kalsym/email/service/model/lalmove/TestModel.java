/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kalsym.email.service.model.lalmove;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author saros
 */
@Getter
@Setter
@ToString
public class TestModel {

    public String serviceType;
    public List<Object> specialRequests;
    public List<Stop> stops;
    public RequesterContact requesterContact;
    public List<Delivery> deliveries;
}
