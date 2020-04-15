package info.podkowinski.sandra.financescanner.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Project saveAndFlush(Project project);

}
