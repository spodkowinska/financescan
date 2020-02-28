package info.podkowinski.sandra.financescanner.keyword;


import info.podkowinski.sandra.financescanner.category.Category;
import info.podkowinski.sandra.financescanner.category.CategoryService;
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
@RequestMapping("/keyword")
public class KeywordController {
    private final UserService userService;
    private final KeywordService keywordService;
    private final CategoryService categoryService;
    private final TransactionService transactionService;

    public KeywordController(UserService userService, KeywordService keywordService,
                             CategoryService categoryService, TransactionService transactionService) {
        this.userService = userService;
        this.keywordService = keywordService;
        this.categoryService = categoryService;
        this.transactionService = transactionService;
    }

    @GetMapping("/add")
    public String add(Model model) {
        User user1 = userService.findById(2l);
        List<Category> categories = categoryService.findByUserId(2l);
//        Map<String, String> usedKeywords = categoryService.usedKeywords(2l);
//        model.addAttribute("usedKeywords", usedKeywords);
        model.addAttribute("categories", categories);
        return "add-keyword";
    }
//todo frontend validation,
    @PostMapping("/add")
    public String addPost(HttpServletRequest request) {
        User user1 = userService.findById(2l);
        Category category = categoryService.findById(Long.parseLong(request.getParameter("category")));
        Keyword keyword1 = new Keyword(request.getParameter("keyword1"), category, user1);
        Keyword keyword2 = new Keyword(request.getParameter("keyword2"), category, user1);
        Keyword keyword3 = new Keyword(request.getParameter("keyword3"), category, user1);
        if(keywordService.isValidKeyword(keyword1.name)){keywordService.save(keyword1);}
        if(keywordService.isValidKeyword(keyword2.name)){keywordService.save(keyword2);}
        if(keywordService.isValidKeyword(keyword3.name)){keywordService.save(keyword3);}
        return "redirect:../keyword/list";
    }
    @GetMapping("/add/{transactionId}")
    public String addFromTransaction(Model model, @PathVariable Long transactionId) {
        User user1 = userService.findById(2l);
        List<Category> categories = categoryService.findByUserId(2l);
        Transaction transaction = transactionService.findById(transactionId);
        String keyword = transaction.getDescription();
        model.addAttribute("keyword", keyword);
        model.addAttribute("categories", categories);
        return "add-keyword";
    }
    @PostMapping("/add/{transactionId}")
    public String addFromTransactionPost(HttpServletRequest request, @PathVariable Long transactionId) {
        User user1 = userService.findById(2l);
        Category category = categoryService.findById(Long.parseLong(request.getParameter("category")));
        Keyword keyword1 = new Keyword(request.getParameter("keyword1"), category, user1);
        Keyword keyword2 = new Keyword(request.getParameter("keyword2"), category, user1);
        Keyword keyword3 = new Keyword(request.getParameter("keyword3"), category, user1);
        if(keywordService.isValidKeyword(keyword1.name)){keywordService.save(keyword1);}
        if(keywordService.isValidKeyword(keyword2.name)){keywordService.save(keyword2);}
        if(keywordService.isValidKeyword(keyword3.name)){keywordService.save(keyword3);}
        return "redirect:../../transaction/list";
    }

    @RequestMapping("/list")
    public String keywordList(Model model) {
        User user1 = userService.findById(2l);
        List<Category>categoriesList = categoryService.findByUserId(2l);
//        List<Keyword>keywordsList = keywordService.findByUserId(2l);
        model.addAttribute("categories", categoriesList);
//        model.addAttribute("keywords", keywordsList);
        return "list-keywords";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        User user1 = userService.findById(2l);
        List<Category> categories = categoryService.findByUserId(2l);
        Keyword keyword = keywordService.findById(id);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categories", categories);
        return "edit-keyword";
    }

    @PostMapping("/edit/{id}")
    public String editPost(@PathVariable Long id, @ModelAttribute Keyword keyword) {
        User user1 = userService.findById(2l);
        Keyword keyword1 = keyword;
        keywordService.save(keyword1);
        return "redirect:../../keyword/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Model model) {
        User user1 = userService.findById(2l);
        Keyword keyword = keywordService.findById(id);
        keywordService.delete(keyword);
        List<Category>categoriesList = categoryService.findByUserId(2l);
        model.addAttribute("categories", categoriesList);
        List<Keyword>keywordsList = keywordService.findByUserId(2l);
        model.addAttribute("keywords",keywordsList);
        return "redirect:../../keyword/list";
    }

}
