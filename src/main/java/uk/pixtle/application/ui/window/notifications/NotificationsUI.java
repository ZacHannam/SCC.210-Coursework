package uk.pixtle.application.ui.window.notifications;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.Application;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchorLayout;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchoredComponent;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.Anchor;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.ValueAnchor;

import javax.swing.*;

public class NotificationsUI extends JPanel implements Notifications {

    private static final int MAX_HEIGHT = 700;

    @Getter
    @Setter
    Application application;

    @Getter
    @Setter
    Anchor dynamicHeightAdjustableAnchor;

    @Override
    public AnchoredComponent getAnchors() {

        AnchoredComponent anchoredComponent = new AnchoredComponent();

        anchoredComponent.createAnchor(Anchor.DirectionType.X, 200);
        anchoredComponent.createAnchor(Anchor.DirectionType.X, -300);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, -50);

        this.setDynamicHeightAdjustableAnchor(anchoredComponent.createAnchor(Anchor.DirectionType.Y, -50));

        return anchoredComponent;
    }

    public void removeNotification(Notification paramNotification) {
        super.remove(paramNotification);
        this.paintNotifications();
    }

    public void paintNotifications() {

        int newHeight = Math.min(MAX_HEIGHT, (160 * this.getApplication().getNotificationManager().getNotifications().size()) + 50);
        ((ValueAnchor) this.getDynamicHeightAdjustableAnchor()).setValue(-newHeight);

        for(int index = 0; index < this.getApplication().getNotificationManager().getNotifications().size(); index++) {

            Notification notification = this.getApplication().getNotificationManager().getNotifications().get(this.getApplication().getNotificationManager().getNotifications().size() - index - 1);

            AnchoredComponent anchoredComponent = new AnchoredComponent();
            anchoredComponent.createAnchor(Anchor.DirectionType.X, 10);
            anchoredComponent.createAnchor(Anchor.DirectionType.X, -10);
            anchoredComponent.createAnchor(Anchor.DirectionType.Y, -10 - (150 * index) - (index * 10));
            anchoredComponent.createAnchor(Anchor.DirectionType.Y, -10 - (150 * (index + 1)) - (index * 10));
            super.add(notification, anchoredComponent);

        }

        super.repaint();
        super.revalidate();
    }

    public NotificationsUI(Application paramApplication) {
        this.setApplication(paramApplication);

        super.setOpaque(false);
        super.setLayout(new AnchorLayout());
    }
}
