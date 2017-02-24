package org.wanderingnet.model;


import java.io.Serializable;

public interface Entity extends Serializable {

    /**
     * Ids go from 0 to up. A id with value -1 means that the entity is not in the database
     * or is not updated.
     *
     * @return id of the entity.
     */
    long getId();

    void setId(long id);

    boolean isPersisted();

}
