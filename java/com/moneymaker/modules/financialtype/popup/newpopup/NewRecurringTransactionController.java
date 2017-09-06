package com.moneymaker.modules.financialtype.popup.newpopup;

import com.moneymaker.modules.financialtype.RecurringTransaction;
import com.moneymaker.modules.financialtype.list.AccountList;
import com.moneymaker.modules.financialtype.list.BudgetList;
import com.moneymaker.modules.financialtype.list.RecurringTransactionList;
import com.moneymaker.modules.financialtype.popup.NewPopupController;
import com.moneymaker.utilities.SQLMethods;
import com.moneymaker.utilities.TransactionType;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Jay Damon on 9/16/2016.
 */
public class NewRecurringTransactionController extends NewPopupController<RecurringTransactionList> {

    @FXML
    ComboBox<String> cmbAccounts,cmbFrequency, cmbOccurrence, cmbType, cmbBudget;

    @FXML
    TextField txtName, txtAmount;

    @FXML
    DatePicker datePickerStartDate, datePickerEndDate;

    @Override
    protected ArrayList<ComboBox> comboBoxesForAutoComplete() {
        ArrayList<ComboBox> c = new ArrayList<>();
        c.add(cmbAccounts);
        c.add(cmbBudget);
        c.add(cmbFrequency);
        c.add(cmbOccurrence);
        c.add(cmbType);
        return c;
    }

    @Override
    protected void fillPrePopulatedControls() {
        fillComboBoxes();
    }

    private void fillComboBoxes() {
        cmbAccounts.getItems().clear();
        cmbAccounts.getItems().addAll(AccountList.getInstance().activateList().getAccountNameList());

        cmbBudget.getItems().clear();
        cmbBudget.getItems().addAll(BudgetList.getInstance().getActiveBudgets());

        cmbFrequency.getItems().clear();
        ArrayList<String> freqList = SQLMethods.listFrequency();
        if (freqList != null) cmbFrequency.getItems().addAll(freqList);

        cmbOccurrence.getItems().clear();
        ArrayList<String> occList = SQLMethods.listOccurrence();
        if (occList != null) cmbOccurrence.getItems().addAll(occList);

        cmbType.getItems().clear();
        cmbType.getItems().addAll(SQLMethods.listTranType());
    }

    @Override
    protected ArrayList<Control> requiredFieldList() {
        ArrayList<Control> c = new ArrayList<>();
        c.add(txtName);
        c.add(datePickerStartDate);
        c.add(cmbType);
        c.add(cmbAccounts);
        c.add(cmbFrequency);
        c.add(cmbOccurrence);
        c.add(txtAmount);
        return c;
    }

    @Override
    protected void confirmOperation() {
        String name = txtName.getText();
        BigDecimal amount = new BigDecimal(txtAmount.getText());
        String account = cmbAccounts.getSelectionModel().getSelectedItem();
        String frequency = cmbFrequency.getSelectionModel().getSelectedItem();
        String occurrence = cmbOccurrence.getSelectionModel().getSelectedItem();
        TransactionType type = TransactionType.getType(cmbType.getSelectionModel().getSelectedItem());
        Calendar startDate = datePickerToCalendar(datePickerStartDate);
        Calendar endDate = datePickerToCalendar(datePickerEndDate);
        String budget = cmbBudget.getSelectionModel().getSelectedItem();

        RecurringTransaction r = new RecurringTransaction(name, account, budget, frequency, occurrence, type, startDate, endDate, amount);
        r.getBehavior().addToDB();
        list.getList().add(r);
    }
}
