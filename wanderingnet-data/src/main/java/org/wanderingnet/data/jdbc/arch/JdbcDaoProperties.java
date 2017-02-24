package org.wanderingnet.data.jdbc.arch;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import java.util.*;
import java.util.function.Consumer;

/**
 * Created by guillermoblascojimenez on 25/05/15.
 */
public class JdbcDaoProperties<E> {

    private static final String DEFAULT_ID_COLUMN = "id";
    private static final String DEFAULT_NAME_COLUMN = "name";

    private Class<E> entityClass;
    private RowMapper<E> rowMapper;
    private FieldMapper<E> fieldMapper;
    private String[] fieldColumns;
    private Optional<String> idColumn = Optional.of(DEFAULT_ID_COLUMN);
    private String tableName;
    private Optional<String> nameColumn = Optional.of(DEFAULT_NAME_COLUMN);
    private boolean staticEntity = false;
    private List<Consumer<E>> onCreatePostprocessors = new ArrayList<>();
    private List<Consumer<E>> onCreatePreprocessors = new ArrayList<>();

    public JdbcDaoProperties<E> withClass(Class<E> entityClass) {
        assert entityClass != null;
        this.entityClass = entityClass;
        return this;
    }

    public JdbcDaoProperties<E> withRowMapper(RowMapper<E> rowMapper) {
        assert rowMapper != null;
        this.rowMapper = rowMapper;
        return this;
    }

    public JdbcDaoProperties<E> withDefaultRowMapper(Class<E> entityClass) {
        assert rowMapper != null;
        this.rowMapper = new BeanPropertyRowMapper<>(entityClass);
        return this;
    }

    public JdbcDaoProperties<E> withFieldMapper(FieldMapper<E> fieldMapper) {
        assert fieldMapper != null;
        this.fieldMapper = fieldMapper;
        return this;
    }

    public JdbcDaoProperties<E> withFieldColumns(String... fieldColumns) {
        assert fieldColumns != null;
        this.fieldColumns = fieldColumns;
        return this;
    }

    public JdbcDaoProperties<E> withIdColumn() {
        return withIdColumn(DEFAULT_ID_COLUMN);
    }

    public JdbcDaoProperties<E> withIdColumn(String idColumn) {
        assert idColumn != null;
        this.idColumn = Optional.of(idColumn);
        return this;
    }

    public JdbcDaoProperties<E> withoutIdColumn() {
        this.idColumn = Optional.empty();
        return this;
    }

    public JdbcDaoProperties<E> withNameColumn() {
        return withNameColumn(DEFAULT_NAME_COLUMN);
    }

    public JdbcDaoProperties<E> withNameColumn(String nameColumn) {
        assert nameColumn != null;
        this.nameColumn = Optional.of(nameColumn);
        return this;
    }

    public JdbcDaoProperties<E> withoutNameColumn() {
        this.nameColumn = Optional.empty();
        return this;
    }

    public JdbcDaoProperties<E> withTableName(String tableName) {
        assert tableName != null;
        this.tableName = tableName;
        return this;
    }

    public Class<E> getEntityClass() {
        return entityClass;
    }

    public RowMapper<E> getRowMapper() {
        return rowMapper;
    }

    public FieldMapper<E> getFieldMapper() {
        return fieldMapper;
    }

    public String[] getFieldColumns() {
        return fieldColumns;
    }

    public Optional<String> getIdColumn() {
        return idColumn;
    }

    public String getTableName() {
        return tableName;
    }

    public boolean isStaticEntity() {
        return staticEntity;
    }

    public JdbcDaoProperties<E> setStaticEntity(boolean staticEntity) {
        this.staticEntity = staticEntity;
        return this;
    }

    public Optional<String> getNameColumn() {
        return nameColumn;
    }

    public List<String> getAllColumnsAsList() {
        return new ArrayList<>(getAllColumnsAsSet());
    }

    public String[] getAllColumnsAsArray() {
        List<String> all = getAllColumnsAsList();
        return all.toArray(new String[all.size()]);
    }

    public Set<String> getAllColumnsAsSet() {
        Set<String> columns = getFieldColumnsAsSet();
        if (idColumn.isPresent()) {
            columns.add(idColumn.get());
        }
        if (nameColumn.isPresent()) {
            columns.add(nameColumn.get());
        }
        return columns;
    }

    public Set<String> getFieldColumnsAsSet() {
        Set<String> columns = new HashSet<>();
        Collections.addAll(columns, fieldColumns);
        return columns;
    }

    public List<String> getFieldColumnAsList() {
        return Arrays.asList(this.fieldColumns);
    }

    public List<Consumer<E>> getOnCreatePostprocessors() {
        return onCreatePostprocessors;
    }

    public JdbcDaoProperties<E> setOnCreatePostprocessors(List<Consumer<E>> onCreatePostprocessors) {
        this.onCreatePostprocessors = onCreatePostprocessors;
        return this;
    }

    public JdbcDaoProperties<E> addOnCreatePostprocessor(Consumer<E> onCreatePostprocessor) {
        this.onCreatePostprocessors.add(onCreatePostprocessor);
        return this;
    }

    public List<Consumer<E>> getOnCreatePreprocessors() {
        return onCreatePreprocessors;
    }

    public JdbcDaoProperties<E> setOnCreatePreprocessors(List<Consumer<E>> onCreatePreprocessors) {
        this.onCreatePreprocessors = onCreatePreprocessors;
        return this;
    }

    public JdbcDaoProperties<E> addOnCreatePreprocessor(Consumer<E> onCreatePreprocessor) {
        this.onCreatePreprocessors.add(onCreatePreprocessor);
        return this;
    }
}
