package info.podkowinski.sandra.financescanner.user;

import info.podkowinski.sandra.financescanner.project.Project;
import info.podkowinski.sandra.financescanner.project.ProjectService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;
    private final ProjectService projectService;

    public UserController(UserService userService, ProjectService projectService) {
        this.userService = userService;
        this.projectService = projectService;
    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public String login() {
        return "user/user-login";
    }

    @RequestMapping(value = {"/register"}, method = RequestMethod.GET)
    public String register() {
        return "user/user-register";
    }

    @RequestMapping(value = {"/forgotpassword"}, method = RequestMethod.GET)
    public String forgotPassword() {
        return "user/user-forgot-password";
    }

    @ResponseBody
    @GetMapping("/user/setcurrentproject/{projectId}")
    public String setCurrentProject(@PathVariable Long projectId, @AuthenticationPrincipal CurrentUser currentUser){
        User user = currentUser.getUser();
        List<Project> usersProjects = user.projects;
        for (Project project: usersProjects) {
            if(project.getId() == projectId){
                user.setCurrentProject(project);
            }
        }
        return "";
    }
}
