package tk.finedesk.finedesk.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import tk.finedesk.finedesk.entities.UserProfile;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.security.cert.CertPathBuilder;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@Builder
public class ResponseProjectDto implements ResponseDto {

    private String id;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date creationDate;

    private String name;

    private String description;

    private Long likeCount;

    private  List<ResponseProjectItemDto> projectItems;

}
