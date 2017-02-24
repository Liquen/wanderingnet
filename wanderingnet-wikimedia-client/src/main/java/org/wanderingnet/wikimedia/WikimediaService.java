package org.wanderingnet.wikimedia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by guillermoblascojimenez on 28/03/16.
 */
@Service
public class WikimediaService {

    @Autowired
    private WikimediaClient wikimediaClient;

    public String extract(String title) {
        return wikimediaClient.getExtract(title);
    }

    public List<String> subcategoriesOf(String category) {
        return wikimediaClient.subcategoriesOf(category);
    }

}
