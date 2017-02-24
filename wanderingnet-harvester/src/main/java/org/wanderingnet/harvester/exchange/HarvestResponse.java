package org.wanderingnet.harvester.exchange;

import org.wanderingnet.common.Builder;
import org.wanderingnet.common.MicroServiceResponse;
import org.wanderingnet.harvester.Harvest;
import org.wanderingnet.harvester.HarvestContent;

/**
 * Created by guillermoblascojimenez on 28/02/16.
 */
public class HarvestResponse extends MicroServiceResponse implements Builder<Harvest> {

    private String targetUrl;
    private String sanitizedUrl;
    private String title;
    private String description;
    private String image;
    private HarvestContent.Status status;

    public HarvestResponse() {
    }

    public HarvestResponse(HarvestRequest request) {
        super(request);
    }

    public HarvestResponse(HarvestRequest request, Harvest harvest) {
        this(request);
        this.targetUrl = harvest.getTargetUrl();
        this.sanitizedUrl = harvest.getSanitizedUrl();
        this.title = harvest.getHarvestContent().getTitle().orElse(null);
        this.description = harvest.getHarvestContent().getDescription().orElse(null);
        this.image = harvest.getHarvestContent().getImage().orElse(null);
        this.status = harvest.getHarvestContent().getStatus();
    }

    public HarvestResponse(HarvestRequest request, String targetUrl, String sanitizedUrl, String title, String description, String image, HarvestContent.Status status) {
        this(request);
        this.targetUrl = targetUrl;
        this.sanitizedUrl = sanitizedUrl;
        this.title = title;
        this.description = description;
        this.image = image;
        this.status = status;
    }

    public HarvestContent.Status getStatus() {
        return status;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getSanitizedUrl() {
        return sanitizedUrl;
    }

    public String getImage() {
        return image;
    }

    @Override
    public Harvest build() {
        return new Harvest.Builder()
                .sanitizedUrl(sanitizedUrl)
                .targetUrl(targetUrl)
                .harvestContent(HarvestContent.ofNullables(status, title, description, image))
                .build();
    }

}
