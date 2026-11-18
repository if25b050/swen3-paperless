package at.fh.technikum.paperless_common.business.integration.search;

import java.util.List;
import java.util.UUID;

/**
 * Service which is used to integrate with Free-Text-Search.
 */
public interface SearchIntegration {
    List<UUID> search(String searchTerm);

    List<UUID> search(String searchTerm, int limit);

    String getContent(UUID uuid);

    void index(UUID uuid, String content);
}
