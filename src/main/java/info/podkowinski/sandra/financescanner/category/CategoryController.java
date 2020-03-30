package info.podkowinski.sandra.financescanner.category;


import info.podkowinski.sandra.financescanner.keyword.Keyword;
import info.podkowinski.sandra.financescanner.keyword.KeywordService;
import info.podkowinski.sandra.financescanner.transaction.TransactionService;
import info.podkowinski.sandra.financescanner.user.User;
import info.podkowinski.sandra.financescanner.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {
    private final UserService userService;
    private final TransactionService transactionService;
    private final CategoryService categoryService;
    private final KeywordService keywordService;

    public CategoryController(UserService userService, TransactionService transactionService, CategoryService categoryService, KeywordService keywordService) {
        this.userService = userService;
        this.transactionService = transactionService;
        this.categoryService = categoryService;
        this.keywordService = keywordService;
    }

    @GetMapping("/add")
    public String add(Model model) {
        User user1 = userService.findById(2l);
        Category category = new Category();
        model.addAttribute("category", category);
        return "add-category";
    }
//todo frontend validation, name cannot be the same, keywords info about usage
    @PostMapping("/add")
    public String addPost(HttpServletRequest request) {
        User user1 = userService.findById(2l);
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String color = request.getParameter("color");
        Category category = new Category(name, description, color, user1);
        category = categoryService.saveAndFlush(category);
        String keywordName = request.getParameter("keywords");
        if(keywordService.isValidKeyword(keywordName)){
            Keyword keyword = new Keyword(keywordName, category, user1);
            keywordService.saveAndFlush(keyword);
            categoryService.addKeywordAndSave(category, keywordService.findById(keyword.getId()));
        }
        return "redirect:../category/list";
    }

    @RequestMapping("/list")
    public String categoryList(Model model) {
        User user1 = userService.findById(2l);
        List<Category>categoriesList = categoryService.findByUserId(2l);
        model.addAttribute("cl", categoriesList);
        return "list-categories";
    }
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        User user1 = userService.findById(2l);
        List<Category> categories = categoryService.findByUserId(2l);
        List<Keyword>keywordList = categoryService.findById(id).keywords;
        Category category = categoryService.findById(id);
        model.addAttribute("category", category);
        model.addAttribute("keywords", keywordList);
        model.addAttribute("categories", categories);
        return "edit-category";
    }

    @PostMapping("/edit/{id}")
    public String editPost(@PathVariable Long id, @ModelAttribute Category category1, HttpServletRequest request) {
        User user1 = userService.findById(2l);
        category1.user=user1;
        categoryService.save(category1);
        String allKeywords = request.getParameter("keywordsList");
        List<String> wordsList = Arrays.asList(allKeywords.split(","));
        List<Keyword> keywordList = keywordService.checkKeywords(wordsList, category1.getId(), user1.getId());
        keywordService.saveKeywords(keywordList);
        return "redirect:../../category/list";
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

}
