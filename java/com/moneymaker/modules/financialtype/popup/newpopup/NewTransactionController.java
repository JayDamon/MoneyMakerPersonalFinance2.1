package com.moneymaker.modules.financialtype.popup.newpopup;

import com.moneymaker.modules.financialtype.Transaction;
import com.moneymaker.modules.financialtype.list.AccountList;
import com.moneymaker.modules.financialtype.list.BudgetList;
import com.moneymaker.modules.financialtype.list.TransactionList;
import com.moneymaker.modules.financialtype.popup.NewPopupController;
import javafx.collections.FXCollections;
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
 * Created for MoneyMaker by Jay Damon on 2/6/2016.
 */
public class NewTransactionController extends NewPopupController<TransactionList> implements Initializable {

    @FXML
    private ComboBox<String> cmbAccount, cmbBudget;

    private final int transferType = 0;

    @FXML
    private DatePicker transactionDatePicker;

    @FXML
    private TextField transactionDescriptionField, transactionAmountField;

    @FXML
    private void listAccounts() {
        ObservableList<String> accountListString = FXCollections.observableList(AccountList.getInstance().activateList().getAccountNameList());
        cmbAccount.getItems().clear();
        cmbAccount.setItems(accountListString);
    }

    @FXML
    private void listBudgets() {
        ObservableList<String> budgetListString = FXCollections.observableList(BudgetList.getInstance().getActiveBudgets());
        cmbBudget.getItems().clear();
        cmbBudget.setItems(budgetListString);
    }

    public void addAccountToCombo(String accountName) {
        cmbAccount.setValue(accountName);
    }

    public void addBudgetToCombo(String budgetName) {
        cmbBudget.setValue(budgetName);
    }

    @Override
    protected void confirmOperation() {
        //Super class checks to see if the required fields are filled in before allowing this operation to be performed
        String account = cmbAccount.getSelectionModel().getSelectedItem();
        String budget = cmbBudget.getSelectionModel().getSelectedItem();
        Calendar date = datePickerToCalendar(transactionDatePicker);
        String description = transactionDescriptionField.getText();
        BigDecimal amount = new BigDecimal(transactionAmountField.getText());

        Transaction t = new Transaction(account, budget, "", date, description, amount);
        t.getBehavior().addToDB();
        list.getList().add(t);
    }

    @Override
    public void fillPrePopulatedControls() {
        listAccounts();
        listBudgets();
    }

    @Override
    protected ArrayList<Control> requiredFieldList() {
        ArrayList<Control> r = new ArrayList<>();
        r.add(cmbAccount);
        r.add(cmbBudget);
        r.add(transactionDatePicker);
        r.add(transactionDescriptionField);
        r.add(transactionAmountField);
        return r;
    }

    @Override
    protected ArrayList<ComboBox> comboBoxesForAutoComplete() {
        ArrayList<ComboBox> c = new ArrayList<>();
        c.add(cmbAccount);
        c.add(cmbBudget);
        return c;
    }

}


