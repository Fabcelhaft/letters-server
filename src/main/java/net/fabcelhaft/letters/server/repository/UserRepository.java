package net.fabcelhaft.letters.server.repository;

import net.fabcelhaft.letters.server.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface UserRepository extends CrudRepository<User, String> {

    Set<User> findAll();

    User findUserByMail(String mail);
}
