package org.wanderingnet.data.api;

import org.wanderingnet.common.Optional;
import org.wanderingnet.data.api.arch.UniqueNamedEntityCrudDAO;
import org.wanderingnet.model.Map;

/**
 * Created by guillermoblascojimenez on 03/04/16.
 */
public interface MapDAO extends UniqueNamedEntityCrudDAO<Map> {
    Optional<Map> mapOfNameKey(String nameKey);
}
