package org.wanderingnet.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.wanderingnet.data.api.UserDAO;
import org.wanderingnet.model.User;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by guillermoblascojimenez on 08/03/16.
 */
@Service
public class WanderingnetUserDetailService implements UserDetailsService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User optionalUser = userDAO.findByEmail(username);
        if (optionalUser != null) {
            User user = optionalUser;
            Set<GrantedAuthority> grantedAuthoritySet = new HashSet<>();
            grantedAuthoritySet.add(new SimpleGrantedAuthority("WEBAPP"));

            WanderingnetUser userDetails = new WanderingnetUser(user, user.getPasswordHash(), grantedAuthoritySet);
            return userDetails;
        } else {
            throw new UsernameNotFoundException("User " + username + "not found");
        }
    }

}