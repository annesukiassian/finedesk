package tk.finedesk.finedesk.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResponseProjectDto implements ResponseDto {

    private String message;


}
