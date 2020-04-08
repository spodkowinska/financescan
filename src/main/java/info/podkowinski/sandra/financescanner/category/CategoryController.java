package info.podkowinski.sandra.financescanner.category;


import info.podkowinski.sandra.financescanner.transaction.Transaction;
import info.podkowinski.sandra.financescanner.transaction.TransactionService;
import info.podkowinski.sandra.financescanner.project.Project;
import info.podkowinski.sandra.financescanner.project.ProjectService;
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
        Project project1 = projectService.findById(2l);
        Category category = new Category();
        model.addAttribute("category", category);
        return "categories/category-edit";
    }

    //todo frontend validation, name cannot be the same, keywords info about usage
    @ResponseBody
    @PostMapping("/add")
    public String addPost(HttpServletRequest request) {
        Project project1 = projectService.findById(2l);
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String color = request.getParameter("color");
        String fontColor = request.getParameter("fontColor");
        List<String> validatedKeywords = categoryService.areValidKeywords(request.getParameter("keywords").split(","));
        Category category = new Category(name, description, validatedKeywords, color, fontColor, project1);
        categoryService.save(category);
        return "";
    }

    @RequestMapping("/list")
    public String categoryList(Model model) {
        Project project1 = projectService.findById(2l);
        List<Category>categoriesList = categoryService.findByProjectId(2l);
        model.addAttribute("cl", categoriesList);
        return "categories/category-table";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Project project1 = projectService.findById(2l);
        List<Category> categories = categoryService.findByProjectId(2l);
        List<String>keywordList = categoryService.findById(id).keywords;
        Category category = categoryService.findById(id);
        model.addAttribute("category", category);
        model.addAttribute("keywords", keywordList);
        model.addAttribute("categories", categories);
        return "categories/category-edit";
    }

    @PostMapping("/edit/{id}")
    public String editPost(@PathVariable Long id, @ModelAttribute Category category, HttpServletRequest request) {
        Project project1 = projectService.findById(2l);
        category.project = project1;
        categoryService.save(category);
        return "redirect:../../category/list";
    }

    @ResponseBody
    @PostMapping("/setcolor/{id}")
    public String setColorPost(@PathVariable Long id, HttpServletRequest request) {
        Project project1 = projectService.findById(2l);
        Category category = categoryService.findById(id);
        if (category != null) {
            category.color = request.getParameter("color");
            String fontColor = request.getParameter("fontColor");
            if (fontColor != null)
                category.fontColor = fontColor;
            categoryService.save(category);
        }
        return "";
    }

    @GetMapping("/delete/{categoryId}")
    public String delete(@PathVariable Long categoryId, Model model) {
        Project project1 = projectService.findById(2l);
        transactionService.removeCategoryFromTransactions(categoryId);
        List<Category>categoriesList = categoryService.findByProjectId(2l);
        categoryService.delete(categoryService.findById(categoryId));
        model.addAttribute("cl", categoriesList);
        return "redirect:../../category/list";
    }

    @ResponseBody
    @PostMapping("/keyword/add")
    public String addKeywordPost(HttpServletRequest request) {
        Project project1 = projectService.findById(2l);
        Category category = categoryService.findById(Long.parseLong(request.getParameter("category")));
        List<String> validatedKeywords = categoryService.areValidKeywords(request.getParameter("keywords").split(","));
        category.keywords.addAll(validatedKeywords);
        List<String> validatedSafeKeywords = categoryService.areValidKeywords(request.getParameter("safeKeywords").split(","));
        category.safeKeywords.addAll(validatedSafeKeywords);
        categoryService.save(category);
        return "";
    }

    @GetMapping("/keyword/add/{transactionId}")
    public String addKeywordFromSafeTransaction(Model model, @PathVariable Long transactionId) {
        Project project1 = projectService.findById(2l);
        List<Category> categories = categoryService.findByProjectId(2l);
        Transaction transaction = transactionService.findById(transactionId);
        String keyword = transaction.getDescription();
        model.addAttribute("keywords", keyword);
        model.addAttribute("categories", categories);
        return "keywords/keyword-add";
    }
    @ResponseBody
    @GetMapping("/numberoftransactions/{categoryId}")
    public String addKeywordFromTransaction(@PathVariable Long categoryId) {
        Project project1 = projectService.findById(2l);
        Long numberOTransactionsPerCategory = categoryService.findNumberOfTransactionsPerCategory(categoryId);
        Long numberOTransactionsPerPendingCategory = categoryService.findNumberOfTransactionsPerPendingCategory(categoryId);
        return numberOTransactionsPerCategory + "," + numberOTransactionsPerPendingCategory;
    }

}
