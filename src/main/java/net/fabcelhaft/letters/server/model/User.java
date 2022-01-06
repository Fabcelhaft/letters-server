package net.fabcelhaft.letters.server.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class User {

    @Id
    private String mail;

    private String displayname; //for selection, when writing a "letter"

    @Column(length = 4000)
    private String publicKey;

    @Column(length = 4000)
    private String privateKey;

    private String salt;
}
