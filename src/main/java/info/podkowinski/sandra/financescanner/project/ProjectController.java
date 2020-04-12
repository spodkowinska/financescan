package info.podkowinski.sandra.financescanner.project;

import info.podkowinski.sandra.financescanner.account.Account;
import info.podkowinski.sandra.financescanner.account.AccountService;
import info.podkowinski.sandra.financescanner.category.CategoryService;
import info.podkowinski.sandra.financescanner.csvScanner.CsvSettings;
import info.podkowinski.sandra.financescanner.csvScanner.CsvSettingsService;
import info.podkowinski.sandra.financescanner.transaction.TransactionService;
import info.podkowinski.sandra.financescanner.user.User;
import info.podkowinski.sandra.financescanner.user.UserService;
import info.podkowinski.sandra.financescanner.user.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
