package tk.finedesk.finedesk.services;


import org.springframework.web.multipart.MultipartFile;
import tk.finedesk.finedesk.dto.response.ResponseLikeDto;
import tk.finedesk.finedesk.dto.response.ResponseProjectDto;
import tk.finedesk.finedesk.dto.response.ResponseProjectsDto;

import java.util.Date;
import java.util.List;

public interface UserProjectService {

    ResponseProjectDto createProjectAndUploadImages(List<MultipartFile> images, String username, String projectName, String description);

    ResponseLikeDto likeProject(String projectId, String username);

    List<ResponseProjectDto> getAllProjects(Integer pageNo, Integer pageSize, Date sortByDate);

    boolean ifProjectExists(String projectName, String username);

    List<ResponseProjectsDto> getProjectsByProfileUuid(String uuid);
}
