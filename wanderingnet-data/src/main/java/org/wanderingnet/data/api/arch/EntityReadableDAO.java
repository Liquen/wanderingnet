/*
 * Copyright (c) 2015 Stilavia
 */

package org.wanderingnet.data.api.arch;

import org.springframework.core.convert.converter.Converter;
import org.wanderingnet.common.Optional;
import org.wanderingnet.model.Entity;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by guillermoblascojimenez on 04/07/15.
 */
public interface EntityReadableDAO<E extends Entity> extends Converter<String, E>, PlainTableReadableDAO<E> {
    /**
     * Reads the entity with given id.
     *
     * @param id Id of requested entity.
     * @return AbstractEntity with matched id, null of none entity matches the id.
     */
    Optional<E> get(long id);

    Set<E> getAll(Long... ids);

    Set<E> getAll(Collection<Long> ids);

    Class<E> getEntityClass();

    Map<Long, E> getAllAsMap();
}
