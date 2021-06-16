/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kalsym.email.service.util;

import com.kalsym.email.service.EmailServiceApplication;
import com.kalsym.email.service.model.EmailBodyContent;
import com.kalsym.email.service.model.EmailBodyContent.OrderStatus;
import com.kalsym.email.service.model.order.OrderItem;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 *
 * @author saros
 */
public class EmailUtil {

    public static String generateEmail(EmailBodyContent bodyContent, String emailTemplatePath, String symplifiedLogoPath) throws FileNotFoundException {
        String emailContent = "";

        String header = fileContent(emailTemplatePath + "header.html");

        header = header.replace("{{store-logo}}", symplifiedLogoPath);
        header = header.replace("{{title}}", bodyContent.getOrderStatus().label);

        String footer = fileContent(emailTemplatePath + "footer.html");

        if (bodyContent.getOrderStatus() == OrderStatus.BEING_DELIVERED) {
            emailContent = header + generateBeingDeliveredEmail(bodyContent, emailTemplatePath);

        } else if (bodyContent.getOrderStatus() == OrderStatus.PAYMENT_CONFIRMED) {
            emailContent = header + generatePaymentConfirmationEmail(bodyContent, emailTemplatePath);

        }

        return emailContent;
    }

    public static String generatePaymentConfirmationEmail(EmailBodyContent bodyContent, String emailTemplatePath) throws FileNotFoundException {

        String body = fileContent(emailTemplatePath + "payment-confirmation-body.html");

        body = body.replace("{{store-name}}", bodyContent.getStoreName());
        body = body.replace("{{store-address}}", bodyContent.getStoreAddress());
        body = body.replace("{{invoice-number}}", bodyContent.getInvoiceId());
        body = body.replace("{{delivery-charges}}", bodyContent.getDeliveryCharges());
        body = body.replace("{{delivery-address}}", bodyContent.getDeliveryAddress());
        body = body.replace("{{delivery-city}}", bodyContent.getDeliveryCity());

        String orderItem = "                <tr>\n"
                + "                    <td>{{item-name}}</td>\n"
                + "                    <td>{{item-price}}</td>\n"
                + "                    <td>{{item-quantity}}</td>\n"
                + "                    <td>{{item-total}}</td>\n"
                + "                </tr>";

        String itemList = "";
        for (OrderItem oi : bodyContent.getOrderItems()) {
            String item = orderItem;

            item = item.replace("{{item-name}}", oi.getName());
            item = item.replace("{{item-price}}", oi.getPriceSingle());
            item = item.replace("{{item-quantity}}", oi.getQuantity());
            item = item.replace("{{item-total}}", oi.getPriceTotal());

            itemList = itemList + item;
        }
        body = body.replace("{{item-list}}", itemList);

        body = body.replace("{{sub-total}}", bodyContent.getSubTotal());

        return body;
    }

    public static String generateBeingDeliveredEmail(EmailBodyContent bodyContent, String emailTemplatePath) throws FileNotFoundException {

        String body = fileContent(emailTemplatePath + "being-delivered-body.html");

        body = body.replace("{{store-name}}", bodyContent.getStoreName());
        body = body.replace("{{store-address}}", bodyContent.getStoreAddress());
        body = body.replace("{{invoice-number}}", bodyContent.getInvoiceId());
        body = body.replace("{{delivery-charges}}", bodyContent.getDeliveryCharges());
        body = body.replace("{{delivery-address}}", bodyContent.getDeliveryAddress());
        body = body.replace("{{delivery-city}}", bodyContent.getDeliveryCity());

        String orderItem = "                <tr>\n"
                + "                    <td>{{item-name}}</td>\n"
                + "                    <td>{{item-price}}</td>\n"
                + "                    <td>{{item-quantity}}</td>\n"
                + "                    <td>{{item-total}}</td>\n"
                + "                </tr>";

        String itemList = "";
        for (OrderItem oi : bodyContent.getOrderItems()) {
            String item = orderItem;

            item = item.replace("{{item-name}}", oi.getName());
            item = item.replace("{{item-price}}", oi.getPriceSingle());
            item = item.replace("{{item-quantity}}", oi.getQuantity());
            item = item.replace("{{item-total}}", oi.getPriceTotal());

            itemList = itemList + item;
        }
        body = body.replace("{{item-list}}", itemList);

        body = body.replace("{{sub-total}}", bodyContent.getSubTotal());

        return body;
    }

    public static String fileContent(String filePath) {
        String logprefix = "fileContent";
        String content = null;
        try {
            content = Files.lines(Paths.get(filePath))
                    .collect(Collectors.joining(System.lineSeparator()));
            Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix, "file content read successfully", "");

        } catch (IOException e) {
            Logger.application.error(Logger.pattern, EmailServiceApplication.VERSION, logprefix, "error reading file", "", e);
        }

        //Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix, "content: " + content, "");
        return content;
    }

}
