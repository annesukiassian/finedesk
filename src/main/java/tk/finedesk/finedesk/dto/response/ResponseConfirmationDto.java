package tk.finedesk.finedesk.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import tk.finedesk.finedesk.dto.response.ResponseDto;
import tk.finedesk.finedesk.entities.User;

@Getter
@Setter
@Builder
public class ResponseConfirmationDto implements ResponseDto {

    private boolean isConfirmed;
    private User user;

}
