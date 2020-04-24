package info.podkowinski.sandra.financescanner.user;

public interface UserService {

    User findByUsername(String name);

    void saveUser(User user);

    User createDefaultUserHack();

    User registerNewUserAccount(UserDto userDto) ;
//            throws UserAlreadyExistException;

}
