package org.wanderingnet.model;


import com.google.common.base.MoreObjects;

public abstract class AbstractNamedEntity extends AbstractEntity implements NamedEntity {

    private String name;

    protected AbstractNamedEntity() {
        super();
    }

    protected AbstractNamedEntity(long id) {
        super(id);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", getId())
                .add("name", getName())
                .toString();
    }

}
