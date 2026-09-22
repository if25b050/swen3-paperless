package at.fh.technikum.paperless_rest.dal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class LabelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true) // um doppelte Labels zu vermeiden
    private String name;

    @ManyToMany(mappedBy = "labels")
    private List<DocumentEntity> documents;

    @Override
    public String toString() {
        return "LabelEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", documents=" + documents +
                '}';
    }
}
