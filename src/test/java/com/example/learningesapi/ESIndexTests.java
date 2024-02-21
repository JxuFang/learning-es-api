package com.example.learningesapi;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import co.elastic.clients.elasticsearch.indices.Alias;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import com.example.learningesapi.po.Product;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@SpringBootTest(classes = LearningEsApiApplication.class)
@Slf4j
class ESIndexTests {


    @Resource(name = "client")
    private ElasticsearchClient client;

    @Test
    public void testAddIndex() throws IOException {
        // Create the "products" index
        client.indices().create(c -> c.index("products"));
//        client.indices().create(new CreateIndexRequest.Builder().index("products").build());
    }

    @Test
    void testDeleteIndex() throws IOException {
        // Delete the "products" index
        client.indices().delete(d -> d.index("products"));
    }

    @Test
    public void testSearchIndex() throws IOException {
        SearchResponse<Product> search = client.search(s -> s
                        .index("products"), Product.class);
        System.out.println(search);
    }

    @Test
    void testAliases() throws IOException {
        CreateIndexResponse createResponse = client.indices().create(
                new CreateIndexRequest.Builder()
                        .index("my-index")
                        .aliases("foo",
                                new Alias.Builder().isWriteIndex(true).build()
                        )
                        .build()
        );
        // 使用lambda表达式进行简化
//        CreateIndexResponse createIndexResponse = client.indices().create(
//                createIndexRequest -> createIndexRequest.index("my-index")
//                        .aliases("foo", alias -> alias.isWriteIndex(true))
//        );
        // 弱化 the name of variables
//        CreateIndexResponse createIndexResponse1 = client.indices().create(
//                c -> c.index("my-index")
//                        .aliases("foo", a -> a.isWriteIndex(true))
//        );
    }

    @Test
    void testAddDocument() throws IOException {
        Product product = new Product("car-1", "BYD car", 666.0F);
        IndexResponse indexResponse = client.index(new IndexRequest.Builder<Product>()
                .index("products")
                .id(product.getSku())
                .document(product)
                .build());
        log.info("Indexed with version: {}", indexResponse.version());
    }

    @Test
    void testSearchDocument() throws IOException {
        String searchText = "bike";
        SearchResponse<Product> response = client.search(new SearchRequest.Builder()
                        .index("products")
                        .query(q -> q.match(t -> t
                                .field("name")
                                .query(searchText))).build(),
                Product.class);
        TotalHits total = response.hits().total();
        boolean isExactResult = total.relation() == TotalHitsRelation.Eq;

        if (isExactResult) {
            log.info("There are " + total.value() + " results");
        } else {
            log.info("There are more than " + total.value() + " results");
        }

        List<Hit<Product>> hits = response.hits().hits();
        for (Hit<Product> hit: hits) {
            Product product = hit.source();
            log.info("Found product " + product.getSku() + ", score " + hit.score());
        }
        log.info("response: {}", response);
    }
}
