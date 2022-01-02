package net.fabcelhaft.letters.server.init;

public interface UserNotificationSender {

    void sendNotificationToUser(String mail, String password);

}
