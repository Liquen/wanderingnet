package org.wanderingnet.webapp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by guillermoblascojimenez on 19/05/15.
 */
@Configuration
public class WebappWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(WebappWebMvcConfigurerAdapter.class);

    private static final String STATIC_PATH_MATCHER = "/static/**";

    @Value("${wanderingnet.webapp.staticResourceLocations}")
    private String staticResourceLocations;

    @Value("${wanderingnet.webapp.http.cache.time:null}")
    private Integer cachePeriod;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        LOG.info("Serving static resources for path {} to location {} and cache period of {}.", STATIC_PATH_MATCHER, staticResourceLocations, cachePeriod);
        registry.addResourceHandler(STATIC_PATH_MATCHER)
                .setCachePeriod(cachePeriod)
                .addResourceLocations(staticResourceLocations);
        registry.addResourceHandler("/robots.txt")
                .setCachePeriod(cachePeriod)
                .addResourceLocations("classpath:/static/robots.txt");
        registry.setOrder(5);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.setOrder(0);
    }

    @Bean
    public EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer() {

        return configurableEmbeddedServletContainer -> configurableEmbeddedServletContainer
                .addErrorPages(
                        new ErrorPage(HttpStatus.NOT_FOUND, "/404"),
                        new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500"),
                        new ErrorPage(HttpStatus.FORBIDDEN, "/500"),
                        new ErrorPage(HttpStatus.METHOD_NOT_ALLOWED, "/500")
                );
    }

}
