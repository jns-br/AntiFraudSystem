package antifraud.DTO;

import antifraud.Model.User.LockStatus;
import lombok.Data;

@Data
public class LockDTO {
    private final String status;

    public LockDTO(String username, LockStatus lockStatus) {
        this.status = "User " + username + " " + lockStatus.name()
            .toLowerCase() + "!";
    }
}
