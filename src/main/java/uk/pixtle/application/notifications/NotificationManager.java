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

    @Getter
    @Setter
    ArrayList<String> noRepeats;

    public String combineData(String paramNotificationTitle, String paramNotificationText) {
        return paramNotificationText + ":" + paramNotificationText;
     }

    public void displayNotification(Notification.ColourModes paramColourMode, String paramNotificationTitle, String paramNotificationText, boolean allowRepeats) {
        this.displayNotification(paramColourMode, paramNotificationTitle, paramNotificationText, -1, LocalDateTime.now(), true);
    }

    public void displayNotification(Notification.ColourModes paramColourMode, String paramNotificationTitle, String paramNotificationText, int paramSelfDeleteTime) {
        this.displayNotification(paramColourMode, paramNotificationTitle, paramNotificationText, paramSelfDeleteTime, LocalDateTime.now(), true);
    }

    public void displayNotification(Notification.ColourModes paramColourMode, String paramNotificationTitle, String paramNotificationText, int paramSelfDeleteTime, boolean allowRepeats) {
        this.displayNotification(paramColourMode, paramNotificationTitle, paramNotificationText, paramSelfDeleteTime, LocalDateTime.now(), allowRepeats);
    }

    public void displayNotification(Notification.ColourModes paramColourMode, String paramNotificationTitle, String paramNotificationText) {
        this.displayNotification(paramColourMode, paramNotificationTitle, paramNotificationText, -1, LocalDateTime.now(), true);
    }

    public void displayNotification(Notification.ColourModes paramColourMode, String paramNotificationTitle, String paramNotificationText, int paramSelfDeleteTime, LocalDateTime paramTime, boolean paramAllowRepeats) {

        if(!paramAllowRepeats && this.getNoRepeats().contains(combineData(paramNotificationTitle, paramNotificationText))) {
            return;
        } else if(!paramAllowRepeats) {
            this.getNoRepeats().add(combineData(paramNotificationTitle, paramNotificationText));
        }

        Notification notification = new Notification(this, paramColourMode, paramNotificationTitle, paramNotificationText, paramSelfDeleteTime, paramTime);
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

        if(this.getNoRepeats().contains(combineData(paramNotification.getRawTitle(), paramNotification.getRawText()))) {
            this.getNoRepeats().remove(combineData(paramNotification.getRawTitle(), paramNotification.getRawText()));
        }
    }

    public NotificationManager(Application paramApplication) {
        super(paramApplication);
        this.setNotifications(new ArrayList<>());
        this.setNoRepeats(new ArrayList<>());
    }
}
