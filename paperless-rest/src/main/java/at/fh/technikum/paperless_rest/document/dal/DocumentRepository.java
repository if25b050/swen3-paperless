package at.fh.technikum.paperless_rest.document.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity, UUID> {
    // Dokumente suchen, denen ein bestimmtes Label zugewiesen ist
    List<DocumentEntity> findByLabelsUuid(UUID labelId);
}
