package org.wanderingnet.data.jdbc.arch;

import org.wanderingnet.model.Entity;
import org.wanderingnet.model.NamedEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by guillermoblascojimenez on 09/05/15.
 */
public final class RowMappers {

    private RowMappers() {
    }

    public static <E> E setupEntity(E entity, ResultSet resultSet) throws SQLException {
        if (entity instanceof Entity) {
            ((Entity) entity).setId(resultSet.getLong("id"));
            if (entity instanceof NamedEntity) {
                ((NamedEntity) entity).setName(resultSet.getString("name"));
            }
        }

        return entity;
    }

}
