package com.kalsym.email.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author 7cu
 */
@SpringBootApplication
public class EmailServiceApplication implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger("application");

    static {
        System.setProperty("spring.jpa.hibernate.naming.physical-strategy", "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl");
        /**
         * To escape SQL reserved keywords
         */
        System.setProperty("hibernate.globally_quoted_identifiers", "true");
    }

    public static String VERSION;

    @Autowired
    private Environment env;

    public static void main(String... args) {
        logger.info("Staring email-service...");
        SpringApplication.run(EmailServiceApplication.class, args);
    }

    @Value("${build.version:not-known}")
    String version;

    @Bean
    CommandLineRunner lookup(ApplicationContext context) {
        return args -> {
            VERSION = version;

            logger.info("[v{}][{}] {}", VERSION, "", "\n"
                    + "                       _ _                            _          \n"
                    + "                      (_) |                          (_)         \n"
                    + "   ___ _ __ ___   __ _ _| |______ ___  ___ _ ____   ___  ___ ___ \n"
                    + "  / _ \\ '_ ` _ \\ / _` | | |______/ __|/ _ \\ '__\\ \\ / / |/ __/ _ \\\n"
                    + " |  __/ | | | | | (_| | | |      \\__ \\  __/ |   \\ V /| | (_|  __/\n"
                    + "  \\___|_| |_| |_|\\__,_|_|_|      |___/\\___|_|    \\_/ |_|\\___\\___|\n"
                    + "                                                                 \n"
                    + "                                                                 "
            );
        };
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Override
    public void run(String... args) throws Exception {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
