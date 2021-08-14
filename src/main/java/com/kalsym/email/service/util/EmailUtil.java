package com.kalsym.email.service.util;

import com.kalsym.email.service.EmailServiceApplication;
import com.kalsym.email.service.model.OrderEmailBodyContent;
import com.kalsym.email.service.model.AccountVerificationEmailBody;
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

    public static String generateOrderEmail(OrderEmailBodyContent bodyContent, String emailTemplatePath, String symplifiedLogoPath) throws FileNotFoundException {
        String emailContent = "";

        String header = fileContent(emailTemplatePath + "header.html");

        header = header.replace("{{store-logo}}", symplifiedLogoPath);
        header = header.replace("{{title}}", bodyContent.getOrderStatus().label);

        String footer = fileContent(emailTemplatePath + "footer.html");

        if (null != bodyContent.getOrderStatus()) {
            switch (bodyContent.getOrderStatus()) {
                case BEING_DELIVERED:
                    emailContent = header + generateBeingDeliveredEmail(bodyContent, emailTemplatePath) + footer;
                    break;
                case PAYMENT_CONFIRMED:
                    emailContent = header + generatePaymentConfirmationEmail(bodyContent, emailTemplatePath) + footer;
                    break;
                case REQUESTING_DELIVERY_FAILED:
                    emailContent = header + generateRequestingDeliveryFailedEmail(bodyContent, emailTemplatePath) + footer;
                    break;
                case AWAITING_PICKUP:
                    emailContent = header + generateAwaitingPickupEmail(bodyContent, emailTemplatePath) + footer;
                    break;
                case READY_FOR_DELIVERY:
                    emailContent = header + generateReadyForDeliveryEmail(bodyContent, emailTemplatePath) + footer;
                    break;
                case DELIVERED_TO_CUSTOMER:
                    emailContent = header + generateDeliveredToCustomerEmail(bodyContent, emailTemplatePath) + footer;
                    break;
                default:
                    break;
            }
        }

        return emailContent;
    }

    public static String generatePaymentConfirmationEmail(OrderEmailBodyContent bodyContent, String emailTemplatePath) throws FileNotFoundException {

        String body = fileContent(emailTemplatePath + "payment-confirmation-body.html");

        body = body.replace("{{store-name}}", bodyContent.getStoreName());
        body = body.replace("{{store-address}}", bodyContent.getStoreAddress());
        body = body.replace("{{invoice-number}}", bodyContent.getInvoiceId());

        if (null != bodyContent.getInvoiceId()) {
            body = body.replace("{{delivery-charges}}", bodyContent.getDeliveryCharges());
        } else {
            body = body.replace("{{delivery-charges}}", "N/A");
        }

        if (null != bodyContent.getDeliveryAddress()) {
            body = body.replace("{{delivery-address}}", bodyContent.getDeliveryAddress());
        } else {
            body = body.replace("{{delivery-address}}", "N/A");
        }

        if (null != bodyContent.getDeliveryCity()) {
            body = body.replace("{{delivery-city}}", bodyContent.getDeliveryCity());
        } else {
            body = body.replace("{{delivery-city}}", "N/A");
        }

        String orderItem = "                <tr>\n"
                + "                    <td>{{item-name}}</td>\n"
                + "                    <td>{{item-price}}</td>\n"
                + "                    <td>{{item-quantity}}</td>\n"
                + "                    <td>{{item-total}}</td>\n"
                + "                </tr>";

        String itemList = "";
        for (OrderItem oi : bodyContent.getOrderItems()) {
            String item = orderItem;
            item = item.replace("{{item-name}}", oi.getProductName());
            item = item.replace("{{item-price}}", oi.getProductPrice() + "");
            item = item.replace("{{item-quantity}}", oi.getQuantity() + "");
            item = item.replace("{{item-total}}", oi.getPrice() + "");

            itemList = itemList + item;
        }
        body = body.replace("{{item-list}}", itemList);

        body = body.replace("{{sub-total}}", bodyContent.getTotal());

        return body;
    }

    public static String generateReadyForDeliveryEmail(OrderEmailBodyContent bodyContent, String emailTemplatePath) throws FileNotFoundException {

        String body = fileContent(emailTemplatePath + "ready-for-delivery-body.html");

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
            item = item.replace("{{item-name}}", oi.getProductName());
            item = item.replace("{{item-price}}", oi.getProductPrice() + "");
            item = item.replace("{{item-quantity}}", oi.getQuantity() + "");
            item = item.replace("{{item-total}}", oi.getPrice() + "");

            itemList = itemList + item;
        }
        body = body.replace("{{item-list}}", itemList);

        body = body.replace("{{sub-total}}", bodyContent.getTotal());

        return body;
    }

    public static String generateBeingDeliveredEmail(OrderEmailBodyContent bodyContent, String emailTemplatePath) throws FileNotFoundException {

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

            item = item.replace("{{item-name}}", oi.getProductName());
            item = item.replace("{{item-price}}", oi.getProductPrice() + "");
            item = item.replace("{{item-quantity}}", oi.getQuantity() + "");
            item = item.replace("{{item-total}}", oi.getPrice() + "");

            itemList = itemList + item;
        }
        body = body.replace("{{item-list}}", itemList);

        body = body.replace("{{sub-total}}", bodyContent.getTotal());

        return body;
    }

    public static String generateAwaitingPickupEmail(OrderEmailBodyContent bodyContent, String emailTemplatePath) throws FileNotFoundException {

        String body = fileContent(emailTemplatePath + "awaiting-pickup.html");

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

            item = item.replace("{{item-name}}", oi.getProductName());
            item = item.replace("{{item-price}}", oi.getProductPrice() + "");
            item = item.replace("{{item-quantity}}", oi.getQuantity() + "");
            item = item.replace("{{item-total}}", oi.getPrice() + "");

            itemList = itemList + item;
        }
        body = body.replace("{{item-list}}", itemList);

        body = body.replace("{{sub-total}}", bodyContent.getTotal());

        return body;
    }

    public static String generateDeliveredToCustomerEmail(OrderEmailBodyContent bodyContent, String emailTemplatePath) throws FileNotFoundException {

        String body = fileContent(emailTemplatePath + "delivered-to-customer.html");

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

            item = item.replace("{{item-name}}", oi.getProductName());
            item = item.replace("{{item-price}}", oi.getProductPrice() + "");
            item = item.replace("{{item-quantity}}", oi.getQuantity() + "");
            item = item.replace("{{item-total}}", oi.getPrice() + "");

            itemList = itemList + item;
        }
        body = body.replace("{{item-list}}", itemList);

        body = body.replace("{{sub-total}}", bodyContent.getTotal());

        return body;
    }

    public static String generateRequestingDeliveryFailedEmail(OrderEmailBodyContent bodyContent, String emailTemplatePath) throws FileNotFoundException {

        String body = fileContent(emailTemplatePath + "requesting-delivery-failed.html");

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
//        for (OrderItem oi : bodyContent.getOrderItems()) {
//            String item = orderItem;
//            item = item.replace("{{item-name}}", oi.getProductName());
//            item = item.replace("{{item-price}}", oi.getProductPrice() + "");
//            item = item.replace("{{item-quantity}}", oi.getQuantity() + "");
//            item = item.replace("{{item-total}}", oi.getPrice() + "");
//
//            itemList = itemList + item;
//        }
        body = body.replace("{{item-list}}", itemList);

        body = body.replace("{{sub-total}}", bodyContent.getTotal());

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

    public static String generateAccountEmail(AccountVerificationEmailBody bodyContent, String emailTemplatePath, String symplifiedLogoPath) throws FileNotFoundException {
        String emailContent = "";

        String header = fileContent(emailTemplatePath + "header.html");

        header = header.replace("{{store-logo}}", symplifiedLogoPath);
        header = header.replace("{{title}}", bodyContent.getActionType().label);

        String footer = fileContent(emailTemplatePath + "footer.html");

        if (null != bodyContent.getActionType()) {
            switch (bodyContent.getActionType()) {
                case EMAIL_VERIFICATION:
                    emailContent = header + generateAccountVerificationEmail(bodyContent, emailTemplatePath) + footer;
                    break;
                case PASSWORD_RESET:
                    emailContent = header + generateAccountVerificationEmail(bodyContent, emailTemplatePath) + footer;
                    break;
                default:
                    break;
            }
        }

        return emailContent;
    }

    public static String generateAccountVerificationEmail(AccountVerificationEmailBody bodyContent, String emailTemplatePath) throws FileNotFoundException {

        String body = fileContent(emailTemplatePath + "user-email-verification.html");

        body = body.replace("{{verification-link}}", bodyContent.getLink());

        return body;
    }

    public static String generatePromotionEmail(String emailTemplatePath, String symplifiedLogoPath, String bodyContent) throws FileNotFoundException {
        String emailContent = "";

        String header = fileContent(emailTemplatePath + "header.html");

        header = header.replace("{{store-logo}}", symplifiedLogoPath);
        header = header.replace("{{title}}", "Promotion");

        String footer = fileContent(emailTemplatePath + "footer.html");

        emailContent = header + bodyContent + footer;

        return emailContent;
    }

}
