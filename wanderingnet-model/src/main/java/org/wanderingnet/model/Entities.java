package org.wanderingnet.model;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utilities for Entities
 */
public final class Entities {

    private static final Comparator<? super Entity> ID_ENTITY_COMPARATOR = new Comparator<Entity>() {
        @Override
        public int compare(Entity o1, Entity o2) {
            return Long.compare(o1.getId(), o2.getId());
        }
    };
    private static final Comparator<? super NamedEntity> NAME_ENTITY_COMPARATOR = new Comparator<NamedEntity>() {
        @Override
        public int compare(NamedEntity o1, NamedEntity o2) {
            return String.CASE_INSENSITIVE_ORDER.compare(o1.getName(), o2.getName());
        }
    };

    private Entities() {
    }

    public static <E extends Entity> Comparator<? super E> idComparator() {
        return ID_ENTITY_COMPARATOR;
    }

    public static <E extends NamedEntity> Comparator<? super E> nameComparator() {
        return NAME_ENTITY_COMPARATOR;
    }

    /**
     * Given a list of entities returns the list of its ids preserving the order.
     */
    public static <E extends Entity> List<Long> getIds(List<? extends E> entities) {
        return entities.stream().map(E::getId).collect(Collectors.toList());
    }

    /**
     * Given a list of entities returns the list of its ids preserving the order.
     */
    public static <E extends Entity> Collection<Long> getIds(Collection<? extends E> entities) {
        return entities.stream().map(E::getId).collect(Collectors.toList());
    }

    /**
     * Given a set of entities returns the set of its ids.
     */
    public static <E extends Entity> Set<Long> getIds(Set<E> entities) {
        return entities.stream().map(E::getId).collect(Collectors.toSet());
    }

    /**
     * Given a set of entities maps the ids to point the entities.
     */
    public static <E extends NamedEntity> java.util.Map<String, E> mapByName(Collection<E> entities) {

        assert entities != null;

        if (entities.isEmpty()) {
            return Collections.emptyMap();
        }

       java.util.Map<String, E> map = new HashMap<>(entities.size());

        for (E entity : entities) {
            assert entity != null;
            map.put(entity.getName(), entity);
        }

        return map;
    }

    /**
     * Given a set of entities maps the ids to point the entities.
     */
    public static <E extends Entity> java.util.Map<Long, E> map(Collection<E> entities) {
        return entities.stream().collect(Collectors.toMap(E::getId, Function.identity()));
    }

    public static <E extends NamedEntity> Set<String> getNames(Set<? extends E> entities) {
        return entities.stream().map(E::getName).collect(Collectors.toSet());
    }

    public static <E extends Entity> List<E> sortyById(Collection<? extends E> entities) {
        List<E> e = new ArrayList<>(entities);
        Collections.sort(e, ID_ENTITY_COMPARATOR);
        return e;
    }

    public static <E extends Entity> SortedSet<E> sortyById(Set<? extends E> entities) {
        SortedSet<E> e = new TreeSet<>(ID_ENTITY_COMPARATOR);
        e.addAll(entities);
        return e;
    }

    public static <E extends NamedEntity> List<E> sortyByName(Collection<? extends E> entities) {
        List<E> e = new ArrayList<>(entities);
        Collections.sort(e, NAME_ENTITY_COMPARATOR);
        return e;
    }

    public static <E extends NamedEntity> SortedSet<E> sortyByName(Set<? extends E> entities) {
        SortedSet<E> e = new TreeSet<>(Entities.NAME_ENTITY_COMPARATOR);
        e.addAll(entities);
        return e;
    }

    public static <E extends Entity, V> java.util.SortedMap<E, V> sortyById(java.util.Map<? extends E, V> entities) {
        SortedMap<E, V> e = new TreeMap<E, V>(ID_ENTITY_COMPARATOR);
        e.putAll(entities);
        return e;
    }

    public static <E extends NamedEntity, V> java.util.SortedMap<E, V> sortyByName(java.util.Map<? extends E, V> entities) {
        SortedMap<E, V> e = new TreeMap<E, V>(NAME_ENTITY_COMPARATOR);
        e.putAll(entities);
        return e;
    }

    public static <E extends Entity> boolean equals(E e1, E e2) {
        if (e1 != null && e2 != null) {
            return com.google.common.base.Objects.equal(e1.getClass(), e2.getClass()) &&
                    com.google.common.base.Objects.equal(e1.getId(), e2.getId());
        } else {
            return e1 == null && e2 == null;
        }
    }

    public static boolean isPersisted(Entity e) {
        return e.isPersisted();
    }

    public static boolean isPersisted(long entityId) {
        return entityId != AbstractEntity.ILLEGAL_ID;
    }

}
