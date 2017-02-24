package org.wanderingnet.model;

/**
 * Created by guillermoblascojimenez on 02/04/16.
 */
public class Document extends AbstractNamedEntity {

    public Document() {
    }

    public Document(long id) {
        super(id);
    }

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
