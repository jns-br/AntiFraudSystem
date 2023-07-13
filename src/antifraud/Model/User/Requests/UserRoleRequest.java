package antifraud.Model.User.Requests;

import antifraud.Auth.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleRequest extends UserBaseRequest {
    private Role role;
}
