package info.podkowinski.sandra.financescanner.account;

import info.podkowinski.sandra.financescanner.project.Project;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    String description;

    LocalDateTime created = LocalDateTime.now();

    @JoinColumn(name = "project_id")
    @ManyToOne
    Project project;
}
