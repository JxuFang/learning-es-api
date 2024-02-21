package com.example.learningesapi.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Fang Jinxu
 * @Description:
 * @Date: 2024-02-20 22:29
 */
@Configuration
public class ElasticsearchConfig {

    // Create the low-level client
    @Bean
    public RestClient restClient() {
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200)).build();
        return restClient;
    }

    // Create the transport with a Jackson mapper
    @Bean
    public ElasticsearchTransport transport(RestClient restClient) {
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());
        return transport;
    }

    // And create the API client
    @Bean
    public ElasticsearchClient client(ElasticsearchTransport transport) {
        ElasticsearchClient client = new ElasticsearchClient(transport);
        return client;
    }

}