package com.mj.tcs;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class TcsWebServerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(TcsWebServerApplication.class).headless(false).run(args);

    }
}
