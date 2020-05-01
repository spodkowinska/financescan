package info.podkowinski.sandra.financescanner.transaction;


import info.podkowinski.sandra.financescanner.account.Account;
import info.podkowinski.sandra.financescanner.imports.Import;
import info.podkowinski.sandra.financescanner.project.Project;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.format.annotation.DateTimeFormat;
import info.podkowinski.sandra.financescanner.category.Category;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate transactionDate;

    @Column(length = 500)
    String description;

    String party;

    @Column(columnDefinition = "DECIMAL(10,2)")
    double amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
    Account account;

    @JoinColumn(name = "categories_id")
    @ManyToMany(fetch = FetchType.EAGER)
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
    Set <Category> categories;


    @JoinColumn(name = "categories_id")
    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
    Set<Category> pendingCategories;

    @JoinColumn(name = "categories_id")
    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
    Set<Category> rejectedCategories;

    void addCategory(Category category){
        this.categories.add(category);
        this.pendingCategories.remove(category);
    }

    void removeCategory(Category category) {
        this.categories.remove(category);
        this.pendingCategories.remove(category);
        this.rejectedCategories.add(category);
    }

    @JoinColumn(name = "project_id")
    @ManyToOne(fetch = FetchType.EAGER)
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
    Project project;

    @JoinColumn(name = "import_id")
    @ManyToOne(fetch = FetchType.EAGER)
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
    Import importName;



}
