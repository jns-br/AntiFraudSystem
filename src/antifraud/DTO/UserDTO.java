package antifraud.DTO;

import antifraud.Model.User.User;

public record UserDTO(long id, String name, String username, String role) {
    public static UserDTO map(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getUsername(), user.getRole()
            .name());
    }
}
