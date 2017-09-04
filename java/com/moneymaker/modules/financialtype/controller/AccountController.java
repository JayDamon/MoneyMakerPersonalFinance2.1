package com.moneymaker.modules.financialtype.controller;

import com.moneymaker.modules.financialtype.Account;
import com.moneymaker.modules.financialtype.list.AccountList;
import javafx.scene.control.ContextMenu;
import javafx.scene.layout.AnchorPane;

/**
 * Created for MoneyMaker by jaynd on 3/27/2016.
 */
public class AccountController extends FinancialTypeController<Account> {

    public AccountController(String newFXMLPath, String updateFXMLPath) {
        super(newFXMLPath, updateFXMLPath);
        setItemList(AccountList.getInstance().activateList());
    }

    @Override
    protected void launchContextMenu(double x, double y) {
        ContextMenu contextMenu = getContextMenu();
        AnchorPane pane = new AnchorPane();
        primaryTable.setContextMenu(contextMenu);
        contextMenu.show(pane, x, y);
    }
}
