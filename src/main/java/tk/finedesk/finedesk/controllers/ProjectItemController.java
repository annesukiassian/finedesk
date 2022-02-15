package tk.finedesk.finedesk.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tk.finedesk.finedesk.dto.response.ResponseBaseDto;

import java.util.List;

@RestController
@RequestMapping("/projectItems")
public class ProjectItemController {

    @RequestMapping(
            method = RequestMethod.GET
    )
    public ResponseEntity<List<ResponseBaseDto>> getAllProjectItems() {
        try {
            return ResponseEntity.ok(List.of());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(List.of(ResponseBaseDto.builder().message(e.getMessage()).build()));
        }
    }
}
