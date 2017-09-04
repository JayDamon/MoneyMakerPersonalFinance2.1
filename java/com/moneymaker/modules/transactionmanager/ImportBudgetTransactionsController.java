package com.moneymaker.modules.transactionmanager;

import com.moneymaker.modules.financialtype.Transaction;
import com.moneymaker.modules.financialtype.list.AccountList;
import com.moneymaker.modules.financialtype.list.BudgetList;
import com.moneymaker.modules.financialtype.list.TransactionCategoryList;
import com.moneymaker.utilities.ParseAndSplitDate;
import com.moneymaker.utilities.RequiredFieldList;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ImportBudgetTransactionsController implements Initializable {

    @FXML TableColumn colIncDate, colIncDesc, colIncAmount;

    @FXML ComboBox<String> cmbDate, cmbDesc, cmbDebit, cmbCred, cmbAcc;

    @FXML TableView<Transaction> tblSpending, tblIncome;

    @FXML AnchorPane anchSpending, anchIncome;

    @FXML Button btnExit;

    @FXML VBox vBoxCategory, vboxBudgets;

    @FXML ToggleButton togSpending, togIncome;

    @FXML Label lblTransactionType;

    File filePath;

    ObservableList<String> tranHead;

    private ObservableList<Transaction> transactions;

    private RequiredFieldList requiredFieldList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<Control> c = new ArrayList<>();
        c.add(cmbAcc);
        requiredFieldList = new RequiredFieldList(c);
        togSpending.setSelected(true);
        lblTransactionType.setText("Spending");

        addBudgets();

        tblSpending.setOnMouseClicked(event -> tblSpending.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE));

        tblIncome.setOnMouseClicked(event -> tblIncome.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE));

    }

    public void setTransactions(ObservableList<Transaction> transactions) {
        this.transactions = transactions;
    }

    void setComboBoxes(String tranDate, String tranDescription, String tranDebit, String tranCredit, String tranAccount) {
        cmbDate.setValue(tranDate);
        cmbDesc.setValue(tranDescription);
        cmbDebit.setValue(tranDebit);
        cmbCred.setValue(tranCredit);
        cmbAcc.setValue(tranAccount);
    }

    void setComboBoxList(ObservableList<String> list) {
        cmbDate.getItems().addAll(list);
        cmbDesc.getItems().addAll(list);
        cmbDebit.getItems().addAll(list);
        cmbCred.getItems().addAll(list);
        cmbAcc.getItems().addAll(AccountList.getInstance().activateList().getAccountNameList());
    }

    void importCSV(String filePath) throws Exception {
        ObservableList<Transaction> data = tblSpending.getItems();
        ObservableList<Transaction> data2 = tblIncome.getItems();
        data.clear();
        data2.clear();

        String transactionDate = cmbDate.getSelectionModel().getSelectedItem();
        String transactionDescription = cmbDesc.getSelectionModel().getSelectedItem();
        String transactionDebit = cmbDebit.getSelectionModel().getSelectedItem();
        String transactionCredit = cmbCred.getSelectionModel().getSelectedItem();
        String delimiter = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
        String line;

        int tranDate = 0;
        int tranDesc = 0;
        int tranDebit = 0;
        int tranCredit = 0;

        Scanner scanner = new Scanner(new File(filePath));
        scanner.useDelimiter(delimiter);
        Path pathToFile = Paths.get(filePath);
        BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII);
        int i = 0;

        while ((line = br.readLine()) != null) {
            //Get the first row of the csv and add the headers to the ListView
            if (i == 0) {
                String[] headerStringArray = line.split(delimiter, -1);
                List<String> headers = Arrays.asList(headerStringArray);

                //Loop through and trim string to remove leading and trailing spaces
                for(final ListIterator<String> listIterator = headers.listIterator(); listIterator.hasNext();) {
                    final String updateString = listIterator.next();
                    listIterator.set(updateString.trim());
                }

                int csvNum = 0;
                for (String s: headers) {
                    if (s.equals(transactionDate)) {
                        tranDate = csvNum;
                    } else if (s.equals(transactionDescription)) {
                        tranDesc = csvNum;
                    } else if (s.equals(transactionDebit)) {
                        tranDebit = csvNum;
                    } else if (s.equals(transactionCredit)) {
                        tranCredit = csvNum;
                    }
                    csvNum++;
                }
            }

            if (i != 0) {
                addToTable(data, data2, delimiter, line, tranDate, tranDesc, tranDebit, tranCredit);
            }
            i++;
        }
    }

    private void addToTable(ObservableList<Transaction> data, ObservableList<Transaction> data2, String delimiter, String line, int tranDate, int tranDesc, int tranDebit, int tranCredit) {
        BigDecimal tranAmount;
        String[] cols = line.split(delimiter, -1);
        String tranDateString = cols[tranDate];
        String tranDescriptionString = cols[tranDesc].replace("\"","");
        String account = cmbAcc.getSelectionModel().getSelectedItem();
        if (!cols[tranDebit].isEmpty()) {
            tranAmount = new BigDecimal(cols[tranDebit]).multiply(BigDecimal.valueOf(-1));
            data.add(new Transaction(0,account,"","", "", ParseAndSplitDate.getDate(tranDateString), tranDescriptionString, tranAmount,null));
        } else if (cols[tranDebit].isEmpty()) {
            tranAmount = new BigDecimal(cols[tranCredit]);
            data2.add(new Transaction(0,account,"","", "", ParseAndSplitDate.getDate(tranDateString), tranDescriptionString, tranAmount,null));
        }
    }

    @FXML
    protected void backBtn(ActionEvent event) throws IOException {

        String tranDate = cmbDate.getSelectionModel().getSelectedItem();
        String tranDesc = cmbDesc.getSelectionModel().getSelectedItem();
        String tranDebit = cmbDebit.getSelectionModel().getSelectedItem();
        String tranCredit = cmbCred.getSelectionModel().getSelectedItem();
        String tranAcc = cmbAcc.getSelectionModel().getSelectedItem();

        ((Node) (event.getSource())).getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/updateitem/importTransactions.fxml"));
        AnchorPane newWindow = loader.load();
        Stage stage = new Stage();
        stage.setTitle("ExpenseList");
        Scene scene = new Scene(newWindow);
        stage.setScene(scene);
        stage.show();
        ImportTransactionsWindowController impWind = loader.getController();
        impWind.fileText(filePath);
        impWind.setTextBoxes(tranDate, tranDesc, tranDebit, tranCredit, tranAcc);
        impWind.fillListBox(this.tranHead);
    }

    @FXML
    protected void exitWindow() {
        Stage stage = (Stage)btnExit.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void togIncome() {
        anchIncome.setVisible(true);
        anchSpending.setVisible(false);
        lblTransactionType.setText("Income");
    }

    @FXML
    private void togSpending() {
        anchIncome.setVisible(false);
        anchSpending.setVisible(true);
        lblTransactionType.setText("Spending");
    }

    @FXML
    private void submitChanges() {
        if (requiredFieldList.isComplete()) {
            boolean missingIncome = false;
            boolean missingSpending = false;
            boolean addMissingTransaction = false;

            if (tblIncome.getItems().size() > 0) {
                missingIncome = true;
            }
            if (tblSpending.getItems().size() > 0) {
                missingSpending = true;
            }

            if (missingIncome && missingSpending) {
                addMissingTransaction = addMissingTransactions("Income and Spending");
            } else if (missingIncome) {
                addMissingTransaction = addMissingTransactions("Income");
            } else if (missingSpending) {
                addMissingTransaction = addMissingTransactions("Spending");
            }

            if (!addMissingTransaction) {
                closeWindow();
            }
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.close();
    }


    @FXML
    private void addBudgets() {

        //Get row data from spending table for dragging
        tblSpending.setOnDragDetected(event -> {
            Dragboard db = tblSpending.startDragAndDrop(TransferMode.COPY);

            int index = tblSpending.getSelectionModel().getSelectedIndex();
            Transaction selTran = tblSpending.getItems().get(index);

            ClipboardContent content = new ClipboardContent();
            content.putString(selTran.getDescription());
            db.setContent(content);
            event.consume();
        });

        //Get row data from income table for dragging
        tblIncome.setOnDragDetected(event -> {
            Dragboard db = tblIncome.startDragAndDrop(TransferMode.COPY);

            int index = tblIncome.getSelectionModel().getSelectedIndex();
            Transaction selTran = tblIncome.getItems().get(index);

            ClipboardContent content = new ClipboardContent();
            content.putString(selTran.getDescription());
            db.setContent(content);
            event.consume();
        });
        
        ObservableList<String> budgets = BudgetList.getInstance().getActiveBudgets();

        for (String e : budgets) {
            Label budgetLabel = new Label(e);
            budgetLabel.setText(e);
            budgetLabel.setStyle("-fx-border-color: BLACK;");
            budgetLabel.setPrefSize(243, 71);
            budgetLabel.setMinHeight(30);
            vboxBudgets.getChildren().add(budgetLabel);

            budgetLabel.setOnMouseClicked(event -> {
                for (Node child : vboxBudgets.getChildren()) {
                    child.setStyle("-fx-border-color: BLACK;");
                }
                addCategoryLabels(budgetLabel);
            });

            budgetLabel.setOnDragDetected(event -> {
                Dragboard db = budgetLabel.startDragAndDrop(TransferMode.COPY);

                ClipboardContent content = new ClipboardContent();
                content.putString(budgetLabel.getText());
                db.setContent(content);
                event.consume();
            });

            budgetLabel.addEventHandler(
                    DragEvent.DRAG_OVER,
                    event -> {
                        event.acceptTransferModes(TransferMode.COPY);
                        addCategoryLabels(budgetLabel);
                        event.consume();
            });

            budgetLabel.addEventHandler(
                    DragEvent.DRAG_EXITED,
                    event -> {
                        event.acceptTransferModes(TransferMode.COPY);
                        budgetLabel.setStyle("-fx-border-color: BLACK;");
                        event.consume();
                    }
            );

            budgetLabel.addEventHandler(
                    DragEvent.DRAG_DROPPED,
                    event -> {
                        if(event.getTransferMode() == TransferMode.COPY && event.getGestureSource() != budgetLabel) {
                            if (event.getGestureSource() == tblSpending) {
                                addToDB(tblSpending, e);
                            } else if (event.getGestureSource() == tblIncome) {
                                addToDB(tblIncome, e);
                            }
                        }
                        event.consume();
            });
        }
    }

    private void addCategoryLabels(Label budgetLabel) {
        budgetLabel.setStyle("-fx-border-color: #3399ff;");
        String selectedCategory = budgetLabel.getText();
        ObservableList<String> transactionCategories = TransactionCategoryList.getInstance().activateList().getTransactionCategories(selectedCategory);

        vBoxCategory.getChildren().clear();
        for (String category : transactionCategories) {
            Label categoryLabel = new Label(category);
            categoryLabel.setText(category);
            categoryLabel.setStyle("-fx-border-color: BLACK;");
            categoryLabel.setPrefSize(243, 71);
            categoryLabel.setMinHeight(30);
            vBoxCategory.getChildren().add(categoryLabel);

            categoryLabel.addEventHandler(
                    DragEvent.DRAG_OVER,
                    labelEvent -> {
                        labelEvent.acceptTransferModes(TransferMode.COPY);
                        categoryLabel.setStyle("-fx-border-color: #3399ff;");
                        labelEvent.consume();
                    });

            categoryLabel.addEventHandler(
                    DragEvent.DRAG_EXITED,
                    labelEvent -> {
                        labelEvent.acceptTransferModes(TransferMode.COPY);
                        categoryLabel.setStyle("-fx-border-color: BLACK;");
                        labelEvent.consume();
                    }
            );

            categoryLabel.addEventHandler(
                    DragEvent.DRAG_DROPPED,
                    labelEvent -> {
                        if(labelEvent.getTransferMode() == TransferMode.COPY && labelEvent.getGestureSource() != budgetLabel) {
                            if (labelEvent.getGestureSource() == tblSpending) {
                                addToDB(tblSpending, selectedCategory, category);
                            } else if (labelEvent.getGestureSource() == tblIncome) {
                                addToDB(tblIncome, selectedCategory, category);
                            }
                        }
                        labelEvent.consume();
                    });
        }
    }

    private void addToDB(TableView<Transaction> table, String budget) {
        ObservableList<Transaction> transactions = table.getSelectionModel().getSelectedItems();
        for (Transaction it : transactions) {
            it.setBudget(budget);
            if (it.getBehavior().addToDB()) {
                this.transactions.add(it);
            }
        }
        table.getItems().removeAll(transactions);
    }

    private void addToDB(TableView<Transaction> table, String budget, String category) {
        ObservableList<Transaction> transactions = table.getSelectionModel().getSelectedItems();
        for (Transaction it : transactions) {
            it.setBudget(budget);
            it.setCategory(category);
            if (it.getBehavior().addToDB()) {
                this.transactions.add(it);
            }
        }
        table.getItems().removeAll(transactions);
    }

    private boolean addMissingTransactions(String transactionType) {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.NO);
        Alert alert = new Alert(Alert.AlertType.NONE, "Are you sure you would like to end this import before adding these transactions?", yes, no);
        alert.setTitle("TransactionList Not Added");
        alert.setHeaderText("Some " + transactionType + " transactions have not been added.");
//        alert.setContentText();
        Optional<ButtonType> result = alert.showAndWait();

        return result.filter(buttonType -> buttonType == no).isPresent();
    }

}
