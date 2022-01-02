package net.fabcelhaft.letters.server.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String displayname; //for selection, when writing a "letter"

    private String mail;

    private String publicKey;

    private String privateKey;

    private String salt;
}
