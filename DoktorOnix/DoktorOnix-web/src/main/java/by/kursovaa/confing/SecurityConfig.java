/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.confing;

import by.kursovaa.service.MyAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author Vladimir
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("userDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    @Qualifier("successHandler")
    private MyAuthenticationSuccessHandler successHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/index").access("isAuthenticated()")
                .antMatchers("/kassa/**").access("isAuthenticated()")
                .antMatchers("/klienty/**").access("isAuthenticated()")
                .antMatchers("/mailbox/**").access("isAuthenticated()")
                .antMatchers("/polzovateli/**").access("isAuthenticated()")
                .antMatchers("/tovary/**").access("isAuthenticated()")
                .antMatchers("/zakazy/**").access("isAuthenticated()")
                .and().exceptionHandling().accessDeniedPage("/403");

        http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/avtozizazia")
                .failureUrl("/login?error")
                .usernameParameter("app_username")
                .passwordParameter("app_password")
                .defaultSuccessUrl("/index")
                .successHandler(successHandler)
                .permitAll();

        http.logout()
                .permitAll()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/logoutSuccess")
                .clearAuthentication(true)
                .invalidateHttpSession(true);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();
        return encoder;
    }
}
