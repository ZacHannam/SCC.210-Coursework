package uk.pixtle.application.ui.layouts.anchorlayout;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.Anchor;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.ValueAnchor;
import uk.pixtle.application.ui.window.toollist.ToolButton;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class AnchorLayout implements LayoutManager2 {

    private static final int SCROLLBAR_WIDTH = 17;

    @Getter
    private HashMap<Component, AnchoredComponent> anchoredComponents = new HashMap<>();

    @Getter
    @Setter
    public boolean widthControl;

    @Getter
    @Setter
    public boolean heightControl;


    /**
     * Adds the specified component to the layout, using the specified
     * constraint object.
     *
     * @param comp        the component to be added
     * @param constraints where/how the component is added to the layout.
     */
    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        if(!(constraints instanceof AnchoredComponent)) {
            // TO-DO error
            return;
        }

        AnchoredComponent anchoredComponent = (AnchoredComponent) constraints;

        this.getAnchoredComponents().put(comp, anchoredComponent);
    }

    /**
     * Calculates the maximum size dimensions for the specified container,
     * given the components it contains.
     *
     * @param target the target container
     * @return the maximum size of the container
     * @see Component#getMaximumSize
     * @see LayoutManager
     */
    @Override
    public Dimension maximumLayoutSize(Container target) {
        return null;
    }

    /**
     * Returns the alignment along the x axis.  This specifies how
     * the component would like to be aligned relative to other
     * components.  The value should be a number between 0 and 1
     * where 0 represents alignment along the origin, 1 is aligned
     * the furthest away from the origin, 0.5 is centered, etc.
     *
     * @param target the target container
     * @return the x-axis alignment preference
     */
    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    /**
     * Returns the alignment along the y axis.  This specifies how
     * the component would like to be aligned relative to other
     * components.  The value should be a number between 0 and 1
     * where 0 represents alignment along the origin, 1 is aligned
     * the furthest away from the origin, 0.5 is centered, etc.
     *
     * @param target the target container
     * @return the y-axis alignment preference
     */
    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    /**
     * Invalidates the layout, indicating that if the layout manager
     * has cached information it should be discarded.
     *
     * @param target the target container
     */
    @Override
    public void invalidateLayout(Container target) {

    }

    /**
     * If the layout manager uses a per-component string,
     * adds the component {@code comp} to the layout,
     * associating it
     * with the string specified by {@code name}.
     *
     * @param name the string to be associated with the component
     * @param comp the component to be added
     */
    @Override
    public void addLayoutComponent(String name, Component comp) {

    }

    /**
     * Removes the specified component from the layout.
     *
     * @param comp the component to be removed
     */
    @Override
    public void removeLayoutComponent(Component comp) {

    }

    /**
     * Calculates the preferred size dimensions for the specified
     * container, given the components it contains.
     *
     * @param parent the container to be laid out
     * @return the preferred dimension for the container
     * @see #minimumLayoutSize
     */
    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return null;
    }

    /**
     * Calculates the minimum size dimensions for the specified
     * container, given the components it contains.
     *
     * @param parent the component to be laid out
     * @return the minimum dimension for the container
     * @see #preferredLayoutSize
     */
    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return null;
    }

    /**
     * Lays out the specified container.
     *
     * @param parent the container to be laid out
     */
    @Override
    public void layoutContainer(Container parent) {

        synchronized (parent.getTreeLock()) {

            Insets insets = parent.getInsets();
            int maxWidth = parent.getWidth() - (insets.left + insets.right);
            int maxHeight = parent.getHeight() - (insets.top + insets.bottom);

            if((parent instanceof JPanel) && ((JPanel) parent).getParent().getParent() instanceof JScrollPane){

                JScrollPane pane = (JScrollPane) ((JPanel) parent).getParent().getParent();
                // TO-DO potential bug with placement of scrollbar

                if (pane.getVerticalScrollBar().isVisible()) {
                    maxWidth -= SCROLLBAR_WIDTH;
                }

                if (pane.getHorizontalScrollBar().isVisible()) {
                    maxHeight -= SCROLLBAR_WIDTH;
                }
            }

            Dimension dimension = new Dimension(maxWidth, maxHeight);

            int largestHeight = 0;
            int largestWidth = 0;

            for (Component component : parent.getComponents()) {
                int x1 = 0, x2 = 0, y1 = 0, y2 = 0;

                AnchoredComponent anchoredComponent = this.getAnchoredComponents().get(component);
                HashMap<Anchor, Integer> anchors = anchoredComponent.getAnchorPixels(dimension);

                for (Anchor anchor : anchors.keySet()) {

                    if (anchor.getDirectionType() == ValueAnchor.DirectionType.X) {
                        if (x1 == 0) {
                            x1 = anchors.get(anchor);
                        } else {
                            x2 = anchors.get(anchor);
                        }
                    }

                    if (anchor.getDirectionType() == ValueAnchor.DirectionType.Y) {
                        if (y1 == 0) {
                            y1 = anchors.get(anchor);
                        } else {
                            y2 = anchors.get(anchor);
                        }
                    }
                }

                if(Math.max(y1, y2) > largestHeight) largestHeight = Math.max(y1, y2);
                if(Math.max(x1, x2) > largestWidth) largestWidth = Math.max(x1, x2);

                int width = Math.abs(x1 - x2);
                int height = Math.abs(y1 - y2);

                component.setPreferredSize(new Dimension(width, height));
                component.setBounds(Math.min(x1, x2), Math.min(y1, y2), width, height);
            }

            int newHeight, newWidth;
            if(parent.getPreferredSize() == null) {
                newHeight = this.isHeightControl() ? 0 : largestHeight;
                newWidth = this.isWidthControl() ? 0 : largestWidth;
            } else {
                newHeight = this.isHeightControl() ? (int) parent.getPreferredSize().getHeight() : largestHeight;
                newWidth = this.isWidthControl() ? (int) parent.getPreferredSize().getWidth() : largestWidth;
            }

            parent.setPreferredSize(new Dimension(newWidth, newHeight));

            //TO-DO fix flickering test with 10 ToolListUI
        }
    }

    public AnchorLayout(boolean paramWidthControl, boolean paramHeightControl) {
        this.setWidthControl(paramWidthControl);
        this.setHeightControl(paramHeightControl);
    }

    public AnchorLayout() {
        this(false, false);
    }
}
