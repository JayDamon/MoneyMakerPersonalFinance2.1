/*
 * Copyright (c) 2012, 2014 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.moneymaker.main;

import com.moneymaker.modules.financialtype.controller.*;
import com.moneymaker.utilities.ConnectionManager.ConnectionManagerUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

//Make it so that a button click changes table contents including columns
//Make this a "workbench layout"

public class MainWindowController implements Initializable {

    @FXML
    AnchorPane mainPane = new AnchorPane();

    @FXML
    private VBox vBoxTransactionOptions, vBoxTransactions;

    @FXML
    private AnchorPane homePane, transactionTablePane, transferTablePane, recurringTransactionTablePane, accountTablePane,
            budgetTablePane, goalsTablePane, cashFlowTablePane;

    @FXML
    private Label userInfoLabel;

    private TransactionController tCon;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addTables();
        hideAllPanes();
        homePane.setVisible(true);

        userInfoLabel.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                ContextMenu contextMenu = new ContextMenu();
                MenuItem logout = new MenuItem("Logout");
                logout.setOnAction(e -> logout());
                contextMenu.getItems().add(logout);
                userInfoLabel.setContextMenu(contextMenu);
                contextMenu.show(userInfoLabel, event.getScreenX(), event.getScreenY());
                event.consume();
            }
        });
    }

    private void addTables() {
        ConnectionManagerUser conn = ConnectionManagerUser.getInstance();
        conn.suspendClose();

        addGoalTable();
        addTransactionTable();
        addBudgetTable();
        addTransferTable();
        addAccountTable();
        addRecurringTransactions();
        addCashFlow();

        conn.activateClose();
        conn.close();
    }



    private void addGoalTable() {
        GoalController con = new GoalController("fxml/goals/newGoalWindow.fxml", "fxml/goals/updateGoalWindow.fxml");
        addTable("fxml/goals/goalsWindow.fxml",
                con,
                goalsTablePane);
    }

    private void addTransactionTable() {
        TransactionController con = new TransactionController("fxml/transactions/transactions/newTransaction.fxml", "fxml/transactions/transactions/updateTransactionWindow.fxml");
        addTable("fxml/transactions/transactions/transactionWindow.fxml",
                con,
                transactionTablePane);
        tCon = con;
    }

    private void addBudgetTable() {
        BudgetController con = new BudgetController("fxml/budget/budgetWindow.fxml", "fxml/budget/updateBudget.fxml");
        addTable("fxml/budget/budgetTableWindow.fxml",
                con,
                budgetTablePane);
    }

    private void addTransferTable() {
        TransferController con = new TransferController("fxml/transfers/newTransfer.fxml", "fxml/transfers/setTransferValueWindow.fxml");
        addTable("fxml/transfers/transferWindow.fxml",
                con,
                transferTablePane);
    }

    private void addAccountTable() {
        AccountController con = new AccountController("fxml/accounts/newAccount.fxml", "fxml/accounts/updateAccount.fxml");
        addTable("fxml/accounts/accountWindow.fxml",
                con,
                accountTablePane);
    }

    private void addRecurringTransactions() {
        RecurringTransactionController con = new RecurringTransactionController("fxml/transactions/recurringtransactions/newRecurringTransactionWindow.fxml",
                "fxml/transactions/recurringtransactions/updateRecurringTransactionWindow.fxml");
        addTable("fxml/transactions/recurringtransactions/recurringTransactionsWindow.fxml",
                con,
                recurringTransactionTablePane);
    }

    private void addCashFlow() {
        CashFlowController con = new CashFlowController();
        addTable("fxml/cashflow/cashFlowWindow.fxml",
                con,
                cashFlowTablePane);
    }

    private void addTable(String tableFXML, FinancialTypeController t, AnchorPane tableContainer) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(tableFXML));
        try {
            loader.setController(t);
            GridPane pane = loader.load();
            AnchorPane.setTopAnchor(pane, 0.0);
            AnchorPane.setBottomAnchor(pane, 0.0);
            AnchorPane.setRightAnchor(pane, 0.0);
            AnchorPane.setLeftAnchor(pane, 0.0);
            tableContainer.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logout() {
        Stage mainStage = (Stage)mainPane.getScene().getWindow();
        mainStage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/main/loginWindow.fxml"));
        try {
            AnchorPane primaryStage = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Login");
            Scene scene = new Scene(primaryStage);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //These are the button actions for the side panel
//Display accounts
    @FXML
    protected void displayHome() {
        hideAllPanes();
        homePane.setVisible(true);
        removeTransactionButtons();
    }

    //Display accounts
    @FXML
    protected void displayAccounts() throws SQLException {
        hideAllPanes();
        accountTablePane.setVisible(true);
        removeTransactionButtons();
    }

    //Display Budget
    @FXML
    protected void displayBudget() throws SQLException {
        hideAllPanes();
        budgetTablePane.setVisible(true);
        removeTransactionButtons();
    }

    //Display Cashflow
    @FXML
    protected void displayCashFlow() throws SQLException {
        hideAllPanes();
        cashFlowTablePane.setVisible(true);
        removeTransactionButtons();
    }

    //Display Goals
    @FXML
    protected void displayGoals() throws SQLException {
        hideAllPanes();
        goalsTablePane.setVisible(true);
        removeTransactionButtons();
    }

    //Display TransactionList
    @FXML
    protected void displayTransactions() throws SQLException {
        showTransactions();

        String stringExpenses = "Expenses";
        String stringIncome = "Income";
        String stringTransfers = "Transfers";
        String stringRecurring = "Recurring";
        String stringImport = "Import";

        String[] buttons = {stringExpenses, stringIncome, stringTransfers, stringRecurring, stringImport};

        for (String s : buttons) {
            Button newButton = new Button(s);
            VBox newVBox = new VBox();
            vBoxTransactionOptions.getChildren().add(newVBox);
            newButton.setText("- " + s);
            newButton.setStyle("-fx-font-size: 18.0;");
            newButton.setMaxWidth(1.7976931348623157E308);

            newButton.addEventHandler(
                    MouseEvent.MOUSE_CLICKED,
                    event -> {
                        switch (s) {
                            case "Expenses":
                                hideAllPanes();
                                transactionTablePane.setVisible(true);
                                tCon.showExpenses();
                                break;
                            case "Income":
                                hideAllPanes();
                                transactionTablePane.setVisible(true);
                                tCon.showIncome();
                                break;
                            case "Transfers":
                                hideAllPanes();
                                transferTablePane.setVisible(true);
                                String stringTransferExpenses = "Expenses";
                                String stringTransferIncome = "Income";
                                String[] transferButtons = {stringTransferExpenses, stringTransferIncome};
                                if (newVBox.getChildren().size() <= 1) {
                                    for (String string : transferButtons) {
                                        Button newTransferButton = new Button(string);
                                        newTransferButton.setText(string);
                                        newTransferButton.setStyle("-fx-font-size: 14.0;");
                                        newTransferButton.setMaxWidth(1.7976931348623157E308);
                                        newTransferButton.setPadding(new Insets(0, 0, 0, 40));
                                        newTransferButton.setOnMouseClicked(newEvent -> {
                                            switch (string) {
                                                case "Expenses":
                                                    hideAllPanes();
                                                    transactionTablePane.setVisible(true);
                                                    tCon.showTransferExpenses();
                                                    break;
                                                case "Income":
                                                    hideAllPanes();
                                                    transactionTablePane.setVisible(true);
                                                    tCon.showTransferIncome();
                                                    break;
                                            }
                                        });
                                        newVBox.getChildren().add(newTransferButton);
                                    }
                                }
                                break;
                            case "Recurring":
                                showRecurringTransactions();
                                String stringPlanned = "Planned";
                                String stringActual = "Actual";
                                String[] recurringButtons = {stringPlanned, stringActual};
                                if (newVBox.getChildren().size() <= 1) {
                                    for (String string : recurringButtons) {
                                        Button newRecurringButton = new Button(string);
                                        newRecurringButton.setText(string);
                                        newRecurringButton.setStyle("-fx-font-size: 14.0;");
                                        newRecurringButton.setMaxWidth(1.7976931348623157E308);
                                        newRecurringButton.setPadding(new Insets(0, 0, 0, 40));
                                        newRecurringButton.setOnMouseClicked(newEvent -> {
                                            switch (string) {
                                                case "Planned":
                                                    showRecurringTransactions();
                                                    break;
                                                case "Actual":
                                                    hideAllPanes();
                                                    transactionTablePane.setVisible(true);
                                                    //ToDo add this
                                                    break;
                                            }
                                        });
                                        newVBox.getChildren().add(newRecurringButton);
                                    }
                                }
                                break;
                            case "Import":
                                tCon.importTransactions();
                                break;
                        }
                        event.consume();
                    });

            newVBox.getChildren().add(newButton);

        }

        vBoxTransactionOptions.setPadding(new Insets(0,0,0,20));
        vBoxTransactions.setStyle("-fx-background-color: #E1EDF8;");
    }

    private void showTransactions() {
        hideAllPanes();
        transactionTablePane.setVisible(true);
        removeTransactionButtons();
        tCon.showTransactions();
    }

    @FXML
    private void showRecurringTransactions() {
        hideAllPanes();
        recurringTransactionTablePane.setVisible(true);
    }

    //Display Trends
    @FXML
    protected void displayTrends() throws Exception {
        hideAllPanes();
        removeTransactionButtons();
    }

    @FXML
    public void setUserName(String userName) {
        userInfoLabel.setText("Welcome " + userName + "!  \u25BC");
    }

    @FXML
    private void removeTransactionButtons() {
        vBoxTransactionOptions.getChildren().removeAll(vBoxTransactionOptions.getChildren());
        vBoxTransactions.setStyle("-fx-background-color: white;");
    }

    private void hideAllPanes() {
        homePane.setVisible(false);
        goalsTablePane.setVisible(false);
        accountTablePane.setVisible(false);
        budgetTablePane.setVisible(false);
        transactionTablePane.setVisible(false);
        recurringTransactionTablePane.setVisible(false);
        transferTablePane.setVisible(false);
        cashFlowTablePane.setVisible(false);
    }

}
