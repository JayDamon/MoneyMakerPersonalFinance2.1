package com.moneymaker.modules.financialtype.popup;

import com.moneymaker.modules.financialtype.Transaction;
import com.moneymaker.modules.financialtype.behavior.FinanceType;
import com.moneymaker.modules.financialtype.list.AccountList;
import com.moneymaker.modules.financialtype.list.TransactionCategoryList;
import com.moneymaker.modules.financialtype.list.TransactionList;
import com.moneymaker.modules.financialtype.popup.newpopup.NewTransactionController;
import com.moneymaker.utilities.gui.PopupController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created for MoneyMaker by Jay Damon on 10/20/2016.
 */
public abstract class SetTransferValueController extends PopupController {

    @FXML
    private Label labelFromDate, labelFromDescription, labelFromAmount,
            labelToDate, labelToDescription, labelToAmount;

    @FXML
    private VBox vBoxFromTransaction, vBoxToTransaction;

    @FXML
    protected ComboBox<String> cmbFromAccount, cmbToAccount, cmbTransferType;

    @FXML
    private TableView<Transaction> tableViewFromAccount, tableViewToAccount;

    @FXML
    private Button buttonNewFromTransaction, buttonNewToTransaction;

    protected Transaction toTransaction, fromTransaction;

    private final TransactionList transactionList = TransactionList.getInstance().activateList();

    private final String hoverBlue = "-fx-border-color: #7999D7;";
    private final String white = "-fx-border-color: white;";

    @Override
    protected ArrayList<ComboBox> comboBoxesForAutoComplete() {
        ArrayList<ComboBox> c = new ArrayList<>();
        c.add(cmbTransferType);
        c.add(cmbFromAccount);
        c.add(cmbToAccount);
        return c;
    }

    @Override
    protected void fillPrePopulatedControls() {
        fillComboBoxes();
    }

    private void fillComboBoxes() {
        cmbTransferType.getItems().clear();
        cmbTransferType.getItems().addAll(TransactionCategoryList.getInstance().activateList().getTransactionCategories(FinanceType.TRANSFER.specialBehavior()));
        cmbFromAccount.getItems().clear();
        cmbFromAccount.getItems().addAll(AccountList.getInstance().activateList().getAccountNameList());
        cmbToAccount.getItems().clear();
        cmbToAccount.getItems().addAll(AccountList.getInstance().activateList().getAccountNameList());
    }

    @Override
    protected ArrayList<Control> requiredFieldList() {
        ArrayList<Control> c = new ArrayList<>();
        c.add(cmbTransferType);
        c.add(cmbFromAccount);
        c.add(cmbToAccount);
        c.add(labelToAmount);
        c.add(labelFromAmount);
        c.add(labelFromDate);
        c.add(labelToDate);
        c.add(labelFromDescription);
        c.add(labelToDescription);

        return c;
    }

    public void initialize (URL url, ResourceBundle rs) {
        super.initialize(url, rs);
        setButtonBehavior();

        setTableOnMouseClicked(tableViewFromAccount);
        setTableOnMouseClicked(tableViewToAccount);
        setComboOnValueChange(cmbFromAccount, tableViewFromAccount);
        setComboOnValueChange(cmbToAccount, tableViewToAccount);

        final String from = "From";
        final String to = "To";

        addTableDragEvent(from, tableViewFromAccount);
        addTableDragEvent(to, tableViewToAccount);
        addVBoxDragOverEvent(from, vBoxFromTransaction);
        addVBoxDragOverEvent(to, vBoxToTransaction);
        addVBoxDragDroppedEvent(from, vBoxFromTransaction, tableViewFromAccount);
        addVBoxDragDroppedEvent(to, vBoxToTransaction, tableViewToAccount);
        addVBoxDragExitedEvent(vBoxFromTransaction);
        addVBoxDragExitedEvent(vBoxToTransaction);
    }

    private void addVBoxDragExitedEvent(VBox vb) {
        vb.addEventHandler(
                DragEvent.DRAG_EXITED,
                event -> {
                    event.acceptTransferModes(TransferMode.COPY);
                    vb.setStyle(this.white);
                    event.consume();
                }
        );
    }

