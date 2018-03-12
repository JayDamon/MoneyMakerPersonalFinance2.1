package com.moneymaker.modules.financialtype;

import com.moneymaker.modules.financialtype.behavior.FinanceType;
import com.moneymaker.modules.financialtype.behavior.TransactionBehavior;
import com.moneymaker.utilities.DateUtility;
import com.moneymaker.utilities.FormatDollarAmount;
import com.moneymaker.utilities.TransactionType;
import javafx.beans.property.SimpleStringProperty;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Created by Jay Damon on 7/20/2017.
 */

public class Transaction extends Bean {

    private final SimpleStringProperty account = new SimpleStringProperty("");
    private final SimpleStringProperty budget = new SimpleStringProperty("");
    private final SimpleStringProperty category = new SimpleStringProperty("");
    private final SimpleStringProperty recurring = new SimpleStringProperty("");
    private final SimpleStringProperty date = new SimpleStringProperty("");
//    private final Simple date = new SimpleStringProperty("");
    private final SimpleStringProperty description = new SimpleStringProperty("");
    private final SimpleStringProperty amount = new SimpleStringProperty("");
    private final SimpleStringProperty timeStamp = new SimpleStringProperty("");
    private BigDecimal actualAmount;
    private Calendar calDate;
    private TransactionType transactionType;
    private Calendar calTimeStamp;

    public Transaction(int id, String account, String budget,
                       String category, String recurring,
                       Calendar date, String description,
                       BigDecimal amount, Calendar timeStamp) {
        super(FinanceType.TRANSACTION);
        setId(id);
        setAccount(account);
        setBudget(budget);
        setCategory(category);
        setRecurring(recurring);
        setDate(date);
        setDescription(description);
        setTransactionAmount(amount);
        setTimeStamp(timeStamp);
        setBehavior(new TransactionBehavior(this));
    }

    public Transaction(String account, String budget,
                       String category, Calendar date, String description,
                       BigDecimal amount) {
        super(FinanceType.TRANSACTION);
        setAccount(account);
        setBudget(budget);
        setCategory(category);
        setDate(date);
        setDescription(description);
        setTransactionAmount(amount);
        setBehavior(new TransactionBehavior(this));
    }

    public SimpleStringProperty accountProperty() {
        return account;
    }

    public SimpleStringProperty budgetProperty() {
        return budget;
    }

    public SimpleStringProperty categoryProperty() {
        return category;
    }

    public SimpleStringProperty recurringProperty() {
        return recurring;
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public String getAmount() {
        return amount.get();
    }

    public SimpleStringProperty amountProperty() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount.set(amount);
    }

    public SimpleStringProperty timeStampProperty() {
        return timeStamp;
    }

    public String getAccount() {
        return account.get();
    }

    public void setAccount(String tAcc) {
        if (tAcc != null) {
            accountProperty().set(tAcc);
        }
    }

    public String getBudget() {
        return budget.get();
    }

    public void setBudget(String tranBud) {
        budgetProperty().set(tranBud);
    }

    public String getCategory() {
        return category.get();
    }

    public void setCategory(String tranCat) {
        categoryProperty().set(tranCat);
    }

    public String getRecurring() {
        return recurring.get();
    }

    public void setRecurring(String recurring) {
        if (recurring != null) {
            recurringProperty().set(recurring);
        }
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(Calendar tDate) {
        this.calDate = tDate;
        dateProperty().set(DateUtility.getCalendarDisplayDate(tDate));
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String tDesc) {
        descriptionProperty().set(tDesc);
    }

    public String getTransactionAmount() {
        return amount.get();
    }

    private void setAmountDollarFormat(BigDecimal amount) {
        amountProperty().set(FormatDollarAmount.FormatAsDollarWithParenthesis(amount));
    }

    public Calendar getTimeStamp() {
        return calTimeStamp;
    }

    public void setTimeStamp(Calendar tTimeStamp) {
        this.calTimeStamp = tTimeStamp;
        if (tTimeStamp != null) {
            timeStampProperty().set(DateUtility.getCalendarDisplayDate(tTimeStamp));
        }
    }

    public void setTransactionAmount(BigDecimal amount) {
        setTransactionTypeFromAmount(amount);
        BigDecimal correctAmount = setAmountOperator(amount);
        this.actualAmount = correctAmount;
        setAmountDollarFormat(correctAmount);
    }

    private void setTransactionTypeFromAmount(BigDecimal amount) {
        if (transactionType == null) {
            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                setTransactionType(TransactionType.EXPENSE);
            } else {
                setTransactionType(TransactionType.INCOME);
            }
        } else if (transactionType == TransactionType.TRANSFER) {
            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                setTransactionType(TransactionType.EXPENSE_TRANSFER);
            } else {
                setTransactionType(TransactionType.INCOME_TRANSFER);
            }
        }
    }

    public BigDecimal getBigDecimalAmount() {
        return this.actualAmount;
    }

    public java.sql.Date getSqlDate() {
        return DateUtility.getSQLDate(this.calDate);
    }

    public Calendar getCalendar() {
        return this.calDate;
    }

    public void setTransactionType(TransactionType type) {
        this.transactionType = type;
    }

    public TransactionType getTransactionType() {
        return this.transactionType;
    }

    private BigDecimal setAmountOperator(BigDecimal amount) {
        int comparison = amount.compareTo(BigDecimal.ZERO);
        if (this.transactionType == TransactionType.EXPENSE || this.transactionType == TransactionType.EXPENSE_TRANSFER) {
            if (comparison < 0) {
                return amount;
            } else {
                return amount.multiply(BigDecimal.valueOf(-1));
            }
        } else {
            if (comparison >= 0) {
                return amount;
            } else {
                return amount.multiply(BigDecimal.valueOf(-1));
            }
        }
    }
}