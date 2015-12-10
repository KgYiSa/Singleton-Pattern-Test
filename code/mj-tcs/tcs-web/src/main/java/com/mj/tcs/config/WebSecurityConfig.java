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
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private Logger logger = Logger.getLogger(WebSecurityConfig.class);

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user").password("user").roles("USER");
        auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN", "USER");
        auth.inMemoryAuthentication().withUser("mj").password("mj").roles("SUPERADMIN", "ADMIN", "USER");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 设置不拦截规则
        web.ignoring()
                // TODO: Add "/**" to avoid ClientAbortException
                .antMatchers("/css/**", "/plugin/**", "/static/**", "/**.jsp", "/**/*.jsp", "/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
            .antMatchers("/**", "/public/**").permitAll(); // TODO: COMMENT or REMOVE
//            .antMatchers("/login", "/public/**").permitAll()
//            .antMatchers("/**").hasRole("SUPERADMIN")
//            .antMatchers("/api/v1/**").hasRole("ADMIN")
//            .antMatchers("/users/**").hasAuthority("ADMIN")
//            .anyRequest().fullyAuthenticated()
//            .and()
//                .formLogin()
//                .loginPage("/tcs-web/login")
//                .failureUrl("/tcs-web/login?error")
//                .permitAll()
//                .and()
//                .logout()
//                .deleteCookies("remember-me")
//                .and()
//                .rememberMe();
//
//        // 设置拦截规则
//        // 自定义accessDecisionManager访问控制器,并开启表达式语言
//        http.authorizeRequests().accessDecisionManager(accessDecisionManager())
//            .expressionHandler(webSecurityExpressionHandler())
//            .antMatchers("/**/*.do*").hasRole("USER")
//            .antMatchers("/**/*.htm").hasRole("ADMIN").and()
//            .exceptionHandling().accessDeniedPage("/templates/login");
//
//        // 自定义登录页面
//        http.csrf().disable().formLogin().loginPage("/templates/login")
//            .failureUrl("/login?error=1")
//            .loginProcessingUrl("/j_spring_security_check")
//            .usernameParameter("j_username")
//            .passwordParameter("j_password").permitAll();
//
//        // 自定义注销
//        http.logout().logoutUrl("/templates/logout").logoutSuccessUrl("/templates/login")
//            .invalidateHttpSession(true);
//
//        // session管理
//        http.sessionManagement().sessionFixation().changeSessionId()
//                .maximumSessions(1).expiredUrl("/");
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
