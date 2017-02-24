package org.wanderingnet.data.jdbc.arch;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.wanderingnet.data.api.arch.PlainTableCrudDAO;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by guillermoblascojimenez on 17/05/15.
 */
public abstract class PlainTableJdbcDAO<E> extends JdbcAbstractDAO implements PlainTableCrudDAO<E> {

    private static final String SELECT_ALL_TEMPLATE = "SELECT * FROM `%s`";
    private static final String COUNT_TEMPLATE = "SELECT COUNT(1) FROM `%s`";
    private static final String INSERT_TEMPLATE = "INSERT INTO `%s`(%s) VALUES (%s)";
    private static final String INSERT_IGNORE_TEMPLATE = "INSERT IGNORE INTO `%s`(%s) VALUES (%s)";
    private static final String DELETE_TEMPLATE = "DELETE FROM `%s`";
    private final List<String> columns;
    private final RowMapper<E> rowMapper;
    private final FieldMapper<E> fieldMapper;
    private final String tableName;
    private final String selectAllQuery;
    private final String countQuery;
    private final String insertQuery;
    private final String insertIgnoreQuery;
    private final String deleteQuery;
    private final boolean staticEntity;
    private final String deleteAllQuery;

    protected PlainTableJdbcDAO(JdbcDaoProperties<E> properties) {
        assert properties != null;
        assert properties.getRowMapper() != null;
        assert properties.getTableName() != null;
        assert properties.getFieldColumnAsList() != null;
        assert properties.getAllColumnsAsList() != null;

        this.rowMapper = properties.getRowMapper();
        this.tableName = properties.getTableName();
        this.fieldMapper = properties.getFieldMapper();
        this.columns = properties.getAllColumnsAsList();
        this.staticEntity = properties.isStaticEntity();

        this.selectAllQuery = String.format(SELECT_ALL_TEMPLATE, tableName);
        this.countQuery = String.format(COUNT_TEMPLATE, tableName);
        this.insertQuery = buildInsertQuery(tableName, properties.getFieldColumnAsList());
        this.deleteQuery = String.format(DELETE_TEMPLATE, tableName);
        this.deleteAllQuery = String.format(DELETE_TEMPLATE, tableName);
        this.insertIgnoreQuery = buildInsertIgnoreQuery(tableName, properties.getFieldColumnAsList());
    }

    private static String buildInsertQuery(String tableName, List<String> columns) {
        String cols = "`" + Joiner.on("`,`").join(columns) + "`";
        String valsTemplate = ":%s";
        List<String> values = new ArrayList<>(columns.size());
        for (String column : columns) {
            values.add(String.format(valsTemplate, column));
        }
        String vals = Joiner.on(",").join(values);
        return String.format(INSERT_TEMPLATE, tableName, cols, vals);
    }

    private static String buildInsertIgnoreQuery(String tableName, List<String> columns) {
        String cols = "`" + Joiner.on("`,`").join(columns) + "`";
        String valsTemplate = ":%s";
        List<String> values = new ArrayList<>(columns.size());
        for (String column : columns) {
            values.add(String.format(valsTemplate, column));
        }
        String vals = Joiner.on(",").join(values);
        return String.format(INSERT_IGNORE_TEMPLATE, tableName, cols, vals);
    }

    public boolean isStaticEntity() {
        return staticEntity;
    }

    @Override
    public int count() {
        return getNamedParameterJdbcTemplate().queryForObject(countQuery, Collections.<String, Object>emptyMap(), Integer.class);
    }

    @Override
    public List<E> getAllAsList() {
        return getNamedParameterJdbcTemplate().query(selectAllQuery, getRowMapper());
    }

    @Override
    public Set<E> getAll() {
        return new HashSet<>(getAllAsList());
    }

    @Override
    public Set<E> getAll(int amount) {
        return getAll(0, amount);
    }

    @Override
    public List<E> getAllAsList(int amount) {
        return getAllAsList(0, amount);
    }

    @Override
    public Set<E> getAll(int offset, int amount) {
        return new HashSet<>(getAllAsList(offset, amount));
    }

    @Override
    public List<E> getAllAsList(int offset, int amount) {
        String query = getSelectAllQuery() + " LIMIT " + String.valueOf(offset) + "," + String.valueOf(amount);
        return getNamedParameterJdbcTemplate().query(query, getRowMapper());
    }

