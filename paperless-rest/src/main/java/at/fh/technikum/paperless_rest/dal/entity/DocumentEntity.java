package at.fh.technikum.paperless_rest.dal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name="documents")
public class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable=false)
    private String name;

    @ManyToMany(mappedBy = "documents")
    private List<LabelEntity> labels;

    public DocumentEntity() {
    }

    @Override
    public String toString() {
        return "DocumentEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", labels=" + labels +
                '}';
    }
}
