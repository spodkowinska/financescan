package info.podkowinski.sandra.financescanner.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;


public interface CategoryRepository extends JpaRepository<Category, Long> {
//    @Query(value = "SELECT * FROM categories c WHERE c.keywords LIKE ?1", nativeQuery = true)
//    public Category findByKeyword(String keyword);

    ArrayList<Category> findAllByUserId(Long id);


}
