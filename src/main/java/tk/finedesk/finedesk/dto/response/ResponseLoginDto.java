package tk.finedesk.finedesk.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import tk.finedesk.finedesk.dto.response.ResponseDto;

@Getter
@Setter
@Builder
public class ResponseLoginDto implements ResponseDto {

    private String jwt;

}
