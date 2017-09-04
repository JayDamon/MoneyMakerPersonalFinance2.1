package com.moneymaker.modules.financialtype;

import com.moneymaker.modules.financialtype.behavior.FinanceType;
import com.moneymaker.modules.financialtype.behavior.TransferBehavior;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Created by Jay Damon on 8/20/2017.
 */
public class Transfer extends TransferBean {
    public Transfer(int id, Calendar date, String transferType, String fromAccount,
                    String toAccount, BigDecimal amount, int fromTransactionID,
                    int toTransactionID) {
        super(FinanceType.TRANSFER, id, date, transferType, fromAccount, toAccount,
                amount, fromTransactionID, toTransactionID);
        setBehavior(new TransferBehavior(this));
    }
    public Transfer(Calendar date, String transferType, String fromAccount, String toAccount, BigDecimal amount,
                    int fromTransactionID, int toTransactionID) {
        super(FinanceType.TRANSFER, date, transferType, fromAccount, toAccount, amount, fromTransactionID, toTransactionID);
        setBehavior(new TransferBehavior(this));
    }
}
