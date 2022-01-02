package net.fabcelhaft.letters.server.repository;

import net.fabcelhaft.letters.server.model.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message,Long> {
}
