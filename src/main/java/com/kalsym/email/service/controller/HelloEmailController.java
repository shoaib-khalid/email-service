package com.kalsym.email.service.controller;

import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.kalsym.email.service.EmailServiceApplication;

/**
 *
 * @author 7cu
 */
public class HelloEmailController {
     @Autowired
    private Environment env;

    private static org.slf4j.Logger logger = LoggerFactory.getLogger("application");

    @RequestMapping(value = "/liveness", method = RequestMethod.GET, produces = "application/json", params = {})
    public String liveness() {
        logger.debug("[" + EmailServiceApplication.VERSION + "] email-service up!");
        JSONObject json = new JSONObject();
        json.put("name","email-service");
        json.put("version", EmailServiceApplication.VERSION);

        return json.toString();
    }
}
