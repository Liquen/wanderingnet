package org.wanderingnet.data.jdbc.arch;


import org.wanderingnet.data.api.arch.NamedEntityCrudDAO;
import org.wanderingnet.model.NamedEntity;

import java.util.Collection;

/**
 * Created by guillermoblascojimenez on 09/05/15.
 */
public abstract class JdbcAbstractNamedEntityDAO<E extends NamedEntity> extends JdbcAbstractNamedDAO<E> implements NamedEntityCrudDAO<E> {


    protected JdbcAbstractNamedEntityDAO(JdbcDaoProperties<E> properties) {
        super(properties);
    }

    @Override
    public Collection<E> getByName(String name) {
        return super.getSetByName(name);
    }


}
