package org.wanderingnet.harvester.server.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wanderingnet.harvester.Harvest;
import org.wanderingnet.harvester.HarvestContent;
import org.wanderingnet.harvester.HarvesterService;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.Optional;

/**
 * Created by guillermoblascojimenez on 28/02/16.
 */
@Service
public class HarvesterServiceImpl implements HarvesterService {

    @Autowired
    private Client client;

    @Override
    public Harvest harvest(String targetUrl) {
        String sanitizedUrl = sanitizeUrl(targetUrl);
        HarvestContent harvestContent;

        try {
            WebTarget webTarget = client.target(sanitizedUrl);
            Response response = webTarget.request().get();
            String html = response.readEntity(String.class);

            Document document = Jsoup.parse(html);
            harvestContent = extractSchemaOrgContent(document)
                    .combine(extractOpenGraphContent(document))
                    .combine(extractMetaContent(document));
        } catch (RuntimeException e) {
            harvestContent = HarvestContent.failure();
        }
        return new Harvest.Builder()
                .harvestContent(harvestContent)
                .targetUrl(targetUrl)
                .sanitizedUrl(sanitizedUrl)
                .build();
    }

    private static String sanitizeUrl(String targetUrl) {
        String sanitizedUrl = targetUrl;
        int hashLastIndex = targetUrl.lastIndexOf('#');
        int slashLastIndex = targetUrl.lastIndexOf('/');
        if (hashLastIndex > slashLastIndex) {
            sanitizedUrl = targetUrl.substring(0,hashLastIndex);
        }
        return sanitizedUrl;
    }

    // https://developers.google.com/+/web/snippet/

    private static HarvestContent extractMetaContent(Document document) {
        Optional<String> title = Optional.ofNullable(document.title());
        Elements descriptions = document.select("meta[name=\"description\"]");
        Optional<String> description;
        if (!descriptions.isEmpty()) {
            description = Optional.ofNullable(descriptions.get(0).attr("content"));
        } else {
            description = Optional.empty();
        }
        return HarvestContent.of(title, description, Optional.<String>empty());
    }

    private static HarvestContent extractOpenGraphContent(Document document) {
        Elements titles = document.select("meta[property=\"og:title\"]");
        Optional<String> title;
        if (!titles.isEmpty()) {
            title = Optional.ofNullable(titles.get(0).attr("content"));
        } else {
            title = Optional.empty();
        }
        Elements descriptions = document.select("meta[property=\"og:description\"]");
        Optional<String> description;
        if (!descriptions.isEmpty()) {
            description = Optional.ofNullable(descriptions.get(0).attr("content"));
        } else {
            description = Optional.empty();
        }
        Elements images = document.select("meta[property=\"og:image\"]");
        Optional<String> image;
        if (!images.isEmpty()) {
            image = Optional.ofNullable(images.get(0).attr("content"));
        } else {
            image = Optional.empty();
        }
        return HarvestContent.of(title, description, image);
    }

    private static HarvestContent extractSchemaOrgContent(Document document) {
        Elements titles = document.select("[itemprop=\"name\"]");
        Optional<String> title;
        if (!titles.isEmpty()) {
            title = Optional.ofNullable(getTextElseContent(titles.first()));
        } else {
            title = Optional.empty();
        }
        Elements descriptions = document.select("[itemprop=\"description\"]");
        Optional<String> description;
        if (!descriptions.isEmpty()) {
            description = Optional.ofNullable(getTextElseContent(descriptions.first()));
        } else {
            description = Optional.empty();
        }
        Elements images = document.select("[itemprop=\"image\"]");
        Optional<String> image;
        if (!images.isEmpty()) {
            image = Optional.ofNullable(getSrcElseHref(images.first()));
        } else {
            image = Optional.empty();
        }
        return HarvestContent.of(title, description, image);
    }

    private static String getTextElseContent(Element element) {
        String text = element.text();
        if (text == null || text.trim().isEmpty()) {
            text = element.attr("content");
        }
        return text;
    }

    private static String getSrcElseHref(Element element) {
        String text = element.attr("src");
        if (text == null || text.trim().isEmpty()) {
            text = element.attr("href");
        }
        return text;
    }
}
