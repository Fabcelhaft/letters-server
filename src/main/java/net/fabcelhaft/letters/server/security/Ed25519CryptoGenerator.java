package net.fabcelhaft.letters.server.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

@Service
@Slf4j
public class Ed25519CryptoGenerator implements UserCryptoGenerator{

    @Override
    public ProtectedKeyPair generateCrypto(String password, String salt) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("Ed25519");
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            return new ProtectedKeyPair(keyPair, password, salt);
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            log.error("Error generating KeyPair. User can't be created!", noSuchAlgorithmException);
            throw new IllegalStateException("Error generating KeyPair. User can't be created!");
        }

    }

}
