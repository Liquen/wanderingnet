package org.wanderingnet.model;

/**
 * Created by guillermoblascojimenez on 09/04/16.
 */
public class Map extends AbstractNamedEntity {

    private String humanName;
    private String nameKey;
    private String mapFileName;
    private int size;

    public Map() {
    }

    public Map(long id) {
        super(id);
    }

    public String getHumanName() {
        return humanName;
    }

    public void setHumanName(String humanName) {
        this.humanName = humanName;
    }

    public String getNameKey() {
        return nameKey;
    }

    public void setNameKey(String nameKey) {
        this.nameKey = nameKey;
    }

    public String getMapFileName() {
        return mapFileName;
    }

    public void setMapFileName(String mapFileName) {
        this.mapFileName = mapFileName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
