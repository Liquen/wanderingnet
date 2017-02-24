package org.wanderingnet.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wanderingnet.data.api.UserDAO;
import org.wanderingnet.model.User;

/**
 * Created by guillermoblascojimenez on 08/03/16.
 */
@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private SecurityService securityService;

    public User registerUser(String email, String name, char[] password) {
        SecurityService.SecurityPair securityPair = securityService.secure(password);
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPasswordHash(securityPair.getHash());
        user.setPasswordSalt(securityPair.getSalt());
        userDAO.create(user);
        return user;
    }

    public boolean authenticate(String email, char[] password) {
        User user = userDAO.findByEmail(email);
        return user != null && securityService.match(password, user.getPasswordHash(), user.getPasswordSalt());
    }

}
