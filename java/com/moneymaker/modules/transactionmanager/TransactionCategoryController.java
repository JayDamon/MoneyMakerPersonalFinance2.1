package com.moneymaker.modules.transactionmanager;

import com.moneymaker.modules.financialtype.Transaction;
import com.moneymaker.modules.financialtype.list.BudgetList;
import com.moneymaker.modules.financialtype.list.TransactionCategoryList;
import com.moneymaker.modules.financialtype.list.TransactionList;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;

/**
 * Created by Jay Damon on 9/10/2016.
 */
public class TransactionCategoryController implements Initializable {

    @FXML
    private VBox vboxTransactionCategory;

    @FXML
    private TableView<Transaction> tblUncategorizedTransactions;

    private TransactionList tList = TransactionList.getInstance();

    @FXML
    Button btnFinish;

    public void initialize(URL location, ResourceBundle rs) {
        tblUncategorizedTransactions.setOnMouseClicked(event -> tblUncategorizedTransactions.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE));
    }


    public void addUncategorizedTransactions(String budgetName) {
        ObservableList<Transaction> data = tblUncategorizedTransactions.getItems();
        data.clear();
        data.addAll(tList.getUncateogrizedTransactions(budgetName));

        setTableDragBehavior();

        addCategoryLabels(budgetName);

    }

    private void setTableDragBehavior() {
        tblUncategorizedTransactions.setOnDragDetected(event -> {
            Dragboard db = tblUncategorizedTransactions.startDragAndDrop(TransferMode.COPY);

            int transactionID = tblUncategorizedTransactions.getSelectionModel().getSelectedItem().getID();

            ClipboardContent content = new ClipboardContent();
            content.putString(String.valueOf(transactionID));
            db.setContent(content);
            event.consume();
        });
    }

    private void addCategoryLabels(String budgetName) {
        ObservableList<String> transactionCategories = TransactionCategoryList.getInstance().activateList().
                getTransactionCategories(budgetName);

        for (String s : transactionCategories) {
            Label newLabel = new Label(s);
            newLabel.setStyle("-fx-border-color: BLACK;");
            newLabel.setPrefSize(243, 71);
            newLabel.setMinHeight(30);
            newLabel.setText(s);

            addLabelDragBehavior(newLabel);

            vboxTransactionCategory.getChildren().add(newLabel);
        }
    }

    private void addLabelDragBehavior(Label newLabel) {
        newLabel.addEventHandler(
                DragEvent.DRAG_OVER,
                event -> {
                    event.acceptTransferModes(TransferMode.COPY);
                    newLabel.setStyle("-fx-border-color: #3399ff;");
                    event.consume();
                }
        );

        newLabel.addEventHandler(
                DragEvent.DRAG_EXITED,
                event -> {
                    newLabel.setStyle("-fx-border-color: BLACK;");
                    event.consume();
                }
        );

        newLabel.addEventHandler(
                DragEvent.DRAG_DROPPED,
                event -> {
                    if(event.getTransferMode() == TransferMode.COPY && event.getDragboard().hasString()) {

                        categorizeTransactions(newLabel);


                        event.setDropCompleted(true);
                    }
                    event.consume();
        });
    }

    private void categorizeTransactions(Label newLabel) {
        ObservableList<Transaction> selectedTransactions = tblUncategorizedTransactions.getSelectionModel().getSelectedItems();
        String transactionCategory = newLabel.getText();
        for (Transaction t : selectedTransactions) {
            t.setCategory(transactionCategory);
            t.getBehavior().update();
        }
        tblUncategorizedTransactions.getItems().removeAll(selectedTransactions);
        BudgetList.getInstance().activateList().updateUncategorizedNumber();
    }

    @FXML
    protected void exitWindow() {
        Stage stage = (Stage)btnFinish.getScene().getWindow();
        stage.close();
    }

}
