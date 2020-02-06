package info.podkowinski.sandra.financescanner.category;

import info.podkowinski.sandra.financescanner.transaction.Transaction;
import info.podkowinski.sandra.financescanner.transaction.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    public CategoryService(CategoryRepository categoryRepository, TransactionRepository transactionRepository) {
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
    }


    public List<Category>findByUserId(Long id){
        return categoryRepository.findAllByUserId(id);
    }

    public Map<String, String>usedKeywords(Long userId){
        Map<String, String>usedKeywords = new HashMap<>();
        List <Category> categoriesByUser = findByUserId(userId);
        for (Category category: categoriesByUser) {
            String keywords = category.keywords;
            List<String> keywordsList = Arrays.asList(keywords.split(",. "));
            for (String word : keywordsList) {
                usedKeywords.put(word, category.name);
            }
        }
        return usedKeywords;
    }
    public void save(Category category){
        categoryRepository.save(category);
    }

    String filterKeywords(String keywords){
        Map<String, String> usedKeywords =usedKeywords(1l);
        StringBuilder sb = new StringBuilder();
        for (String keyword: keywords.split(",. ")) {
            if(!usedKeywords.containsKey(keyword.toLowerCase())){
                sb.append(keyword);
            }
        }
        return sb.toString();
    }

    public Category findById(Long id){
        return categoryRepository.findById(id).orElse(null);
    }

    Category compareCategories(Long categoryId, Category newCategory){
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (!category.equals(newCategory)){
            category.name=newCategory.name;
            category.description=newCategory.description;
            category.keywords=filterKeywords(newCategory.keywords);
            category.parentCategoryId=newCategory.parentCategoryId;
        }
        return category;
    }
    void delete(Category category){
        if(transactionRepository.findByCategoryId(category.id)==null)
            categoryRepository.delete(category);}
}


