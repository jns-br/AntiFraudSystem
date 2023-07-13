package antifraud.Repository;

import antifraud.Model.User.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findUserByUsername(String username);

    List<User> findAllByOrderByIdAsc();

    long deleteUserByUsername(String username);
}
