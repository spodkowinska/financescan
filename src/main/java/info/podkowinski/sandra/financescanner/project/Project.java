package info.podkowinski.sandra.financescanner.project;

import info.podkowinski.sandra.financescanner.user.Role;
import info.podkowinski.sandra.financescanner.user.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role_mapping",
            joinColumns = {@JoinColumn(name = "project_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    @MapKeyJoinColumn(name = "user_id")
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
    Map<User, Role> usersWithRolesMap;


}
