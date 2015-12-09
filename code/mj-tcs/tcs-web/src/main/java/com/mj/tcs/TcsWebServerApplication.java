package com.mj.tcs;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class TcsWebServerApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(TcsWebServerApplication.class).headless(false);
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(TcsWebServerApplication.class).headless(false).run(args);
    }
}
