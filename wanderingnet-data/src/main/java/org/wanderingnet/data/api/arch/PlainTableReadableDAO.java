/*
 * Copyright (c) 2015 Stilavia
 */

package org.wanderingnet.data.api.arch;

import java.util.List;
import java.util.Set;

/**
 * Created by guillermoblascojimenez on 04/07/15.
 */
public interface PlainTableReadableDAO<E> extends DAO {
    int count();

    Set<E> getAll();

    List<E> getAllAsList();

    Set<E> getAll(int amount);

    List<E> getAllAsList(int amount);

    Set<E> getAll(int offset, int amount);

    List<E> getAllAsList(int offset, int amount);
}
