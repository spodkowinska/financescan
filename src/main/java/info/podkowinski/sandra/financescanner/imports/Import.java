package info.podkowinski.sandra.financescanner.imports;

import info.podkowinski.sandra.financescanner.account.Account;
import info.podkowinski.sandra.financescanner.csvScanner.CsvSettings;
import info.podkowinski.sandra.financescanner.project.Project;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Import {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    LocalDateTime created = LocalDateTime.now();

    @JoinColumn(name = "project_id")
    @ManyToOne
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
    Project project;

    String fileName;

    @ManyToOne
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
    Account account;

    @ManyToOne
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
    CsvSettings usedSettings;
}
