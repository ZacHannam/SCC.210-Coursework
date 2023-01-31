package uk.pixtle.application.ui.window.notifications;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.notifications.NotificationManager;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchorLayout;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchoredComponent;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.Anchor;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

public class Notification extends JPanel {

    public enum ColourModes {

        INFO(Color.GRAY, Color.WHITE),
        ERROR(Color.RED, Color.WHITE);

        @Getter
        @Setter
        private Color background;

        @Getter
        @Setter
        private Color foreground;

        ColourModes(Color paramBackground, Color paramForeground) {
            this.setBackground(paramBackground);
            this.setForeground(paramForeground);
        }

    }

    public void removeNotification() {
        this.getNotificationManager().removeNotification(this);
    }

    public String capitalise(String text) {
        StringBuilder sb = new StringBuilder();
        boolean lastSpace = true;
        for(Character c : text.toCharArray()) {
            if(lastSpace) {
                sb.append(c.toString().toUpperCase());
                lastSpace = false;
            } else {
                sb.append(c.toString().toLowerCase());
            }

            if(c.equals(' ')){
                lastSpace = true;
            }
        }
        return sb.toString();
    }

    @Getter
    @Setter
    ColourModes colourMode;

    @Getter
    @Setter
    JTextArea textArea;

    @Getter
    @Setter
    JScrollPane scrollPane;

    @Getter
    @Setter
    JLabel title;

    @Getter
    @Setter
    JButton exitButton;

    @Getter
    @Setter
    JLabel time;

    @Getter
    @Setter
    NotificationManager notificationManager;

    private String zfill(String s, int n, char c) {
        if(s.length() >= n){
            return s;
        }
        StringBuilder a = new StringBuilder();
        for(int i = 0; i < n - s.length(); i++) {
            a.append(c);
        }
        a.append(s);

        return a.toString();
    }

    private String formatTime(LocalDateTime time) {
        StringBuilder sb = new StringBuilder();
        sb.append(zfill(String.valueOf(time.getDayOfMonth()), 2, '0') + "/" + zfill(String.valueOf(time.getMonth().getValue()), 2, '0') + "/" + time.getYear());
        sb.append(" " + time.getHour() + "h " + time.getMinute() + "m " + time.getSecond()+ "s");
        return sb.toString();
    }

