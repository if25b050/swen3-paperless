package at.fh.technikum.paperless_rest.document.dal;

import at.fh.technikum.paperless_rest.common.dal.AuditedEntity;
import at.fh.technikum.paperless_rest.label.entity.LabelEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "documents")
public class DocumentEntity extends AuditedEntity {

    @Column(nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<LabelEntity> labels = new ArrayList<>();

    public DocumentEntity() {
    }
}
