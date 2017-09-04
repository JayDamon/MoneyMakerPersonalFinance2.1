package com.moneymaker.modules.financialtype;

import com.moneymaker.modules.financialtype.behavior.BudgetBehavior;
import com.moneymaker.modules.financialtype.behavior.FinanceType;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Created for MoneyMaker by Jay Damon on 6/25/2016.
 */
public class Budget extends BudgetBean {

    public Budget(int id, String name, Calendar startDate, Calendar endDate,
                  String frequency, BigDecimal amount, boolean generic, boolean inUse) {
        super(FinanceType.BUDGET, id, name, startDate, endDate,
                frequency, amount, generic, inUse);
        setBehavior(new BudgetBehavior(this));
    }

    public Budget(String name) {
        super(FinanceType.BUDGET, name);
        setBehavior(new BudgetBehavior(this));
    }
}
