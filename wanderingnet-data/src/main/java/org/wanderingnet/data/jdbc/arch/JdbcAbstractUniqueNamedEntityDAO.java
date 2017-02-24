package org.wanderingnet.data.jdbc.arch;


import org.wanderingnet.common.Optional;
import org.wanderingnet.data.api.arch.UniqueNamedEntityCrudDAO;
import org.wanderingnet.model.NamedEntity;

import java.util.ArrayList;

/**
 * Created by guillermoblascojimenez on 09/05/15.
 */
public abstract class JdbcAbstractUniqueNamedEntityDAO<E extends NamedEntity> extends JdbcAbstractNamedDAO<E> implements UniqueNamedEntityCrudDAO<E> {

    protected JdbcAbstractUniqueNamedEntityDAO(JdbcDaoProperties<E> properties) {
        super(properties);
        assert properties.getNameColumn().isPresent();
    }

    @Override
    public Optional<E> getByName(String name) {
        return listToOptional(new ArrayList<>(super.getSetByName(name)));
    }

}
