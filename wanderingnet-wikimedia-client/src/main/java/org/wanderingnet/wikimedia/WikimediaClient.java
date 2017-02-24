package org.wanderingnet.wikimedia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by guillermoblascojimenez on 28/03/16.
 */
@Component
public class WikimediaClient {

    private static final String WIKIMEDIA_ENDPOINT = "https://en.wikipedia.org/w/api.php";

    @Autowired
    private Client client;

    public String getExtract(String title) {
        WebTarget webTarget = client.target(WIKIMEDIA_ENDPOINT)
                .queryParam("format", "json")
                .queryParam("action", "query")
                .queryParam("redirects", "")
                .queryParam("prop", "extracts")
                .queryParam("exintro", "")
                .queryParam("explaintext", "")
                .queryParam("titles", title);
        Map map = webTarget.request()
                .get(Map.class);
        return getExtractFromRawMap(map);
    }

    public List<String> subcategoriesOf(String category) {
        WebTarget webTarget = client.target(WIKIMEDIA_ENDPOINT)
                .queryParam("format", "json")
                .queryParam("action", "query")
                .queryParam("redirects", "")
                .queryParam("list", "categorymembers")
                .queryParam("cmtitle", category);
        Map map = webTarget.request()
                .get(Map.class);
        return getCategoriesOfCategory(map);
    }

    private List<String> getCategoriesOfCategory(Map map) {
        map = (Map) map.get("query");
        List<Map<String, String>> map2 = (List<Map<String, String>>) map.get("categorymembers");
        return map2.stream()
                .map(s -> s.get("title"))
                .collect(Collectors.toList());
    }


    private String getExtractFromRawMap(Map map) {
        // probably the dirtiest java code you are going to see ever.
        // the cause is that the nice wikipedia api has a dynamic key entity:
        /*
        {
            batchcomplete: "",
            query: {
            pages: {
            21721040: { <<-- THIS ONE
            pageid: 21721040,
            ns: 0,
            title: "Stack Overflow",
            extract: "Stack Overflow is a privately held website, the flagship site of the Stack Exchange Network, created in 2008 by Jeff Atwood and Joel Spolsky. It was created to be a more open alternative to earlier Q&A sites such as Experts-Exchange. The name for the website was chosen by voting in April 2008 by readers of Coding Horror, Atwood's popular programming blog. It features questions and answers on a wide range of topics in computer programming. The website serves as a platform for users to ask and answer questions, and, through membership and active participation, to vote questions and answers up or down and edit questions and answers in a fashion similar to a wiki or Digg. Users of Stack Overflow can earn reputation points and "badges"; for example, a person is awarded 10 reputation points for receiving an "up" vote on an answer given to a question, and can receive badges for their valued contributions, which represents a kind of gamification of the traditional Q&A site or forum. All user-generated content is licensed under a Creative Commons Attribute-ShareAlike license. Closing questions is a main differentiation from Yahoo! Answers and a way to prevent low quality questions. The mechanism was overhauled in 2013; questions edited after being put "on hold" now appear in a review queue. Jeff Atwood stated in 2010 that duplicate questions are not seen as a problem but rather they constitute an advantage if such additional questions drive extra traffic to the site by multiplying relevant keyword hits in search engines. As of April 2014, Stack Overflow has over 4,000,000 registered users and more than 10,000,000 questions, with 10,000,000 questions celebrated in late August 2015. Based on the type of tags assigned to questions, the top eight most discussed topics on the site are: Java, JavaScript, C#, PHP, Android, jQuery, Python and HTML. Stack Overflow also has a Careers section to assist developers in finding their next opportunity. For employers, Stack Overflow provides tools to brand their business, advertise their openings on the site, and source candidates from Stack Overflow's database of developers who are open to being contacted."
            }
            }
            }
            }
         */
        // as result of this and that I do not have time for a clever solution, let's dig the hands in the mud.

        map = (Map) map.get("query");
        map = (Map) map.get("pages");
        String key = (String) map.keySet().iterator().next();
        if ("-1".equals(key)) {
            return "";
        } else {
            map = (Map) map.get(key);
            return (String) map.get("extract");
        }
    }


}
