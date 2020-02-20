package info.podkowinski.sandra.financescanner.keyword;

import info.podkowinski.sandra.financescanner.category.Category;
import info.podkowinski.sandra.financescanner.category.CategoryRepository;
import info.podkowinski.sandra.financescanner.transaction.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeywordService {

    private final KeywordRepository keywordRepository;

    public KeywordService(KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }

    void save(Keyword keyword){
        keywordRepository.save(keyword);
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

    Keyword findById(Long id){
        return keywordRepository.findById(id).orElse(null);
    }


}


