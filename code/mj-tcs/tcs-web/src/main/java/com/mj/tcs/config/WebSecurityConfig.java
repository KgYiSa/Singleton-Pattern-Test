package com.mj.tcs.config;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author Wang Zhen
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private Logger logger = Logger.getLogger(WebSecurityConfig.class);

    @Value("${spring.profiles.active}")
    private String env;

    public WebSecurityConfig() {
        super(false);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user").password("user").roles("USER");
        auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN", "USER");
        auth.inMemoryAuthentication().withUser("mj").password("mj").roles("SUPERADMIN", "ADMIN", "USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Enables Https instead of Http except the local request.
        if (!env.equals("dev")) {
            http.requiresChannel().anyRequest().requiresSecure();
        }

        http.csrf().disable();

        http.exceptionHandling().and()
                .anonymous().and()
                .servletApi().and()
//                .headers().cacheControl().and() // ERROR!!!
                .authorizeRequests()

                //allow anonymous resource requests
                .antMatchers("/").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/plugin/**").permitAll()
                .antMatchers("/static/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/html/**").permitAll()
                .antMatchers("/login", "/public/**").permitAll() // TODO: REMOVE ???
                .antMatchers("/test").hasRole("ADMIN") // TODO: REMOVE ???
                .anyRequest().authenticated()

                //allow anonymous POSTs to login
                .antMatchers(HttpMethod.POST, "/web/login").permitAll()

                //allow anonymous GETs to API
                .antMatchers(HttpMethod.GET, "/web/**").permitAll()

                .and()
                .formLogin()
                .loginPage("/web/login")
                /**
                 * REF: org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer
                 */
//                .usernameParameter("user")
//                .passwordParameter("pass")
                .defaultSuccessUrl("/web/operating")
                .permitAll()
            .and()
                .logout()
                .logoutSuccessUrl("/")
                .permitAll()
                .invalidateHttpSession(true);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
