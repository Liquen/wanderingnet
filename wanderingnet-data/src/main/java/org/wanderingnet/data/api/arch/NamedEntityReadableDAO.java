/*
 * Copyright (c) 2015 Stilavia
 */

package org.wanderingnet.data.api.arch;


import org.wanderingnet.model.NamedEntity;

import java.util.Collection;
import java.util.Set;

/**
 * Created by guillermoblascojimenez on 04/07/15.
 */
public interface NamedEntityReadableDAO<E extends NamedEntity> extends EntityReadableDAO<E> {
    /**
     * Finds by name entities. Since name is not always a unique key the
     * return type is a set of entities.
     *
     * @param name Name to search for.
     * @return Set of entities that matches this name.
     */
    Collection<E> getByName(String name);

    Set<E> getByNameLike(String... name);

    Set<E> getByNameLike(boolean caseSensitive, String... name);
}