    private void createTime(LocalDateTime time) {

        AnchoredComponent anchoredComponent = new AnchoredComponent();
        anchoredComponent.createAnchor(Anchor.DirectionType.X, -55);
        anchoredComponent.createAnchor(Anchor.DirectionType.X, -230);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, 10);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, 30);

        JLabel label = new JLabel(formatTime(time), SwingConstants.RIGHT);
        label.setForeground(this.getColourMode().getForeground());
        Font font = new Font("Consolas", Font.ITALIC, 12);
        label.setFont(font);

        super.add(label, anchoredComponent);
        this.setTime(label);

    }

    private void createExitButton() {
        AnchoredComponent anchoredComponent = new AnchoredComponent();
        anchoredComponent.createAnchor(Anchor.DirectionType.X, -30);
        anchoredComponent.createAnchor(Anchor.DirectionType.X, -50);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, 10);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, 30);

        JButton button = new JButton();
        button.setText("x");
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        button.setOpaque(true);
        button.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, this.getColourMode().getForeground(), this.getColourMode().getForeground().darker()));
        button.setBackground(this.getColourMode().getBackground());
        button.setForeground(this.getColourMode().getForeground());

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeNotification();
            }
        });

        super.add(button, anchoredComponent);
        this.setExitButton(button);
    }

    private void createTitle(String paramText) {

        AnchoredComponent anchoredComponent = new AnchoredComponent();
        anchoredComponent.createAnchor(Anchor.DirectionType.X, 30);
        anchoredComponent.createAnchor(Anchor.DirectionType.X, -235);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, 10);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, 30);

        JLabel label = new JLabel();
        label.setForeground(this.getColourMode().getForeground());
        Font font = new Font("Consolas", Font.BOLD, 18);
        label.setFont(font);
        label.setText(paramText);

        super.add(label, anchoredComponent);
        this.setTitle(label);

    }



    private void createScrollPane() {
        AnchoredComponent anchoredComponent = new AnchoredComponent();
        anchoredComponent.createAnchor(Anchor.DirectionType.X, 30);
        anchoredComponent.createAnchor(Anchor.DirectionType.X, -30);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, 40);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, -20);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBackground(this.getColourMode().getBackground());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setViewportView(this.getTextArea());

        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {

            private JButton createZeroButton() {
                JButton jbutton = new JButton();
                jbutton.setPreferredSize(new Dimension(0, 0));
                jbutton.setMinimumSize(new Dimension(0, 0));
                jbutton.setMaximumSize(new Dimension(0, 0));
                return jbutton;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected void configureScrollBarColors(){
                this.trackColor = getColourMode().getBackground();
                this.thumbColor = getColourMode().getForeground();
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {

                Rectangle thumb = thumbRect;
                int height = thumb.height;


                Graphics2D g2d = (Graphics2D) g.create();
                Shape thumbShape = createThumbShape(10, height - 1);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.translate(thumb.x, thumb.y);

                g2d.setColor(new Color(210, 210, 210));
                g2d.fill(thumbShape);
                g2d.draw(thumbShape);
                g2d.dispose();
            }

            private Shape createThumbShape(int width, int height) {
                RoundRectangle2D shape = new RoundRectangle2D.Double(0, 0, width, height, 10, 10);
                return shape;
            }
        });

        scrollPane.setOpaque(true);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());


        super.add(scrollPane, anchoredComponent);
        this.setScrollPane(scrollPane);
    }


    private void createTextArea(String paramText) {
        AnchoredComponent anchoredComponent = new AnchoredComponent();
        anchoredComponent.createAnchor(Anchor.DirectionType.X, 30);
        anchoredComponent.createAnchor(Anchor.DirectionType.X, -55);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, 40);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, -20);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(this.getColourMode().getBackground());
        textArea.setForeground(this.getColourMode().getForeground());

        Font font = new Font("Consolas", Font.PLAIN, 16);
        textArea.setFont(font);
        textArea.setToolTipText(paramText);
        textArea.setText(paramText);
        textArea.setLineWrap(true);
        super.add(textArea, anchoredComponent);
        this.setTextArea(textArea);
        this.createScrollPane();
    }

    @Getter
    @Setter
    JLabel selfDeleteTime;

    public String convertSecondsToString(int paramSeconds) {
        StringBuilder sb = new StringBuilder();

        int time = paramSeconds;
        boolean cont = false;

        if(cont || (double) time / (60 * 60 * 24) > 1) {
            int days = (int) Math.floor((double) time / (60 * 60 * 24));
            sb.append(days + "d ");
            time -= days * 60 * 60 * 24;
            cont = true;
        }

        if(cont || (double) time / (60 * 60) > 1) {
            int hours = (int) Math.floor((double) time / (60 * 60));
            sb.append(hours + "h ");
            time -= hours * 60 * 60;
            cont = true;
        }

        if(cont || (double) time / (60) > 1) {
            int minutes = (int) Math.floor((double) time / (60));
            sb.append(minutes + "m ");
            time -= minutes * 60;
        }

        sb.append(time + "s");

        return sb.toString();
    }

    @Getter
    @Setter
    JLabel timeLeftLabel;

    public void setSelfDelete(int paramTimeSeconds) {

        if(paramTimeSeconds <= 0) {
            return;
        }

        AnchoredComponent anchoredComponent = new AnchoredComponent();
        anchoredComponent.createAnchor(Anchor.DirectionType.X, -30);
        anchoredComponent.createAnchor(Anchor.DirectionType.X, -300);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, -5);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, -18);

        JLabel label = new JLabel(convertSecondsToString(paramTimeSeconds), SwingConstants.RIGHT);

        label.setForeground(this.getColourMode().getForeground());
        Font font = new Font("Consolas", Font.PLAIN, 13);
        label.setFont(font);

        super.add(label, anchoredComponent);
        this.setTimeLeftLabel(label);

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {

            int timeLeft = paramTimeSeconds;

            @Override
            public void run() {

                timeLeft--;

                if(timeLeft <= 0) {
                    timer.cancel();
                    removeNotification();
                    return;
                }

                label.setText(convertSecondsToString(timeLeft));

            }
        };

        timer.schedule(task, 1000, 1000);
    }

    @Getter
    @Setter
    public String rawTitle;

    @Getter
    @Setter
    public String rawText;

    public Notification(NotificationManager paramNotificationManager, ColourModes paramColourMode, String paramTitle, String paramText, int paramSelfDeleteTime, LocalDateTime paramTime) {
        this.setNotificationManager(paramNotificationManager);
        this.setColourMode(paramColourMode);

        super.setBackground(this.getColourMode().getBackground());
        super.setLayout(new AnchorLayout());

        //this.createScrollBar();

        this.createTitle(capitalise(paramTitle));
        this.createTime(paramTime);
        this.createTextArea(paramText);
        this.createExitButton();
        this.setSelfDelete(paramSelfDeleteTime);

        this.setRawTitle(paramTitle);
        this.setRawText(paramText);
    }

    public Notification(NotificationManager paramNotificationManager, ColourModes paramColourMode, String paramTitle, String paramText, int paramSelfDeleteTime) {
        this(paramNotificationManager, paramColourMode, paramTitle, paramText, paramSelfDeleteTime, LocalDateTime.now());
    }
}
