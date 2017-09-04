package com.moneymaker.modules.financialtype.popup.updatepopup;

import com.moneymaker.modules.financialtype.Budget;
import com.moneymaker.modules.financialtype.popup.UpdatePopupController;
import com.moneymaker.utilities.SQLMethods;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;


/**
 * Created for MoneyMaker by jaynd on 6/27/2016.
 */
public class UpdateBudgetController extends UpdatePopupController<Budget> {

    @FXML private ComboBox<String> cmbFrequency;

    @FXML private TextField textFieldStartingBalance;

    @FXML private Label budgetType;

    @FXML private DatePicker datePickerStartDate, datePickerEndDate;

    @Override
    protected ArrayList<ComboBox> comboBoxesForAutoComplete() {
        ArrayList<ComboBox> c = new ArrayList<>();
        c.add(cmbFrequency);
        return c;
    }

    @Override
    public void fillPrePopulatedControls() {
        fillFrequency();
    }

    private void fillFrequency() {
        ObservableList<String> freqList = FXCollections.observableList(SQLMethods.listFrequency());
        cmbFrequency.getItems().clear();
        cmbFrequency.setItems(freqList);
    }

    @Override
    protected ArrayList<Control> requiredFieldList() {
        ArrayList<Control> c = new ArrayList<>();
        c.add(textFieldStartingBalance);
        c.add(datePickerStartDate);
        c.add(cmbFrequency);
        return c;
    }

    @Override
    protected void confirmOperation() {
        //Super class checks to see if the required fields are filled in before allowing this operation to be performed
        Budget b = itemToUpdate;
        // Set fields of the Transaction Object
        b.setFrequency(cmbFrequency.getSelectionModel().getSelectedItem());
        b.setAmount(new BigDecimal(textFieldStartingBalance.getText()));
        b.setStartDate(datePickerToCalendar(datePickerStartDate));
        b.setEndDate(datePickerToCalendar(datePickerEndDate));

        // Run updateItem of Behavior
        b.getBehavior().update();
    }

    @Override
    protected void setControlValues() {
        Budget b = itemToUpdate;
        budgetType.setText(b.getName());
        cmbFrequency.setValue(b.getFrequency());
        BigDecimal amount = b.getBdAmount();
        if (amount != null) {
            textFieldStartingBalance.setText(b.getBdAmount().setScale(2, RoundingMode.CEILING).toString());
        }
        addDateToDatePicker(b.getCalStartDate(), datePickerStartDate);
        addDateToDatePicker(b.getCalEndDate(), datePickerEndDate);
    }

}
