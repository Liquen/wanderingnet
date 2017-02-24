package org.wanderingnet.data.jdbc.arch;

import java.util.Map;

/**
 * Interface to translate an entity to its table fields.
 */
public interface FieldMapper<R> {

    /**
     * Given an entity returns its map parametrization where keys are its column names.
     *
     * @param record record to map
     * @return Map such that keys are column names and values are row values
     */
    Map<String, Object> map(R record);
}
