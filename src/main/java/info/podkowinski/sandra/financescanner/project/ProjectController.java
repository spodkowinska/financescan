package info.podkowinski.sandra.financescanner.project;

import info.podkowinski.sandra.financescanner.account.Account;
import info.podkowinski.sandra.financescanner.account.AccountService;
import info.podkowinski.sandra.financescanner.category.Category;
import info.podkowinski.sandra.financescanner.category.CategoryService;
import info.podkowinski.sandra.financescanner.csvScanner.CsvSettings;
import info.podkowinski.sandra.financescanner.csvScanner.CsvSettingsService;
import info.podkowinski.sandra.financescanner.transaction.Transaction;
import info.podkowinski.sandra.financescanner.transaction.TransactionService;
import info.podkowinski.sandra.financescanner.user.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        List<Project> projectsList = projectService.findAllByUserId(currentUser.getUser().getId());
        model.addAttribute("projectsList", projectsList);
        return "projects/project-table";
    }

    @GetMapping("/add")
    public String add(Model model, @AuthenticationPrincipal CurrentUser currentUser) {
        model.addAttribute("project", new Project());
        return "projects/project-edit";
    }

    @PostMapping("/add")
    public String addPost(@ModelAttribute Project project, @AuthenticationPrincipal CurrentUser currentUser) {
        Role role = userService.findRole("OWNER");
        User user = currentUser.getUser();
        HashMap<User, Role> userRole = new HashMap<>();
        userRole.put(user, role);
        project.setUsersWithRolesMap(userRole);
        projectService.save(project);
        categoryService.createDefaultCategories(project.getId());

        currentUser.getUser().getProjects().add(project);
        userService.saveUser(currentUser.getUser());

        return "redirect:/project/list";
    }

    @GetMapping("/edit/{projectId}")
    public String edit(Model model, @PathVariable Long projectId, @AuthenticationPrincipal CurrentUser currentUser) {
        List<Project> projects = currentUser.getUser().getProjects().stream().filter(x -> x.getId() == projectId).collect(Collectors.toList());
        List<User> friends = currentUser.getUser().getFriends();
        List<Role> roles = userService.findProjectRoles();
        model.addAttribute("friends", friends);
        model.addAttribute("projectRoles", roles);
        model.addAttribute("project", projects.get(0));
        return "projects/project-edit";
    }

    //todo frontend validation
    @ResponseBody
    @PostMapping("/edit/{projectId}")
    public String editPost(@ModelAttribute Project project, @PathVariable Long projectId, @AuthenticationPrincipal CurrentUser currentUser) {
        if (currentUser.getUser().getProjects().stream().map(Project::getId).anyMatch(x -> x == projectId))
            projectService.save(project);
        currentUser.getUser().getProjects().clear();
        currentUser.getUser().getProjects().addAll(projectService.findAllByUserId(currentUser.getUser().getId()));
        if(currentUser.getUser().getCurrentProject().getId()==projectId) {
            currentUser.getUser().setCurrentProject(project);
        }
        return "";
    }

    @ResponseBody
    @GetMapping("/delete/{projectId}")
    public String delete(@PathVariable Long projectId, @AuthenticationPrincipal CurrentUser currentUser) {
        User user = currentUser.getUser();
        List<Project> usersProjects = user.getProjects();
        for (Project project : usersProjects) {
            if (project.getId() == projectId) {
                projectService.delete(project);
            }
        }
        return "";
    }

    @ResponseBody
    @GetMapping("/archive/{projectId}")
    public String archive(@PathVariable Long projectId, @AuthenticationPrincipal CurrentUser currentUser) {
        if (currentUser.getUser().getCurrentProject().getId() == projectId) {
            // Current project can't be archived. This also ensures we always have at least 1 un-archived project.
            return "error:current";
        }

        Project project = projectService.findById(projectId);
        if (!currentUser.getUser().getProjects().stream().map(Project::getId).anyMatch(x -> x == projectId)) {
            // This project doesn't belong to the user
            return "error:owner";
        }

        project.archived = true;
        project.archivedDate = LocalDateTime.now();
        System.out.println(project.archivedDate);
        projectService.save(project);

        return "";
    }

    @ResponseBody
    @GetMapping("/restore/{projectId}")
    public String restore(@PathVariable Long projectId, @AuthenticationPrincipal CurrentUser currentUser) {
        Project project = projectService.findById(projectId);
        if (currentUser.getUser().getProjects().stream().map(Project::getId).anyMatch(x -> x == projectId)) {
            project.archived = false;
            projectService.save(project);
        }
        return "";
    }

    // TODO remove this hack mapping
    @GetMapping("/setProject")
    public String setProjectHack() {
        Long projectId = projectService.createDefaultProject();
        categoryService.createDefaultCategories(projectId);
        csvSettingsService.createDefaultBanksSettings();

        User user = userService.createDefaultUserHack();
        List<Project> project1 = new ArrayList<>();
        project1.add(projectService.findById(projectId));
        user.setProjects(project1);
        userService.saveUser(user);

        return "redirect:/project/list";
    }
}
