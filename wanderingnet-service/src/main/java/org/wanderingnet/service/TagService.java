package org.wanderingnet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wanderingnet.common.Optional;
import org.wanderingnet.data.api.DocumentDAO;
import org.wanderingnet.data.api.TagDAO;
import org.wanderingnet.model.Document;
import org.wanderingnet.model.Tag;
import org.wanderingnet.model.User;

/**
 * Created by guillermoblascojimenez on 03/04/16.
 */
@Service
public class TagService {

    @Autowired
    private TagDAO tagDAO;

    @Autowired
    private DocumentDAO documentDAO;

    @Transactional
    public void toggleTag(long tagId, User user, String documentName, String documentUrl) {
        Optional<Tag> tagOptional = tagDAO.get(tagId);
        if (tagOptional.isPresent()) {
            Tag tag = tagOptional.get();
            Document document = documentDAO.createIfNotExists(documentName, documentUrl);
            if (tagDAO.hasTag(tag, user, document)) {
                tagDAO.removeTag(tag, user, document);
            } else {
                tagDAO.addTag(tag, user, document);
            }
        }
    }

}
