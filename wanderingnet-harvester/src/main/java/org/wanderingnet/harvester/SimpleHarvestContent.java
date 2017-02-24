package org.wanderingnet.harvester;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by guillermoblascojimenez on 01/03/16.
 */
class SimpleHarvestContent implements HarvestContent {

    private final Optional<String> title;
    private final Optional<String> description;
    private final Optional<String> image;
    private final long score;
    private final Status status;

    SimpleHarvestContent(Status status, Optional<String> title, Optional<String> description, Optional<String> image, long score) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.score = score;
        this.status = status;
    }

    SimpleHarvestContent(Status status, Optional<String> title, Optional<String> description, Optional<String> image) {
        this.status = status;
        this.title = title;
        this.description = description;
        this.image = image;
        this.score = Arrays.asList(title, description, image)
                .stream()
                .filter(Optional::isPresent)
                .count();
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public Optional<String> getTitle() {
        return title;
    }

    @Override
    public Optional<String> getDescription() {
        return description;
    }

    @Override
    public Optional<String> getImage() {
        return image;
    }

    @Override
    public long score() {
        return score;
    }

    @Override
    public HarvestContent combine(HarvestContent other) {
        assert other != null;
        return new SimpleHarvestContent(
                status.combine(other.getStatus()),
                Optional.ofNullable(this.getTitle().orElse(other.getTitle().orElse(null))),
                Optional.ofNullable(this.getDescription().orElse(other.getDescription().orElse(null))),
                Optional.ofNullable(this.getImage().orElse(other.getImage().orElse(null))),
                this.score() + other.score()
                );
    }
}
