package info.podkowinski.sandra.financescanner.category;

import com.opencsv.exceptions.CsvValidationException;
import info.podkowinski.sandra.financescanner.bank.Bank;
import info.podkowinski.sandra.financescanner.csvScanner.CsvSettings;
import info.podkowinski.sandra.financescanner.transaction.Transaction;
import info.podkowinski.sandra.financescanner.transaction.TransactionService;
import info.podkowinski.sandra.financescanner.user.User;
import info.podkowinski.sandra.financescanner.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/category")
public class CategoryController {
    private final UserService userService;
    private final TransactionService transactionService;
    private final CategoryService categoryService;

    public CategoryController(UserService userService, TransactionService transactionService, CategoryService categoryService) {
        this.userService = userService;
        this.transactionService = transactionService;
        this.categoryService = categoryService;
    }

    @GetMapping("/add")
    public String add(Model model) {
        User user1 = userService.findById(2l);
        List<Category> categories = categoryService.findByUserId(2l);
        Map<String, String> usedKeywords = categoryService.usedKeywords(2l);
        model.addAttribute("usedKeywords", usedKeywords);
        model.addAttribute("categories", categories);
        return "add-category";
    }
//todo frontend validation, name cannot be the same, keywords info about usage
    @PostMapping("/add")
    public String addPost(HttpServletRequest request) {
        User user1 = userService.findById(2l);
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String keywords= categoryService.filterKeywords(request.getParameter("keywords"));
        Long parent = Long.parseLong(request.getParameter("parent"));
        Category category = new Category(name, description, keywords, parent, user1);
        categoryService.save(category);
        return "redirect:../category/list";
    }

    @RequestMapping("/list")
    public String categoryList(Model model) {
        User user1 = userService.findById(2l);
        List<Category>categoriesList = categoryService.findByUserId(2l);
        model.addAttribute("cl", categoriesList);
        return "categories-list";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        User user1 = userService.findById(2l);
        List<Category> categories = categoryService.findByUserId(2l);
        Map<String, String> usedKeywords = categoryService.usedKeywords(2l);
        Category category = categoryService.findById(id);
        model.addAttribute("category", category);
        model.addAttribute("usedKeywords", usedKeywords);
        model.addAttribute("categories", categories);
        return "edit-category";
    }
    @PostMapping("/edit/{id}")
    public String editPost(@PathVariable Long id, @ModelAttribute Category category1) {
        User user1 = userService.findById(2l);
        Category category = categoryService.compareCategories(id, category1);
        category.user=user1;
        categoryService.save(category);
        return "redirect:../../category/list";
    }
}
