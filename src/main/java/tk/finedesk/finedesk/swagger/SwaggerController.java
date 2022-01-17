package tk.finedesk.finedesk.swagger;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/swagger")
public class SwaggerController {
    @GetMapping
    public ModelAndView forwardToSwaggerUi() {
        return new ModelAndView("redirect:/swagger-ui/index.html");
    }
}
