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
@Table(name = "labels")
public class LabelEntity {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true) // um doppelte Labels zu vermeiden
    private String name;

    @ManyToMany(mappedBy = "labels", fetch = FetchType.LAZY)
    private List<DocumentEntity> documents = new ArrayList<>();
}
