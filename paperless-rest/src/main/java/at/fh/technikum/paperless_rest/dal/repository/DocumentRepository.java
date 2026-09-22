package at.fh.technikum.paperless_rest.dal.repository;

import at.fh.technikum.paperless_rest.dal.entity.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity, Integer> {
    // Dokumente suchen, denen ein bestimmtes Label zugewiesen ist
    List<DocumentEntity> findByLabels_Id(Integer labelId);

    // Prüfen, ob ein Dokument mit einem bestimmten Dateinamen existiert
    boolean existsByName(String name);
}
