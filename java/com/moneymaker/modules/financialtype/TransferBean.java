package com.moneymaker.modules.financialtype;

import com.moneymaker.modules.financialtype.behavior.FinanceType;
import com.moneymaker.utilities.DateUtility;
import com.moneymaker.utilities.FormatDollarAmount;
import com.moneymaker.utilities.TransactionType;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Created for MoneyMaker by Jay Damon on 10/20/2016.
 */
public class TransferBean extends Bean {
    private final SimpleStringProperty date = new SimpleStringProperty("");
    private final SimpleStringProperty transferType = new SimpleStringProperty("");
    private final SimpleStringProperty fromAccount = new SimpleStringProperty("");
    private final SimpleStringProperty toAccount = new SimpleStringProperty("");
    private final SimpleStringProperty amount = new SimpleStringProperty("");
    private final SimpleIntegerProperty fromTransactionID = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty toTransactionID = new SimpleIntegerProperty(0);
    private Calendar calDate;
    private BigDecimal bdAmount;

    public TransferBean(FinanceType type, int id, Calendar date, String transferType, String fromAccount,
                        String toAccount, BigDecimal amount, int fromTransactionID,
                        int toTransactionID) {
        super(type);
        setId(id);
        setDate(date);
        setTransferType(transferType);
        setFromAccount(fromAccount);
        setToAccount(toAccount);
        setAmount(amount);
        setFromTransactionID(fromTransactionID);
        setToTransactionID(toTransactionID);
    }

    public TransferBean(FinanceType type, Calendar date, String transferType, String fromAccount, String toAccount, BigDecimal amount,
                        int fromTransactionID, int toTransactionID) {
        super(type);
        setDate(date);
        setTransferType(transferType);
        setFromAccount(fromAccount);
        setToAccount(toAccount);
        setAmount(amount);
        setFromTransactionID(fromTransactionID);
        setToTransactionID(toTransactionID);
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(Calendar date) {
        this.calDate = date;
        dateProperty().set(DateUtility.getCalendarDisplayDate(date));
    }

    public Calendar getCalDate() {
        return this.calDate;
    }

    public java.sql.Date getSqlDate() {
        return DateUtility.getSQLDate(this.calDate);
    }

    public String getTransferType() {
        return transferType.get();
    }

    public SimpleStringProperty transferTypeProperty() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        transferTypeProperty().set(transferType);
    }

    public String getFromAccount() {
        return fromAccount.get();
    }

    public SimpleStringProperty fromAccountProperty() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        fromAccountProperty().set(fromAccount);
    }

    public String getToAccount() {
        return toAccount.get();
    }

    public SimpleStringProperty toAccountProperty() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        toAccountProperty().set(toAccount);
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

    public int getFromTransactionID() {
        return fromTransactionID.get();
    }

    public SimpleIntegerProperty fromTransactionIDProperty() {
        return fromTransactionID;
    }

    public void setFromTransactionID(int fromTransactionID) {
        fromTransactionIDProperty().set(fromTransactionID);
    }

    public int getToTransactionID() {
        return toTransactionID.get();
    }

    public SimpleIntegerProperty toTransactionIDProperty() {
        return toTransactionID;
    }

    public void setToTransactionID(int toTransactionID) {
        toTransactionIDProperty().set(toTransactionID);
    }
}
