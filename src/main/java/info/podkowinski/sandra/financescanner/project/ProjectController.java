package info.podkowinski.sandra.financescanner.project;

import info.podkowinski.sandra.financescanner.account.Account;
import info.podkowinski.sandra.financescanner.account.AccountService;
import info.podkowinski.sandra.financescanner.category.Category;
import info.podkowinski.sandra.financescanner.category.CategoryService;
import info.podkowinski.sandra.financescanner.csvScanner.CsvSettings;
import info.podkowinski.sandra.financescanner.csvScanner.CsvSettingsService;
import info.podkowinski.sandra.financescanner.transaction.Transaction;
import info.podkowinski.sandra.financescanner.transaction.TransactionService;
import info.podkowinski.sandra.financescanner.user.CurrentUser;
import info.podkowinski.sandra.financescanner.user.User;
import info.podkowinski.sandra.financescanner.user.UserService;
import info.podkowinski.sandra.financescanner.user.UserServiceImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/project")
public class ProjectController {

    private final TransactionService transactionService;
    private final ProjectService projectService;
    private final AccountService accountService;
    private final CategoryService categoryService;
    private final CsvSettingsService csvSettingsService;
    private final UserServiceImpl userService;


    public ProjectController(TransactionService transactionService, ProjectService projectService, AccountService accountService,
                             CategoryService categoryService, CsvSettingsService csvSettingsService, UserServiceImpl userService) {
        this.transactionService = transactionService;
        this.projectService = projectService;
        this.accountService = accountService;
        this.categoryService = categoryService;
        this.csvSettingsService = csvSettingsService;
        this.userService = userService;
    }

    @GetMapping("/list")
    public String list(Model model, @AuthenticationPrincipal CurrentUser currentUser) {

        Project project = currentUser.getUser().getCurrentProject();
        List<Project> projectsList = projectService.findAllByUserId(currentUser.getUser().getId());
        model.addAttribute("projectsList", projectsList);
        return "project-table";
    }

    @GetMapping("/add")
    public String add(Model model, @AuthenticationPrincipal CurrentUser currentUser) {

        Project project = currentUser.getUser().getCurrentProject();
        Long projectId = projectService.createDefaultProject();
        categoryService.createDefaultCategories(projectId);
        Project newProject  = projectService.findById(projectId);
        model.addAttribute("currentProject", project);
        model.addAttribute("newProject", newProject);
        return "project-add";
    }

    @PostMapping("/add")
    public String addPost(@ModelAttribute Project project, @AuthenticationPrincipal CurrentUser currentUser) {

        Project currentProject = currentUser.getUser().getCurrentProject();
        projectService.save(project);

        return "redirect:/list";
    }

    @GetMapping("/edit/{projectId}")
    public String edit(Model model, @PathVariable Long projectId, @AuthenticationPrincipal CurrentUser currentUser) {
        currentUser.getUser().getProjects().contains(projectService.findById(projectId));
        Project project = projectService.findById(projectId);
        model.addAttribute("project", project);
        return "project-edit";
    }

    //todo frontend validation
    @PostMapping("/edit/{projectId}")
    public String editPost(@ModelAttribute Project project, @PathVariable Long projectId, @AuthenticationPrincipal CurrentUser currentUser) {
        currentUser.getUser().getProjects().contains(projectService.findById(projectId));
        projectService.save(project);
        return "redirect:/list";
    }

    @ResponseBody
    @GetMapping("/delete/{projectId}")
    public String delete(@PathVariable Long projectId, @AuthenticationPrincipal CurrentUser currentUser) {
        currentUser.getUser().getProjects().contains(projectService.findById(projectId));
        projectService.deleteProjectsCategories(projectService.findById(projectId));
        return "";
    }



    @GetMapping("/setProject")
    public String setProject() {

        Long projectId = projectService.createDefaultProject();
        categoryService.createDefaultCategories(projectId);
        csvSettingsService.createDefaultBanksSettings();
        User user = new User();
        user.setPassword("qwe");
        user.setUsername("qwe");
        List<Project> project1= new ArrayList<>();
        project1.add(projectService.findById(projectId));
        user.setProjects(project1);
        userService.saveUser(user);
        return "redirect:../category/list";
    }
}
