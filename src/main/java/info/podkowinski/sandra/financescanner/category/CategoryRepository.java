package info.podkowinski.sandra.financescanner.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;


public interface CategoryRepository extends JpaRepository<Category, Long> {


    ArrayList<Category> findAllByUserId(Long id);


}
