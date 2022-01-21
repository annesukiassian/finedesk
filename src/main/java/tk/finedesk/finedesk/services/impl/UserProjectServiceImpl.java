package tk.finedesk.finedesk.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tk.finedesk.finedesk.aws.services.AmazonS3Service;
import tk.finedesk.finedesk.dto.request.RequestImageDto;
import tk.finedesk.finedesk.dto.response.ResponseProjectDto;
import tk.finedesk.finedesk.entities.UserProject;
import tk.finedesk.finedesk.repositories.UserProjectRepository;
import tk.finedesk.finedesk.services.ProjectItemService;
import tk.finedesk.finedesk.services.UserProjectService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProjectServiceImpl implements UserProjectService {

    private final UserProjectRepository userProjectRepository;
    private final ProjectItemService projectItemService;
    private AmazonS3Service amazonS3Service;

    @Override
    public ResponseProjectDto addNewItemToProject(RequestImageDto requestImageDto) {

        //requestImageDto.getImages().stream().map(projectItemService->projectItemService.getInputStream().)


//        List<ProjectItem> projectItems = requestImageDto.getImages().stream()
//                .map(projectItemService::createProjectItem)
//                .collect(Collectors.toList());

        List<String> imageNames = requestImageDto.getImages().stream().map(MultipartFile::getOriginalFilename).collect(Collectors.toList());

        List<MultipartFile> multipartFiles = requestImageDto.getImages();

        Path filepath = Paths.get("src/main/resources/files");
        List<File> files = null;
        multipartFiles.forEach(multipartFile -> {
            try {
                multipartFile.transferTo(new File(filepath.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        amazonS3Service.putImage();

        //projectItems.forEach(projectItem -> projectItem.setImageURL());

        UserProject userProject = userProjectRepository.findUserProjectByName(requestImageDto.getProjectName());
        // userProject.getProjectItems().addAll(projectItems);
        //TODO create update method and use it.
        UserProject updatedUserProject = userProjectRepository.saveAndFlush(userProject);

        ResponseProjectDto uploaded = ResponseProjectDto.builder().message("uploaded").build();
        return uploaded;
    }
}
