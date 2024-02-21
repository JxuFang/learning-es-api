package com.example.learningesapi;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
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
    }

    @Test
    void testAddDocument2ByCreate() throws IOException {
        Product product = new Product("bk-2", "GH bike", 33.3F);
        CreateResponse response = client.create(r -> r
                .index("products")
                .id(product.getSku())
                .document(product));
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
    void testDeleteDocument() throws IOException {
        client.delete(d -> d
                .index("products")
                .id("bk-2"));
    }

    @Test
    void testUpdateDocument() throws IOException {
        Product product = new Product("car-1", "BYD car", 666.0F);
        UpdateResponse<Product> response = client.update(u -> u
                .index("products")
                .id(product.getSku())
                .doc(product), Product.class);
    }

    @Test
    void testGetDocument() throws IOException {
        GetResponse<Product> response = client.get(g -> g
                .index("products")
                .id("bk-1"), Product.class);

        if (response.found()) {
            Product product = response.source();
            log.info("Product: {}", product);
        } else {
            log.info("Product not found");
        }
    }

    @Test
    void testSearchDocument() throws IOException {
        SearchResponse<Product> response = client.search(s -> s
                .index("products")
                , Product.class);
        log.info("response:{}", response);
    }
}