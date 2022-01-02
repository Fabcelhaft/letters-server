package net.fabcelhaft.letters.server.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "letters")
@Getter
@Setter
public class LettersConfig {
    private String usersCsvPath="/users";

    private String mailgreeting = "Hello";

    private String mailtext = "You receive this mail, because your account was added to a letters instance.\n" +
            "\n" +
            "You can now provide anonymous nice words to your colleagues and brigthen their day:";

    private String url="http://localhost:8080";

    private String mailfrom="letters@example.com";
    private String mailsubject="Letters account created";

    private String mailserver="example.com";

    private Integer mailport=25;
}
