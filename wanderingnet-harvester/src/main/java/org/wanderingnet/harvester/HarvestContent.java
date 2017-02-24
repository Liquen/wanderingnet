package org.wanderingnet.harvester;

import java.util.Optional;

/**
 * Created by guillermoblascojimenez on 01/03/16.
 */
public interface HarvestContent {

    enum Status {
        SUCCESS,
        FAILURE;

        public Status combine(Status status) {
            if (status.isFailure()) {
                return FAILURE;
            } else {
                return this;
            }
        }

        public boolean isSuccess() {
            return SUCCESS.equals(this);
        }

        public boolean isFailure() {
            return FAILURE.equals(this);
        }
    }

    Status getStatus();

    Optional<String> getTitle();

    Optional<String> getDescription();

    Optional<String> getImage();

    long score();

    HarvestContent combine(HarvestContent harvestContent);

    static HarvestContent failure() {
        return new SimpleHarvestContent(Status.FAILURE, Optional.<String>empty(), Optional.<String>empty(), Optional.<String>empty());
    }
    static HarvestContent of(Optional<String> title, Optional<String> description, Optional<String> image) {
        return new SimpleHarvestContent(Status.SUCCESS, title, description, image);
    }

    static HarvestContent ofNullables(Status status, String title, String description, String image) {
        return new SimpleHarvestContent(status, Optional.ofNullable(title), Optional.ofNullable(description), Optional.ofNullable(image));
    }

}
