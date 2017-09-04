package com.moneymaker.modules.financialtype.controller;

import com.moneymaker.modules.financialtype.Goal;
import com.moneymaker.modules.financialtype.list.GoalList;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.layout.AnchorPane;

/**
 * Created by Jay Damon on 8/4/2017.
 */
public class GoalController extends FinancialTypeController<Goal> {

    public GoalController(String newFXMLPath, String updateFXMLPath) {
        super(newFXMLPath, updateFXMLPath);
        setItemList(GoalList.getInstance().activateList());
    }

    @Override
    protected void launchContextMenu(double x, double y) {
        ContextMenu contextMenu = getContextMenu();
        AnchorPane pane = new AnchorPane();
        primaryTable.setContextMenu(contextMenu);
        contextMenu.show(pane, x, y);
    }
}
