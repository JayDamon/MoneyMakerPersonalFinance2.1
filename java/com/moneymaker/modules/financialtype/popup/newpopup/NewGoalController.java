package com.moneymaker.modules.financialtype.popup.newpopup;

import com.moneymaker.modules.financialtype.Goal;
import com.moneymaker.modules.financialtype.list.AccountList;
import com.moneymaker.modules.financialtype.list.GoalList;
import com.moneymaker.modules.financialtype.list.GoalTypeList;
import com.moneymaker.modules.financialtype.popup.NewPopupController;
import com.moneymaker.utilities.ConnectionManager.ConnectionManagerUser;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Jay Damon on 9/19/2016.
 */
public class NewGoalController extends NewPopupController<GoalList> {

    @FXML
    Button btnAddGoal, btnExit;

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
    public void fillPrePopulatedControls() {
        fillComboBoxes();
    }

    @Override
    protected ArrayList<Control> requiredFieldList() {
        ArrayList<Control> c = new ArrayList<>();
        c.add(txtAmount);
        c.add(txtName);
        c.add(datePickerStartDate);
        c.add(cmbPriority);
        c.add(cmbType);
        c.add(cmbAccount);
        return c;
    }


    private void fillComboBoxes() {
        int size = GoalList.getInstance().activateList().getList().size();
        for (int i = 1 ; i < size + 2 ; i++) {
            cmbPriority.getItems().add(String.valueOf(i));
        }
        cmbType.getItems().addAll(GoalTypeList.getInstance().activateList().getGoalTypes());
        cmbAccount.getItems().addAll(AccountList.getInstance().activateList().getAccountNameList());
    }

    @FXML
    protected void confirmOperation() {
        //Super class checks to see if the required fields are filled in before allowing this operation to be performed
        ConnectionManagerUser.getInstance().suspendClose();
        String name = txtName.getText();
        BigDecimal amount = new BigDecimal(this.txtAmount.getText());
        int priority = Integer.parseInt(cmbPriority.getSelectionModel().getSelectedItem());
        String type = cmbType.getSelectionModel().getSelectedItem();
        String account = cmbAccount.getSelectionModel().getSelectedItem();
        Calendar startDate = this.datePickerToCalendar(datePickerStartDate);
        Calendar endDate = this.datePickerToCalendar(datePickerEndDate);
        GoalList.getInstance().activateList().addPriority(priority);
        Goal g = new Goal(name, amount, priority, type, account, startDate, endDate);
        g.getBehavior().addToDB();
        list.getList().add(g);
        ConnectionManagerUser.getInstance().activateClose();
        ConnectionManagerUser.getInstance().close();
    }

}
