package org.wanderingnet.data.api;

import org.wanderingnet.data.api.arch.NamedEntityCrudDAO;
import org.wanderingnet.model.User;

/**
 * Created by guillermoblascojimenez on 03/04/16.
 */
public interface UserDAO extends NamedEntityCrudDAO<User> {

    User findByEmail(String email);

    boolean existsByEmail(String email);

}
