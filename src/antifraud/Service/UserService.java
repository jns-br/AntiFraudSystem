package antifraud.Service;

import antifraud.Auth.Role;
import antifraud.DTO.DeleteUserDTO;
import antifraud.DTO.LockDTO;
import antifraud.DTO.UserDTO;
import antifraud.Exceptions.ElementExistsException;
import antifraud.Exceptions.ElementNotFoundException;
import antifraud.Exceptions.User.UserRoleException;
import antifraud.Model.User.LockOperation;
import antifraud.Model.User.LockStatus;
import antifraud.Model.User.User;
import antifraud.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Transactional
    public UserDTO saveUser(User user) throws ElementExistsException {
        Optional<User> savedUser = Optional.ofNullable(userRepository.findUserByUsername(user.getUsername()));
        savedUser.ifPresent(u -> {
            throw new ElementExistsException(user.getUsername() + " already exists");
        });

        user.setPassword(encoder.encode(user.getPassword()));
        if (isUserTableEmpty()) {
            user.setRole(Role.ADMINISTRATOR);
            user.setLockStatus(LockStatus.UNLOCKED);
        } else {
            user.setRole(Role.MERCHANT);
            user.setLockStatus(LockStatus.LOCKED);
        }
        return UserDTO.map(userRepository.save(user));
    }

    @Transactional
    public UserDTO updateUserRole(String username, Role role) throws UserRoleException {
        if (!role.equals(Role.SUPPORT) && !role.equals(Role.MERCHANT)) {
            throw new UserRoleException(role + " is not applicable", HttpStatus.BAD_REQUEST);
        }
        Optional<User> savedUser = Optional.ofNullable(userRepository.findUserByUsername(username));
        User user = savedUser.orElseThrow(() -> new ElementNotFoundException(username));
        if (user.getRole()
            .equals(role)) {
            String msg = "User " + username + " already has role " + role;
            throw new UserRoleException(msg, HttpStatus.CONFLICT);
        }
        user.setRole(role);
        return UserDTO.map(userRepository.save(user));
    }

    @Transactional
    public LockDTO updateLockStatus(String username, LockOperation lockOperation) throws ElementNotFoundException, UserRoleException {
        Optional<User> savedUser = Optional.ofNullable(userRepository.findUserByUsername(username));
        User user = savedUser.orElseThrow(() -> new ElementNotFoundException(username));
        if (user.getRole()
            .equals(Role.ADMINISTRATOR) && lockOperation == LockOperation.LOCK) {
            throw new UserRoleException("Admins can't be blocked", HttpStatus.BAD_REQUEST);
        }
        user.setLockStatus(lockOperation == LockOperation.LOCK ? LockStatus.LOCKED : LockStatus.UNLOCKED);
        userRepository.save(user);
        return new LockDTO(user.getUsername(), user.getLockStatus());
    }


    @Transactional
    public DeleteUserDTO deleteUser(String username) throws ElementNotFoundException {
        long count = userRepository.deleteUserByUsername(username);
        if (count < 1) {
            throw new ElementNotFoundException(username);
        }
        return new DeleteUserDTO(username);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAllByOrderByIdAsc()
            .stream()
            .map(UserDTO::map)
            .toList();
    }

    private boolean isUserTableEmpty() {
        return userRepository.findAllByOrderByIdAsc()
            .isEmpty();
    }

}
