package info.podkowinski.sandra.financescanner.category;


import info.podkowinski.sandra.financescanner.project.Project;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "categories",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"id", "name"})}
)
public class Category {

    public Category(String name, String description, List<String> keywords, String color, String fontColor, Project project) {
        this.name = name;
        this.description = description;
        this.keywords = keywords;
        this.color = color;
        this.fontColor = fontColor;
        this.project = project;
    }

    public Category(String name, String description, String color, String fontColor, Project project) {
        this.name = name;
        this.description = description;
        this.color = color;
        this.fontColor = fontColor;
        this.project = project;
    }

    public Category() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    public
    String name;

    String description;

    @ElementCollection
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
    List<String> keywords;

    @ElementCollection
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
    List<String> safeKeywords;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
    Project project;

    @Column(length = 7)
    String color;
    
    @Column(length = 7)
    String fontColor;
}
