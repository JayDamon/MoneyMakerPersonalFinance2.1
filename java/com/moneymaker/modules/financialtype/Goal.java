package com.moneymaker.modules.financialtype;

import com.moneymaker.modules.financialtype.behavior.FinanceType;
import com.moneymaker.modules.financialtype.behavior.GoalBehavior;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Created by Jay Damon on 9/19/2016.
 */
public class Goal extends GoalBean {

    public Goal(int id, String name, int priority, String type, String account,
                Calendar startDate, Calendar endDate, BigDecimal amount,
                BigDecimal actualAmount) {
        super(FinanceType.GOAL, id, name, priority, type, account, startDate,
                endDate, amount, actualAmount);
        setBehavior(new GoalBehavior(this));
    }

    public Goal(String name, BigDecimal amount, int priority,
                String type, String account, Calendar startDate, Calendar endDate) {
        super(FinanceType.GOAL, name, amount, priority, type, account, startDate, endDate);
        setBehavior(new GoalBehavior(this));
    }

}
