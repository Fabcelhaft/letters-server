package net.fabcelhaft.letters.server.init;

import lombok.extern.slf4j.Slf4j;
import net.fabcelhaft.letters.server.config.LettersConfig;
import net.fabcelhaft.letters.server.model.User;
import net.fabcelhaft.letters.server.repository.UserRepository;
import net.fabcelhaft.letters.server.security.ProtectedKeyPair;
import net.fabcelhaft.letters.server.security.UserCryptoGenerator;
import net.fabcelhaft.letters.server.security.UserPasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class LettersInitializer implements ApplicationListener<ApplicationStartedEvent> {

    private final UserCryptoGenerator userCryptoGenerator;
    private final UserPasswordGenerator userPasswordGenerator;
    private final UserRepository userRepository;
    private final LettersConfig lettersConfig;

    @Autowired
    public LettersInitializer(UserCryptoGenerator userCryptoGenerator, UserPasswordGenerator userPasswordGenerator,
                              UserRepository userRepository, LettersConfig lettersConfig) {
        this.userCryptoGenerator = userCryptoGenerator;
        this.userPasswordGenerator = userPasswordGenerator;
        this.userRepository = userRepository;
        this.lettersConfig = lettersConfig;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        initializeUsers();
    }

    @Async
    @Transactional
    public void initializeUsers() {
        Set<User> usersFromCSV = getUsersFromCSV();
        for(User user : usersFromCSV) {
            String userPassword = userPasswordGenerator.getGeneratedPassword();
            user.setSalt(userPasswordGenerator.getGeneratedSalt());
            setCryptoToUser(user, userPassword);
            userRepository.save(user);
        }
    }

    private void setCryptoToUser(User user, String password) {
        ProtectedKeyPair protectedKeyPair = userCryptoGenerator.generateCrypto(password, user.getSalt());
        user.setPublicKey(protectedKeyPair.getPublicKey());
        user.setPrivateKey(protectedKeyPair.getPrivateKey());
    }

    private Set<User> getUsersFromCSV() {
        Set<User> usersFromCSV = new HashSet<>();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(lettersConfig.getUsersCsvPath()))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                User userFromCSV = createUserFromCsvLine(line);
                if(userFromCSV != null) {
                    usersFromCSV.add(userFromCSV);
                }
            }
        } catch (IOException ioException) {
            log.error("Error reading csv!", ioException);
        }
        return usersFromCSV;
    }

    private User createUserFromCsvLine(String csvLine) {
        String[] values = csvLine.split(";");
        if(values.length == 2) {
            User user = new User();
            user.setDisplayname(values[0]);
            user.setMail(values[1]);
            return user;
        }
        log.warn("Invalid line in csv! Line: " + csvLine);
        return null;
    }

}
