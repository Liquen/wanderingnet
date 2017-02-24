package org.wanderingnet.harvester.client.config;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.wanderingnet.harvester.client.ObjectMapperProvider;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 * Created by guillermoblascojimenez on 29/02/16.
 */
@Configuration
public class HarvesterClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Client httpClient() {
        return ClientBuilder.newBuilder()
                .register(JacksonFeature.class)
                .register(ObjectMapperProvider.class)
                .build();
    }

}
