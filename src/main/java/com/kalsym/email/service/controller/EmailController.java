package com.kalsym.email.service.controller;

import com.kalsym.email.service.model.HttpReponse;
import com.kalsym.email.service.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import com.kalsym.email.service.EmailServiceApplication;
import com.kalsym.email.service.util.Logger;
import java.util.Arrays;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.kalsym.email.service.util.EmailUtil;

/**
 *
 * @author 7cu
 */
@RestController()
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${session.expiry:3600}")
    private int expiry;

    @Value("${symplified.noreply.email.from:no-reply@symplified.biz}")
    private String noReplyFrom;

    @Value("${symplified.email.template.path:D:/kalsym-repo/Symplified/email-service/src/main/resources/templates/email/}")
    private String emailTemplatePath;

    @Value("${symplified.logo.path:https://symplified.biz/store-assets/symplified-logo-small.png}")
    private String symplifiedLogoPath;

    @Value("${deliverin.logo.path:https://symplified.biz/store-assets/deliverin-logo-small.png}")
    private String deliverinLogoPath;

    @Value("${easydukan.logo.path:https://symplified.biz/store-assets/easydukan-logo-small.png}")
    private String easydukanLogoPath;

    @Value("${symplified.interest.email.to:ask@symplified.biz}")
    private String interestReceiverEmail;

    @PostMapping(path = { "/no-reply/orders" }, name = "post-email-noreply-orders")
    @PreAuthorize("hasAnyAuthority('post-email-noreply-orders', 'all')")
    public ResponseEntity<HttpReponse> postNoReplyEmailForOrders(HttpServletRequest request,
            @RequestBody Email body) {
        String logprefix = request.getRequestURI();
        HttpReponse response = new HttpReponse(request.getRequestURI());
        Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix, "body: " + body, "");

        try {

            MimeMessage message = mailSender.createMimeMessage();

            message.setSubject(body.getBody().getOrderStatus().label);
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true);

            String logoUrl = body.getBody().getLogoUrl();
            if (logoUrl == null) {
                logoUrl = symplifiedLogoPath;
            }

            String emailBody = "";
            if (body.getDomain() != null) {
                String logoPath = symplifiedLogoPath;
                if (body.getDomain().contains("dev-my")) {
                    logoPath = deliverinLogoPath;
                } else if (body.getDomain().contains("dev-pk")) {
                    logoPath = easydukanLogoPath;
                } else if (body.getDomain().contains("deliverin")) {
                    logoPath = deliverinLogoPath;
                } else if (body.getDomain().contains("easydukan")) {
                    logoPath = easydukanLogoPath;
                }
                emailBody = EmailUtil.generateOrderEmail(body.getBody(), emailTemplatePath, logoPath);
            } else {
                emailBody = EmailUtil.generateOrderEmail(body.getBody(), emailTemplatePath, symplifiedLogoPath);
            }

            Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix,
                    "customerTrackingUrl: " + body.getBody().getCustomerTrackingUrl(), "");

            if (body.getBody().getCustomerTrackingUrl() != null) {
                emailBody = emailBody.replace("{{tracking-url}}", body.getBody().getCustomerTrackingUrl());
            }

            Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix,
                    "storeContact: " + body.getBody().getStoreContact(), "");

            if (body.getBody().getStoreContact() != null) {
                emailBody = emailBody.replace("{{store-contact}}", body.getBody().getStoreContact());
            }

            if (body.getFrom() != null) {
                if (body.getFromName() != null)
                    helper.setFrom(body.getFrom(), body.getFromName());
                else
                    helper.setFrom(body.getFrom());
                Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix,
                        "email sent from: " + body.getFrom() + " fromName:" + body.getFromName());
            } else {
                helper.setFrom(noReplyFrom);
                Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix,
                        "email sent from: " + noReplyFrom);
            }

            helper.setTo(body.getTo());
            helper.setText(emailBody, true);
            Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix,
                    "email subject: " + message.getSubject(), "");

            mailSender.send(message);

        } catch (Exception e) {
            Logger.application.error(Logger.pattern, EmailServiceApplication.VERSION, logprefix,
                    "email could not be send", "", e);
            response.setErrorStatus(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix,
                "email sent to: " + Arrays.toString(body.getTo()), "");

        response.setSuccessStatus(HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(path = { "/no-reply/user-account" }, name = "post-email-noreply-user-account")
    @PreAuthorize("hasAnyAuthority('post-email-noreply-user-account', 'all')")
    public ResponseEntity<HttpReponse> postNoReplyEmail(HttpServletRequest request,
            @RequestBody Email body) {
        String logprefix = request.getRequestURI();
        HttpReponse response = new HttpReponse(request.getRequestURI());
        Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix, "body: " + body, "");

        try {

            MimeMessage message = mailSender.createMimeMessage();

            message.setSubject(body.getUserAccountBody().getActionType().label);
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true);

            String emailBody = null;
            if (body.getDomain() != null) {
                String logoPath = symplifiedLogoPath;
                if (body.getDomain().contains("dev-my")) {
                    logoPath = deliverinLogoPath;
                } else if (body.getDomain().contains("dev-pk")) {
                    logoPath = easydukanLogoPath;
                } else if (body.getDomain().contains("deliverin")) {
                    logoPath = deliverinLogoPath;
                } else if (body.getDomain().contains("easydukan")) {
                    logoPath = easydukanLogoPath;
                }
                emailBody = EmailUtil.generateAccountEmail(body.getUserAccountBody(), emailTemplatePath, logoPath);
            } else {
                emailBody = EmailUtil.generateAccountEmail(body.getUserAccountBody(), emailTemplatePath,
                        symplifiedLogoPath);
            }

            if (body.getFrom() != null) {
                if (body.getFromName() != null)
                    helper.setFrom(body.getFrom(), body.getFromName());
                else
                    helper.setFrom(body.getFrom());
                Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix,
                        "email sent from: " + body.getFrom() + " fromName:" + body.getFromName());
            } else {
                helper.setFrom(noReplyFrom);
                Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix,
                        "email sent from: " + noReplyFrom);
            }

            helper.setTo(body.getTo());
            helper.setText(emailBody, true);
            Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix,
                    "email subject: " + message.getSubject(), "");

            mailSender.send(message);

        } catch (Exception e) {
            Logger.application.error(Logger.pattern, EmailServiceApplication.VERSION, logprefix,
                    "email could not be send", "", e);
            response.setErrorStatus(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix,
                "email sent to: " + Arrays.toString(body.getTo()), "");

        response.setSuccessStatus(HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(path = { "/no-reply/promotion" }, name = "post-email-noreply-promotion")
    @PreAuthorize("hasAnyAuthority('post-email-noreply-promotion', 'all')")
    public ResponseEntity<HttpReponse> postNoReplyEmailPromotion(HttpServletRequest request,
            @RequestBody Email body) {
        String logprefix = request.getRequestURI();
        HttpReponse response = new HttpReponse(request.getRequestURI());
        Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix, "body: " + body, "");

        try {

            MimeMessage message = mailSender.createMimeMessage();

            message.setSubject(body.getUserAccountBody().getActionType().label);
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true);

            String emailBody = "";
            if (body.getDomain() != null) {
                String logoPath = symplifiedLogoPath;
                if (body.getDomain().contains("dev-my")) {
                    logoPath = deliverinLogoPath;
                } else if (body.getDomain().contains("dev-pk")) {
                    logoPath = easydukanLogoPath;
                } else if (body.getDomain().contains("deliverin")) {
                    logoPath = deliverinLogoPath;
                } else if (body.getDomain().contains("easydukan")) {
                    logoPath = easydukanLogoPath;
                }
                emailBody = EmailUtil.generatePromotionEmail(emailTemplatePath, logoPath, body.getRawBody());
            } else {
                emailBody = EmailUtil.generatePromotionEmail(emailTemplatePath, symplifiedLogoPath, body.getRawBody());
            }

            if (body.getFrom() != null) {
                if (body.getFromName() != null)
                    helper.setFrom(body.getFrom(), body.getFromName());
                else
                    helper.setFrom(body.getFrom());
                Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix,
                        "email sent from: " + body.getFrom() + " fromName:" + body.getFromName());
            } else {
                helper.setFrom(noReplyFrom);
                Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix,
                        "email sent from: " + noReplyFrom);
            }

            helper.setTo(body.getTo());
            helper.setText(emailBody, true);
            Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix,
                    "email subject: " + message.getSubject(), "");

            mailSender.send(message);

        } catch (Exception e) {
            Logger.application.error(Logger.pattern, EmailServiceApplication.VERSION, logprefix,
                    "email could not be send", "", e);
            response.setErrorStatus(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix,
                "email sent to: " + Arrays.toString(body.getTo()), "");

        response.setSuccessStatus(HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(path = { "/no-reply/orders-completion-status" }, name = "post-email-noreply-orders-completion-status")
    @PreAuthorize("hasAnyAuthority('post-email-noreply-orders', 'all')")
    public ResponseEntity<HttpReponse> postNoReplyEmailForOrdersCompletionStatus(HttpServletRequest request,
            @RequestBody Email body) {
        String logprefix = request.getRequestURI();
        HttpReponse response = new HttpReponse(request.getRequestURI());
        Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix, "body: " + body, "");

        try {
            MimeMessage message = mailSender.createMimeMessage();
            message.setSubject(body.getBody().getOrderStatus().label);
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true);
            if (body.getFrom() != null) {
                if (body.getFromName() != null)
                    helper.setFrom(body.getFrom(), body.getFromName());
                else
                    helper.setFrom(body.getFrom());
                Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix,
                        "email sent from: " + body.getFrom() + " fromName:" + body.getFromName());
            } else {
                helper.setFrom(noReplyFrom);
                Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix,
                        "email sent noReplyFrom: " + noReplyFrom);
            }
            helper.setTo(body.getTo());

            if (body.getDomain() != null) {
                String logoPath = symplifiedLogoPath;
                if (body.getDomain().contains("dev-my")) {
                    logoPath = deliverinLogoPath;
                } else if (body.getDomain().contains("dev-pk")) {
                    logoPath = easydukanLogoPath;
                } else if (body.getDomain().contains("deliverin")) {
                    logoPath = deliverinLogoPath;
                } else if (body.getDomain().contains("easydukan")) {
                    logoPath = easydukanLogoPath;
                }
                body.setRawBody(body.getRawBody().replace("{{store-logo}}", logoPath));
            }
            helper.setText(body.getRawBody(), true);

            Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix,
                    "email subject: " + message.getSubject(), "");
            mailSender.send(message);
        } catch (Exception e) {
            Logger.application.error(Logger.pattern, EmailServiceApplication.VERSION, logprefix,
                    "email could not be send", "", e);
            response.setErrorStatus(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix,
                "email sent to: " + Arrays.toString(body.getTo()), "");

        response.setSuccessStatus(HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(path = { "/no-reply/interest" }, name = "post-interest")
    public ResponseEntity<HttpReponse> postInterest(HttpServletRequest request,
            @RequestBody Interest body) {
        String logprefix = request.getRequestURI();
        HttpReponse response = new HttpReponse(request.getRequestURI());
        Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix, "body: " + body, "");

        try {
            MimeMessage message = mailSender.createMimeMessage();
            message.setSubject("New Interest");
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(noReplyFrom);
            helper.setTo(interestReceiverEmail);
            Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix,
                    "email subject: " + message.getSubject(), "");

            String logoPath = symplifiedLogoPath;
            String emailBody = EmailUtil.generateInterestEmail(emailTemplatePath, logoPath, body);
            helper.setText(emailBody, true);

            mailSender.send(message);

            Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix,
                    "email sent to: " + interestReceiverEmail, "");
        } catch (Exception e) {
            Logger.application.error(Logger.pattern, EmailServiceApplication.VERSION, logprefix,
                    "email could not be send", "", e);
            response.setErrorStatus(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        response.setSuccessStatus(HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
