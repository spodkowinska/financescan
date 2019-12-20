package info.podkowinski.sandra.financescanner.user;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(User user){
        userRepository.save(user);
    }

    public User findById(Long id){ return userRepository.findById(id).orElse(null);}
}
