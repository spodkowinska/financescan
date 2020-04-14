package info.podkowinski.sandra.financescanner.imports;

import info.podkowinski.sandra.financescanner.account.Account;
import info.podkowinski.sandra.financescanner.csvScanner.CsvSettings;
import info.podkowinski.sandra.financescanner.project.Project;
import lombok.Getter;
import lombok.Setter;

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
    Project project;

    String fileName;

    @ManyToOne
    Account account;

    @ManyToOne
    CsvSettings usedSettings;
}
