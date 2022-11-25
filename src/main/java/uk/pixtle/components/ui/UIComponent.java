package uk.pixtle.components.ui;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.components.Component;
import uk.pixtle.components.ui.elements.Window;

public class UIComponent implements Component {

    @Getter
    @Setter
    public Window window;

    public UIComponent() {
        this.setWindow(new Window());
    }
}
