package info.podkowinski.sandra.financescanner.project;

import info.podkowinski.sandra.financescanner.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    String description;

    LocalDateTime createdDate = LocalDateTime.now();

    LocalDateTime archivedDate;

    Boolean archived = false;
}
