package org.wanderingnet.data.api.arch;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Created by guillermoblascojimenez on 18/05/15.
 */
public interface PlainTableCrudDAO<E> extends PlainTableReadableDAO<E> {

    int count();

    Set<E> getAll();

    List<E> getAllAsList();

    int bulkInsert(Collection<? extends E> entities);

    int bulkInsert(Stream<? extends E> entities);

    boolean insert(E record);

    boolean insertIgnore(E record);

    int deleteAll();

    int bulkInsertIgnore(Collection<? extends E> entities);

    int bulkInsertIgnore(Stream<? extends E> entities);

}
