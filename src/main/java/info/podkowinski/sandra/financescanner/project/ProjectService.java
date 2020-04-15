package info.podkowinski.sandra.financescanner.project;

import info.podkowinski.sandra.financescanner.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {

        this.projectRepository = projectRepository;
    }
    public void save(Project project){
        projectRepository.save(project);
    }

    public void deleteProjectsCategories(Project project){ projectRepository.deleteProjectsCategories(project); }

    public Project findById(Long id){ return projectRepository.findById(id).orElse(null);}

    Long createDefaultProject(){
        Project project = new Project();
        project.name = "First Project";
//        projectRepository.save(project);
//        projectRepository.flush();
//        long test = project.getId();
        project = projectRepository.saveAndFlush(project);
        long test = project.getId();
        return project.getId();
    }
    List<Project> findAllByUserId(Long userId){
        return projectRepository.findAllByUserId(userId);
    }
}
