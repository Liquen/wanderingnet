package org.wanderingnet.harvester.client;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.wanderingnet.harvester.Harvest;
import org.wanderingnet.harvester.HarvesterService;
import org.wanderingnet.harvester.exchange.HarvestRequest;
import org.wanderingnet.harvester.exchange.HarvestResponse;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

/**
 * Created by guillermoblascojimenez on 07/03/16.
 */
@Service
public class HarvesterServiceClient implements HarvesterService {

    @Autowired
    private Client client;

    @Value("${wanderingnet.harvester.server.host}")
    private String host;

    @Override
    public Harvest harvest(String targetUrl) {
        HarvestRequest harvestRequest = new HarvestRequest(targetUrl);

        HarvestResponse response = client.target(host)
                .path("/harvest")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(harvestRequest), HarvestResponse.class);
        return response.build();
    }
}
