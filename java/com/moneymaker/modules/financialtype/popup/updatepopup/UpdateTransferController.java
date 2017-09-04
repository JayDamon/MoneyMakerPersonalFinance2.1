package com.moneymaker.modules.financialtype.popup.updatepopup;

import com.moneymaker.modules.financialtype.Transfer;
import com.moneymaker.modules.financialtype.list.TransactionList;
import com.moneymaker.modules.financialtype.popup.SetTransferValueController;

/**
 * Created for MoneyMaker by Jay Damon on 10/20/2016.
 */
public class UpdateTransferController extends SetTransferValueController {
    private Transfer transfer;

    public void setItemToUpdate(Transfer t) {
        this.transfer = t;
        setControlValues();
    }

    private void setControlValues() {
        cmbTransferType.setValue(transfer.getTransferType());
        cmbFromAccount.setValue(transfer.getFromAccount());
        cmbToAccount.setValue(transfer.getToAccount());
        this.toTransaction = TransactionList.getInstance().activateList().getItem(transfer.getToTransactionID());
        this.fromTransaction = TransactionList.getInstance().activateList().getItem(transfer.getFromTransactionID());
    }

    @Override
    protected void confirmOperation() {

    }
}
