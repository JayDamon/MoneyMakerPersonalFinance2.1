package com.moneymaker.modules.financialtype;

import com.moneymaker.modules.financialtype.behavior.FinanceType;
import com.moneymaker.modules.financialtype.behavior.RecurringTransactionBehavior;
import com.moneymaker.utilities.TransactionType;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Created by Jay Damon on 9/13/2016.
 */
public class RecurringTransaction extends RecurringTransactionBean {

    public RecurringTransaction(int id, String name, String account, String budget, String frequency, String occurrence, TransactionType type, Calendar startDate, Calendar endDate, BigDecimal amount) {
        super(FinanceType.RECURRING_TRANSACTION, id, name, account, budget, frequency, occurrence, type, startDate, endDate, amount);
        setBehavior(new RecurringTransactionBehavior(this));
    }

    public RecurringTransaction(String name, String account, String budget, String frequency, String occurrence, TransactionType type, Calendar startDate, Calendar endDate, BigDecimal amount) {
        super(FinanceType.RECURRING_TRANSACTION, name, account, budget, frequency, occurrence, type, startDate, endDate, amount);
        setBehavior(new RecurringTransactionBehavior(this));
    }
}
