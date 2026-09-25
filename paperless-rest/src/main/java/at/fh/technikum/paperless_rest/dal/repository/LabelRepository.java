package at.fh.technikum.paperless_rest.dal.repository;

import at.fh.technikum.paperless_rest.dal.entity.LabelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LabelRepository extends JpaRepository<LabelEntity, UUID> {
    // Label nach Namen suchen
    Optional<LabelEntity> findByName(String name);

    // Liefer alle Labels, die einem bestimmten Dokument zugewiesen sind
    List<LabelEntity> findByDocumentsUuid(UUID documentId);

    // Liefert alle Labels, die mindestens einem Dokument zugewiesen sind
    List<LabelEntity> findDistinctByDocumentsIsNotEmpty();

    // Prüfen, ob ein Label mit einem bestimmten Namen bereits existiert
    boolean existsByName(String name);
}
