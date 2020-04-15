package info.podkowinski.sandra.financescanner.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Project saveAndFlush(Project project);

    @Query(value = "SELECT p.id, p.name, p.description FROM projects p, user_projects  up  WHERE up.user_id = ?", nativeQuery = true)
    List<Project> findAllByUserId(Long userId);

    @Modifying
    @Query(value = "DELETE FROM categories WHERE categories.project_id = ?", nativeQuery = true)
    void deleteProjectsCategories(Project project);
}
