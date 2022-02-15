package tk.finedesk.finedesk.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date likeDate = Date.from(Instant.from(ZonedDateTime.now()));

//    @ManyToMany(mappedBy = "likes", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
//    private List<UserProfile> userProfiles = new LinkedList<>();

    @ManyToOne
    @JoinColumn(name = "user_project_id")
    private UserProject userProject;

}
