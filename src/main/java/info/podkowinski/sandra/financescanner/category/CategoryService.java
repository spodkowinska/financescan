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

    Long parseParentCategory(String parent){
        if(parent.equals("--select category if it is not a parent category--")){
            parent= "0";
        }
        return Long.parseLong(parent);
    }
    Category findById(Long id){
        return categoryRepository.findById(id).orElse(null);
    }
}


