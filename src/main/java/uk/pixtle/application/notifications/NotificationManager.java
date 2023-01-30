package uk.pixtle.application.notifications;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.Application;
import uk.pixtle.application.ApplicationComponent;
import uk.pixtle.application.ui.window.notifications.Notification;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class NotificationManager extends ApplicationComponent {

    @Getter
    @Setter
    ArrayList<Notification> notifications;


    public void displayNotification(Notification.ColourModes paramColourMode, String paramNotificationTitle, String paramNotificationText) {
        Notification notification = new Notification(this, paramColourMode, paramNotificationTitle, paramNotificationText, -1, LocalDateTime.now());
        displayNotification(notification);
    }

    public void displayNotification(Notification.ColourModes paramColourMode, String paramNotificationTitle, String paramNotificationText, int paramSelfDeleteTime) {
        Notification notification = new Notification(this, paramColourMode, paramNotificationTitle, paramNotificationText, paramSelfDeleteTime, LocalDateTime.now());
        displayNotification(notification);
    }

    private void displayNotification(Notification paramNotification) {
        this.getNotifications().add(paramNotification);
        super.getApplication().getUIManager().getWindow().getNotifications().paintNotifications();
    }

    public void removeNotification(Notification paramNotification) {
        if(this.getNotifications().contains(paramNotification)) {
            this.getNotifications().remove(paramNotification);
            this.getApplication().getUIManager().getWindow().getNotifications().removeNotification(paramNotification);

        }
    }

    public NotificationManager(Application paramApplication) {
        super(paramApplication);
        this.setNotifications(new ArrayList<>());
    }
}
