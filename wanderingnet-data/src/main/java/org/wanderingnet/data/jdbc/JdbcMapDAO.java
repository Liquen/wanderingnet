package org.wanderingnet.data.jdbc;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.wanderingnet.common.Optional;
import org.wanderingnet.data.api.MapDAO;
import org.wanderingnet.data.jdbc.arch.*;

import java.util.Collections;

/**
 * Created by guillermoblascojimenez on 03/04/16.
 */
@Repository
public class JdbcMapDAO extends JdbcAbstractUniqueNamedEntityDAO<org.wanderingnet.model.Map> implements MapDAO {
    public static final RowMapper<org.wanderingnet.model.Map> ROW_MAPPER = (resultSet, i) -> {
        org.wanderingnet.model.Map map = RowMappers.setupEntity(new org.wanderingnet.model.Map(), resultSet);
        map.setHumanName(resultSet.getString("human_name"));
        map.setMapFileName(resultSet.getString("map_file_name"));
        map.setNameKey(resultSet.getString("name_key"));
        map.setSize(resultSet.getInt("size"));
        return map;
    };
    private static final FieldMapper<org.wanderingnet.model.Map> FIELD_MAPPER = record -> FieldMappers.mapBuilder(record)
            .put("map_file_name", record.getMapFileName())
            .put("size", record.getSize())
            .put("human_name", record.getHumanName())
            .put("name_key", record.getNameKey())
            .build();

    public JdbcMapDAO() {
        super((new JdbcDaoProperties<org.wanderingnet.model.Map>())
                        .withClass(org.wanderingnet.model.Map.class)
                        .withFieldMapper(FIELD_MAPPER)
                        .withRowMapper(ROW_MAPPER)
                        .withIdColumn()
                        .withNameColumn()
                        .withTableName("map")
                        .withFieldColumns(
                                "name",
                                "human_name",
                                "name_key",
                                "map_file_name",
                                "size"
                        )
        );
    }

    @Override
    public Optional<org.wanderingnet.model.Map> mapOfNameKey(String nameKey) {
        String sql = "SELECT * FROM map where name_key=:name_key";
        return listToOptional(getNamedParameterJdbcTemplate().query(sql, Collections.singletonMap("name_key", nameKey), getRowMapper()));
    }
}
