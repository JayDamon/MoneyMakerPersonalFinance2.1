package com.moneymaker.modules.financialtype.popup.newpopup;

import com.moneymaker.modules.financialtype.Transfer;
import com.moneymaker.modules.financialtype.list.TransferList;
import com.moneymaker.modules.financialtype.popup.SetTransferValueController;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Created by Jay Damon on 8/22/2017.
 */

public class NewTransferController extends SetTransferValueController {
    private TransferList list = TransferList.getInstance().activateList();
    @Override
    protected void confirmOperation() {
        //Super class checks to see if the required fields are filled in before allowing this operation to be performed
        Calendar date = this.fromTransaction.getCalendar();
        String transferType = cmbTransferType.getSelectionModel().getSelectedItem();
        String fromAccount = cmbTransferType.getSelectionModel().getSelectedItem();
        String toAccount = cmbTransferType.getSelectionModel().getSelectedItem();
        BigDecimal amount =  fromTransaction.getBigDecimalAmount();
        int fromID = fromTransaction.getID();
        int toID = toTransaction.getID();
        Transfer t = new Transfer(date, transferType, fromAccount, toAccount, amount, fromID, toID);
        t.getBehavior().addToDB();
        list.getList().add(t);
        updateTransaction(transferType, this.fromTransaction);
        updateTransaction(transferType, this.toTransaction);
    }
}
