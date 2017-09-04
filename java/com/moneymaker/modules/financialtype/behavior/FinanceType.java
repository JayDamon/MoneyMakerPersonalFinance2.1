package com.moneymaker.modules.financialtype.behavior;

/**
 * Created by Jay Damon on 7/22/2017.
 */
public enum FinanceType {
    TRANSACTION,
    ACCOUNT,
    BUDGET,
    CASHFLOW,
    GOAL,
    TRANSFER,
    RECURRINGTRANSACTION;
    public String getTableName() {
        switch (this) {
            case TRANSACTION:
                return "transactions";
            case ACCOUNT:
                return "accounts";
            case BUDGET:
                return "budget";
            case CASHFLOW:
                return "";
            case GOAL:
                return "goals";
            case TRANSFER:
                return "transfers";
            case RECURRINGTRANSACTION:
                return "recurring_transactions";
            default:
                return null;
        }
    }

    public String specialBehavior() {
        switch (this) {
            case TRANSACTION:
                return "";
            case ACCOUNT:
                return "";
            case BUDGET:
                return "";
            case CASHFLOW:
                return "";
            case GOAL:
                return "";
            case TRANSFER: //Needed for "BudgetWindowController" and now more
                return "Transfer/Payment";
            case RECURRINGTRANSACTION:
                return "";
            default:
                return null;
        }
    }
}
