package info.podkowinski.sandra.financescanner.user;

import info.podkowinski.sandra.financescanner.project.Project;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    String mail;

    @ManyToMany
    @JoinColumn(name = "project_id")
    List <Project> projects;

}
