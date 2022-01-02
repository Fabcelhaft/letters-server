package net.fabcelhaft.letters.server.init;

import net.fabcelhaft.letters.server.security.UserCryptoGenerator;
import net.fabcelhaft.letters.server.security.UserPasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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
    public void initializeUsers() {

    }

}
