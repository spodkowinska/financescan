package info.podkowinski.sandra.financescanner.category;


import info.podkowinski.sandra.financescanner.transaction.Transaction;
import info.podkowinski.sandra.financescanner.transaction.TransactionService;
import info.podkowinski.sandra.financescanner.user.User;
import info.podkowinski.sandra.financescanner.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

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
        Category category = new Category();
        model.addAttribute("category", category);
        return "category-edit";
    }

    //todo frontend validation, name cannot be the same, keywords info about usage
    @ResponseBody
    @PostMapping("/add")
    public String addPost(HttpServletRequest request) {
        User user1 = userService.findById(2l);
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String color = request.getParameter("color");
        List<String> validatedKeywords = categoryService.areValidKeywords(request.getParameter("keywords").split(","));
        Category category = new Category(name, description, validatedKeywords, color, user1);
        categoryService.save(category);
        return "";
    }

    @RequestMapping("/list")
    public String categoryList(Model model) {
        User user1 = userService.findById(2l);
        List<Category>categoriesList = categoryService.findByUserId(2l);
        model.addAttribute("cl", categoriesList);
        return "category-table";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        User user1 = userService.findById(2l);
        List<Category> categories = categoryService.findByUserId(2l);
        List<String>keywordList = categoryService.findById(id).keywords;
        Category category = categoryService.findById(id);
        model.addAttribute("category", category);
        model.addAttribute("keywords", keywordList);
        model.addAttribute("categories", categories);
        return "category-edit";
    }

    @PostMapping("/edit/{id}")
    public String editPost(@PathVariable Long id, @ModelAttribute Category category, HttpServletRequest request) {
        User user1 = userService.findById(2l);
        category.user=user1;
        categoryService.save(category);
        return "redirect:../../category/list";
    }

    @ResponseBody
    @PostMapping("/setcolor/{id}")
    public String setColorPost(@PathVariable Long id, HttpServletRequest request) {
        User user1 = userService.findById(2l);
        Category category = categoryService.findById(id);
        if (category != null) {
            category.color = request.getParameter("color");
            categoryService.save(category);
        }
        return "";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Model model) {
        User user1 = userService.findById(2l);
        Category category = categoryService.findById(id);
        categoryService.delete(category);
        List<Category>categoriesList = categoryService.findByUserId(2l);
        model.addAttribute("cl", categoriesList);
        return "redirect:../../category/list";
    }


    @GetMapping("/keyword/add")
    public String addKeyword(Model model) {
        User user1 = userService.findById(2l);
        List<Category> categories = categoryService.findByUserId(2l);
        model.addAttribute("categories", categories);
        return "add-keyword";
    }
    @ResponseBody
    @PostMapping("/keyword/add")
    public String addKeywordPost(HttpServletRequest request) {
        User user1 = userService.findById(2l);
        Category category = categoryService.findById(Long.parseLong(request.getParameter("category")));
        List<String> validatedKeywords = categoryService.areValidKeywords(request.getParameter("keywords").split(","));
        category.keywords.addAll(validatedKeywords);

        categoryService.save(category);
        return "";
    }
    @GetMapping("/keyword/add/{transactionId}")
    public String addKeywordFromTransaction(Model model, @PathVariable Long transactionId) {
        User user1 = userService.findById(2l);
        List<Category> categories = categoryService.findByUserId(2l);
        Transaction transaction = transactionService.findById(transactionId);
        String keyword = transaction.getDescription();
        model.addAttribute("keywords", keyword);
        model.addAttribute("categories", categories);
        return "add-keyword-modal";
    }

    @RequestMapping("/keyword/list")
    public String keywordList(Model model) {
        User user1 = userService.findById(2l);
        List<Category>categoriesList = categoryService.findByUserId(2l);
        model.addAttribute("categories", categoriesList);
        return "list-keywords";
    }

//    @GetMapping("/keyword/edit/{categoryId}/{keyword}")
//    public String editKeyword(@PathVariable String keyword, @PathVariable Long categoryId, Model model) {
//        User user1 = userService.findById(2l);
//        List<Category> categories = categoryService.findByUserId(2l);
//        model.addAttribute("keyword", keyword);
//        model.addAttribute("category", categoryService.findById(categoryId));
//        model.addAttribute("categories", categories);
//        return "edit-keyword";
//    }
//
//    @PostMapping("/keyword/edit/{categoryId}/{keyword}")
//    public String editKeywordPost(@PathVariable String keyword, @PathVariable Long categoryId, HttpServletRequest request) {
//        User user1 = userService.findById(2l);
//        Category category = categoryService.findById(categoryId);
//        category.keywords.remove(keyword);
//        List<String> validatedKeywords = categoryService.areValidKeywords(request.getParameter("keywords").split(","));
//        category.keywords.addAll(validatedKeywords);
//        categoryService.save(category);
//        return "redirect:../../keyword/list";
//    }

    @GetMapping("/keyword/delete/{categoryId}/{keyword}")
    public String deleteKeyword(@PathVariable String keyword, @PathVariable Long categoryId) {
        User user1 = userService.findById(2l);
        Category category = categoryService.findById(categoryId);
        category.keywords.remove(keyword);
        categoryService.save(category);
        return "redirect:../../list";
    }
}
