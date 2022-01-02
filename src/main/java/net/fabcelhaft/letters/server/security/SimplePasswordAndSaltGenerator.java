package net.fabcelhaft.letters.server.security;

import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;

@Service
public class SimplePasswordAndSaltGenerator implements UserPasswordGenerator {

    @Override
    public String getGeneratedPassword() {
        return RandomString.make(12);
    }

    @Override
    public String getGeneratedSalt() {
        return RandomString.make(8);
    }

}
