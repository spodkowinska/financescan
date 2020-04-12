package info.podkowinski.sandra.financescanner.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Project saveAndFlush(Project project);

    @Query(value = "SELECT projects_id FROM user_projects up , projects p WHERE up.user_id =? AND p.is_current_project = true ORDER BY projects_id ASC LIMIT 1", nativeQuery = true)
    Long findCurrentByUserId(Long userId);
}
