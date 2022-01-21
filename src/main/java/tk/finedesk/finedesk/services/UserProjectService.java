package tk.finedesk.finedesk.services;


import tk.finedesk.finedesk.dto.request.RequestImageDto;
import tk.finedesk.finedesk.dto.response.ResponseProjectDto;

public interface UserProjectService {

    ResponseProjectDto addNewItemToProject(RequestImageDto requestImageDto);
}
