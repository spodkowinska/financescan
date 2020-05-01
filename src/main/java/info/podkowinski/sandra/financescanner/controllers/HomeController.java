package info.podkowinski.sandra.financescanner.controllers;

import info.podkowinski.sandra.financescanner.account.AccountService;
import info.podkowinski.sandra.financescanner.csvScanner.CsvSettingsService;
import info.podkowinski.sandra.financescanner.transaction.Transaction;
import info.podkowinski.sandra.financescanner.transaction.TransactionService;
import info.podkowinski.sandra.financescanner.project.ProjectService;
import info.podkowinski.sandra.financescanner.user.RoleRepository;
import info.podkowinski.sandra.financescanner.user.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class HomeController {

    private final TransactionService transactionService;
    private final ProjectService projectService;
    private final CsvSettingsService csvSettingsService;
    private final AccountService accountService;
    private final UserServiceImpl userService;
    private final RoleRepository roleRepository;

    public HomeController(RoleRepository roleRepository, TransactionService transactionService, ProjectService projectService, AccountService accountService, CsvSettingsService csvSettingsService, UserServiceImpl userService) {
        this.transactionService = transactionService;
        this.projectService = projectService;
        this.csvSettingsService = csvSettingsService;
        this.accountService = accountService;
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }


    @GetMapping("/setDatabase")
    public String setDatabase() {

        csvSettingsService.createDefaultBanksSettings();
        userService.saveDefaultRoles();
        return "redirect:home";
    }
}

