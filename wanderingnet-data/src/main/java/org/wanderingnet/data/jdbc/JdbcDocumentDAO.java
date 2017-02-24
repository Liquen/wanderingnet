package org.wanderingnet.data.jdbc;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.wanderingnet.common.Optional;
import org.wanderingnet.data.api.DocumentDAO;
import org.wanderingnet.data.jdbc.arch.*;
import org.wanderingnet.model.Document;

/**
 * Created by guillermoblascojimenez on 03/04/16.
 */
@Repository
public class JdbcDocumentDAO extends JdbcAbstractUniqueNamedEntityDAO<Document> implements DocumentDAO {
    public static final RowMapper<Document> ROW_MAPPER = (resultSet, i) -> {
        Document document = RowMappers.setupEntity(new Document(), resultSet);
        document.setUrl(resultSet.getString("url"));
        return document;
    };
    private static final FieldMapper<Document> FIELD_MAPPER = record -> FieldMappers.mapBuilder(record)
            .put("url", record.getUrl())
            .build();

    public JdbcDocumentDAO() {
        super((new JdbcDaoProperties<Document>())
                        .withClass(Document.class)
                        .withFieldMapper(FIELD_MAPPER)
                        .withRowMapper(ROW_MAPPER)
                        .withIdColumn()
                        .withNameColumn()
                        .withTableName("document")
                        .withFieldColumns(
                                "name",
                                "url"
                        )
        );
    }


    @Override
    @Transactional
    public Document createIfNotExists(String documentName, String documentUrl) {
        Optional<Document> documentOptional = getByName(documentName);
        if (documentOptional.isNotPresent()) {
            Document document = new Document();
            document.setUrl(documentUrl);
            document.setName(documentName);
            create(document);
            return document;
        } else {
            return documentOptional.get();
        }
    }

}
