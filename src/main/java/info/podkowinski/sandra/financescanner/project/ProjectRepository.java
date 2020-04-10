package info.podkowinski.sandra.financescanner.project;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Project saveAndFlush(Project project);
}
