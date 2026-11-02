package at.fh.technikum.paperless_rest.label.dal;

import at.fh.technikum.paperless_rest.label.entity.LabelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LabelRepository extends JpaRepository<LabelEntity, UUID> {
}
