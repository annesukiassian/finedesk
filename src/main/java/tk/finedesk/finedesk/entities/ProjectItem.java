package tk.finedesk.finedesk.entities;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "project_items")
public class ProjectItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "uuid",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "project_item_UUID", columnDefinition = "VARCHAR(255)", updatable = false, nullable = false)
    private String uuid = UUID.randomUUID().toString();

    @NotNull
    private String imageURL;

    @ManyToOne
    @JoinColumn(name = "user_project_id", referencedColumnName = "id")
    private UserProject userProject;

}
