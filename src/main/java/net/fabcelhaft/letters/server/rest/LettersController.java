package net.fabcelhaft.letters.server.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.fabcelhaft.letters.server.init.LettersInitializer;
import net.fabcelhaft.letters.server.model.Message;
import net.fabcelhaft.letters.server.model.User;
import net.fabcelhaft.letters.server.repository.MessageRepository;
import net.fabcelhaft.letters.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class LettersController {

    private final MessageRepository messageRepository;

    private final UserRepository userRepository;

    private final LettersInitializer lettersInitializer;

    @Autowired
    public LettersController(MessageRepository messageRepository, UserRepository userRepository, LettersInitializer lettersInitializer) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.lettersInitializer = lettersInitializer;
    }

    @PutMapping("/message")
    public void createMessage(@RequestBody MessageCreationData messageCreationData){
        User userByMail = userRepository.findUserByMail(messageCreationData.getMail());
        if(userByMail==null){
            throw new RuntimeException("user with mail " + messageCreationData.getMail() + " not existent");
        }
        Message message = new Message();
        message.setUser(userByMail);
        message.setEncryptedContent(messageCreationData.getEncryptedMessage());
        messageRepository.save(message);
    }

    @PostMapping("/reinit")
    public void RerunInit(){
        lettersInitializer.initializeUsers();
    }


    public void getMessagesForUser(){
        //TODO Aitareko
    }

    @GetMapping("/publicKey/{mail}")
    public Key getPublicKey(@PathVariable String mail){
        User userByMail = userRepository.findUserByMail(mail);
        if (userByMail==null){
            return null;
        }
        String publicKey = userByMail.getPublicKey();
        return new Key(publicKey);
    }

    @DeleteMapping("/panic")
    public void panic(){
        log.error("Panic-function triggerd. Killing service.");
        System.exit(0);
    }

    @Data
    private class MessageCreationData{
        private String encryptedMessage;
        private String mail;
    }

    @Data
    @AllArgsConstructor
    private class Key{
        private String publicKey;
    }
}


