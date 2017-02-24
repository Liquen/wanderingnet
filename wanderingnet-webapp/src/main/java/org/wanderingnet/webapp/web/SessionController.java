package org.wanderingnet.webapp.web;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.wanderingnet.service.user.WanderingnetUser;

/**
 * Created by guillermoblascojimenez on 20/03/16.
 */
public abstract class SessionController {

    @ModelAttribute("user")
    public WanderingnetUser user() {
        return currentUser();
    }

    protected WanderingnetUser currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof WanderingnetUser) {
                return (WanderingnetUser) principal;
            }
        }
        return null;
    }

    protected boolean isAutenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
    }

}
