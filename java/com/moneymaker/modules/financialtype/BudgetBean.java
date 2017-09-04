package com.moneymaker.modules.financialtype;

import com.moneymaker.modules.financialtype.behavior.FinanceType;
import com.moneymaker.modules.financialtype.list.TransactionList;
import com.moneymaker.utilities.DateUtility;
import com.moneymaker.utilities.FormatDollarAmount;
import com.moneymaker.utilities.TransactionType;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Created by Jay Damon on 8/17/2017.
 */
public class BudgetBean extends Bean {
    private final SimpleStringProperty name = new SimpleStringProperty("");
    private final SimpleStringProperty startDate = new SimpleStringProperty("");
    private final SimpleStringProperty endDate = new SimpleStringProperty("");
    private final SimpleStringProperty frequency = new SimpleStringProperty("");
    private final SimpleStringProperty amount = new SimpleStringProperty("");
    private final SimpleIntegerProperty uncategorizedTransactions = new SimpleIntegerProperty(0);
    private final SimpleBooleanProperty bGeneric = new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty bInUse = new SimpleBooleanProperty(false);
    private Calendar calStartDate, calEndDate;
    private BigDecimal bdAmount;

    public BudgetBean(FinanceType type, int id, String name, Calendar startDate, Calendar endDate,
                      String frequency, BigDecimal amount, boolean bGeneric, boolean bInUse) {
        super(type);
        setId(id);
        setName(name);
        setStartDate(startDate);
        setEndDate(endDate);
        setFrequency(frequency);
        setAmount(amount);
        setUncategorizedTransactions();
        setGeneric(bGeneric);
        setInUse(bInUse);
    }

    public BudgetBean(FinanceType t, String name) {
        super(t);
        setName(name);
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

    public String getStartDate() {
        return startDate.get();
    }

    public Calendar getCalStartDate() {
        return calStartDate;
    }

    public java.sql.Date getSQLStartDate() {
        return DateUtility.getSQLDate(calStartDate);
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
        return DateUtility.getSQLDate(calEndDate);
    }

    public SimpleStringProperty endDateProperty() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.calEndDate = endDate;
        endDateProperty().set(DateUtility.getCalendarDisplayDate(endDate));
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
        bdAmount = amount;
        if (amount != null) {
            amountProperty().set(FormatDollarAmount.FormatAsDollarWithParenthesis(amount));
        }
    }

    public int getUncategorizedTransactions() {
        return uncategorizedTransactions.get();
    }

    public SimpleIntegerProperty uncategorizedTransactionsProperty() {
        return uncategorizedTransactions;
    }

    public void setUncategorizedTransactions() {
        ObservableList<Transaction> l = TransactionList.getInstance().activateList().
                getUncateogrizedTransactions(this.getName());
        int n = 0;
        if (l != null) {
            n = l.size();
        }
        uncategorizedTransactionsProperty().set(n);
    }

    public boolean inUse() {
        return bInUse.get();
    }

    public SimpleBooleanProperty bInUseProperty() {
        return bInUse;
    }

    public void setInUse(boolean inUse) {
        bInUseProperty().set(inUse);
    }

    public boolean isGeneric() {
        return bGeneric.get();
    }

    public SimpleBooleanProperty bGenericProperty() {
        return bGeneric;
    }

    public void setGeneric(boolean generic) {
        bGenericProperty().set(generic);
    }
}
