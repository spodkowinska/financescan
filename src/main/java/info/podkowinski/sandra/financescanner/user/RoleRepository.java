package info.podkowinski.sandra.financescanner.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);

    public Role save(Role role);

    @Query(value = "SELECT * FROM role r WHERE r.name IN ('OWNER', 'EDITOR', 'VIEWER')", nativeQuery = true)
    List<Role>findProjectRoles();
}