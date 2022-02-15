package tk.finedesk.finedesk.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Builder
@Table(name = "likes")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "uuid",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "like_UUID", columnDefinition = "VARCHAR(255)", updatable = false, nullable = false)
    private String uuid = UUID.randomUUID().toString();

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date likeDate = Date.from(Instant.from(ZonedDateTime.now()));

    @ManyToOne
    @JoinColumn(name = "user_project_id")
    private UserProject userProject;

}
