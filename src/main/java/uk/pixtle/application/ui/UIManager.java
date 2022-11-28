package uk.pixtle.application.ui;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.Application;
import uk.pixtle.application.ApplicationComponent;
import uk.pixtle.application.ui.window.Window;
import uk.pixtle.application.ui.window.menubar.MenuBar;

public class UIManager extends ApplicationComponent {

    @Getter
    @Setter
    public Window window;

    public MenuBar getMenuBar(){
        return this.getWindow().getMenuBarElement();
    }

    public UIManager(Application paramApplication) {
        super(paramApplication);

        this.setWindow(new Window());
    }
}
