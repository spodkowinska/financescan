package info.podkowinski.sandra.financescanner.category;

import info.podkowinski.sandra.financescanner.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "categories",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"id", "name"})}
)
public class Category {

    public Category(String name, String description, String keywords, Long parentCategoryId, User user) {
        this.name = name;
        this.description = description;
        this.keywords = keywords;
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

    @Column(nullable = false)
    String keywords;

    Long parentCategoryId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

}
