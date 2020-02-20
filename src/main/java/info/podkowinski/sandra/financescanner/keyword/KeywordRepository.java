package info.podkowinski.sandra.financescanner.keyword;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface KeywordRepository extends JpaRepository <Keyword, Long> {

  List<Keyword> findAllByUserId(Long userId);
  List<Keyword> findAllByCategoryId(Long categoryId);
}
