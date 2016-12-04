/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.service;

import by.kursovaa.entity.Email;
import by.kursovaa.entity.Polzovateli;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.SavedRequest;

/**
 *
 * @author Vladimir
 */
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication a)
            throws IOException {

        HttpSession httpSession = request.getSession();

        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = authUser.getUsername();

        Polzovateli user = Ejb.getInterface().lookupPolzovateliFacadeRemote().findByUserName(userName);
        httpSession.setAttribute("user", user);

        Email accountInfo = user.getIdEmailSluzebny();
        Email email = new Email(accountInfo);
        Ejb.getInterface().lookupEmailServiceRemote().startSynchronization(email);

        SavedRequest savedRequest = (SavedRequest) httpSession.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
        String targetUrl;

        if (savedRequest == null) {
            targetUrl = "/index";
        } else {
            targetUrl = savedRequest.getRedirectUrl();
        }

        RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }
}
