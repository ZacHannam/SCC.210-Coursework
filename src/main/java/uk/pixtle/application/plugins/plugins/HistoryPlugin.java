package uk.pixtle.application.plugins.plugins;

import lombok.Getter;
import lombok.Setter;
import uk.pixtle.application.Application;
import uk.pixtle.application.events.annotations.EventHandler;
import uk.pixtle.application.events.events.ExampleEvent;
import uk.pixtle.application.plugins.expansions.PluginMiniToolExpansion;
import uk.pixtle.application.ui.layouts.anchorlayout.AnchoredComponent;
import uk.pixtle.application.ui.layouts.anchorlayout.anchors.Anchor;
import uk.pixtle.application.ui.window.minitoollist.MiniToolPanel;

import javax.swing.*;
import java.awt.*;

public class HistoryPlugin extends Plugin implements PluginMiniToolExpansion {

    // ---------------------- ABSTRACT METHODS ----------------------

    @Override
    public String getToolIconLocation() {
        return null;
    }

    // ---------------------- TEST METHODS ----------------------
    @EventHandler
    public void test(ExampleEvent paramEvent) {
        System.out.println(paramEvent.getCreationTime().toString());
    }

    // ---------------------- MINI TOOL EXPANSION METHODS ----------------------

    @Getter
    @Setter
    MiniToolPanel miniToolPanel;

    @Getter
    @Setter
    JList historyList;

    @Getter
    @Setter
    JScrollPane historyPane;

    String historyArray[] = new String[1];

    @Override
    public int getMiniToolPanelHeight() {
        return 250;
    }

    public void addToHistory(String eventName)
    {
        // create a new array with one extra element
        String newHistory[] = new String[historyArray.length + 1];

        // copy the elements from the old array to the new array
        for (int i = 0; i < historyArray.length; i++) {
            newHistory[i] = historyArray[i];
        }

        // add the new eventName to the last element of the new array
        newHistory[newHistory.length - 1] = eventName;


        // replace the old array with the new array#
        historyArray = newHistory;

        historyList.setListData(historyArray);
        historyPane.setViewportView(historyList);

    }

    @Override
    public void instanceMiniToolPanel(MiniToolPanel paramMiniToolPanel) {
        this.setMiniToolPanel(paramMiniToolPanel);

        AnchoredComponent anchoredComponent = new AnchoredComponent();
        anchoredComponent.createAnchor(Anchor.DirectionType.X, 10);
        anchoredComponent.createAnchor(Anchor.DirectionType.X, -10);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, 10);
        anchoredComponent.createAnchor(Anchor.DirectionType.Y, -10);

        GridLayout HistoryLayout = new GridLayout(0,1);
        paramMiniToolPanel.setLayout(HistoryLayout);
        historyList = new JList(historyArray);
        historyPane = new JScrollPane(historyList);
        JButton undoButton = new JButton("UNDO");
        undoButton.setMaximumSize(new Dimension(40, 5));

        paramMiniToolPanel.add(historyPane);
        paramMiniToolPanel.add(undoButton);


        for(int i=0;i<59;i++)
            this.addToHistory("test" + i);






        paramMiniToolPanel.setBackground(Color.WHITE);
    }

    // ---------------------- CONSTRUCTOR ----------------------

    public HistoryPlugin(Application paramApplication) {
        super(paramApplication);

        super.getApplication().getEventManager().registerEvents(this);
    }

}
