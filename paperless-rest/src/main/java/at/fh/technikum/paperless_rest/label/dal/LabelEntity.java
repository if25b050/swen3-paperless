package at.fh.technikum.paperless_rest.label.dal;

import at.fh.technikum.paperless_rest.common.dal.AuditedEntity;
import at.fh.technikum.paperless_rest.document.dal.DocumentEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@Table(name = "labels")
public class LabelEntity extends AuditedEntity {

    @Column(nullable = false, unique = true) // um doppelte Labels zu vermeiden
    private String name;

    @ManyToMany(mappedBy = "labels", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Set<DocumentEntity> documents = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LabelEntity that = (LabelEntity) o;
        return Objects.equals(getUuid(), that.getUuid()) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid(), name);
    }

    @PreRemove
    public void preRemove() {
        for (DocumentEntity documentEntity : documents) {
            documentEntity.getLabels().remove(this);
        }
    }
}
