package org.wanderingnet.webapp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.wanderingnet.data.api.MapDAO;
import org.wanderingnet.data.api.TagDAO;
import org.wanderingnet.model.Tag;
import org.wanderingnet.service.TagService;
import org.wanderingnet.wikimedia.WikimediaService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by guillermoblascojimenez on 20/03/16.
 */
@Controller
@RequestMapping("/explore")
public class ExploreController extends SessionController {

    @Autowired
    private WikimediaService wikimediaService;

    @Autowired
    private TagDAO tagDAO;

    @Autowired
    private TagService tagService;

    @Autowired
    private MapDAO mapDAO;

    @RequestMapping
    public ModelAndView explore() {
        return new ModelAndView("explore/list", "maps", mapDAO.getAllAsList());
    }

    @RequestMapping("/{mapNameKey}")
    public ModelAndView exploreMap(@PathVariable("mapNameKey") String mapNameKey) {
        org.wanderingnet.model.Map map = mapDAO.mapOfNameKey(mapNameKey).get();
        Map<String, Object> model = new HashMap<>();
        model.put("tags", tagDAO.getAllAsList());
        model.put("map", map);
        return new ModelAndView("explore/index", model);
    }

    @RequestMapping(value = "/wiki/extract", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> wikiExtract(@RequestParam("title") String title) {
        String extract = wikimediaService.extract(title);
        return Collections.singletonMap("extract", extract);
    }

    @RequestMapping(value = "/wiki/subcategories", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> wikiCategoriesOf(@RequestParam("subcategory") String subcategory) {
        List<String> categories = wikimediaService.subcategoriesOf(subcategory);
        return Collections.singletonMap("categories", categories);
    }

    @RequestMapping(value = "/wiki/tags", method = RequestMethod.GET)
    @ResponseBody
    public Map<Long, Integer> tagsOf(@RequestParam("docName") String docName) {
        return tagDAO.tagsOf(docName)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getKey().getId(), Map.Entry::getValue));
    }

    @RequestMapping(value = "/my/wiki/tags", method = RequestMethod.GET)
    @ResponseBody
    public List<Tag> myTagsOf(@RequestParam("docName") String docName) {
        return tagDAO.tagsOf(docName, super.currentUser().getUser());
    }

    @RequestMapping(value = "/wiki/tag", method = RequestMethod.POST)
    @ResponseBody
    public void toggleTag(@RequestParam("tagId") long tagId, @RequestParam("docName") String docName, @RequestParam("docUrl") String docUrl) {
        tagService.toggleTag(tagId, currentUser().getUser(), docName, docUrl);
    }

}
