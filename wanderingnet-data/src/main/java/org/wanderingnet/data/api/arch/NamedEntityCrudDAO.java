package org.wanderingnet.data.api.arch;


import org.wanderingnet.model.NamedEntity;

/**
 * Generic DAO for named entities. Adds find by name.
 *
 * @param <E>
 */
public interface NamedEntityCrudDAO<E extends NamedEntity> extends EntityCrudDAO<E>, NamedEntityReadableDAO<E> {

}
