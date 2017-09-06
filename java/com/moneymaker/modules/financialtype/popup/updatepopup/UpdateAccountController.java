package com.moneymaker.modules.financialtype.popup.updatepopup;

import com.moneymaker.modules.financialtype.Account;
import com.moneymaker.modules.financialtype.list.AccountTypeList;
import com.moneymaker.modules.financialtype.popup.UpdatePopupController;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Created for MoneyMaker by Jay Damon on 2/22/2016.
 */
public class UpdateAccountController extends UpdatePopupController<Account> {

    @FXML
    private TextField textFieldAccountName, textFieldStartingBalance;
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
        cmbAccType.getItems().clear();
        cmbAccType.getItems().addAll(AccountTypeList.getInstance().activateList().getList());
    }

    @Override
    protected ArrayList<Control> requiredFieldList() {
        ArrayList<Control> c = new ArrayList<>();
        c.add(cmbAccType);
        c.add(textFieldAccountName);
        c.add(textFieldStartingBalance);
        return c;
    }

    @Override
    protected void confirmOperation() {
        //Super class checks to see if the required fields are filled in before allowing this operation to be performed
        String name = textFieldAccountName.getText();
        String type = cmbAccType.getValue();
        BigDecimal startingBalance = new BigDecimal(textFieldStartingBalance.getText());
        boolean isPrimaryAccount = checkBoxPrimaryAccount.isSelected();
        boolean isInCashFlow = checkBoxInCashFlow.isSelected();

        Account a = itemToUpdate;
        a.setType(type);
        a.setName(name);
        a.setStartingBalance(startingBalance);
        a.setIsPrimaryAccount(isPrimaryAccount);
        a.setInCashFlow(isInCashFlow);

        a.getBehavior().update();
    }

    @Override
    protected void setControlValues() {
        Account a = itemToUpdate;
        cmbAccType.setValue(a.getType());
        textFieldAccountName.setText(a.getName());
        textFieldStartingBalance.setText(a.getBdStartingBalance().setScale(2, RoundingMode.CEILING).toString());
        checkBoxPrimaryAccount.setSelected(a.isPrimaryAccount());
        checkBoxInCashFlow.setSelected(a.isInCashFlow());
    }
}
