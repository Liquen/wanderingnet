package org.wanderingnet.data.jdbc;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.wanderingnet.data.api.UserDAO;
import org.wanderingnet.data.jdbc.arch.*;
import org.wanderingnet.model.User;

import java.util.Collections;
import java.util.List;

/**
 * Created by guillermoblascojimenez on 03/04/16.
 */
@Repository
public class JdbcUserDAO extends JdbcAbstractNamedEntityDAO<User> implements UserDAO {
    public static final RowMapper<User> ROW_MAPPER = (resultSet, i) -> {
        User user = RowMappers.setupEntity(new User(), resultSet);
        user.setEmail(resultSet.getString("email"));
        user.setPasswordHash(resultSet.getString("password_hash"));
        user.setPasswordSalt(resultSet.getString("password_salt"));
        return user;
    };
    private static final FieldMapper<User> FIELD_MAPPER = record -> FieldMappers.mapBuilder(record)
            .put("email", record.getEmail())
            .put("password_salt", record.getPasswordSalt())
            .put("password_hash", record.getPasswordHash())
            .build();

    public JdbcUserDAO() {
        super((new JdbcDaoProperties<User>())
                        .withClass(User.class)
                        .withFieldMapper(FIELD_MAPPER)
                        .withRowMapper(ROW_MAPPER)
                        .withIdColumn()
                        .withNameColumn()
                        .withTableName("user")
                        .withFieldColumns(
                                "name",
                                "email",
                                "password_salt",
                                "password_hash",
                                "creation_date",
                                "facebook_token",
                                "gender_id",
                                "color_test_passed",
                                "eye_color_id",
                                "hair_color_id",
                                "skin_color_id",
                                "cookies_accepted",
                                "hints_explorer_left_viewed",
                                "hints_explorer_right_viewed",
                                "birth_date",
                                "email_verified",
                                "last_birthday_email_sent",
                                "country_id",
                                "google_analytics_hash",
                                "google_analytics_hash_set_on",
                                "mdc_hash"
                        )
        );
    }


    @Override
    public User findByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email=:email";
        List<User> user = getNamedParameterJdbcTemplate().query(sql, Collections.singletonMap("email", email), getRowMapper());
        return super.listToOptional(user).orNull();
    }

    @Override
    public boolean existsByEmail(String email) {
        return findByEmail(email) != null;
    }
}
