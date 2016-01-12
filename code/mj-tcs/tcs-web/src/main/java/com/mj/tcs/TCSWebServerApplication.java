package com.mj.tcs;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class TCSWebServerApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(TCSWebServerApplication.class).headless(false);
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(TCSWebServerApplication.class).headless(false).run(args);
    }
}
