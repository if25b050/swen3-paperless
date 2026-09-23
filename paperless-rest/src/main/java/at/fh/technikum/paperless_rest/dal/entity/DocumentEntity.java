package at.fh.technikum.paperless_rest.dal.entity;

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
public class DocumentEntity {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // TODO Umbauen auf UUID?
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String fileUrl;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<LabelEntity> labels = new ArrayList<>();

    public DocumentEntity() {
    }
}
