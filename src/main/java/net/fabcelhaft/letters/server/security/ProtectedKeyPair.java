package net.fabcelhaft.letters.server.security;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Slf4j
@Data
public class ProtectedKeyPair {

    private String publicKey;
    private String privateKey;

    public ProtectedKeyPair(KeyPair keyPairToProtect, String password, String salt) {
        publicKey = encodeToBase64(keyPairToProtect.getPublic().getEncoded());
        privateKey = encryptPrivateKey(keyPairToProtect.getPrivate(), password, salt);
    }

    private String encryptPrivateKey(PrivateKey privateKey, String password, String salt) {
        SecretKey secretKey = getSecretKeyByPassword(password, salt);
        var encodedPrivateKey = privateKey.getEncoded();

        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] cipherText = cipher.doFinal(encodedPrivateKey);
            return encodeToBase64(cipherText);
        } catch(NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException
                | IllegalBlockSizeException | BadPaddingException privateKeyEncryptionException) {
            log.error("Error encrypting private key from user. User can't be created!", privateKeyEncryptionException);
            throw new IllegalStateException("Error encrypting private key from user. User can't be created!");
        }
    }

    private SecretKey getSecretKeyByPassword(String password, String salt) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
            return new SecretKeySpec(factory.generateSecret(spec)
                    .getEncoded(), "AES");
        } catch(NoSuchAlgorithmException | InvalidKeySpecException secretKeyCreationException) {
            log.error("Error generating SecretKey. User can't be created!", secretKeyCreationException);
            throw new IllegalStateException("Error generating SecretKey. User can't be created!");
        }
    }

    private String encodeToBase64(byte[] toEncode) {
        return Base64.getEncoder()
                .encodeToString(toEncode);
    }

}
