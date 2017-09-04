package com.moneymaker.modules.financialtype;

import com.moneymaker.modules.financialtype.CashFlowBean;
import com.moneymaker.modules.financialtype.behavior.FinanceType;
import javafx.beans.property.SimpleStringProperty;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Created by Jay Damon on 9/20/2016.
 */
public class CashFlow extends CashFlowBean {

    public CashFlow(String account, String budget, Calendar date, String category, BigDecimal projected, BigDecimal actual, BigDecimal cohStarting, BigDecimal cohActual, BigDecimal cohCurrent) {
        super(FinanceType.CASHFLOW, account, budget, date, category, projected, actual, cohStarting, cohActual, cohCurrent);
    }

    public CashFlow(String budget, Calendar date, BigDecimal projected, BigDecimal actual) {
        super(FinanceType.CASHFLOW, budget, date, projected, actual);
    }
    public CashFlow(String account, String budget, Calendar date, String category, BigDecimal projected, BigDecimal actual) {
        super(FinanceType.CASHFLOW, account, budget, date, category, projected, actual);
    }

}
