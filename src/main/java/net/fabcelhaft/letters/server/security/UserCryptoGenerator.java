package net.fabcelhaft.letters.server.security;

public interface UserCryptoGenerator {

    ProtectedKeyPair generateCrypto(String password, String salt);

}
