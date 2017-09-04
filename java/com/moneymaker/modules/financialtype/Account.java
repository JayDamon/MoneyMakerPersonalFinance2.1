package com.moneymaker.modules.financialtype;

import com.moneymaker.modules.financialtype.behavior.AccountBehavior;
import com.moneymaker.modules.financialtype.behavior.FinanceType;

import java.math.BigDecimal;

/**
 * Created for Money Maker by Jay Damon on 2/23/2016.
 */
public class Account extends AccountBean {

    public Account(int id, String name, String type, BigDecimal startingBalance,
                   BigDecimal currentBalance, String classification, boolean isPrimaryAccount, boolean inCashFlow) {
        super(FinanceType.ACCOUNT, classification, id, name, type, startingBalance, currentBalance, isPrimaryAccount, inCashFlow);
        setBehavior(new AccountBehavior(this));
    }

    public Account(String name, String type, BigDecimal startingBalance,
                   boolean isPrimaryAccount, boolean inCashFlow) {
        super(FinanceType.ACCOUNT, name, type, startingBalance, isPrimaryAccount, inCashFlow);
        setBehavior(new AccountBehavior(this));
    }
}
