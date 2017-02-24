/*
 * Copyright (c) 2015 Stilavia
 */

package org.wanderingnet.data.api.arch;


import org.wanderingnet.common.Optional;
import org.wanderingnet.model.NamedEntity;

import java.util.Set;

/**
 * Created by guillermoblascojimenez on 04/07/15.
 */
public interface UniqueNamedEntityReadableDAO<E extends NamedEntity> extends EntityReadableDAO<E> {
    Optional<E> getByName(String name);

    /**
     * Executes exact match
     *
     * @param names
     * @return
     */
    Set<E> getByNameLike(String... names);

    Set<E> getByNameLike(boolean caseSensitive, String... names);
}
