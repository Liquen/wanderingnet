package org.wanderingnet.data.jdbc.arch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.wanderingnet.data.api.arch.DAO;

import javax.sql.DataSource;

/**
 * Created by guillermoblascojimenez on 09/05/15.
 */
@Repository
public abstract class JdbcAbstractDAO implements DAO {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private DataSource dataSource;

    protected DataSource getDataSource() {
        return dataSource;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return namedParameterJdbcTemplate;
    }

    @Autowired
    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

}
