package antifraud.Model.User.Requests;

import antifraud.Model.User.LockOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLockRequest extends UserBaseRequest {
    private LockOperation operation;
}
