package info.podkowinski.sandra.financescanner.keyword;

import info.podkowinski.sandra.financescanner.category.Category;
import info.podkowinski.sandra.financescanner.category.CategoryRepository;
import info.podkowinski.sandra.financescanner.user.User;
import info.podkowinski.sandra.financescanner.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeywordService {

    private final KeywordRepository keywordRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public KeywordService(KeywordRepository keywordRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.keywordRepository = keywordRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public void save(Keyword keyword){
        keywordRepository.save(keyword);
    }

    public Keyword saveAndFlush(Keyword keyword){
        return keywordRepository.saveAndFlush(keyword);
    }

    void delete(Keyword keyword){
        keywordRepository.delete(keyword);
    }

    List<Keyword>findByUserId(Long id){
        return keywordRepository.findAllByUserId(id);
    }

    List<Keyword> findByCategoryId(Long categoryId){
        return keywordRepository.findAllByCategoryId(categoryId);
    }

    public Keyword findById(Long id){
        return keywordRepository.findById(id).orElse(null);
    }

    public boolean isValidKeyword(String keywordToCheck){
        if(keywordToCheck==null || keywordToCheck.length()==0 || keywordToCheck==" "){
            return false;
        }
        for(Keyword keyword: keywordRepository.findAllByUserId(2l)){
            if(keyword.name.equals(keywordToCheck)){
                return false;
            }
        }
        return true;
    }

    public List <Keyword> checkKeywords(List<String> wordsList, Long categoryId, Long userId){
        User user = userRepository.findById(userId).orElse(null);
        Category category = categoryRepository.findById(categoryId).orElse(null);
        List<Keyword>oldKeywords = keywordRepository.findAllByCategoryId(categoryId);
        List<Keyword>newKeywords = new ArrayList<>();
        for (String word : wordsList) {
            boolean isInNewDatabase = false;
            for(Keyword keyword :oldKeywords){
                if(keyword.name.toLowerCase().equals(word.toLowerCase())){
                    newKeywords.add(keyword);
                    oldKeywords.remove(keyword);
                    isInNewDatabase = true;
                    break;
                }
            }
            if(isInNewDatabase=false) {
                Keyword keyword = new Keyword(word, category, user);
                newKeywords.add(keyword);
            }
        }
        return newKeywords;
    }

    public void saveKeywords(List<Keyword> keywordsList){
        for (Keyword keyword: keywordsList) {
            keywordRepository.save(keyword);
        }
    }

}


