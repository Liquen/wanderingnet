package org.wanderingnet.data.jdbc.arch;

import com.google.common.base.CharMatcher;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.wanderingnet.common.*;
import org.wanderingnet.data.api.arch.EntityCrudDAO;
import org.wanderingnet.model.Entities;
import org.wanderingnet.model.Entity;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by guillermoblascojimenez on 09/05/15.
 */
public abstract class JdbcAbstractEntityDAO<E extends Entity> extends PlainTableJdbcDAO<E> implements EntityCrudDAO<E> {

    private static final Logger LOG = LoggerFactory.getLogger(JdbcAbstractEntityDAO.class);
    private static final String SELECT_TEMPLATE = "SELECT * FROM `%s` WHERE %s";
    private static final String DELETE_TEMPLATE = "DELETE FROM `%s` WHERE %s";
    private static final String UPDATE_TEMPLATE = "UPDATE `%s` SET %s WHERE %s";
    private final Class<E> entityClass;
    private final String idColumn;
    private final String[] idColumnAsArray;
    private final List<String> fieldColumnList;
    private final String selectQuery;
    private final String deleteQuery;
    private final String updateQuery;
    private final String selectInQuery;

    private final List<Consumer<E>> onCreatePreprocessors = new ArrayList<>();
    private final List<Consumer<E>> onCreatePostprocessors = new ArrayList<>();

    protected JdbcAbstractEntityDAO(JdbcDaoProperties<E> properties) {
        super(properties);
        assert properties.getIdColumn().isPresent();
        assert properties.getFieldColumnAsList() != null;
        assert properties.getEntityClass() != null;

        this.idColumn = properties.getIdColumn().get();
        this.fieldColumnList = properties.getFieldColumnAsList();
        this.entityClass = properties.getEntityClass();
        this.idColumnAsArray = new String[]{this.idColumn};

        this.selectQuery = buildSelectQuery(super.getTableName(), idColumn);
        this.deleteQuery = buildDeleteQuery(super.getTableName(), idColumn);
        this.updateQuery = buildUpdateQuery(super.getTableName(), fieldColumnList, idColumn);
        this.selectInQuery = buildSelectInQuery(super.getTableName(), idColumn);
        this.onCreatePostprocessors.addAll(properties.getOnCreatePostprocessors());
        this.onCreatePreprocessors.addAll(properties.getOnCreatePreprocessors());
    }

    public static String buildUpdateQuery(String tableName, List<String> columns, String idColumn) {
        String valsTemplate = "`%s`=:%s";
        List<String> values = new ArrayList<>(columns.size());
        for (String column : columns) {
            values.add(String.format(valsTemplate, column, column));
        }
        String sets = Joiner.on(",").join(values);
        String where = String.format(valsTemplate, idColumn, idColumn);
        return String.format(UPDATE_TEMPLATE, tableName, sets, where);
    }

    public static String buildSelectQuery(String tableName, String idColumn) {
        String valsTemplate = "`%s`=:%s";
        String where = String.format(valsTemplate, idColumn, idColumn);
        return String.format(SELECT_TEMPLATE, tableName, where);
    }

    public static String buildSelectInQuery(String tableName, String idColumn) {
        String valsTemplate = "`%s` IN (:%s)";
        String where = String.format(valsTemplate, idColumn, idColumn);
        return String.format(SELECT_TEMPLATE, tableName, where);
    }

    public static String buildDeleteQuery(String tableName, String idColumn) {
        String valsTemplate = "`%s`=:%s";
        String where = String.format(valsTemplate, idColumn, idColumn);
        return String.format(DELETE_TEMPLATE, tableName, where);
    }

    @Override
    public Set<E> getAll(Long... ids) {
        return getAll(Lists.<Long>newArrayList(ids));
    }

    @Override
    public Set<E> getAll(Collection<Long> ids) {
        Preconditions.checkNotNull(ids);
        if (ids.isEmpty()) {
            return new HashSet<>();
        }
        List<E> list = getNamedParameterJdbcTemplate().query(selectInQuery, Collections.singletonMap(this.idColumn, ids), getRowMapper());
        return new HashSet<E>(list);
    }

    protected String[] getIdColumns() {
        return idColumnAsArray;
    }

    public Class<E> getEntityClass() {
        return entityClass;
    }

    @Override
    public org.wanderingnet.common.Optional<E> get(long id) {
        List<E> list = getNamedParameterJdbcTemplate().query(selectQuery, Collections.singletonMap(this.idColumn, id), getRowMapper());
        return listToOptional(list);
    }

    @Override
    public List<E> getAllAsList() {
        List<E> list = super.getAllAsList();
        Collections.sort(list, Entities.idComparator());
        return list;
    }

