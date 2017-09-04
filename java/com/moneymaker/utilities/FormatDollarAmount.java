package com.moneymaker.utilities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * Created by Jay Damon on 9/17/2016.
 */
public class FormatDollarAmount {

    public static String CleanDollarAmountsForSQL(String amount) {

        if (amount.contains("$")) {
            amount = amount.replace("$","");
        }
        if (amount.startsWith("(")) {
            amount = amount.replace("(","-");
            amount = amount.replace(")","");
        }
        if (amount.contains(",")) {
            amount = amount.replace(",","");
        }

        return amount;
    }

    public static String FormatAsDollarWithParenthesis(BigDecimal amount) {
        amount = amount.setScale(2, RoundingMode.CEILING);
        NumberFormat fmt = NumberFormat.getCurrencyInstance();
        return fmt.format(amount);
    }

    public static String FormatAsDollarWithParenthesis(String amount, TransactionType type) {
        BigDecimal bigAmount = new BigDecimal(amount).setScale(2, RoundingMode.CEILING);
//        if (type == TransactionType.EXPENSE) {
//            bigAmount = bigAmount.multiply(BigDecimal.valueOf(-1));
//        }
        NumberFormat fmt = NumberFormat.getCurrencyInstance();
        return fmt.format(bigAmount);
    }

}
