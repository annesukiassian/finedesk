package tk.finedesk.finedesk.services;


import org.springframework.web.multipart.MultipartFile;
import tk.finedesk.finedesk.dto.response.ResponseLikeDto;
import tk.finedesk.finedesk.dto.response.ResponseProjectDto;

import java.util.List;

public interface UserProjectService {

    ResponseProjectDto addNewItemToProject(List<MultipartFile> images, String username, String projectName, String description);

    ResponseLikeDto likeProject(Long projectId, String username);
}
