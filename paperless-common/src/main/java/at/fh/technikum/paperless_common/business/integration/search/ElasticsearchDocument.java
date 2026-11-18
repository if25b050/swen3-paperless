package at.fh.technikum.paperless_common.business.integration.search;

import java.util.UUID;

/**
 * Object which is used to create a document in the index in Elasticsearch.
 */
record ElasticsearchDocument(UUID id, String content) {
}
