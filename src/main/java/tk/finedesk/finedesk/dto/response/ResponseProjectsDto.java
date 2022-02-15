package tk.finedesk.finedesk.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Getter
@Setter
@Builder
public class ResponseProjectsDto implements ResponseDto {

    private String id;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date creationDate;

    private String name;

    private Long likeCount;

}
