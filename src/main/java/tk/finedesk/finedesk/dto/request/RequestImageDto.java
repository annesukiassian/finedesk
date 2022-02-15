package tk.finedesk.finedesk.dto.request;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class RequestImageDto {

    @NotEmpty
    private List<MultipartFile> images;
    private String projectName;

}
