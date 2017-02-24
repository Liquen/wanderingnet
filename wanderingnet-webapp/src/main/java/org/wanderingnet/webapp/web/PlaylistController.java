package org.wanderingnet.webapp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.wanderingnet.harvester.Harvest;
import org.wanderingnet.harvester.HarvesterService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by guillermoblascojimenez on 08/03/16.
 */
@Controller
@RequestMapping("/playlist")
public class PlaylistController extends SessionController {

    @Autowired
    private HarvesterService harvesterService;

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public ModelAndView newPlaylist() {
        return new ModelAndView("playlist/new");
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String createPlaylist() {
        return null;
    }

    @RequestMapping(value = "/harvest", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> harvest(@RequestParam("url") String url) {
        Harvest harvest = harvesterService.harvest(url);
        Map<String, Object> map = new HashMap<>();
        map.put("status", harvest.getHarvestContent().getStatus());
        map.put("title", harvest.getHarvestContent().getTitle().orElse(null));
        map.put("url", harvest.getTargetUrl());
        map.put("sanitizedUrl", harvest.getSanitizedUrl());
        map.put("description", harvest.getHarvestContent().getDescription().orElse(null));
        return map;
    }

}
