package org.wanderingnet.harvester.exchange;

import org.wanderingnet.common.MicroServiceRequest;

/**
 * Created by guillermoblascojimenez on 29/02/16.
 */
public class HarvestRequest extends MicroServiceRequest {

    private String targetUrl;

    public HarvestRequest() {
    }

    public HarvestRequest(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }
}
