package org.wanderingnet.harvester.server.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 * Created by guillermoblascojimenez on 29/02/16.
 */
@Configuration
public class HarvesterServerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Client httpClient() {
        return ClientBuilder.newClient();
    }

}
