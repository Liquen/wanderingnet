package org.wanderingnet.harvester.server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.wanderingnet.harvester.Harvest;
import org.wanderingnet.harvester.HarvesterService;
import org.wanderingnet.harvester.exchange.HarvestRequest;
import org.wanderingnet.harvester.exchange.HarvestResponse;

/**
 * Created by guillermoblascojimenez on 28/02/16.
 */
@RestController
public class HarvesterController {

    @Autowired
    private HarvesterService harvesterService;

    @RequestMapping(
            value = "/harvest",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    public HarvestResponse harvest(@RequestBody HarvestRequest harvestRequest) {
        Harvest harvest =  harvesterService.harvest(harvestRequest.getTargetUrl());
        return new HarvestResponse(harvestRequest, harvest);
    }

}
