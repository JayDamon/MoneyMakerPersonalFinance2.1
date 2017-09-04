package com.moneymaker.modules.financialtype;

import com.moneymaker.modules.financialtype.behavior.FinanceType;
import com.moneymaker.utilities.DateUtility;
import com.moneymaker.utilities.FormatDollarAmount;
import com.moneymaker.utilities.TransactionType;
import javafx.beans.property.SimpleStringProperty;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;

/**
 * Created by Jay Damon on 8/24/2017.
 */
public class RecurringTransactionBean extends Bean {

    private final SimpleStringProperty name = new SimpleStringProperty("");
    private final SimpleStringProperty account = new SimpleStringProperty("");
    private final SimpleStringProperty budget = new SimpleStringProperty("");
    private final SimpleStringProperty frequency = new SimpleStringProperty("");
    private final SimpleStringProperty occurrence = new SimpleStringProperty("");
    private final SimpleStringProperty type = new SimpleStringProperty("");
    private final SimpleStringProperty startDate = new SimpleStringProperty("");
    private final SimpleStringProperty endDate = new SimpleStringProperty("");
    private final SimpleStringProperty amount = new SimpleStringProperty("");
    private TransactionType transactionType;
    private BigDecimal bdAmount;
    private Calendar calStartDate, calEndDate;

    public RecurringTransactionBean(FinanceType type, int id, String name,
                                    String account, String budget,
                                    String frequency, String occurrence,
                                    TransactionType recurringTransactionType, Calendar startDate,
                                    Calendar endDate, BigDecimal amount) {
        super(type);
        setId(id);
        setName(name);
        setAccount(account);
        setBudget(budget);
        setFrequency(frequency);
        setOccurrence(occurrence);
        setType(recurringTransactionType);
        setStartDate(startDate);
        setEndDate(endDate);
        setAmount(amount);
    }

    public RecurringTransactionBean(FinanceType type, String name,
                                    String account, String budget,
                                    String frequency, String occurrence,
                                    TransactionType recurringTransactionType, Calendar startDate,
                                    Calendar endDate, BigDecimal amount) {
        super(type);
        setName(name);
        setAccount(account);
        setBudget(budget);
        setFrequency(frequency);
        setOccurrence(occurrence);
        setType(recurringTransactionType);
        setStartDate(startDate);
        setEndDate(endDate);
        setAmount(amount);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        if (name != null) {
            nameProperty().set(name);
        } else nameProperty().set("");
    }

    public String getAccount() {
        return account.get();
    }

    public SimpleStringProperty accountProperty() {
        return account;
    }

    public void setAccount(String account) {
        accountProperty().set(account);
    }

    public String getBudget() {
        return budget.get();
    }

    public SimpleStringProperty budgetProperty() {
        return budget;
    }

    public void setBudget(String budget) {
        budgetProperty().set(budget);
    }

    public String getFrequency() {
        return frequency.get();
    }

    public SimpleStringProperty frequencyProperty() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        frequencyProperty().set(frequency);
    }

    public String getOccurrence() {
        return occurrence.get();
    }

    public SimpleStringProperty occurrenceProperty() {
        return occurrence;
    }

    public void setOccurrence(String occurrence) {
        occurrenceProperty().set(occurrence);
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(TransactionType type) {
        this.transactionType = type;
        typeProperty().set(type.toString());
    }

    public String getStartDate() {
        return startDate.get();
    }

    public Calendar getCalStartDate() {
        return calStartDate;
    }

    public java.sql.Date getSQLStartDate() {
        if (calStartDate != null) {
            return DateUtility.getSQLDate(calStartDate);
        } else return null;
    }

    public SimpleStringProperty startDateProperty() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.calStartDate = startDate;
        startDateProperty().set(DateUtility.getCalendarDisplayDate(startDate));
    }

    public String getEndDate() {
        return endDate.get();
    }

    public Calendar getCalEndDate() {
        return calEndDate;
    }

    public java.sql.Date getSQLEndDate() {
        if (calEndDate != null) {
            return DateUtility.getSQLDate(calEndDate);
        } return null;
    }

    public SimpleStringProperty endDateProperty() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.calEndDate = endDate;
        endDateProperty().set(DateUtility.getCalendarDisplayDate(endDate));
    }

    public String getAmount() {
        return amount.get();
    }

    public BigDecimal getBdAmount() {
        return bdAmount;
    }

    public SimpleStringProperty amountProperty() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.bdAmount = amount;
        amountProperty().set(FormatDollarAmount.FormatAsDollarWithParenthesis(amount));
    }
}
