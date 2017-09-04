package com.moneymaker.modules.financialtype.controller;

import com.moneymaker.modules.financialtype.Budget;
import com.moneymaker.modules.financialtype.Transaction;
import com.moneymaker.modules.financialtype.list.BudgetList;
import com.moneymaker.modules.financialtype.list.TransactionCategoryList;
import com.moneymaker.modules.financialtype.list.TransactionList;
import com.moneymaker.modules.transactionmanager.TransactionCategoryController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created for MoneyMaker by jaynd on 3/28/2016.
 */
public class BudgetController extends FinancialTypeController<Budget> {

    @FXML
    private TableView<TransactionCategory> tblTranCat;

    @FXML
    private Button btnUncategorizedTransactions;

    public BudgetController(String newFXMLPath, String updateFXMLPath) {
        super(newFXMLPath, updateFXMLPath);
        setItemList(BudgetList.getInstance().activateList());
    }

    public void initialize(URL url, ResourceBundle rs) {
        super.initialize(url, rs);
        primaryTable.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (!primaryTable.getSelectionModel().getSelectedItems().isEmpty()) {
                String budgetCategory = primaryTable.getSelectionModel().getSelectedItem().getName();
                ObservableList<TransactionCategory> data = tblTranCat.getItems();
                data.clear();
                ObservableList<String> transactionCategories = TransactionCategoryList.getInstance().activateList().getTransactionCategories(budgetCategory);
                data.addAll(transactionCategories.stream().map(TransactionCategory::new).collect(Collectors.toList()));
            }
        });
        btnUncategorizedTransactions.setOnAction(event -> {
            showUncategorizedTransactions();
        });
    }

    @Override
    protected void createNew() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/budget/budgetWindow.fxml"));
        try {
            AnchorPane newWindow = loader.load();
            Stage stage = new Stage();
            stage.setTitle(("Budget Categories"));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryPane.getScene().getWindow());
            Scene scene = new Scene(newWindow);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showUncategorizedTransactions() {
        int selectedIndex = primaryTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            String budgetName = primaryTable.getSelectionModel().getSelectedItem().getName();
            ObservableList<Transaction> t = TransactionList.getInstance().activateList().getUncateogrizedTransactions(budgetName);
                if (t != null) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/transactions/transactions/transactionCategoryWindow.fxml"));
                        AnchorPane newWindow = loader.load();
                        Stage stage = new Stage();
                        stage.setTitle(budgetName);
                        stage.initModality(Modality.WINDOW_MODAL);
                        stage.initOwner(btnUncategorizedTransactions.getScene().getWindow());
                        Scene scene = new Scene(newWindow);
                        stage.setScene(scene);

                        TransactionCategoryController c = loader.getController();
                        c.addUncategorizedTransactions(budgetName);

                        stage.showAndWait();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    ButtonType btnSelectOK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                    Dialog<String> dialog = new Dialog<>();
                    dialog.getDialogPane().getButtonTypes().add(btnSelectOK);
                    dialog.setContentText("All \"" + budgetName + "\" entries have already been categorized.");
                    dialog.getDialogPane().lookupButton(btnSelectOK).setDisable(false);
                    dialog.showAndWait();
                }
        }
    }

    @Override
    protected void launchContextMenu(double x, double y) {
        ContextMenu menu = getContextMenu();
//        MenuItem budgetCategories = new MenuItem("Budget Categories");
        MenuItem uncategorizedTransactions = new MenuItem("Uncategorized TransactionList");

        uncategorizedTransactions.setOnAction(event -> {
            showUncategorizedTransactions();
            event.consume();
        });

        menu.getItems().addAll(uncategorizedTransactions);
        AnchorPane pane = new AnchorPane();
        primaryTable.setContextMenu(menu);
        menu.show(pane, x, y);

    }

    public class TransactionCategory {
        private final SimpleStringProperty transactionCategory = new SimpleStringProperty("");

        private TransactionCategory() {this(""); }

        public TransactionCategory(String transactionCategory) {
            setTransactionCategory(transactionCategory);
        }

        public String getTransactionCategory() { return transactionCategory.get(); }

        public void setTransactionCategory(String tCat) { this.transactionCategory.set(tCat); }
    }
}
