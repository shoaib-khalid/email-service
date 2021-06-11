package com.kalsym.email.service.controller;

import com.kalsym.email.service.model.HttpReponse;
import com.kalsym.email.service.model.EmailBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import com.kalsym.email.service.EmailServiceApplication;
import com.kalsym.email.service.util.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author 7cu
 */
@RestController()
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${session.expiry:3600}")
    private int expiry;

    @Value("${symplified.noreply.email.from:no-reply@symplified.biz}")
    private String noReplyFrom;

    @PostMapping(path = {"/no-reply"}, name = "post-email-noreply")
     @PreAuthorize("hasAnyAuthority('post-email-noreply', 'all')")
    public ResponseEntity<HttpReponse> postNoReplyEmail(HttpServletRequest request,
            @RequestBody EmailBody body) {
        String logprefix = request.getRequestURI();
        HttpReponse response = new HttpReponse(request.getRequestURI());
        Logger.application.info(Logger.pattern, EmailServiceApplication.VERSION, logprefix, "", "");

        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            //msg.setTo("to_1@gmail.com", "to_2@gmail.com", "to_3@yahoo.com");
            msg.setFrom(noReplyFrom);
            msg.setTo(body.getTo());

            msg.setSubject(body.getSubject());
            msg.setText(body.getBody());

            javaMailSender.send(msg);
        } catch (MailException e) {
            Logger.application.error(Logger.pattern, EmailServiceApplication.VERSION, logprefix, "email could not be send", "", e);
            response.setErrorStatus(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        response.setSuccessStatus(HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
