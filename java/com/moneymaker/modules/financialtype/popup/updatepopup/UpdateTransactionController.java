package com.moneymaker.modules.financialtype.popup.updatepopup;

import com.moneymaker.modules.financialtype.Transaction;
import com.moneymaker.modules.financialtype.list.*;
import com.moneymaker.modules.financialtype.popup.UpdatePopupController;
import com.moneymaker.utilities.SQLMethods;
import com.moneymaker.utilities.TransactionType;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Created for MoneyMaker by Jay Damon on 9/18/2016.
 */
public class UpdateTransactionController extends UpdatePopupController<Transaction> {

    @FXML
    TextField txtDescription, txtAmount;

    @FXML
    ComboBox<String> cmbAccounts, cmbBudget, cmbType, cmbCategory, cmbRecurring;

    @FXML
    DatePicker datePickerStartDate;

    @Override
    protected ArrayList<ComboBox> comboBoxesForAutoComplete() {
        ArrayList<ComboBox> c = new ArrayList<>();
        c.add(cmbAccounts);
        c.add(cmbBudget);
        c.add(cmbCategory);
        c.add(cmbType);
        c.add(cmbRecurring);
        return c;
    }

    @Override
    public void fillPrePopulatedControls() {
        addBudgetComboActionListener();
        fillCategoryAccounts();
        fillBudgetCombo();
        fillTypeCombo();
        fillRecurringCombo();
    }

    private void addBudgetComboActionListener() {
        cmbBudget.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue) {
                if (cmbBudget.getSelectionModel().getSelectedIndex() != -1) {
                    cmbBudget.setStyle(FX_CONTROL_INNER_BACKGROUND_WHITE);

                    String item = cmbBudget.getSelectionModel().getSelectedItem();
                    addItemsToCombo(item);
                }
            }
        }));
    }

    private void checkValue() {
        if (!cmbCategory.getItems().contains(cmbCategory.getValue())) {
            cmbCategory.setValue("");
        }
    }

    private void fillCategoryAccounts() {
        cmbAccounts.getItems().clear();
        cmbAccounts.getItems().addAll(AccountList.getInstance().activateList().getAccountNameList());
    }

    private void fillBudgetCombo() {
        cmbBudget.getItems().clear();
        cmbBudget.getItems().addAll(BudgetList.getInstance().getActiveBudgets());
    }

    private void fillTypeCombo() {
        cmbType.getItems().clear();
        cmbType.getItems().addAll(SQLMethods.listTranType());
    }

    private void fillRecurringCombo() {
        cmbRecurring.getItems().clear();
        cmbRecurring.getItems().addAll(RecurringTransactionList.getInstance().activateList().getRecurringTransactionList());
        cmbRecurring.getItems().add("Not Recurring");
    }

    @Override
    protected ArrayList<Control> requiredFieldList() {
        ArrayList<Control> c = new ArrayList<>();
        c.add(txtDescription);
        c.add(cmbAccounts);
        c.add(cmbCategory);
        c.add(cmbType);
        c.add(datePickerStartDate);
        c.add(txtAmount);
        return c;
    }

    @Override
    protected void confirmOperation() {
        //Super class checks to see if the required fields are filled in before allowing this operation to be performed
        Transaction t = itemToUpdate;
        // Set fields of the Transaction Object
        t.setDescription(txtDescription.getText());
        t.setAccount(cmbAccounts.getSelectionModel().getSelectedItem());
        t.setBudget(cmbBudget.getSelectionModel().getSelectedItem());
        t.setCategory(cmbCategory.getSelectionModel().getSelectedItem());
        t.setRecurring(cmbRecurring.getSelectionModel().getSelectedItem());
        t.setTransactionType(TransactionType.getType(cmbType.getValue()));
        t.setDate(datePickerToCalendar(datePickerStartDate));
        t.setTransactionAmount(new BigDecimal(txtAmount.getText()));

        // Run updateItem of Behavior
        t.getBehavior().update();
    }

    @Override
    protected void setControlValues() {
        Transaction t = itemToUpdate;
        BigDecimal amount = t.getBigDecimalAmount();
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            amount = amount.multiply(BigDecimal.valueOf(-1));
        }
        txtAmount.setText(amount.setScale(2, RoundingMode.CEILING).toString());
        txtDescription.setText(t.getDescription());
        cmbAccounts.setValue(t.getAccount());
        cmbBudget.setValue(t.getBudget());
        cmbCategory.setValue(t.getCategory());
        cmbType.setValue(t.getTransactionType().toString());
        if (t.getRecurring() != null) {
            cmbRecurring.setValue(t.getRecurring());
        }
        fillCategoryCombo();
        addDateToDatePicker(t.getCalendar(), datePickerStartDate);
    }

    private void fillCategoryCombo() {
        String b = itemToUpdate.getBudget();
        addItemsToCombo(b);
    }

    private void addItemsToCombo(String item) {
        if (item.equals("Goal")) {
            cmbCategory.getItems().clear();
            cmbCategory.getItems().addAll(GoalList.getInstance().activateList().getGoalNameList());
            checkValue();
        } else {

            cmbCategory.getItems().clear();
            cmbCategory.getItems().addAll(TransactionCategoryList.getInstance().activateList().getTransactionCategories(item));
            checkValue();
        }
    }
}
