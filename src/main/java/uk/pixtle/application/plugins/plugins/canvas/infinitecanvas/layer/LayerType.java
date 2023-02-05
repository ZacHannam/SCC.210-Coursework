package uk.pixtle.application.plugins.plugins.canvas.infinitecanvas.layer;

import lombok.Getter;
import lombok.Setter;

public enum LayerType {
    DRAWING("Drawing");

    @Getter
    @Setter
    String description;

    LayerType(String paramDescription) {
        this.setDescription(paramDescription);
    }
}
