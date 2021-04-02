package info.podkowinski.sandra.financescanner.category;


import info.podkowinski.sandra.financescanner.transaction.Transaction;
import info.podkowinski.sandra.financescanner.transaction.TransactionService;
import info.podkowinski.sandra.financescanner.project.Project;
import info.podkowinski.sandra.financescanner.project.ProjectService;
import info.podkowinski.sandra.financescanner.user.CurrentUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {
    private final ProjectService projectService;
    private final TransactionService transactionService;
    private final CategoryService categoryService;


    public CategoryController(ProjectService projectService, TransactionService transactionService, CategoryService categoryService) {
        this.projectService = projectService;
        this.transactionService = transactionService;
        this.categoryService = categoryService;

    }

    @GetMapping("/add")
    public String add(Model model) {
        Category category = new Category();
        model.addAttribute("category", category);
        return "categories/category-edit";
    }

    //todo frontend validation, name cannot be the same, keywords info about usage
    @ResponseBody
    @PostMapping("/add")
    public String addPost(HttpServletRequest request, @AuthenticationPrincipal CurrentUser currentUser, @ModelAttribute Category category) {
        Project project = currentUser.getUser().getCurrentProject();
        category.project = project;
        categoryService.save(category);
//        Project project = currentUser.getUser().getCurrentProject();
//        String name = request.getParameter("name");
//        String description = request.getParameter("description");
//        String color = request.getParameter("color");
//        String fontColor = request.getParameter("fontColor");
//        List<String> validatedKeywords = categoryService.areValidKeywords(request.getParameter("keywords").split(","));
//        Category category = new Category(name, description, validatedKeywords, color, fontColor, project);
//        categoryService.save(category);
        return "";
    }

    @RequestMapping("/list")
    public String categoryList(Model model, @AuthenticationPrincipal CurrentUser currentUser) {
        Project project = currentUser.getUser().getCurrentProject();
        List<Category> categoriesList = categoryService.findByProjectId(project.getId());
        model.addAttribute("cl", categoriesList);
        return "categories/category-table";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model, @AuthenticationPrincipal CurrentUser currentUser) {
        Project project = currentUser.getUser().getCurrentProject();
        List<Category> categories = categoryService.findByProjectId(project.getId());
        List<String> keywordList = categoryService.findById(id).keywords;
        Category category = categoryService.findById(id);
        model.addAttribute("category", category);
        model.addAttribute("keywords", keywordList);
        model.addAttribute("categories", categories);
        return "categories/category-edit";
    }

    @PostMapping("/edit/{id}")
    public String editPost(@PathVariable Long id, @ModelAttribute Category category, @AuthenticationPrincipal CurrentUser currentUser) {
        Project project = currentUser.getUser().getCurrentProject();
        category.project = project;
        categoryService.save(category);
        return "redirect:../../category/list";
    }

    @ResponseBody
    @PostMapping("/setcolor/{id}")
    public String setColorPost(@PathVariable Long id, HttpServletRequest request, @AuthenticationPrincipal CurrentUser currentUser) {
        Project project = currentUser.getUser().getCurrentProject();
        Category category = categoryService.findById(id);

        if (category != null && project.getId() == category.project.getId()) {
            category.color = request.getParameter("color");
            String fontColor = request.getParameter("fontColor");
            if (fontColor != null)
                category.fontColor = fontColor;
            categoryService.save(category);
        }
        return "";
    }

    @GetMapping("/delete/{categoryId}")
    public String delete(@PathVariable Long categoryId, Model model, @AuthenticationPrincipal CurrentUser currentUser) {
        Project project = currentUser.getUser().getCurrentProject();
        Category category = categoryService.findById(categoryId);
        if (category != null && project.getId() == category.project.getId()) {
            categoryService.delete(category);
        }
        List<Category> categoriesList = categoryService.findByProjectId(project.getId());
        model.addAttribute("cl", categoriesList);
        return "redirect:../../category/list";
    }

    @ResponseBody
    @PostMapping("/keyword/add")
    public String addKeywordPost(HttpServletRequest request, @ModelAttribute KeywordDTO keywordDTO, @AuthenticationPrincipal CurrentUser currentUser) {
        Project project = currentUser.getUser().getCurrentProject();
        Category category = keywordDTO.getCategory();
        if (project.getId().equals(category.project.getId())) {
            List<String> validatedKeywords = categoryService.areValidKeywords(keywordDTO.keyword.split(","));
            if (keywordDTO.safeWord) {
                category.safeKeywords.addAll(validatedKeywords);
            } else {
                category.keywords.addAll(validatedKeywords);
            }
            categoryService.save(category);
        }
        return "";
    }

    @GetMapping("/keyword/add/{transactionId}")
    public String addKeywordFromSafeTransaction(Model model, @PathVariable Long transactionId, @AuthenticationPrincipal CurrentUser currentUser) {
        Project project = currentUser.getUser().getCurrentProject();
        List<Category> categories = categoryService.findByProjectId(project.getId());
        Transaction transaction = transactionService.findById(transactionId);
        KeywordDTO keywordDTO = new KeywordDTO();
        keywordDTO.setKeyword(transaction.getDescription());
        System.out.println(keywordDTO.keyword);
        model.addAttribute("keywordDTO", keywordDTO);
        model.addAttribute("categories", categories);
        return "keywords/keyword-add";
    }

    @ResponseBody
    @GetMapping("/numberoftransactions/{categoryId}")
    public String numberOfTransactions(@PathVariable Long categoryId, @AuthenticationPrincipal CurrentUser currentUser) {
        Project project = currentUser.getUser().getCurrentProject();
        if (project.getId().equals(categoryService.findById(categoryId).project.getId())) {
            Long numberOTransactionsPerCategory = categoryService.findNumberOfTransactionsPerCategory(categoryId);
            Long numberOTransactionsPerPendingCategory = categoryService.findNumberOfTransactionsPerPendingCategory(categoryId);
            return numberOTransactionsPerCategory + "," + numberOTransactionsPerPendingCategory;
        }
        return "";
    }

}
