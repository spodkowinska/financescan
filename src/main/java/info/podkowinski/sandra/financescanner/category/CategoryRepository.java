package info.podkowinski.sandra.financescanner.category;

import info.podkowinski.sandra.financescanner.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "SELECT * FROM categories c WHERE c.keywords LIKE ?1", nativeQuery = true)
    public Category findByKeyword(String keyword);

    public List<Category> findAllByUser(User user);
}
