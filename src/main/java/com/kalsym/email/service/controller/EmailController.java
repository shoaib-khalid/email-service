package com.kalsym.email.service.controller;

import com.kalsym.email.service.model.lalmove.TestModel;
import com.kalsym.email.service.model.HttpReponse;
import com.kalsym.email.service.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import com.kalsym.email.service.EmailServiceApplication;
import com.kalsym.email.service.util.Logger;
import java.util.Arrays;
import java.util.Date;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author 7cu
 */
@RestController()
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${session.expiry:3600}")
    private int expiry;

    @Value("${symplified.noreply.email.from:no-reply@symplified.biz}")
    private String noReplyFrom;

    @Value("${symplified.email.template.path:D:/kalsym-repo/Symplified/email-service/src/main/resources/templates/email/}")
    private String emailTemplatePath;

    @Value("${symplified.logo.path:https://www.kalsym.com/sym-logo.png}")
    private String symplifiedLogoPath;

    @PostMapping(path = {"/no-reply"}, name = "post-email-noreply")
    @PreAuthorize("hasAnyAuthority('post-email-noreply', 'all')")
    public ResponseEntity<HttpReponse> postNoReplyEmail(HttpServletRequest request,
            @RequestBody Email body) {
        String logprefix = request.getRequestURI();
        HttpReponse response = new HttpReponse(request.getRequestURI());
        Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix, "body: " + body, "");

        try {

            MimeMessage message = mailSender.createMimeMessage();

            message.setSubject(body.getBody().getOrderStatus().label + " " + (new Date()).toString());
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true);

            String emailBody = EmailUtil.generateEmail(body.getBody(), emailTemplatePath, symplifiedLogoPath);

            helper.setFrom(noReplyFrom);
            helper.setTo(body.getTo());
            helper.setText(emailBody, true);
            Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix, "email subject: " + body.getSubject(), "");

            mailSender.send(message);
            Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix, "email sent from: " + noReplyFrom, "");

        } catch (Exception e) {
            Logger.application.error(Logger.pattern, EmailServiceApplication.VERSION, logprefix, "email could not be send", "", e);
            response.setErrorStatus(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix, "email sent to: " + Arrays.toString(body.getTo()), "");

        response.setSuccessStatus(HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(path = {"/lala-move"}, name = "post-test-lalamove")
    @PreAuthorize("hasAnyAuthority('post-test-lalamove', 'all')")
    public ResponseEntity<HttpReponse> postTestLalamove(HttpServletRequest request,
            @RequestBody TestModel body) {
        String logprefix = request.getRequestURI();
        HttpReponse response = new HttpReponse(request.getRequestURI());
        Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix, "body: " + body, "");

        try {

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            String auth = "hmac 6e4e7adb5797632e54172dc2dd2ca748:1623836317245:9cca24efe604d44c5f034cee1bfc1b851efaa157e0607a861aac5c66b27685c0";
            headers.add("Authorization", auth);
            headers.add("X-LLM-Country", "MY_KUL");
            headers.add("Accept", "*/*");

            HttpEntity<TestModel> entity;
            entity = new HttpEntity<>(body, headers);
            
//            headers.setContentType(MediaType.APPLICATION_JSON);

            String url = "https://rest.sandbox.lalamove.com/v2/quotations";
            Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix, "url: " + url, "");

            Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix, "entity: " + entity, "");

            ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix, "res: " + res.toString(), "");

        } catch (Exception e) {
            Logger.application.error(Logger.pattern, EmailServiceApplication.VERSION, logprefix, "email could not be send", "", e);
            response.setErrorStatus(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        response.setSuccessStatus(HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
