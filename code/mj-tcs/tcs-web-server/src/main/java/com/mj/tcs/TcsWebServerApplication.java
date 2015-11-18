package com.mj.tcs;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

@SpringBootApplication
public class TcsWebServerApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TcsWebServerApplication.class);
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(TcsWebServerApplication.class).headless(false).run(args);
    }
}
