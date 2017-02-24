package org.wanderingnet.data.jdbc;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.wanderingnet.common.Pair;
import org.wanderingnet.data.api.TagDAO;
import org.wanderingnet.data.jdbc.arch.*;
import org.wanderingnet.model.Document;
import org.wanderingnet.model.Tag;
import org.wanderingnet.model.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guillermoblascojimenez on 03/04/16.
 */
@Repository
public class JdbcTagDAO extends JdbcAbstractUniqueNamedEntityDAO<Tag> implements TagDAO {
    public static final RowMapper<Tag> ROW_MAPPER = (resultSet, i) -> {
        Tag tag = RowMappers.setupEntity(new Tag(), resultSet);
        tag.setIconName(resultSet.getString("icon_name"));
        return tag;
    };
    private static final FieldMapper<Tag> FIELD_MAPPER = record -> FieldMappers.mapBuilder(record)
            .put("icon_name", record.getIconName())
            .build();

    public JdbcTagDAO() {
        super((new JdbcDaoProperties<Tag>())
                        .withClass(Tag.class)
                        .withFieldMapper(FIELD_MAPPER)
                        .withRowMapper(ROW_MAPPER)
                        .withIdColumn()
                        .withNameColumn()
                        .withTableName("tag")
                        .withFieldColumns(
                                "name",
                                "icon_name"
                        )
        );
    }

    @Override
    public Map<Tag, Integer> tagsOf(String docName) {
        RowMapper<Pair<Tag, Integer>> rowMapper = (rs, i) -> {
            Tag tag = getRowMapper().mapRow(rs, i);
            Integer c = rs.getInt("c");
            return new Pair<>(tag, c);
        };
        return Pair.toMap(getNamedParameterJdbcTemplate().query(SQL_TAGS_OF, Collections.singletonMap("name", docName), rowMapper));
    }

    @Override
    public List<Tag> tagsOf(String docName, User user) {
        Map<String, Object> params = new HashMap<>();
        params.put("doc_name", docName);
        params.put("user_id", user.getId());
        return getNamedParameterJdbcTemplate().query(SQL_TAGS_OF_GIVEN_USER, params, getRowMapper());
    }

    @Override
    public void addTag(Tag tag, User user, Document document) {
        String sql = "INSERT INTO user_document_tag(user_id, document_id, tag_id) VALUES (:user_id, :document_id, :tag_id)";
        Map<String, Object> params = new HashMap<>(3);
        params.put("user_id", user.getId());
        params.put("document_id", document.getId());
        params.put("tag_id", tag.getId());
        getNamedParameterJdbcTemplate().update(sql, params);
    }

    @Override
    public void removeTag(Tag tag, User user, Document document) {
        String sql = "DELETE FROM user_document_tag WHERE user_id=:user_id AND document_id=:document_id AND tag_id=:tag_id";
        Map<String, Object> params = new HashMap<>(3);
        params.put("user_id", user.getId());
        params.put("document_id", document.getId());
        params.put("tag_id", tag.getId());
        getNamedParameterJdbcTemplate().update(sql, params);
    }

    @Override
    public boolean hasTag(Tag tag, User user, Document document) {
        String sql = "SELECT count(1) FROM user_document_tag WHERE user_id=:user_id AND document_id=:document_id AND tag_id=:tag_id";
        Map<String, Object> params = new HashMap<>(3);
        params.put("user_id", user.getId());
        params.put("document_id", document.getId());
        params.put("tag_id", tag.getId());
        return getNamedParameterJdbcTemplate().queryForObject(sql, params, Integer.class) > 0;
    }

    private static final String SQL_TAGS_OF_GIVEN_USER ="select \n" +
            "  t.*\n" +
            "from user_document_tag udt\n" +
            "join tag t on t.id=udt.tag_id\n" +
            "join document d on d.id=udt.document_id\n" +
            "where udt.user_id=:user_id and d.name=:doc_name";

    private static final String SQL_TAGS_OF = "SELECT\n" +
            "    t.*,\n" +
            "    count(DISTINCT udt.user_id) c\n" +
            "from tag t\n" +
            "LEFT JOIN (\n" +
            "    SELECT\n" +
            "      udt.tag_id,\n" +
            "      udt.document_id,\n" +
            "      udt.user_id\n" +
            "    FROM user_document_tag udt\n" +
            "      join document d on d.id=udt.document_id\n" +
            "    where d.name=:name\n" +
            "    ) udt ON t.id=udt.tag_id\n" +
            "GROUP BY t.id";
}
