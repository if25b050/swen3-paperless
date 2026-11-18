package at.fh.technikum.paperless_common.business.integration.search;

import at.fh.technikum.paperless_common.business.exception.SearchIndexingException;
import at.fh.technikum.paperless_common.business.properties.ElasticsearchProperties;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static at.fh.technikum.paperless_common.business.BusinessUtil.kv;

@Service
@Slf4j
@EnableConfigurationProperties(ElasticsearchProperties.class)
public class ElasticsearchSearchIntegrationImpl implements SearchIntegration {

    private final String INDEX_NAME = "paperless-document-index";
    private final ElasticsearchProperties elasticsearchProperties;

    public ElasticsearchSearchIntegrationImpl(ElasticsearchProperties properties) {
        this.elasticsearchProperties = properties;

        try (ElasticsearchClient esClient = createEsClient()) {
            // Test the connection
            esClient.info();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private @NonNull ElasticsearchClient createEsClient() {
        return ElasticsearchClient.of(b -> b
                .host(elasticsearchProperties.getUrl())
                .usernameAndPassword(elasticsearchProperties.getUsername(),
                        elasticsearchProperties.getPassword())
        );
    }

    @Override
    public List<UUID> search(String searchTerm) {
        return search(searchTerm, 1000);
    }

    @Override
    public List<UUID> search(String searchTerm, int limit) {
        try (ElasticsearchClient esClient = createEsClient()) {

            SearchResponse<Void> result = esClient.search(s -> s
                    .index(INDEX_NAME)
                    .query(q -> q
                            .match(t -> t
                                    .field("content")
                                    .query(searchTerm)
                            )
                    )
            );
            // TODO fix Response

        } catch (IOException e) {
            throw new SearchIndexingException("There was an issue while searching " + kv("term", searchTerm), e);
        }
        return List.of();
    }

    @Override
    public String getContent(UUID uuid) {
        // TODO implement
        return "";
    }

    @Override
    public void index(UUID uuid, String content) {

        try (ElasticsearchClient esClient = createEsClient()) {
            ElasticsearchDocument document = new ElasticsearchDocument(uuid, content);

            IndexResponse response = esClient.index(i -> i
                    .index(INDEX_NAME)
                    .id(uuid.toString())
                    .document(document));

            log.info("Indexed {} with {}", kv("uuid", uuid), kv("version", response.version()));
        } catch (IOException e) {
            throw new SearchIndexingException("There was an issue while indexing " + kv("uuid", uuid), e);
        }

    }
}
