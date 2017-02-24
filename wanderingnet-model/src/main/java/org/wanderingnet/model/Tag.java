package org.wanderingnet.model;

/**
 * Created by guillermoblascojimenez on 02/04/16.
 */
public class Tag extends AbstractNamedEntity {

    private String iconName;

    public Tag() {
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }
}
