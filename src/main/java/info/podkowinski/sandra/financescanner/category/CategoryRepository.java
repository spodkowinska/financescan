package info.podkowinski.sandra.financescanner.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;


public interface CategoryRepository extends JpaRepository<Category, Long> {


    ArrayList<Category> findAllByProjectId(Long id);

    @Query(value = "SELECT Count(*) FROM transactions_categories WHERE categories_id = ?", nativeQuery = true)
    Long findNumberOfTransactionsPerCategory(Long categoryId);

    @Query(value = "SELECT Count(*) FROM transactions_pending_categories WHERE pending_categories_id = ?", nativeQuery = true)
    Long findNumberOfTransactionsPerPendingCategory(Long pendingCategoryId);
}
