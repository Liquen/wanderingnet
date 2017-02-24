package org.wanderingnet.data.api;

import org.wanderingnet.data.api.arch.UniqueNamedEntityCrudDAO;
import org.wanderingnet.model.Document;

/**
 * Created by guillermoblascojimenez on 03/04/16.
 */
public interface DocumentDAO extends UniqueNamedEntityCrudDAO<Document> {

    Document createIfNotExists(String documentName, String documentUrl);
}
