package at.fh.technikum.paperless_rest.label.entity;

import at.fh.technikum.paperless_rest.common.dal.AuditedEntity;
import at.fh.technikum.paperless_rest.document.dal.DocumentEntity;
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
@Table(name = "labels")
public class LabelEntity extends AuditedEntity {

    @Column(nullable = false, unique = true) // um doppelte Labels zu vermeiden
    private String name;

    @ManyToMany(mappedBy = "labels", fetch = FetchType.LAZY)
    private List<DocumentEntity> documents = new ArrayList<>();
}
