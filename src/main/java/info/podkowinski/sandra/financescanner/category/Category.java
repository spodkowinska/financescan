package info.podkowinski.sandra.financescanner.category;

import info.podkowinski.sandra.financescanner.keyword.Keyword;
import info.podkowinski.sandra.financescanner.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "categories",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"id", "name"})}
)
public class Category {

    public Category(String name, String description, Long parentCategoryId, User user) {
        this.name = name;
        this.description = description;
        this.parentCategoryId = parentCategoryId;
        this.user = user;

    }

    public Category() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    public
    String name;

    String description;

    @OneToMany(mappedBy ="category")
    List<Keyword> keywords;

    Long parentCategoryId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;


}
