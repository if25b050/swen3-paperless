package at.fh.technikum.paperless_rest.DAL.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "documents")
public class DocumentEntity {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String fileUrl;

    @ManyToMany
    private List<LabelEntity> labels;

    public DocumentEntity() {
    }
}
