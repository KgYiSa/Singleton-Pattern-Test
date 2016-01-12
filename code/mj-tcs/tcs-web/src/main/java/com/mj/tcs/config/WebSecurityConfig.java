package com.mj.tcs.config;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

/**
 * @author Wang Zhen
 */
@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private Logger logger = Logger.getLogger(WebSecurityConfig.class);

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user").password("user").roles("USER");
        auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN", "USER");
        auth.inMemoryAuthentication().withUser("mj").password("mj").roles("SUPERADMIN", "ADMIN", "USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/plugin/**").permitAll()
                .antMatchers("/static/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/html/**").permitAll()
                .antMatchers("/login", "/public/**").permitAll()
                .antMatchers("/test").hasRole("ADMIN")
                .anyRequest().authenticated()
            .and()
                .formLogin()
                .loginPage("/login")
                /**
                 * ref org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer
                 */
//                .usernameParameter("user")
//                .passwordParameter("pass")
                .defaultSuccessUrl("/operating")
                .permitAll()
            .and()
                .logout()
                .logoutSuccessUrl("/")
                .permitAll()
                .invalidateHttpSession(true);
    }

//    /*
//     *
//     * 这里可以增加自定义的投票器
//     */
//    @SuppressWarnings("rawtypes")
//    @Bean(name = "accessDecisionManager")
//    public AccessDecisionManager accessDecisionManager() {
//        logger.info("AccessDecisionManager");
//        List<AccessDecisionVoter> decisionVoters = new ArrayList<AccessDecisionVoter>();
//        decisionVoters.add(new RoleVoter());
//        decisionVoters.add(new AuthenticatedVoter());
//        decisionVoters.add(webExpressionVoter());// 启用表达式投票器
//
//        AffirmativeBased accessDecisionManager = new AffirmativeBased(
//                decisionVoters);
//
//        return accessDecisionManager;
//    }
//
//    /*
//     * 表达式控制器
//     */
//    @Bean(name = "expressionHandler")
//    public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
//        logger.info("DefaultWebSecurityExpressionHandler");
//        DefaultWebSecurityExpressionHandler webSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
//        return webSecurityExpressionHandler;
//    }
//
//    /*
//     * 表达式投票器
//     */
//    @Bean(name = "expressionVoter")
//    public WebExpressionVoter webExpressionVoter() {
//        logger.info("WebExpressionVoter");
//        WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
//        webExpressionVoter.setExpressionHandler(webSecurityExpressionHandler());
//        return webExpressionVoter;
//    }

//    @Configuration
//    protected static class AuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {
//
//        @Autowired
//        private PersonRepository personRepository;
//
//        @Override
//        public void init(AuthenticationManagerBuilder auth) throws Exception {
//            auth.userDetailsService(userDetailsService());
//        }
//
//        @Bean
//        public UserDetailsService userDetailsService() {
//            return (email) -> personRepository.findByEmail(email)
//                    .orElseThrow(() -> new UsernameNotFoundException("Could not find the user with email '" + email + "'."));
//        }
//    }
}
