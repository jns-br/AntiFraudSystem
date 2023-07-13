package antifraud.DTO;

import lombok.Data;

@Data
public class DeleteUserDTO {
    final String username;
    final String status = "Deleted successfully!";
}
