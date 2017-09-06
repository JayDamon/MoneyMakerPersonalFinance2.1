package com.moneymaker.modules.financialtype.controller;

import com.moneymaker.modules.financialtype.Transaction;
import com.moneymaker.modules.financialtype.list.RecurringTransactionList;
import com.moneymaker.modules.financialtype.list.TransactionList;
import com.moneymaker.modules.transactionmanager.ImportTransactionsWindowController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created for MoneyMaker by Jay Damon on 3/27/2016.
 */
public class TransactionController extends FinancialTypeController<Transaction> {

    @FXML
    private final Button buttonImport = new Button();

    public TransactionController(String newFXMLPath, String updateFXMLPath) {
        super(newFXMLPath, updateFXMLPath);
        setItemList(TransactionList.getInstance().activateList());
    }

    @Override
    public void initialize(URL url, ResourceBundle rs) {
        super.initialize(url, rs);
        buttonImport.setOnAction(e -> importTransactions());
    }

    @Override
    protected void launchContextMenu(double x, double y) {
        ContextMenu menu = getContextMenu();
        MenuItem importTransactions = new MenuItem("Import");
        MenuItem setRecurringTransaction = new MenuItem("Set Recurring");
        importTransactions.setOnAction(event -> {
            importTransactions();
            event.consume();
        });
        setRecurringTransaction.setOnAction(event -> {
            setRecurringTransaction(primaryTable.getSelectionModel().getSelectedItems());
            event.consume();
        });
        menu.getItems().addAll(importTransactions, setRecurringTransaction);
        AnchorPane pane = new AnchorPane();
        primaryTable.setContextMenu(menu);
        menu.show(pane, x, y);
    }

    @FXML
    public void importTransactions() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/transactions/transactions/importTransactions.fxml"));
        try {
            AnchorPane newWindow = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Import");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryPane.getScene().getWindow());
            Scene scene = new Scene(newWindow);
            stage.setScene(scene);

            ImportTransactionsWindowController it = loader.getController();
            it.setTransactionList(this.getItemList().getList());

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setRecurringTransaction(ObservableList<Transaction> transactions) {
        AnchorPane newWindow = new AnchorPane();
        HBox hBox = new HBox();
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10,10,10,10));
        hBox.setPadding(new Insets(10,10,10,10));
        newWindow.getChildren().add(hBox);
        Label label = new Label("Recurring Transaction");
        label.setPrefWidth(150);
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPrefWidth(200);
        comboBox.setItems(RecurringTransactionList.getInstance().activateList().getRecurringTransactionList());

        hBox.getChildren().addAll(label, comboBox);

        Button button = new Button("Submit");

        button.setOnAction(event -> {
            if (comboBox.getSelectionModel().getSelectedIndex() != -1) {
                for (Transaction t : transactions) {
                    t.setRecurring(comboBox.getSelectionModel().getSelectedItem());
                    t.getBehavior().update();
                }
            }
            Stage stage = (Stage)button.getScene().getWindow();
            stage.close();
        });

        vBox.getChildren().setAll(hBox, button);
        newWindow.getChildren().add(vBox);

        Stage stage = new Stage();
        stage.setTitle("Set Recurring Transaction");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryPane.getScene().getWindow());
        Scene scene = new Scene(newWindow);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void showExpenses() {
        TransactionList list = TransactionList.getInstance();
        primaryTable.getItems().clear();
        primaryTable.getItems().addAll(list.getExpenses());
    }

    public void showIncome() {
        TransactionList list = TransactionList.getInstance();
        primaryTable.getItems().clear();
        primaryTable.getItems().addAll(list.getIncome());
    }

    public void showTransactions() {
        TransactionList list = TransactionList.getInstance();
        primaryTable.getItems().clear();
        primaryTable.getItems().addAll(list.getList());
    }

    public void showTransferExpenses() {
        TransactionList list = TransactionList.getInstance();
        primaryTable.getItems().clear();
        primaryTable.getItems().addAll(list.getTransferExpenses());
    }

    public void showTransferIncome() {
        TransactionList list = TransactionList.getInstance();
        primaryTable.getItems().clear();
        primaryTable.getItems().addAll(list.getTransferIncome());
    }
}
