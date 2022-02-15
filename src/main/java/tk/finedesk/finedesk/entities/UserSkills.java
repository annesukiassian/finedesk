package tk.finedesk.finedesk.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "user_skills")
public class UserSkills {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "uuid",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "user_skills_UUID", columnDefinition = "VARCHAR(255)", updatable = false, nullable = false)
    private String uuid = UUID.randomUUID().toString();

    private String level;

    private String description;

}
