package com.moneymaker.modules.financialtype.popup.updatepopup;

import com.moneymaker.modules.financialtype.Goal;
import com.moneymaker.modules.financialtype.list.AccountList;
import com.moneymaker.modules.financialtype.list.GoalList;
import com.moneymaker.modules.financialtype.list.GoalTypeList;
import com.moneymaker.modules.financialtype.popup.UpdatePopupController;
import com.moneymaker.utilities.ConnectionManager.ConnectionManagerUser;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Created by Jay Damon on 9/19/2016.
 */
public class UpdateGoalController extends UpdatePopupController<Goal> {

    @FXML
    ComboBox<String> cmbType, cmbAccount, cmbPriority;

    @FXML
    DatePicker datePickerStartDate, datePickerEndDate;

    @FXML
    TextField txtName, txtAmount;

    @Override
    protected ArrayList<ComboBox> comboBoxesForAutoComplete() {
        ArrayList<ComboBox> c = new ArrayList<>();
        c.add(cmbType);
        c.add(cmbPriority);
        c.add(cmbAccount);
        return c;
    }

    @Override
    protected void fillPrePopulatedControls() {
        fillComboBoxes();
    }

    @Override
    protected ArrayList<Control> requiredFieldList() {
        ArrayList<Control> c = new ArrayList<>();
        c.add(txtAmount);
        c.add(txtName);
        c.add(datePickerStartDate);
        c.add(cmbPriority);
        c.add(cmbAccount);
        c.add(cmbType);
        return c;
    }

    @Override
    public void setItemToUpdate(Goal g) {
        super.setItemToUpdate(g);
        fillDatePickers();
    }

    private void fillDatePickers() {
        Goal g = itemToUpdate;
        if(g.getCalEndDate() != null) addDateToDatePicker(g.getCalEndDate(), datePickerStartDate);
        if(g.getCalStartDate() != null) addDateToDatePicker(g.getCalStartDate(), datePickerEndDate);
    }

    public void setControlValues() {
        Goal g = itemToUpdate;
        txtName.setText(g.getName());
        cmbPriority.setValue(String.valueOf(g.getPriority()));
        cmbType.setValue(g.getType());
        cmbAccount.setValue(g.getAccount());
        txtAmount.setText(g.getBDAmount().setScale(2, RoundingMode.CEILING).toString());
    }

    private void fillComboBoxes() {
        int size = GoalList.getInstance().activateList().getList().size();
        for (int i = 1 ; i < size + 2 ; i++) {
            cmbPriority.getItems().add(String.valueOf(i));
        }
//        cmbPriority.getItems().addAll(SQLGoal.goalCount());
        cmbType.getItems().addAll(GoalTypeList.getInstance().activateList().getGoalTypes());
        cmbAccount.getItems().addAll(AccountList.getInstance().activateList().getAccountNameList());
    }

    @FXML
    protected void confirmOperation() {

        //Super class checks to see if the required fields are filled in before allowing this operation to be performed
        Goal g = this.itemToUpdate;
        GoalList list = GoalList.getInstance().activateList();
        ConnectionManagerUser.getInstance().suspendClose();
        int priority = Integer.parseInt(cmbPriority.getSelectionModel().getSelectedItem());

        if (g.getPriority() != priority) {
            list.updatePriorityList(priority, g.getPriority());
        }
        g.setName(txtName.getText());
        g.setAmount(new BigDecimal(txtAmount.getText()));
        g.setPriority(priority);
        g.setType(cmbType.getSelectionModel().getSelectedItem());
        g.setAccount(cmbAccount.getSelectionModel().getSelectedItem());
        g.setStartDate(datePickerToCalendar(datePickerStartDate));
        g.setEndDate(datePickerToCalendar(datePickerEndDate));

        itemToUpdate.getBehavior().update();
        ConnectionManagerUser.getInstance().activateClose();
        ConnectionManagerUser.getInstance().close();
    }


}
