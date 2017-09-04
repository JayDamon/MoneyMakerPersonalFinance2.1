package com.moneymaker.modules.financialtype;

import com.moneymaker.modules.financialtype.behavior.FinanceType;
import com.moneymaker.modules.financialtype.behavior.TransactionBehavior;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Created by Jay Damon on 7/20/2017.
 */

public class Transaction extends TransactionBean {

    public Transaction(int id, String transactionAccount, String transactionBudget,
                       String transactionCategory, String transactionRecurring,
                       Calendar transactionDate, String transactionDescription,
                       BigDecimal transactionAmount, Calendar transactionTimeStamp) {
        super(FinanceType.TRANSACTION, id, transactionAccount, transactionBudget,
                transactionCategory, transactionRecurring, transactionDate,
                transactionDescription, transactionAmount, transactionTimeStamp);
        setBehavior(new TransactionBehavior(this));
    }

    public Transaction(String transactionAccount, String transactionBudget,
                       String transactionCategory, Calendar transactionDate,
                       String transactionDescription, BigDecimal transactionAmount) {
        super(FinanceType.TRANSACTION, transactionAccount, transactionBudget,
                transactionCategory, transactionDate, transactionDescription,
                transactionAmount);
        setBehavior(new TransactionBehavior(this));
    }
}