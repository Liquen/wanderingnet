package org.wanderingnet.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.wanderingnet.data.api.UserDAO;
import org.wanderingnet.model.User;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by guillermoblascojimenez on 08/03/16.
 */
@Component
public class WanderingnetAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserService userService;

    @Autowired
    private WanderingnetUserDetailService wanderingnetUserDetailService;

    public static Set<GrantedAuthority> toGrantedAuthority() {
        Set<GrantedAuthority> grantedAuthoritySet = new HashSet<>();
        grantedAuthoritySet.add(new SimpleGrantedAuthority("WEBAPP"));
        return grantedAuthoritySet;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        if (authentication == null || !supports(authentication.getClass())) {
            return null;
        }
        String email = authentication.getName();
        String password = (String) authentication.getCredentials();
        if (userService.authenticate(email, password.toCharArray())) {
            User user = userDAO.findByEmail(email);
            Set<GrantedAuthority> grantedAuthoritySet = toGrantedAuthority();

            UsernamePasswordAuthenticationToken authentication1 = new UsernamePasswordAuthenticationToken(
                    wanderingnetUserDetailService.loadUserByUsername(email),
                    null,
                    grantedAuthoritySet);
            return authentication1;
        } else {
            throw new BadCredentialsException("User " + email + " wrong password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
