package com.moneymaker.modules.financialtype.behavior;

import com.moneymaker.modules.financialtype.CashFlow;

/**
 * Created by Jay Damon on 8/25/2017.
 */
public class CashFlowBehavior extends FinancialTypeBehavior<CashFlow> {
    CashFlowBehavior(CashFlow cashFlow) {
        super(cashFlow);
    }

    @Override
    public void update() {

    }

    @Override
    public boolean addToDB() {
        return false;
    }
}
