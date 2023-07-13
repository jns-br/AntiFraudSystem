package antifraud.Controller;

import antifraud.DTO.DeleteUserDTO;
import antifraud.DTO.LockDTO;
import antifraud.DTO.UserDTO;
import antifraud.Model.ApiRoutes;
import antifraud.Model.User.Requests.UserLockRequest;
import antifraud.Model.User.Requests.UserRoleRequest;
import antifraud.Model.User.User;
import antifraud.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(ApiRoutes.USER)
    public ResponseEntity<UserDTO> register(@Valid @RequestBody User user) {
        UserDTO savedUser = userService.saveUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping(ApiRoutes.USER_LIST)
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping({ApiRoutes.USERNAME, ApiRoutes.USERS})
    public ResponseEntity<DeleteUserDTO> deleteUser(@PathVariable(value = "username", required = false) String name) {
        DeleteUserDTO body = userService.deleteUser(name);
        return ResponseEntity.ok(body);
    }

    @PutMapping(ApiRoutes.USER_ROLE)
    public ResponseEntity<UserDTO> updateRole(@Valid @RequestBody UserRoleRequest request) {
        UserDTO updatedUser = userService.updateUserRole(request.getUsername(), request.getRole());
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping(value = ApiRoutes.USER_ACCESS, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LockDTO> updateAccess(@Valid @RequestBody UserLockRequest request) {
        LockDTO body = userService.updateLockStatus(request.getUsername(), request.getOperation());
        return ResponseEntity.ok(body);
    }
}
