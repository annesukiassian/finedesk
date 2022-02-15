package tk.finedesk.finedesk.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class ResponseUserDto implements ResponseDto {

    private String userUuid;
    private String firstName;
    private String lastName;
    private String username;
    private String coverPhotoUrl;
    private String profilePhotoUrl;
    private Long profileId;
    private Set<String> roles;

}
