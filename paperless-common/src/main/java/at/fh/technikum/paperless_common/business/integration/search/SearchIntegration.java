package at.fh.technikum.paperless_common.business.integration.search;

import java.util.List;
import java.util.UUID;

/**
 * Service which is used to integrate with Free-Text-Search.
 */
public interface SearchIntegration {

    /**
     * Function which searches the content of the documents
     * and returns a list of matching UUIDs.
     */
    List<UUID> search(String searchTerm);

    /**
     * Function which searches the content of the documents
     * and returns a list of matching UUIDs.
     * <p>
     * The limit-parameter can be used to limit the maximum amount of results.
     */
    List<UUID> search(String searchTerm, int limit);

    /**
     * Function which retrieves the content of a given Document-UUID.
     */
    String getContent(UUID uuid);

    /**
     * Function which indexes a new Document-Content-Entry.
     */
    void indexDocument(UUID uuid, String content);

    /**
     * Function which deletes the entry of a given Document-UUID.
     */
    void deleteDocument(UUID uuid);
}
