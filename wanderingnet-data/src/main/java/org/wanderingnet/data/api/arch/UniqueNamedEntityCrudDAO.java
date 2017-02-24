package org.wanderingnet.data.api.arch;


import org.wanderingnet.model.NamedEntity;

/**
 * @author guillermo
 */
public interface UniqueNamedEntityCrudDAO<E extends NamedEntity> extends EntityCrudDAO<E>, UniqueNamedEntityReadableDAO<E> {

}
