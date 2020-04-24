package info.podkowinski.sandra.financescanner.user;

import info.podkowinski.sandra.financescanner.category.CategoryService;
import info.podkowinski.sandra.financescanner.project.Project;
import info.podkowinski.sandra.financescanner.project.ProjectService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ProjectService projectService;
    private final CategoryService categoryService;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           BCryptPasswordEncoder passwordEncoder, ProjectService projectService, CategoryService categoryService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.projectService = projectService;
        this.categoryService = categoryService;
    }
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    User findById(Long userId){ return userRepository.findById(userId).orElse(null);}

    // TODO get rid of this method, it's a big hack
    @Override
    public User createDefaultUserHack() {
        User user = new User();
        user.setUsername("qwe");
        user.setPassword(passwordEncoder.encode("qwe"));
        user.setEnabled(1);

        Role userRole = roleRepository.findByName("USER");
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));

        userRepository.save(user);

        return user;
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void saveRole(String roleName) {
        Role userRole = new Role();
        userRole.setName(roleName);
        roleRepository.save(userRole);
    }

    public void saveDefaultRoles(){
        saveRole("USER");
        saveRole("PROJ_OWNER");
        saveRole("PROJ_EDITOR");
        saveRole("PROJ_VIEWER");
    }

    public Role findRole(String roleName){
        return roleRepository.findByName(roleName);
    }
    public List<Role> findProjectRoles(){
        return roleRepository.findProjectRoles();
    }

    @Transactional
    @Override
    public User registerNewUserAccount(UserDto userDto)
//            throws UserAlreadyExistException
    {

        if (emailExists(userDto.getMail())) {
//            throw new UserAlreadyExistException("There is an account with that email address: " + userDto.getMail());
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setMail(userDto.getMail());
        user.setRoles(new HashSet<>(Arrays.asList(roleRepository.findByName("USER"))));
        Long projectId = projectService.createDefaultProject();
        Project project =projectService.findById(projectId);
        categoryService.createDefaultCategories(projectId);
        user.setCurrentProject(project);
        user.setProjects(Arrays.asList(project));
        return userRepository.save(user);
    }

    private boolean emailExists(String mail) {
        return userRepository.findByMail(mail) != null;
    }
}
