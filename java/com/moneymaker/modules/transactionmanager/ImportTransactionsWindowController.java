package com.moneymaker.modules.transactionmanager;

import com.moneymaker.modules.financialtype.Transaction;
import com.moneymaker.modules.financialtype.list.AccountList;
import com.moneymaker.utilities.RequiredFieldList;
import javafx.collections.FXCollections;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;

/**
 * Created for MoneyMaker by Jay Damon on 7/8/2016.
 */
public class ImportTransactionsWindowController implements Initializable {

    @FXML
    public TextField txtTransactionDate;

    @FXML
    private TextArea txtAreaFilePath;

    @FXML
    private TextField txtDescription, txtDebit, txtCredit;

    @FXML
    private ComboBox<String> cmbAccount;

    @FXML
    private ListView<String> listHeaders;

    @FXML
    private Button btnImportTransactions, btnExit;

    private final FileChooser fileChooser = new FileChooser();

    private ObservableList<Transaction> transactionList;

    private RequiredFieldList requiredFieldList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ArrayList<Control> c = new ArrayList<>();
        c.add(txtTransactionDate);
        c.add(txtDescription);
        c.add(txtDebit);
        c.add(cmbAccount);
        requiredFieldList = new RequiredFieldList(c);
        try {
            fillAccountsCombo();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        txtAreaFilePath.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if(db.hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            } else {
                event.consume();
            }
        });

        txtAreaFilePath.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                success = true;
                String filePath = null;
                if(db.getFiles().size() == 1) {
                    for(File file : db.getFiles()) {
                        filePath = file.getAbsolutePath();
                        txtAreaFilePath.setText(filePath);
                        try {
                            parseCSV(filePath);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });

        listHeaders.setOnDragDetected(event -> {
            Dragboard db = listHeaders.startDragAndDrop(TransferMode.COPY);
            ClipboardContent content = new ClipboardContent();
            content.putString(listHeaders.getSelectionModel().getSelectedItem());
            db.setContent(content);
            event.consume();
        });

        //Add Drag Event to Transaction Date text field
        txtTransactionDate.addEventHandler(
                DragEvent.DRAG_OVER,
                event -> {
                    if(event.getDragboard().hasString()) {
                        event.acceptTransferModes(TransferMode.COPY);
                        txtTransactionDate.requestFocus();
                    }
                    event.consume();
                });

        txtTransactionDate.addEventHandler(
                DragEvent.DRAG_DROPPED,
                event -> {
                    Dragboard dragboard = event.getDragboard();
                    if(event.getTransferMode() == TransferMode.COPY && dragboard.hasString()) {
                        txtTransactionDate.setText(dragboard.getString());
                        event.setDropCompleted(true);
                    }
                    event.consume();
                }
        );

        //Add Drag Event to Transaction Description text field
        txtDescription.addEventHandler(
                DragEvent.DRAG_OVER,
                event -> {
                    if(event.getDragboard().hasString()) {
                        event.acceptTransferModes(TransferMode.COPY);
                        txtDescription.requestFocus();
                    }
                    event.consume();
                });
        txtDescription.addEventHandler(
                DragEvent.DRAG_DROPPED,
                event -> {
                    Dragboard dragboard = event.getDragboard();
                    if(event.getTransferMode() == TransferMode.COPY && dragboard.hasString()) {
                        txtDescription.setText(dragboard.getString());
                        event.setDropCompleted(true);
                    }
                    event.consume();
                }
        );

        //Add Drag Event to Transaction Debit text field
        txtDebit.addEventHandler(
                DragEvent.DRAG_OVER,
                event -> {
                    if(event.getDragboard().hasString()) {
                        event.acceptTransferModes(TransferMode.COPY);
                        txtDebit.requestFocus();
                    }
                    event.consume();
                });
        txtDebit.addEventHandler(
                DragEvent.DRAG_DROPPED,
                event -> {
                    Dragboard dragboard = event.getDragboard();
                    if(event.getTransferMode() == TransferMode.COPY && dragboard.hasString()) {
                        txtDebit.setText(dragboard.getString());
                        event.setDropCompleted(true);
                    }
                    event.consume();
                }
        );

        //Add Drag Event to Transaction Credit text field
        txtCredit.addEventHandler(
                DragEvent.DRAG_OVER,
                event -> {
                    if(event.getDragboard().hasString()) {
                        event.acceptTransferModes(TransferMode.COPY);
                        txtCredit.requestFocus();
                    }
                    event.consume();
                });
        txtCredit.addEventHandler(
                DragEvent.DRAG_DROPPED,
                event -> {
                    Dragboard dragboard = event.getDragboard();
                    if(event.getTransferMode() == TransferMode.COPY && dragboard.hasString()) {
                        txtCredit.setText(dragboard.getString());
                        event.setDropCompleted(true);
                    }
                    event.consume();
                }
        );
    }

    public void setTransactionList(ObservableList<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @FXML
    private void fillAccountsCombo() throws SQLException {
        ObservableList<String> accList = FXCollections.observableList(AccountList.getInstance().activateList().getAccountNameList());
        cmbAccount.getItems().clear();
        cmbAccount.setItems(accList);
    }

    @FXML
    protected void openFile() throws IOException {
        final Stage stage = new Stage();
        fileChooser.setTitle("Open CSV");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            txtAreaFilePath.setText(file.getPath());
            parseCSV(file.getPath());
        }
    }

    @FXML
    protected void importCSV(ActionEvent event) throws Exception {
        if (requiredFieldList.isComplete()) {
            File file = new File(txtAreaFilePath.getText());

            ObservableList<String> headerList = listHeaders.getItems();

            String transactionDate = txtTransactionDate.getText();
            String transactionDescription = txtDescription.getText();
            String transactionDebit = txtDebit.getText();
            String transactionCredit = txtCredit.getText();
            String transactionAccount = cmbAccount.getSelectionModel().getSelectedItem();

            ((Node) (event.getSource())).getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/transactions/transactions/importbudgetTransactions.fxml"));
            AnchorPane newWindow = loader.load();
            Stage stage = new Stage();
            stage.setTitle("ExpenseList");
            Scene scene = new Scene(newWindow);
            stage.setScene(scene);
            ImportBudgetTransactionsController it = loader.getController();
            it.setComboBoxList(headerList);
            it.setComboBoxes(transactionDate, transactionDescription, transactionDebit, transactionCredit, transactionAccount);
            it.importCSV(file.toString());
            it.filePath = file;
            it.tranHead = headerList;
            it.setTransactions(this.transactionList);
            stage.show();
        }
    }

    private void parseCSV(String filePath) throws FileNotFoundException {

        Scanner scanner = new Scanner(new File(filePath));
        scanner.useDelimiter(",");
        int i = 0;
        while (scanner.hasNext()) {

            //Get the first row of the csv and add the headers to the ListView
            if (i == 0) {
                List<String> headers = new ArrayList<>();

                //Get the filepath as a filepath and not as a string
                Path pathToFile = Paths.get(filePath);


                try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)) {
                    String line = br.readLine();
                    String[] headerStringArray = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                    headers = Arrays.asList(headerStringArray);

                    //Loop through and trim string to remove leading and trailing spaces
                    for(final ListIterator<String> listIterator = headers.listIterator(); listIterator.hasNext();) {
                        final String updateString = listIterator.next();
                        listIterator.set(updateString.trim());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Convert list to Observable list, remove ListView items and add list
                ObservableList<String> headersList = FXCollections.observableList(headers);
                listHeaders.getItems().removeAll();
                listHeaders.setItems(headersList);

            } else {
                break;
            }
            i++;
        }
    }

    @FXML
    protected void exitWindow() {
        Stage stage = (Stage)btnExit.getScene().getWindow();
        stage.close();
    }

    public void fileText(File file) {
        txtAreaFilePath.setText(file.toString());
    }

    public void setTextBoxes(String tranDate, String tranDesc, String tranDebit, String tranCredit, String tranAcc) {
        txtTransactionDate.setText(tranDate);
        txtDescription.setText(tranDesc);
        txtDebit.setText(tranDebit);
        txtCredit.setText(tranCredit);
        cmbAccount.setValue(tranAcc);
    }

    public void fillListBox(ObservableList<String> list) {
        listHeaders.getItems().clear();
        listHeaders.setItems(list);
    }
}