    @Override
    public Map<Long, E> getAllAsMap() {
        Map<Long, E> map = Maps.uniqueIndex(super.getAllAsList(), new Function<E, Long>() {
            @Override
            public Long apply(E input) {
                return input.getId();
            }
        });
        return map;
    }

    @Override
    public Set<E> getAll() {
        return new HashSet<>(super.getAllAsList()); // avoid sort elements to put them later in a set.
    }

    @Override
    public boolean delete(long id) {
        checkWriteAllowed();
        int v = getNamedParameterJdbcTemplate().update(deleteQuery, Collections.singletonMap(this.idColumn, id));
        return v > 0;
    }

    @Override
    public boolean delete(E entity) {
        checkWriteAllowed();
        Preconditions.checkNotNull(entity);
        if (entity.isPersisted()) {
            return this.delete(entity.getId());
        }
        return false;
    }

    @Override
    public int update(E entity) {
        checkWriteAllowed();
        Map<String, Object> params = getFieldMapper().map(entity);
        List<String> writableColumns = new ArrayList<>(fieldColumnList);
        writableColumns.retainAll(params.keySet());
        int n = getNamedParameterJdbcTemplate().update(getUpdateQuery(writableColumns), params);
        return n;
    }

    @Override
    public int bulkUpdate(Collection<? extends E> entities) {
        checkWriteAllowed();
        if (entities.isEmpty()) {
            return 0;
        }
        if (entities.size() == 1) {
            return update(entities.iterator().next());
        }
        List<Map<String, Object>> paramsList = new ArrayList<>();
        for (E entity : entities) {
            paramsList.add(getFieldMapper().map(entity));
        }
        List<String> writableColumns = new ArrayList<>(fieldColumnList);
        writableColumns.retainAll(paramsList.get(0).keySet());
        @SuppressWarnings("unchecked")
        Map<String, Object>[] paramsArray = paramsList.toArray((Map<String, Object>[]) new Map[paramsList.size()]);
        int[] updates = getNamedParameterJdbcTemplate().batchUpdate(getUpdateQuery(writableColumns), paramsArray);
        return Arrays.stream(updates).sum();
    }

    @Override
    public int bulkUpdate(Stream<? extends E> entities) {
        return bulkUpdate(entities.collect(Collectors.toList()));
    }

    protected String buildSelectRegexQuery(String tableName, String idColumn) {
        String valsTemplate = "`%s` REGEXP :%s";
        String where = String.format(valsTemplate, idColumn, idColumn);
        return String.format(SELECT_TEMPLATE, tableName, where);
    }

    @Override
    public long create(E entity) {
        checkWriteAllowed();
        this.onCreatePreprocessors.stream().forEach((e) -> e.accept(entity));
        Map<String, Object> params = getFieldMapper().map(entity);
        List<String> columns = new ArrayList<>(super.getColumns());
        columns.retainAll(params.keySet());
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        getNamedParameterJdbcTemplate().update(getInsertQuery(columns), new MapSqlParameterSource(params), keyHolder, getIdColumns());
        long id = keyHolder.getKey().longValue();
        entity.setId(id);
        this.onCreatePostprocessors.stream().forEach((e) -> e.accept(entity));
        return id;
    }

    @Override
    protected String getInsertQuery(Collection<String> columns) {
        for (String idCol : getIdColumns()) {
            columns.remove(idCol);
        }
        return super.getInsertQuery(columns);
    }

    @Override
    protected String getInsertIgnoreQuery(Collection<String> columns) {
        for (String idCol : getIdColumns()) {
            columns.remove(idCol);
        }
        return super.getInsertIgnoreQuery(columns);
    }

    @Override
    public E convert(String source) {
        if (source.isEmpty()) {
            return null;
        }
        if (CharMatcher.DIGIT.matchesAllOf(source)) {
            Long id = Long.valueOf(source);
            return this.get(id).orElse(null);
        }
        LOG.debug("Could not convert \"{}\" to entity {}", source, this.getEntityClass());
        return null;
    }

    @Override
    protected void checkWriteAllowed() {
        if (this.isStaticEntity()) {
            throw new IllegalStateException(String.format(
                    "Unable to write static entity %s with table %s from %s.",
                    getEntityClass().getCanonicalName(),
                    super.getTableName(),
                    getClass().getCanonicalName()));
        }
    }

    protected String getDeleteQuery() {
        return deleteQuery;
    }

    protected String getUpdateQuery(Collection<String> columns) {
        return buildUpdateQuery(super.getTableName(), new ArrayList<>(columns), idColumn);
    }

}
