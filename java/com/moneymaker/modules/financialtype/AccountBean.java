package com.moneymaker.modules.financialtype;

import com.moneymaker.modules.financialtype.Bean;
import com.moneymaker.modules.financialtype.behavior.FinanceType;
import com.moneymaker.utilities.FormatDollarAmount;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import java.math.BigDecimal;

/**
 * Created by Jay Damon on 8/24/2017.
 */
public class AccountBean extends Bean {
    private final SimpleStringProperty name = new SimpleStringProperty("");
    private final SimpleStringProperty type = new SimpleStringProperty("");
    private final SimpleStringProperty startingBalance = new SimpleStringProperty("");
    private final SimpleStringProperty currentBalance = new SimpleStringProperty("");
    private final SimpleStringProperty classification = new SimpleStringProperty("");
    private final SimpleBooleanProperty isPrimaryAccount = new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty inCashFlow = new SimpleBooleanProperty(false);
    private BigDecimal bdStartingBalance, bdCurrentBalance;

    public AccountBean(FinanceType type, String classification, int id, String name, String accountType,
                       BigDecimal startingBalance, BigDecimal currentBalance,
                   boolean isPrimaryAccount, boolean inCashFlow) {
        super(type);
        setClassification(classification);
        setId(id);
        setName(name);
        setType(accountType);
        setStartingBalance(startingBalance);
        setCurrentBalance(currentBalance);
        setIsPrimaryAccount(isPrimaryAccount);
        setInCashFlow(inCashFlow);
    }

    public AccountBean(FinanceType type, String name, String accountType,
                       BigDecimal startingBalance, boolean isPrimaryAccount, boolean inCashFlow) {
        super(type);
        setName(name);
        setType(accountType);
        setStartingBalance(startingBalance);
        setIsPrimaryAccount(isPrimaryAccount);
        setInCashFlow(inCashFlow);
    }

    public String getClassification() {
        return classification.get();
    }

    public SimpleStringProperty classificationProperty() {
        return classification;
    }

    public void setClassification(String classification) {
        classificationProperty().set(classification);
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

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        typeProperty().set(type);
    }

    public String getStartingBalance() {
        return startingBalance.get();
    }

    public BigDecimal getBdStartingBalance() {
        return bdStartingBalance;
    }

    public SimpleStringProperty startingBalanceProperty() {
        return startingBalance;
    }

    public void setStartingBalance(BigDecimal startingBalance) {
        if (startingBalance == null) {
            startingBalance = BigDecimal.ZERO;
        }
        startingBalanceProperty().set(FormatDollarAmount.FormatAsDollarWithParenthesis(startingBalance));
        this.bdStartingBalance = startingBalance;
    }

    public String getCurrentBalance() {
        return currentBalance.get();
    }

    public BigDecimal getBdCurrentBalance() {
        return bdCurrentBalance;
    }

    public SimpleStringProperty currentBalanceProperty() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        if (currentBalance == null) {
            currentBalance = BigDecimal.ZERO;
        }
        currentBalanceProperty().set(FormatDollarAmount.FormatAsDollarWithParenthesis(currentBalance));
        this.bdCurrentBalance = currentBalance;
    }

    public boolean isPrimaryAccount() {
        return isPrimaryAccount.get();
    }

    public SimpleBooleanProperty isPrimaryAccountProperty() {
        return isPrimaryAccount;
    }

    public void setIsPrimaryAccount(boolean isPrimaryAccount) {
        isPrimaryAccountProperty().set(isPrimaryAccount);
    }

    public boolean isInCashFlow() {
        return inCashFlow.get();
    }

    public SimpleBooleanProperty inCashFlowProperty() {
        return inCashFlow;
    }

    public void setInCashFlow(boolean inCashFlow) {
        inCashFlowProperty().set(inCashFlow);
    }
}
