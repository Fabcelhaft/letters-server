package net.fabcelhaft.letters.server.repository;

import net.fabcelhaft.letters.server.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

}
