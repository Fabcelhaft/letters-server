package net.fabcelhaft.letters.server.init;

public interface UserNotificationSender {

    void sendNotificationToUser(String displayname, String mail, String password);

}
