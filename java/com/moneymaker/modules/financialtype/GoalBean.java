package com.moneymaker.modules.financialtype;

import com.moneymaker.modules.financialtype.behavior.FinanceType;
import com.moneymaker.utilities.DateUtility;
import com.moneymaker.utilities.FormatDollarAmount;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Created by Jay Damon on 8/2/2017.
 */
public abstract class GoalBean extends Bean {
    private final SimpleStringProperty name = new SimpleStringProperty("");
    private final SimpleIntegerProperty priority = new SimpleIntegerProperty(0);
    private final SimpleStringProperty type = new SimpleStringProperty("");
    private final SimpleStringProperty account = new SimpleStringProperty("");
    private final SimpleStringProperty startDate = new SimpleStringProperty("");
    private final SimpleStringProperty endDate = new SimpleStringProperty("");
    private final SimpleStringProperty amount = new SimpleStringProperty("");
    private final SimpleStringProperty actualAmount = new SimpleStringProperty("");
    private Calendar calStartDate, calEndDate;
    private BigDecimal bdActualAmount, bdAmount;

    GoalBean(FinanceType t, int id, String name, int priority, String type,
             String account, Calendar startDate, Calendar endDate,
             BigDecimal amount, BigDecimal actualAmount) {
        super(t);
        setId(id);
        setName(name);
        setPriority(priority);
        setType(type);
        setAccount(account);
        setStartDate(startDate);
        setEndDate(endDate);
        setAmount(amount);
        setActualAmount(actualAmount);
    }

    GoalBean(FinanceType t, String name, BigDecimal amount, int priority,
             String type, String account, Calendar startDate, Calendar endDate) {
        super(t);
        setName(name);
        setAmount(amount);
        setPriority(priority);
        setType(type);
        setAccount(account);
        setStartDate(startDate);
        setEndDate(endDate);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        nameProperty().set(name);
    }

    public int getPriority() {
        return priority.get();
    }

    public SimpleIntegerProperty priorityProperty() {
        return priority;
    }

    public void setPriority(int priority) {
        priorityProperty().set(priority);
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        typeProperty().set(type);
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

    public String getStartDate() {
        return startDate.get();
    }

    private SimpleStringProperty startDateProperty() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.calStartDate = startDate;
        startDateProperty().set(DateUtility.getCalendarDisplayDate(startDate));
    }

    public String getEndDate() {
        return endDate.get();
    }

    public SimpleStringProperty endDateProperty() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.calEndDate = endDate;
        endDateProperty().set(DateUtility.getCalendarDisplayDate(endDate));
    }

    public BigDecimal getBDAmount() {
        return bdAmount;
    }

    public SimpleStringProperty amountProperty() {
        return amount;
    }

    public String getAmount() {
        return this.amount.get();
    }

    public void setAmount(BigDecimal amount) {
        amountProperty().set(FormatDollarAmount.FormatAsDollarWithParenthesis(amount));
        this.bdAmount = amount;
    }

    public BigDecimal getBDActualAmount() {
        return this.bdActualAmount;
    }

    public SimpleStringProperty actualAmountProperty() {
        return actualAmount;
    }

    public String getActualAmount() {
        return this.actualAmount.get();
    }

    public void setActualAmount(BigDecimal actualAmount) {
        if (actualAmount == null) {
            actualAmount = BigDecimal.ZERO;
        }
        if (actualAmount.compareTo(BigDecimal.ZERO) < 0) {
            actualAmount = actualAmount.multiply(BigDecimal.valueOf(-1));
        }
        actualAmountProperty().set(FormatDollarAmount.FormatAsDollarWithParenthesis(actualAmount));
        this.bdActualAmount = actualAmount;
    }

    public Calendar getCalStartDate() {
        return calStartDate;
    }

    public Calendar getCalEndDate() {
        return calEndDate;
    }

    public java.sql.Date getSQLStartDate() {
        return DateUtility.getSQLDate(calStartDate);
    }

    public java.sql.Date getSQLEndDate() {
        return DateUtility.getSQLDate(calEndDate);
    }

}
