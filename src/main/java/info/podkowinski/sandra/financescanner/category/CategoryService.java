package info.podkowinski.sandra.financescanner.category;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    private final CategoryRepository categoryRepository;

    List<Category>findByUserId(Long id){
        return categoryRepository.findAllByUserId(id);
    }
}
