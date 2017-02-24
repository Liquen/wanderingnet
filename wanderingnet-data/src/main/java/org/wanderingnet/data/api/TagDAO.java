package org.wanderingnet.data.api;

import org.wanderingnet.data.api.arch.UniqueNamedEntityCrudDAO;
import org.wanderingnet.model.Document;
import org.wanderingnet.model.Tag;
import org.wanderingnet.model.User;

import java.util.List;
import java.util.Map;

/**
 * Created by guillermoblascojimenez on 03/04/16.
 */
public interface TagDAO extends UniqueNamedEntityCrudDAO<Tag> {
    Map<Tag,Integer> tagsOf(String docName);

    List<Tag> tagsOf(String docName, User user);

    void addTag(Tag tag, User user, Document document);

    void removeTag(Tag tag, User user, Document document);

    boolean hasTag(Tag tag, User user, Document document);
}
