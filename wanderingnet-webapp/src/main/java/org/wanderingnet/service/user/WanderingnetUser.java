package org.wanderingnet.service.user;

import org.springframework.security.core.GrantedAuthority;
import org.wanderingnet.model.User;

import java.util.Collection;

/**
 * Created by guillermoblascojimenez on 08/03/16.
 */
public class WanderingnetUser extends org.springframework.security.core.userdetails.User {

    private User user;

    public WanderingnetUser(User user, String password, Collection<? extends GrantedAuthority> authorities) {
        super(user.getEmail(), password, authorities);
        this.user = user;
    }

    public WanderingnetUser(User user, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(user.getEmail(), password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
