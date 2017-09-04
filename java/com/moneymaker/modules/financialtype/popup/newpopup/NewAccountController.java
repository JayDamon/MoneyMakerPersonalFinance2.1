package com.moneymaker.modules.financialtype.popup.newpopup;

import com.moneymaker.modules.financialtype.Account;
import com.moneymaker.modules.financialtype.list.AccountList;
import com.moneymaker.modules.financialtype.list.AccountTypeList;
import com.moneymaker.modules.financialtype.popup.NewPopupController;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created for MoneyMaker by jaynd on 2/22/2016.
 */
public class NewAccountController extends NewPopupController<AccountList> {

    @FXML
    private TextField accountNameField, startingBalanceField;
    @FXML
    private ComboBox<String> cmbAccType;
    @FXML
    private CheckBox checkBoxPrimaryAccount, checkBoxInCashFlow;

    @Override
    protected ArrayList<ComboBox> comboBoxesForAutoComplete() {
        ArrayList<ComboBox> c = new ArrayList<>();
        c.add(cmbAccType);
        return c;
    }

    @Override
    protected void fillPrePopulatedControls() {
        fillComboBoxes();
    }

    @Override
    protected ArrayList<Control> requiredFieldList() {
        ArrayList<Control> c = new ArrayList<>();
        c.add(cmbAccType);
        c.add(accountNameField);
        c.add(startingBalanceField);
        return c;
    }

    private void fillComboBoxes() {
        cmbAccType.getItems().clear();
        cmbAccType.getItems().addAll(AccountTypeList.getInstance().activateList().getList());
    }

    @Override
    protected void confirmOperation() {
        //Super class checks to see if the required fields are filled in before allowing this operation to be performed
        String name = accountNameField.getText();
        String type = cmbAccType.getValue();
        BigDecimal startingBalance = new BigDecimal(startingBalanceField.getText());
        boolean isPrimaryAccount = checkBoxPrimaryAccount.isSelected();
        boolean isInCashFlow = checkBoxInCashFlow.isSelected();

        Account a = new Account(name, type, startingBalance, isPrimaryAccount, isInCashFlow);
        a.getBehavior().addToDB();
        list.getList().add(a);
    }
}
