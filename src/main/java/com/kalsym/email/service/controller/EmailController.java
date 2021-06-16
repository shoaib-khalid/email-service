package com.kalsym.email.service.controller;

import com.kalsym.email.service.model.HttpReponse;
import com.kalsym.email.service.model.Email;
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

}
