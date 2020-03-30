package info.podkowinski.sandra.financescanner.category;
//
//import info.podkowinski.sandra.financescanner.keyword.Keyword;
//import info.podkowinski.sandra.financescanner.keyword.KeywordRepository;
import info.podkowinski.sandra.financescanner.transaction.TransactionRepository;
import info.podkowinski.sandra.financescanner.user.User;
import info.podkowinski.sandra.financescanner.user.UserRepository;
import javassist.compiler.ast.Keyword;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
//    private final KeywordRepository keywordRepository;
    private final UserRepository userRepository;

    public CategoryService(CategoryRepository categoryRepository, TransactionRepository transactionRepository,
                           UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }


    public List<Category>findByUserId(Long id){
        return categoryRepository.findAllByUserId(id);
    }



    public void save(Category category){ categoryRepository.save(category); }

    public Category findById(Long id){
        return categoryRepository.findById(id).orElse(null);
    }

//    Category compareCategories(Long categoryId, Category newCategory){
//        Category category = categoryRepository.findById(categoryId).orElse(null);
//        if (!category.equals(newCategory)){
//            category.name=newCategory.name;
//            category.description=newCategory.description;
//            category.keywords=filterKeywords(newCategory.keywords);
//            category.parentCategoryId=newCategory.parentCategoryId;
//        }
//        return category;
//    }
    void delete(Category category) {
            categoryRepository.delete(category);
    }

    public List<String> areValidKeywords(String [] keywordToCheck) {
        List<String> keywordsList = new ArrayList<>();
        for (String word : keywordToCheck) {
            if (word != null && word.length() != 0 && word != " ") {
                keywordsList.add(word.trim());
            }
        }
        return keywordsList;
    }
}


