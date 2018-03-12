package com.moneymaker.modules.financialtype.controller;

import com.moneymaker.modules.financialtype.Transaction;
import com.moneymaker.modules.financialtype.list.RecurringTransactionList;
import com.moneymaker.modules.financialtype.list.TransactionList;
import com.moneymaker.modules.transactionmanager.ImportTransactionsWindowController;
import com.mysql.cj.api.xdevapi.Column;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import com.sun.javafx.scene.control.skin.TableViewSkin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

/**
 * Created for MoneyMaker by Jay Damon on 3/27/2016.
 */
public class TransactionController extends FinancialTypeController<Transaction> {

    @FXML
    private Button buttonImport = new Button();

    public TransactionController(String newFXMLPath, String updateFXMLPath) {
        super(newFXMLPath, updateFXMLPath);
        setItemList(TransactionList.getInstance().activateList());
    }

    @Override
    public void initialize(URL url, ResourceBundle rs) {
        super.initialize(url, rs);
        buttonImport.setOnAction(e -> importTransactions());
        primaryTable.setOnMouseClicked(e -> {
            sortTableView();
        });
//        primaryTable.sortPolicyProperty().set(t -> {
//            ObservableList<TableColumn<Transaction, ?>> sortColumns = primaryTable.getSortOrder();
////            if (sortColumns.size() == 1 && sortColumns.get(0).getText().equals("Date")) {
////
////                primaryTable.getItems().sort(Comparator.comparing(tra -> tra.getCalendar()));
////            }
//            ObservableList<Transaction> transactions = TransactionList.getInstance().sortListFromTableViewCriteria(sortColumns);
//            if (transactions != null) {
//                System.out.println(transactions.size());
//                primaryTable.getItems().clear();
//                primaryTable.getItems().addAll(transactions);
//            }
////            return true;
//            FXCollections.sort(primaryTable.getItems(), Comparator.comparing(tran -> tran.getCalendar()));
////            primaryTable.getItems().sort(Comparator.comparing(tran -> tran.getCalendar()));
//            return true;
//        });
//        primaryTable.sortPolicyProperty().set(t -> {
//            Comparator<Transaction> comparator = (r1, r2)
//                    -> t.getComparator() == null ? 0 //no column sorted: don't change order
//                    : t.getComparator().compare(r1, r2); //columns are sorted: sort accordingly
//            FXCollections.sort(primaryTable.getItems(), comparator);
//            return true;
//        });
    }

    private void sortTableView() {
        ObservableList<TableColumn<Transaction, ?>> columns = primaryTable.getColumns();
        for (TableColumn<Transaction, ?> c : columns) {
            TableViewSkin<?> skin = (TableViewSkin)primaryTable.getSkin();
            ObservableList<Node> list = skin.getChildren();
            for (Node n : list) {
                if (n instanceof TableHeaderRow) {
                    System.out.println(n.getId());
                }
            }
//            c.getGraphic().setOnMouseClicked(e -> {
//                System.out.println("Clicked");
//            });
        }
//        System.out.println(primaryTable.skinProperty().getName());
//        System.out.println(primaryTable.lookup("TableHeaderRow"));
//        primaryTable.setOnMouseClicked(e -> {
//            if (e.getButton() == MouseButton.SECONDARY &&
//                e.getSource().getClass().equals(TableView.class)) {
//                TableView<Transaction> table = (TableView<Transaction>)e.getSource();
////                System.out.println(table.lookup("TableHeaderRow"));
////                if (headerRowNotNull) {
//                if (table.lookup("TableHeaderRow").getClass() == TableHeaderRow.class) {
//                    System.out.println("Found"); // Triggers all the time
//                }
//
//            }
//        });

//        primaryTable.skinProperty().addListener();
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
