/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.Kursovaa.confingAvtushkoVM;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 *
 * @author Vladimir
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().
                antMatchers("/app/secure/**").access("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')").
                and().formLogin(). //login configuration
                loginPage("/app/login").
                loginProcessingUrl("/appLogin").
                usernameParameter("app_username").
                passwordParameter("app_password").
                defaultSuccessUrl("/app/secure/studentDetail").
                and().logout(). //logout configuration
                logoutUrl("/appLogout").
                logoutSuccessUrl("/app/login");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("ramesh").password("ram123").roles("ADMIN");
        auth.inMemoryAuthentication().withUser("mahesh").password("mah123").roles("USER");
    }
}
