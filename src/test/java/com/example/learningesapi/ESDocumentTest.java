package com.example.learningesapi;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.example.learningesapi.po.Product;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * @Author: Fang Jinxu
 * @Description:
 * @Date: 2024-02-21 17:22
 */
@SpringBootTest(classes = LearningEsApiApplication.class)
@Slf4j
public class ESDocumentTest {

    @Autowired
    private ElasticsearchClient client;

    @Test
    void testAddDocument() throws IOException {
        Product product = new Product("car-1", "BYD car", 666.0F);
        IndexResponse indexResponse = client.index(i -> i
                .index("products")
                .id(product.getSku())
                .document(product));
        client.create()
    }

    @Test
    void testAddDocumentWithJson() throws IOException {
        Reader input = new StringReader(
                "{'sku': 'bk-1', 'name': 'City bike', 'price': 128.0}"
                        .replace('\'', '"'));
        client.index(i -> i
                .index("products")
                .withJson(input));
    }

    @Test
    void testSearchDocument() throws IOException {
        SearchResponse<Product> response = client.search(s -> s
                .index("products")
                , Product.class);
        log.info("response:{}", response);
    }
}