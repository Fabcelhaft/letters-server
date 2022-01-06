package net.fabcelhaft.letters.server.rest;

import lombok.extern.slf4j.Slf4j;
import net.fabcelhaft.letters.server.init.LettersInitializer;
import net.fabcelhaft.letters.server.model.Message;
import net.fabcelhaft.letters.server.model.User;
import net.fabcelhaft.letters.server.repository.MessageRepository;
import net.fabcelhaft.letters.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public void createMessage(@RequestBody MessageCreationDataDto messageCreationDataDto){
        User userByMail = userRepository.findUserByMail(messageCreationDataDto.getMail());
        if(userByMail==null){
            throw new RuntimeException("user with mail " + messageCreationDataDto.getMail() + " not existent");
        }
        Message message = new Message();
        message.setUser(userByMail);
        message.setEncryptedContent(messageCreationDataDto.getEncryptedMessage());
        messageRepository.save(message);
    }

    @PostMapping("/reinit")
    public ResponseEntity<Void> RerunInit(){
        lettersInitializer.initializeUsers();
        return new ResponseEntity<>(HttpStatus.OK);
    }


    public void getMessagesForUser(){
        //TODO Aitareko
    }

    @GetMapping("/publicKey/{mail}")
    public KeyDto getPublicKey(@PathVariable String mail){
        User userByMail = userRepository.findUserByMail(mail);
        if (userByMail==null){
            return null;
        }
        String publicKey = userByMail.getPublicKey();
        return new KeyDto(publicKey);
    }

    @DeleteMapping("/panic")
    public void panic(){
        log.error("Panic-function triggerd. Killing service.");
        System.exit(0);
    }

    @GetMapping("users")
    public ResponseEntity<List<UserDto>> getUsers() {
        List<UserDto> userDtos = userRepository.findAll().stream()
                .map(this::getUserDtoFromUserEntity)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    private UserDto getUserDtoFromUserEntity(User userEntity) {
        UserDto userDto = new UserDto();
        userDto.setDisplayName(userEntity.getDisplayname());
        userDto.setMail(userEntity.getMail());
        return userDto;
    }

}


