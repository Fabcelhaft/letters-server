package net.fabcelhaft.letters.server.init;

import net.fabcelhaft.letters.server.model.User;
import net.fabcelhaft.letters.server.security.ProtectedKeyPair;
import net.fabcelhaft.letters.server.security.UserCryptoGenerator;
import net.fabcelhaft.letters.server.security.UserPasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class LettersInitializer {

    private final UserCryptoGenerator userCryptoGenerator;
    private final UserPasswordGenerator userPasswordGenerator;

    @Autowired
    public LettersInitializer(UserCryptoGenerator userCryptoGenerator, UserPasswordGenerator userPasswordGenerator) {
        this.userCryptoGenerator = userCryptoGenerator;
        this.userPasswordGenerator = userPasswordGenerator;
    }

    @Async
    @Transactional
    public void initializeUsers() {
        User user = new User();
        String userPassword = userPasswordGenerator.getGeneratedPassword();

        user.setSalt(userPasswordGenerator.getGeneratedSalt());
        setCryptoToUser(user, userPassword);

    }

    private void setCryptoToUser(User user, String password) {
        ProtectedKeyPair protectedKeyPair = userCryptoGenerator.generateCrypto(password, user.getSalt());
        user.setPublicKey(protectedKeyPair.getPublicKey());
        user.setPrivateKey(protectedKeyPair.getPrivateKey());
    }

}
