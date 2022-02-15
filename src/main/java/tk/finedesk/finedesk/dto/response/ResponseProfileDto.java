package tk.finedesk.finedesk.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResponseProfileDto implements ResponseDto {

    private String id;

    private String firstName;

    private String lastName;

    private String profilePhotoUrl;

    private String coverPhotoUrl;

    private String username;
}
