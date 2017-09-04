package com.moneymaker.modules.financialtype;

import com.moneymaker.modules.financialtype.behavior.FinanceType;
import com.moneymaker.modules.financialtype.list.TransactionList;
import com.moneymaker.utilities.DateUtility;
import com.moneymaker.utilities.FormatDollarAmount;
import javafx.beans.property.SimpleStringProperty;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Objects;

/**
 * Created by Jay Damon on 8/25/2017.
 */
public class CashFlowBean extends Bean {
    private final SimpleStringProperty account = new SimpleStringProperty("");
    private final SimpleStringProperty budget = new SimpleStringProperty("");
    private final SimpleStringProperty date = new SimpleStringProperty("");
    private final SimpleStringProperty category = new SimpleStringProperty("");
    private final SimpleStringProperty projected = new SimpleStringProperty("");
    private final SimpleStringProperty actual = new SimpleStringProperty("");
    private final SimpleStringProperty cohStarting = new SimpleStringProperty("");
    private final SimpleStringProperty cohActual = new SimpleStringProperty("");
    private final SimpleStringProperty cohCurrent = new SimpleStringProperty("");
    private BigDecimal bdProjected, bdActual, bdCOHStarting, bdCOHActual, bdCOHCurrent;
    private Calendar calDate;

    public CashFlowBean(FinanceType type, String account, String budget, Calendar date, String category,
                    BigDecimal projected, BigDecimal actual, BigDecimal cohStarting,
                        BigDecimal cohActual, BigDecimal cohCurrent) {
        super(type);
        setAccount(account);
        setBudget(budget);
        setDate(date);
        setCategory(category);
        setProjected(projected);
        setActual(actual);
        setCohStarting(cohStarting);
        setCohActual(cohActual);
        setCohCurrent(cohCurrent);
    }

    public CashFlowBean(FinanceType type, String budget, Calendar date, BigDecimal projected, BigDecimal actual) {
        super(type);
        setBudget(budget);
        setDate(date);
        setProjected(projected);
        setActual(actual);
    }

    public CashFlowBean(FinanceType type, String account, String budget, Calendar date, String category, BigDecimal projected, BigDecimal actual) {
        super(type);
        setAccount(account);
        setBudget(budget);
        setDate(date);
        setCategory(category);
        setProjected(projected);
        setActual(actual);
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

    public String getDate() {
        return date.get();
    }

    public Calendar getCalDate() {
        return calDate;
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(Calendar date) {
        DateUtility.setCalBeginningOfDay(date);
        this.calDate = date;
        dateProperty().set(DateUtility.getCalendarDisplayDate(date));
    }

    public String getCategory() {
        return category.get();
    }

    public SimpleStringProperty categoryProperty() {
        return category;
    }

    public void setCategory(String category) {
        categoryProperty().set(category);
    }

    public String getProjected() {
        return projected.get();
    }

    public BigDecimal getBdProjected() {
        return bdProjected;
    }

    public SimpleStringProperty projectedProperty() {
        return projected;
    }

    public void setProjected(BigDecimal projected) {
        if (projected != null) {
            this.bdProjected = projected;
        } else this.bdProjected = BigDecimal.ZERO;
        projectedProperty().set(FormatDollarAmount.FormatAsDollarWithParenthesis(projected));
    }

    public String getActual() {
        return actual.get();
    }

    public BigDecimal getBdActual() {
        return bdActual;
    }

    public SimpleStringProperty actualProperty() {
        return actual;
    }

    public void setActual(BigDecimal actual) {
        if (actual != null) {
            this.bdActual = actual;
        } else this.bdActual = BigDecimal.ZERO;
        actualProperty().set(FormatDollarAmount.FormatAsDollarWithParenthesis(actual));
    }

    public String getCohStarting() {
        return cohStarting.get();
    }

    public BigDecimal getBdCOHStarting() {
        return bdCOHStarting;
    }

    public SimpleStringProperty cohStartingProperty() {
        return cohStarting;
    }

    public void setCohStarting(BigDecimal cohStarting) {
        if (cohStarting != null) {
            this.bdCOHStarting = cohStarting;
        } else this.bdCOHStarting = BigDecimal.ZERO;
        cohStartingProperty().set(FormatDollarAmount.FormatAsDollarWithParenthesis(cohStarting));
    }

    public String getCohActual() {
        return cohActual.get();
    }

    public BigDecimal getBdCOHActual() {
        return bdCOHActual;
    }

    public SimpleStringProperty cohActualProperty() {
        return cohActual;
    }

    public void setCohActual(BigDecimal cohActual) {
        if (cohActual != null) {
            this.bdCOHActual = cohActual;
        } else this.bdCOHActual = BigDecimal.ZERO;
        if (this.bdCOHActual.compareTo(BigDecimal.ZERO) == 0) {
            cohActualProperty().set("");
        } else {
            cohActualProperty().set(FormatDollarAmount.FormatAsDollarWithParenthesis(cohActual));
        }
    }

    public String getCohCurrent() {
        return cohCurrent.get();
    }

    public BigDecimal getBdCOHCurrent() {
        return bdCOHCurrent;
    }

    public SimpleStringProperty cohCurrentProperty() {
        return cohCurrent;
    }

    public void setCohCurrent(BigDecimal cohCurrent) {
        if (cohCurrent != null) {
            this.bdCOHCurrent = cohCurrent;
        } else this.bdCOHCurrent = BigDecimal.ZERO;
        if (this.bdCOHCurrent.compareTo(BigDecimal.ZERO) == 0) {
            cohCurrentProperty().set("");
        } else {
            cohCurrentProperty().set(FormatDollarAmount.FormatAsDollarWithParenthesis(cohCurrent));
        }
    }

    public void setActualSpending(Calendar endDate) {
        BigDecimal amount = BigDecimal.ZERO;
        TransactionList list = TransactionList.getInstance().activateList();
        for (Transaction t : list.getList()) {
            if (t.getCalendar().compareTo(endDate) <= 0) {
                if (getCategory().equals("") && getBudget().equals(t.getBudget())) {
                    amount = amount.add(t.getBigDecimalAmount());
                } else if (getCategory().equals(t.getRecurring())) {
                    amount = amount.add(t.getBigDecimalAmount());
                }
            }
        }
        setActual(amount);
    }
}
