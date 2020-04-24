package info.podkowinski.sandra.financescanner.user;

import info.podkowinski.sandra.financescanner.user.validation.PasswordMatches;
import info.podkowinski.sandra.financescanner.user.validation.ValidEmail;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@PasswordMatches
public class UserDto {
    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    private String password;
    private String matchingPassword;

    @ValidEmail
    @NotNull
    @NotEmpty
    private String mail;

    // standard getters and setters
}