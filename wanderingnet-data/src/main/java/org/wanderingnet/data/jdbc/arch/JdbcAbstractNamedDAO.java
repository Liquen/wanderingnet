/*
 * Copyright (c) 2015 Stilavia
 */

package org.wanderingnet.data.jdbc.arch;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import org.wanderingnet.model.NamedEntity;

import java.util.*;

/**
 * Created by guillermoblascojimenez on 12/08/15.
 */
public class JdbcAbstractNamedDAO<E extends NamedEntity> extends JdbcAbstractEntityDAO<E> {
    protected final String selectNameQuery;
    protected final String nameColumn;
    protected final String selectNameLikeQuery;

    protected JdbcAbstractNamedDAO(JdbcDaoProperties<E> properties) {
        super(properties);
        this.nameColumn = properties.getNameColumn().get();
        this.selectNameLikeQuery = buildSelectRegexQuery(getTableName(), nameColumn);
        this.selectNameQuery = buildSelectQuery(getTableName(), nameColumn);
    }

    protected Set<E> getSetByName(String name) {
        Preconditions.checkNotNull(name);
        List<E> list = getNamedParameterJdbcTemplate().query(selectNameQuery, Collections.singletonMap(nameColumn, name), getRowMapper());
        return new HashSet<>(list);
    }

    public Set<E> getByNameLike(String... names) {
        return getByNameLike(true, names);
    }

    public Set<E> getByNameLike(boolean caseSensitive, String... names) {
        Preconditions.checkNotNull(names);
        Preconditions.checkArgument(names.length > 0);
        List<String> cleanNames = new ArrayList<>(names.length);
        if (caseSensitive) {
            for (String name : names) {
                if (!name.isEmpty()) {
                    cleanNames.add(name);
                }
            }
        } else {
            for (String name : names) {
                if (!name.isEmpty()) {
                    cleanNames.add(name.toUpperCase());
                    cleanNames.add(name.toLowerCase());
                }
            }
        }

        String regex = Joiner.on("|").join(cleanNames);
        List<E> list = getNamedParameterJdbcTemplate().query(selectNameLikeQuery, Collections.singletonMap(nameColumn, regex), getRowMapper());
        return new HashSet<>(list);
    }
}
