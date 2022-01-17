package tk.finedesk.finedesk.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
public class RequestAuthentication implements Serializable {

    @NotEmpty
    private String username;
    @NotEmpty
    private String password;

}
