package at.fh.technikum.paperless_rest.common.dal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public abstract class AuditedEntity {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Version
    @Column(nullable = false)
    private Integer version;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdDate;

    @UpdateTimestamp
    @Column(nullable = false)
    private OffsetDateTime modifiedDate;

}
