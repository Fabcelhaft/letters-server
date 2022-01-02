package net.fabcelhaft.letters.server.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Message {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    private String encryptedContent;
}
