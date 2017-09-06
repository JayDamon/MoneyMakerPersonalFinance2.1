package com.moneymaker.modules.financialtype.popup.updatepopup;

import com.moneymaker.modules.financialtype.RecurringTransaction;
import com.moneymaker.modules.financialtype.list.AccountList;
import com.moneymaker.modules.financialtype.list.BudgetList;
import com.moneymaker.modules.financialtype.popup.UpdatePopupController;
import com.moneymaker.utilities.SQLMethods;
import com.moneymaker.utilities.TransactionType;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Jay Damon on 9/18/2016.
 */
public class UpdateRecurringTransactionController extends UpdatePopupController<RecurringTransaction> {

    @FXML
    TextField txtName, txtAmount;

    @FXML
    ComboBox<String> cmbAccounts, cmbBudget, cmbFrequency, cmbOccurrence, cmbType;

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

    @Override
    public void setItemToUpdate(RecurringTransaction r) {
        super.setItemToUpdate(r);
        setControlValues();
        setDatePickerValues();
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
        String account = cmbAccounts.getSelectionModel().getSelectedItem();
        String budget = cmbBudget.getSelectionModel().getSelectedItem();
        String frequency = cmbFrequency.getSelectionModel().getSelectedItem();
        String occurrence = cmbOccurrence.getSelectionModel().getSelectedItem();
        TransactionType type = TransactionType.getType(cmbType.getSelectionModel().getSelectedItem());
        Calendar startDate = datePickerToCalendar(datePickerStartDate);
        Calendar endDate = datePickerToCalendar(datePickerEndDate);
        BigDecimal amount = new BigDecimal(txtAmount.getText());

        RecurringTransaction r = itemToUpdate;
        r.setName(name);
        r.setAccount(account);
        r.setBudget(budget);
        r.setFrequency(frequency);
        r.setOccurrence(occurrence);
        r.setType(type);
        r.setStartDate(startDate);
        r.setEndDate(endDate);
        r.setAmount(amount);

        r.getBehavior().update();
    }

    private void setDatePickerValues() {
        Calendar endDate = itemToUpdate.getCalEndDate();
        if(endDate != null) {
            int selectedEndYear = endDate.get(Calendar.YEAR);
            int selectedEndMonth = endDate.get(Calendar.MONTH) + 1;
            int selectedEndDay = endDate.get(Calendar.DAY_OF_MONTH);

            datePickerEndDate.setValue(LocalDate.of(selectedEndYear, selectedEndMonth, selectedEndDay));
        }

        Calendar startDate = itemToUpdate.getCalStartDate();
        if(startDate != null) {
            int selectedStartYear = startDate.get(Calendar.YEAR);
            int selectedStartMonth = startDate.get(Calendar.MONTH) + 1;
            int selectedStartDay = startDate.get(Calendar.DAY_OF_MONTH);
            datePickerStartDate.setValue(LocalDate.of(selectedStartYear, selectedStartMonth, selectedStartDay));
        }
    }

    @Override
    protected void setControlValues() {
        txtName.setText(this.itemToUpdate.getName());
        cmbAccounts.setValue(this.itemToUpdate.getAccount());
        cmbBudget.setValue(this.itemToUpdate.getBudget());
        cmbFrequency.setValue(this.itemToUpdate.getFrequency());
        cmbOccurrence.setValue(this.itemToUpdate.getOccurrence());
        cmbType.setValue(this.itemToUpdate.getType());
        txtAmount.setText(this.itemToUpdate.getBdAmount().setScale(2, RoundingMode.CEILING).toString());
    }
}
