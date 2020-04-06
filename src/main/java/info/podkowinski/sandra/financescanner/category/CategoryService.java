package info.podkowinski.sandra.financescanner.category;

import info.podkowinski.sandra.financescanner.transaction.TransactionRepository;
import info.podkowinski.sandra.financescanner.user.User;
import info.podkowinski.sandra.financescanner.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
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

    Long findNumberOfTransactionsPerCategory(Long categoryId){
      return  categoryRepository.findNumberOfTransactionsPerCategory(categoryId);
    }

    Long findNumberOfTransactionsPerPendingCategory(Long categoryId){
        return  categoryRepository.findNumberOfTransactionsPerPendingCategory(categoryId);
    }
//    public Long doesKeywordExistInDifferentCategory(String keyword, Long categoryId){
//        List<Category> usersCategories = categoryRepository.findAllByUserId(2l);
//        for (Category category :usersCategories) {
//            if (!category.equals(categoryRepository.findById(categoryId))) {
//                for (String key : category.keywords) {
//                    if (key.toLowerCase().equals(keyword.toLowerCase())) {
//                        return category.getId();
//                    }
//                }
//            }
//        }
//        return -1l;
//    }
}


