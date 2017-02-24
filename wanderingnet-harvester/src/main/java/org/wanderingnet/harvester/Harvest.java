package org.wanderingnet.harvester;

/**
 * Created by guillermoblascojimenez on 28/02/16.
 */
public class Harvest {

    private final String targetUrl;
    private final String sanitizedUrl;
    private final HarvestContent harvestContent;

    public Harvest(String targetUrl, String sanitizedUrl, HarvestContent harvestContent) {
        this.targetUrl = targetUrl;
        this.sanitizedUrl = sanitizedUrl;
        this.harvestContent = harvestContent;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public HarvestContent getHarvestContent() {
        return harvestContent;
    }

    public String getSanitizedUrl() {
        return sanitizedUrl;
    }

    public static class Builder implements org.wanderingnet.common.Builder<Harvest> {

        private String targetUrl;
        private String sanitizedUrl;
        private HarvestContent harvestContent;

        public Builder targetUrl(String targetUrl) {
            this.targetUrl = targetUrl;
            return this;
        }

        public Builder harvestContent(HarvestContent harvestContent) {
            this.harvestContent = harvestContent;
            return this;
        }

        public Builder sanitizedUrl(String sanitizedUrl) {
            this.sanitizedUrl = sanitizedUrl;
            return this;
        }

        @Override
        public Harvest build() {
            assert targetUrl != null;
            assert sanitizedUrl != null;
            assert harvestContent != null;
            return new Harvest(targetUrl, sanitizedUrl, harvestContent);
        }
    }
}
