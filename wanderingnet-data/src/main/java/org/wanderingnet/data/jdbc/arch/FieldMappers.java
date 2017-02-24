package org.wanderingnet.data.jdbc.arch;


import org.wanderingnet.model.Entity;
import org.wanderingnet.model.NamedEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by guillermoblascojimenez on 10/05/15.
 */
public final class FieldMappers {


    public static MapBuilder<String, Object> mapBuilder(Object entity) {
        MapBuilder<String, Object> builder = new MapBuilder<>();
        if (entity instanceof Entity) {
            builder.put("id", ((Entity) entity).getId());
            if (entity instanceof NamedEntity) {
                builder.put("name", ((NamedEntity) entity).getName());
            }
        }

        return builder;
    }

    public static class MapBuilder<K, V> {
        private final Map<K, V> map = new HashMap<>();

        public MapBuilder<K, V> put(K key, V value) {
            map.put(key, value);
            return this;
        }

        public Map<K, V> build() {
            return new HashMap<>(map);
        }
    }
}
