package org.wanderingnet.model;


import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Abstract entity represented by its id.
 */
public abstract class AbstractEntity implements Entity {

    static final long ILLEGAL_ID = -1;
    private long id;

    protected AbstractEntity() {
        this.id = ILLEGAL_ID; // not in database.
    }

    protected AbstractEntity(long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean isPersisted() {
        return id != ILLEGAL_ID;
    }

    @Override
    public boolean equals(Object o) {
        return
                o != null &&
                        Objects.equal(this.getClass(), o.getClass()) &&
                        Objects.equal(this.getId(), ((Entity) o).getId());
    }

    /**
     * Consider that if at some point the id of an entity is changed then this method is
     * unsafe. Be aware with hash maps if you are modifying the ids of entities.
     *
     * @return id of entity as hashCode
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", getId())
                .toString();
    }
}
