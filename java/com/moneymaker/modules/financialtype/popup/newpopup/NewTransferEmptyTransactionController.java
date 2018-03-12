package com.moneymaker.modules.financialtype.popup.newpopup;

import com.moneymaker.modules.financialtype.Transaction;
import com.moneymaker.modules.financialtype.Transfer;
import com.moneymaker.modules.financialtype.behavior.FinanceType;
import com.moneymaker.modules.financialtype.list.AccountList;
import com.moneymaker.modules.financialtype.list.TransactionCategoryList;
import com.moneymaker.modules.financialtype.list.TransactionList;
import com.moneymaker.modules.financialtype.list.TransferList;
import com.moneymaker.utilities.gui.PopupController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created for MoneyMaker by Jay Damon on 10/22/2016.
 */
public class NewTransferEmptyTransactionController extends PopupController {

    @FXML
    private final ComboBox<String> comboBoxFromAccount = new ComboBox<>();
    private final ComboBox<String> comboBoxToAccount = new ComboBox<>();
    private final ComboBox<String> comboBoxTransferType = new ComboBox<>();

    @FXML
    private final DatePicker datePickerTransferDate = new DatePicker();

    @FXML
    private final TextField textFieldTransferAmount = new TextField();

    private final TransferList list = TransferList.getInstance().activateList();

    @Override
    protected ArrayList<ComboBox> comboBoxesForAutoComplete() {
        ArrayList<ComboBox> c = new ArrayList<>();
        c.add(comboBoxTransferType);
        c.add(comboBoxFromAccount);
        c.add(comboBoxToAccount);
        return c;
    }

    @Override
    public void fillPrePopulatedControls() {
        fillComboBoxes();
        buttonExit.setOnAction(event -> exitWindow());
    }

    @Override
    protected ArrayList<Control> requiredFieldList() {
        ArrayList<Control> c = new ArrayList<>();
        c.add(comboBoxTransferType);
        c.add(comboBoxFromAccount);
        c.add(comboBoxToAccount);
        c.add(datePickerTransferDate);
        c.add(textFieldTransferAmount);
        return c;
    }

    private void fillComboBoxes() {
        comboBoxTransferType.getItems().clear();
        comboBoxTransferType.getItems().addAll(TransactionCategoryList.getInstance().activateList().getTransactionCategories(FinanceType.TRANSFER.specialBehavior()));
        comboBoxToAccount.getItems().clear();
        comboBoxToAccount.setItems(AccountList.getInstance().activateList().getAccountNameList());
        comboBoxFromAccount.getItems().clear();
        comboBoxFromAccount.setItems(AccountList.getInstance().activateList().getAccountNameList());    }

    @FXML
    protected void confirmOperation() {
        //Super class checks to see if the required fields are filled in before allowing this operation to be performed
        String transferType = comboBoxTransferType.getSelectionModel().getSelectedItem();
        String fromAccount = comboBoxFromAccount.getSelectionModel().getSelectedItem();
        String toAccount = comboBoxToAccount.getSelectionModel().getSelectedItem();
        Calendar transferDate = datePickerToCalendar(datePickerTransferDate);
        String transferFromDescription = "Transfer From " + fromAccount;
        String transferToDescription = "Transfer To " + toAccount;
        BigDecimal transferAmount = new BigDecimal(textFieldTransferAmount.getText());

        Transaction fromTransaction = new Transaction(fromAccount, FinanceType.TRANSFER.specialBehavior(), transferType, transferDate,
                transferFromDescription, transferAmount);
        Transaction toTransaction = new Transaction(toAccount, FinanceType.TRANSFER.specialBehavior(), transferType, transferDate,
                transferToDescription, transferAmount.multiply(BigDecimal.valueOf(-1)));
        toTransaction.getBehavior().addToDB();
        fromTransaction.getBehavior().addToDB();

        ObservableList<Transaction> list = TransactionList.getInstance().activateList().getList();
        list.add(toTransaction);
        list.add(fromTransaction);

        int fromTransactionID = fromTransaction.getID();
        int toTransactionID = toTransaction.getID();

        Transfer t = new Transfer(datePickerToCalendar(datePickerTransferDate), transferType, fromAccount, toAccount, transferAmount, fromTransactionID, toTransactionID);
        t.getBehavior().addToDB();
        this.list.getList().add(t);
    }
}
