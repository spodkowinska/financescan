package info.podkowinski.sandra.financescanner.category;

import com.opencsv.exceptions.CsvValidationException;
import info.podkowinski.sandra.financescanner.bank.Bank;
import info.podkowinski.sandra.financescanner.csvScanner.CsvSettings;
import info.podkowinski.sandra.financescanner.transaction.TransactionService;
import info.podkowinski.sandra.financescanner.user.User;
import info.podkowinski.sandra.financescanner.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.text.ParseException;
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
        User user1 = userService.findById(1l);
        List<Category> categories = categoryService.findByUserId(1l);
        Map<String, String> usedKeywords = categoryService.usedKeywords(1l);
        model.addAttribute("usedKeywords", usedKeywords);
        model.addAttribute("categories", categories);
        return "add-category";
    }
//todo is keyword already used
    @PostMapping("/add")
    @ResponseBody
    public String addPost(HttpServletRequest request) {
        User user1 = userService.findById(1l);
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String keywords= categoryService.filterKeywords(request.getParameter("keywords"));
        Long parent = categoryService.parseParentCategory(request.getParameter("parent"));
        Category category = new Category(name, description, keywords, parent, user1);
        categoryService.save(category);
        return "good";
    }
}
