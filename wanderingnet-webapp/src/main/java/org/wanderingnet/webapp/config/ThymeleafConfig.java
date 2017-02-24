package org.wanderingnet.webapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.thymeleaf.templateresolver.FileTemplateResolver;

/**
 * Created by guillermoblascojimenez on 01/03/16.
 */
@Configuration
public class ThymeleafConfig {

    @Configuration
    @Profile("development")
    public static class ThymeleafDevelopmentConfig {

        @Autowired
        @Value("${spring.thymeleaf.prefix}")
        private String prefix;

        @Bean
        public FileTemplateResolver fileTemplateResolver() {
            FileTemplateResolver resolver = new FileTemplateResolver();
            resolver.setPrefix(prefix);
            resolver.setSuffix(".html");
            resolver.setTemplateMode("HTML5");
            resolver.setOrder(1);
            resolver.setCacheable(false);
            return resolver;
        }
    }
}