    @Override
    public boolean insertIgnore(E record) {
        Preconditions.checkNotNull(record);
        checkWriteAllowed();
        Map<String, Object> params = getFieldMapper().map(record);
        int i = getNamedParameterJdbcTemplate().update(getInsertIgnoreQuery(params.keySet()), new MapSqlParameterSource(params));
        return i > 0;
    }

    @Override
    public int bulkInsertIgnore(Collection<? extends E> entities) {
        Preconditions.checkNotNull(entities);
        if (entities.isEmpty()) {
            return 0;
        }
        checkWriteAllowed();
        List<Map<String, Object>> paramsList = new ArrayList<>();
        for (E entity : entities) {
            paramsList.add(getFieldMapper().map(entity));
        }
        String query = this.getInsertIgnoreQuery(paramsList.get(0).keySet());
        @SuppressWarnings("unchecked")
        Map<String, Object>[] paramsArray = paramsList.toArray((Map<String, Object>[]) new Map[paramsList.size()]);
        int[] updates = getNamedParameterJdbcTemplate().batchUpdate(query, paramsArray);
        int up = 0;
        for (int i : updates) {
            up += i;
        }
        return up;
    }

    @Override
    public int bulkInsertIgnore(Stream<? extends E> entities) {
        return bulkInsertIgnore(entities.collect(Collectors.toList()));
    }

    @Override
    public int bulkInsert(Collection<? extends E> entities) {
        Preconditions.checkNotNull(entities);
        if (entities.isEmpty()) {
            return 0;
        }
        checkWriteAllowed();
        List<Map<String, Object>> paramsList = new ArrayList<>();
        for (E entity : entities) {
            paramsList.add(getFieldMapper().map(entity));
        }
        String query = this.getInsertQuery(paramsList.get(0).keySet());
        @SuppressWarnings("unchecked")
        Map<String, Object>[] paramsArray = paramsList.toArray((Map<String, Object>[]) new Map[paramsList.size()]);
        int[] updates = getNamedParameterJdbcTemplate().batchUpdate(query, paramsArray);
        return Arrays.stream(updates).sum();
    }

    @Override
    public int bulkInsert(Stream<? extends E> entities) {
        return bulkInsert(entities.collect(Collectors.toList()));
    }

    @Override
    public boolean insert(E record) {
        Preconditions.checkNotNull(record);
        checkWriteAllowed();
        Map<String, Object> params = getFieldMapper().map(record);
        int i = getNamedParameterJdbcTemplate().update(getInsertQuery(params.keySet()), new MapSqlParameterSource(params));
        return i > 0;
    }

    @Override
    public int deleteAll() {
        checkWriteAllowed();
        return getNamedParameterJdbcTemplate().update(deleteAllQuery, Collections.<String, Object>emptyMap());
    }

    protected void checkWriteAllowed() {
        if (this.isStaticEntity()) {
            throw new IllegalStateException(String.format("Unable to write static table %s from %s.", tableName, this.getClass().getCanonicalName()));
        }
    }

    protected String getDeleteQuery() {
        return deleteQuery;
    }

    protected String getInsertQuery(Collection<String> columns) {
        List<String> insertableColumns = new ArrayList<>(getColumns());
        insertableColumns.retainAll(columns);
        return buildInsertQuery(tableName, insertableColumns);
    }
    protected String getInsertIgnoreQuery(Collection<String> columns) {
        List<String> insertableColumns = new ArrayList<>(getColumns());
        insertableColumns.retainAll(columns);
        return buildInsertIgnoreQuery(tableName, insertableColumns);
    }


    protected FieldMapper<E> getFieldMapper() {
        return fieldMapper;
    }

    protected List<String> getColumns() {
        return columns;
    }

    protected RowMapper<E> getRowMapper() {
        return rowMapper;
    }

    protected String getTableName() {
        return tableName;
    }

    protected String getSelectAllQuery() {
        return selectAllQuery;
    }

    protected String getCountQuery() {
        return countQuery;
    }

    protected <T> org.wanderingnet.common.Optional<T> listToOptional(List<T> list) {
        if (list == null) {
            return null;
        }
        if (list.isEmpty()) {
            return org.wanderingnet.common.Optional.empty();
        }
        if (list.size() == 1) {
            return org.wanderingnet.common.Optional.ofNullable(list.get(0));
        } else {
            throw new IllegalAccessError("Expected one unique result on select for unique key in dao " + this.getClass().getCanonicalName() + " instead got " + list.size() + " results.");
        }
    }
}
