package at.fh.technikum.paperless_rest.document.dal;

import at.fh.technikum.paperless_rest.common.dal.AuditedEntity;
import at.fh.technikum.paperless_rest.label.dal.LabelEntity;
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
@Table(name = "documents")
public class DocumentEntity extends AuditedEntity {

    @Column(nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    private Set<LabelEntity> labels = new HashSet<>();

    public DocumentEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DocumentEntity that = (DocumentEntity) o;
        return Objects.equals(getUuid(), that.getUuid()) && Objects.equals(name, that.name) && Objects.equals(labels, that.labels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid(), name, labels);
    }
}
