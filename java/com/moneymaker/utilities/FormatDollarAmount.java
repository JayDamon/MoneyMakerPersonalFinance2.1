package com.moneymaker.utilities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * Created by Jay Damon on 9/17/2016.
 */
public class FormatDollarAmount {

    public static String FormatAsDollarWithParenthesis(BigDecimal amount) {
        amount = amount.setScale(2, RoundingMode.CEILING);
        NumberFormat fmt = NumberFormat.getCurrencyInstance();
        return fmt.format(amount);
    }

}
