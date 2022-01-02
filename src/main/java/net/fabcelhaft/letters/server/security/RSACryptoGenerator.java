package net.fabcelhaft.letters.server.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Service
public class RSACryptoGenerator implements UserCryptoGenerator {

    @Override
    public ProtectedKeyPair generateCrypto(String password, String salt) {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(4096);
            KeyPair keyPair = generator.generateKeyPair();
            return new ProtectedKeyPair(keyPair, password, salt);
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            log.error("Error generating KeyPair. User can't be created!", noSuchAlgorithmException);
            throw new IllegalStateException("Error generating KeyPair. User can't be created!");
        }
    }

}
