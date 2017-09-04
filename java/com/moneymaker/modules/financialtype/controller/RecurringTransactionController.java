package com.moneymaker.modules.financialtype.controller;

import com.moneymaker.modules.financialtype.RecurringTransaction;
import com.moneymaker.modules.financialtype.list.RecurringTransactionList;
import javafx.scene.control.ContextMenu;
import javafx.scene.layout.AnchorPane;

/**
 * Created for MoneyMaker by Jay Damon on 9/14/2016.
 */
public class RecurringTransactionController extends FinancialTypeController<RecurringTransaction> {

    public RecurringTransactionController(String newFXMLPath, String updateFXMLPath) {
        super(newFXMLPath, updateFXMLPath);
        setItemList(RecurringTransactionList.getInstance().activateList());
    }

    @Override
    protected void launchContextMenu(double x, double y) {
        ContextMenu contextMenu = getContextMenu();
        AnchorPane pane = new AnchorPane();
        primaryTable.setContextMenu(contextMenu);
        contextMenu.show(pane, x, y);
    }

}
