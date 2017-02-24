package org.wanderingnet.data.jdbc.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.aspectj.lang.Aspects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.aspectj.AnnotationTransactionAspect;

import javax.sql.DataSource;

/**
 * Created by guillermoblascojimenez on 08/03/16.
 */
@Configuration
public class PersistenceConfig {

    @Value("${wanderingnet.db.name:wanderingnet}")
    private String dbName;
    @Value("${wanderingnet.db.port:3306}")
    private String dbPort;
    @Value("${wanderingnet.db.host:127.0.0.1}")
    private String dbHost;
    @Value("${wanderingnet.db.user:root}")
    private String dbUser;
    @Value("${wanderingnet.db.password:root}")
    private String dbPassword;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName);
        config.setUsername(dbUser);
        config.setPassword(dbPassword);
        //config.setDataSourceClassName("org.mariadb.jdbc.MySQLDataSource");
        return new HikariDataSource(config);
    }

    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return getNamedParameterJdbcTemplate(dataSource());
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }

    @Bean
    public AnnotationTransactionAspect annotationTransactionAspect() {
        AnnotationTransactionAspect aspect = Aspects.aspectOf(AnnotationTransactionAspect.class);
        aspect.setTransactionManager(platformTransactionManager());
        return aspect;
    }

}
