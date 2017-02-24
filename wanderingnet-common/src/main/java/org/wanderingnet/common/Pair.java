package org.wanderingnet.common;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by guillermoblascojimenez on 03/04/16.
 */
public class Pair<K,V> {
    private final K key;
    private final V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public static <K,V> Map<K,V> toMap(List<Pair<K,V>> pairs) {
        return pairs.stream()
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }
}
