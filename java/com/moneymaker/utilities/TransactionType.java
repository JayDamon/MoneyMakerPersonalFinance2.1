package com.moneymaker.utilities;

/**
 * Created by Jay Damon on 7/18/2017.
 */
public enum TransactionType {
    INCOME,
    EXPENSE,
    TRANSACTION,
    ACTUALRECURRING,
    TRANSFER,
    INCOMETRANSFER,
    EXPENSETRANSFER;

    public static TransactionType getType(String type) {
        switch (type.toLowerCase()) {
            case "income":
                return INCOME;
            case "expense":
                return EXPENSE;
            case "transaction":
                return TRANSACTION;
            case "actualrecurring":
                return ACTUALRECURRING;
            case "transfer":
                return TRANSFER;
            case "incometransfer":
                return INCOMETRANSFER;
            case "expensetransfer":
                return EXPENSETRANSFER;
            default:
                return null;
        }
    }

    public String toString() {
        String startString = super.toString().toLowerCase();
        return startString.substring(0, 1).toUpperCase() + startString.substring(1);
    }
}