    private void addVBoxDragDroppedEvent(String loc, VBox vb, TableView<Transaction> tbl) {
        vb.addEventHandler(
                DragEvent.DRAG_DROPPED,
                event -> {
                    event.acceptTransferModes(TransferMode.COPY);
                    Dragboard dragboard = event.getDragboard();
                    if (dragboard.hasString()) {
                        if(dragboard.getString().equals(loc)) {
                            if (loc.equals("From")) {
                                addFromTransaction(tbl.getSelectionModel().getSelectedItem());
                            } else if (loc.equals("To")) {
                                addToTransaction(tbl.getSelectionModel().getSelectedItem());
                            }
                        }
                    }
                    vb.setStyle(this.white);
                    event.consume();
                }
        );
    }

    private void addVBoxDragOverEvent(String from, VBox vb) {
        vb.addEventHandler(
                DragEvent.DRAG_OVER,
                event -> {
                    event.acceptTransferModes(TransferMode.COPY);
                    Dragboard dragboard = event.getDragboard();
                    if (dragboard.hasString()) {
                        if (dragboard.getString().equals(from)) {
                            vb.setStyle(this.hoverBlue);
                        }
                    }
                }
        );
    }

    private void addTableDragEvent(String from, TableView tbl) {
        tbl.setOnDragDetected(event -> {
            Dragboard dragboard = tbl.startDragAndDrop(TransferMode.COPY);
            ClipboardContent content = new ClipboardContent();
            content.putString(from);
            dragboard.setContent(content);
            event.consume();
        });
    }

    private void setButtonBehavior() {
        buttonNewFromTransaction.setOnAction(event -> launchNewTransactionWindow(cmbFromAccount, tableViewFromAccount));

        buttonNewToTransaction.setOnAction(event -> launchNewTransactionWindow(cmbToAccount, tableViewToAccount));
        buttonExit.setOnAction(event -> exitWindow());
    }

    private void setTableOnMouseClicked(TableView<Transaction> t) {
        t.setOnMouseClicked(event -> {
            if(event.getButton() == MouseButton.SECONDARY && t.getSelectionModel().getSelectedItems().size() == 1) {
                AnchorPane pane = new AnchorPane();
                ContextMenu contextMenu = createContextMenu(t);
                t.setContextMenu(contextMenu);
                contextMenu.show(pane, event.getSceneX(), event.getSceneY());
            }
            event.consume();
        });
    }

    private ContextMenu createContextMenu(TableView<Transaction> t) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem addTransaction = new MenuItem("Add Transaction");
        addTransaction.setOnAction(event -> addFromTransaction(t.getSelectionModel().getSelectedItem()));
        contextMenu.getItems().add(addTransaction);
        return contextMenu;
    }

    private void setComboOnValueChange(ComboBox<String> cmb, TableView<Transaction> table) {
        cmb.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                setTableValues(cmb, table);
            }
        });
    }

    private void setTableValues(ComboBox<String> cmb, TableView<Transaction> table) {
        String val = cmb.getSelectionModel().getSelectedItem();
        table.getItems().clear();
        ObservableList<Transaction> t = this.transactionList.getListByAccount(val);
        if (t != null) {
            table.setItems(t);
        }
    }

    private void addFromTransaction(Transaction t) {
        labelFromDate.setText(t.getDate());
        labelFromDescription.setText(t.getDescription());
        labelFromAmount.setText(t.getAmount());
        this.fromTransaction = t;
    }

    private void addToTransaction(Transaction t) {
        labelToDate.setText(t.getDate());
        labelToDescription.setText(t.getDescription());
        labelToAmount.setText(t.getAmount());
        this.toTransaction = t;
    }

    protected void updateTransaction(String transferType, Transaction t) {
        if (t != null) {
            t.setCategory(transferType);
            t.setBudget(FinanceType.TRANSFER.specialBehavior());
            t.getBehavior().update();
        }
    }

    private void launchNewTransactionWindow(ComboBox<String> account, TableView<Transaction> table) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/transactions/transactions/newTransaction.fxml"));
        AnchorPane newWindow;
        try {
            newWindow = loader.load();
            Stage stage = new Stage();
            stage.setTitle("New Transaction");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryPane.getScene().getWindow());
            Scene scene = new Scene(newWindow);
            stage.setScene(scene);

            NewTransactionController c = loader.getController();
            c.setListToAddTo(TransactionList.getInstance().activateList());
            c.addAccountToCombo(account.getSelectionModel().getSelectedItem());
            c.addBudgetToCombo(FinanceType.TRANSFER.specialBehavior());

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setTableValues(account, table);
    }
}
