package tk.finedesk.finedesk.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class ResponseUsersDto implements ResponseDto {

    private Set<String> registeredUsersUuids;

}
