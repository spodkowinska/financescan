package info.podkowinski.sandra.financescanner.imports;

import info.podkowinski.sandra.financescanner.project.Project;

import javax.persistence.*;
import java.time.LocalDateTime;

public class Import {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    LocalDateTime created = LocalDateTime.now();

    @JoinColumn(name = "project_id")
    @ManyToOne
    Project project;


}
