/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.kursovaa.service;

import by.kursovaa.entity.Polzovateli;
import by.kursovaa.entity.UserRoles;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author Vladimir
 */
@Service("userDetailsService")
public class MyUserDetailsService implements UserDetailsService {

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        Polzovateli user = Ejb.getInterface().lookupPolzovateliFacadeRemote().findByUserName(username);
        List<GrantedAuthority> authorities = buildUserAuthority(user.getUserRolesList());
        return buildUserForAuthentication(user, authorities);
    }

    private User buildUserForAuthentication(Polzovateli user, List<GrantedAuthority> authorities) {
        return new User(user.getLogin(), user.getParol(), !Boolean.parseBoolean(Integer.toString(user.getAktiven())),
                true, true, true, authorities);
    }

    private List<GrantedAuthority> buildUserAuthority(List<UserRoles> userRoles) {
        Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();

        for (UserRoles userRole : userRoles) {
            setAuths.add(new SimpleGrantedAuthority(userRole.getRole()));
        }

        List<GrantedAuthority> result = new ArrayList<GrantedAuthority>(setAuths);
        return result;
    }
}
