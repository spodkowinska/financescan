package info.podkowinski.sandra.financescanner.category;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService {
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    private final CategoryRepository categoryRepository;

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
}


