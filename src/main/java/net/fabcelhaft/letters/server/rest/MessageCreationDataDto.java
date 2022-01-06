package net.fabcelhaft.letters.server.rest;

import lombok.Data;

@Data
public class MessageCreationDataDto {

    private String encryptedMessage;
    private String mail;

}
