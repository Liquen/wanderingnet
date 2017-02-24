package org.wanderingnet.data.api.arch;


import org.wanderingnet.model.Entity;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Generic DAO for entities. Defines CRUD methods.
 *
 * @param <E> Entity type that DAO operates.
 */
public interface EntityCrudDAO<E extends Entity> extends PlainTableCrudDAO<E>, EntityReadableDAO<E> {


    /**
     * Creates a new entity.
     *
     * @param entity New entity to create.
     * @return The id assigned to created entity.
     * @throws IllegalArgumentException If the entity fails some logical constraint.
     */
    long create(E entity);

    /**
     * Updates the given entity.
     *
     * @param entity AbstractEntity to update.
     * @throws IllegalArgumentException If the entity fails some logical constraint.
     */
    int update(E entity);

    int bulkUpdate(Collection<? extends E> entity);

    int bulkUpdate(Stream<? extends E> entity);

    /**
     * Deletes the given entity by id.
     *
     * @param id Id of entity to delete.
     * @return true if the entity was properly deleted, false otherwise (e.g. was already deleted or id not found).
     */
    boolean delete(long id);

    boolean delete(E entity);

    Class<E> getEntityClass();

    Map<Long, E> getAllAsMap();

}
