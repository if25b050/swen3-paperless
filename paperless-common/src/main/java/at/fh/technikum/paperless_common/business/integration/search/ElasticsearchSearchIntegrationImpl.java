package at.fh.technikum.paperless_common.business.integration.search;

import at.fh.technikum.paperless_common.business.exception.SearchException;
import at.fh.technikum.paperless_common.business.exception.SearchIndexingException;
import at.fh.technikum.paperless_common.business.properties.ElasticsearchProperties;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
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
        if (elasticsearchProperties.getTestOnStartup()) {
            try (ElasticsearchClient esClient = createEsClient()) {
                // Test the connection
                esClient.count();
                log.info("Connected to Elasticsearch at {}", elasticsearchProperties.getUrl());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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

            SearchResponse<ElasticsearchDocument> result = esClient.search(s -> s
                            .index(INDEX_NAME)
                            .query(q -> q
                                    .match(t -> t
                                            .field("content")
                                            .query(searchTerm)
                                    )
                            ),
                    ElasticsearchDocument.class
            );

            List<UUID> resultUUIDs = result.hits().hits().stream()
                    .map(ed -> ed.source() != null ? ed.source().id() : null)
                    .filter(Objects::nonNull)
                    .toList();

            log.info("Found {} results", kv("count", resultUUIDs.size()));

            return resultUUIDs;
        } catch (IOException e) {
            throw new SearchException("There was an issue while searching " + kv("term", searchTerm), e);
        }
    }

    @Override
    public String getContent(UUID uuid) {
        try (ElasticsearchClient esClient = createEsClient()) {
            GetResponse<ElasticsearchDocument> elasticsearchDocumentGetResponse = esClient.get(g -> g
                            .index(INDEX_NAME)
                            .id(uuid.toString()),
                    ElasticsearchDocument.class);

            ElasticsearchDocument document = elasticsearchDocumentGetResponse.source();
            if (Objects.nonNull(document) && Objects.nonNull(document.content())) {
                log.info("Found document content for {}", kv("uuid", uuid));

                return document.content();
            }
        } catch (IOException e) {
            throw new SearchException("There was an issue while getting the content for " + kv("uuid", uuid), e);
        }
        log.info("Did not find document content for {}", kv("uuid", uuid));

        return "";
    }

    @Override
    public void indexDocument(UUID uuid, String content) {
        try (ElasticsearchClient esClient = createEsClient()) {
            ElasticsearchDocument document = new ElasticsearchDocument(uuid, content);

            IndexResponse response = esClient.index(i -> i
                    .index(INDEX_NAME)
                    .id(uuid.toString())
                    .document(document));

            log.info("Indexed document-content {} with {}", kv("uuid", uuid), kv("version", response.version()));
        } catch (IOException e) {
            throw new SearchIndexingException("There was an issue while indexing " + kv("uuid", uuid), e);
        }
    }

    @Override
    public void deleteDocument(UUID uuid) {
        try (ElasticsearchClient esClient = createEsClient()) {

            DeleteResponse response = esClient.delete(d -> d
                    .id(uuid.toString()));

            log.info("Deleted indexed document-content {}", kv("uuid", uuid));
        } catch (IOException e) {
            throw new SearchIndexingException("There was an issue while indexing " + kv("uuid", uuid), e);
        }
    }
}
