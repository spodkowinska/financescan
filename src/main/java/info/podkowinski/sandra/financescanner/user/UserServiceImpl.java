package info.podkowinski.sandra.financescanner.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // TODO get rid of this method, it's a big hack
    @Override
    public User createDefaultUserHack() {
        User user = new User();
        user.setUsername("qwe");
        user.setPassword(passwordEncoder.encode("qwe"));
        user.setEnabled(1);

        Role userRole = roleRepository.findByName("USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));

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
}
