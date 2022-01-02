package net.fabcelhaft.letters.server.security;

public interface UserPasswordGenerator {

    String getGeneratedPassword();
    String getGeneratedSalt();

}
