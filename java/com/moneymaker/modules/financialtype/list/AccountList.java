package com.moneymaker.modules.financialtype.list;

import com.moneymaker.modules.financialtype.Account;
import com.moneymaker.modules.financialtype.Transaction;
import com.moneymaker.modules.financialtype.behavior.FinanceType;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Jay Damon on 8/24/2017.
 */
public class AccountList extends FinancialTypeList<Account> {

    private static AccountList instance = null;

    private boolean listActive = false;

    private AccountList() {
    }

    public static AccountList getInstance() {
        if (instance == null) {
            instance = new AccountList();
        }
        return instance;
    }

    @Override
    public AccountList activateList() {
        if (!listActive) {
            this.setType(FinanceType.ACCOUNT);
            super.activateList();
            listActive = true;
        }
        return instance;
    }

    public void close() {
        if (instance != null) {
            instance = null;
        }
    }

    @Override
    protected void sortList() {
    }

    @Override
    protected Account getItem(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID");
        String name = rs.getString("acc_name");
        String type = rs.getString("acctype_name");
        BigDecimal balance = rs.getBigDecimal("acc_start_balance");
        String classification = rs.getString("accountClassificationName");
        Boolean isPrimaryAccount = rs.getInt("isPrimary") ==1;
        boolean isInCashFlow = rs.getInt("isInCashFlow") == 1;

        BigDecimal currentBalance = getAccountBalance(Calendar.getInstance(), name, balance, classification);
        return new Account(id, name, type, balance, currentBalance, classification, isPrimaryAccount, isInCashFlow);
    }

    private BigDecimal getAccountBalance(Calendar endDate, String accountName, BigDecimal startingBalance, String classification) {
        BigDecimal balance = BigDecimal.ZERO;
        ObservableList<Transaction> transactions = TransactionList.getInstance().activateList().getListByAccount(accountName);
        if (transactions != null) {
            addTransactionListener(TransactionList.getInstance().activateList().getList());
            balance = getBalance(endDate, balance, transactions);
        }
        if (classification.equals("Asset") || classification.equals("Equity")) {
            return startingBalance.add(balance);
        } else return startingBalance.subtract(balance);

    }

    private BigDecimal getAccountBalance(Calendar endDate, Account a) {
        BigDecimal balance = BigDecimal.ZERO;
        ObservableList<Transaction> list = TransactionList.getInstance().activateList().getListByAccount(a.getName());
        if (list != null) {
            addTransactionListener(TransactionList.getInstance().activateList().getList());
            balance = getBalance(endDate, balance, list);
        }
        return getCurrentBalance(a, balance);

    }

    private BigDecimal getCurrentBalance(Account a, BigDecimal balance) {
        if (a.getClassification().equals("Asset") || a.getClassification().equals("Equity")) {
            return a.getBdStartingBalance().add(balance);
        } else return a.getBdStartingBalance().subtract(balance);
    }

    private BigDecimal getBalance(Calendar endDate, BigDecimal balance, ObservableList<Transaction> list) {
        for (Transaction t : list) {
            if (t.getCalendar().compareTo(endDate) < 0) {
                balance = balance.add(t.getBigDecimalAmount());
            }
        }
        return balance;
    }

    private void addTransactionListener(ObservableList<Transaction> list) {
            list.addListener((ListChangeListener<Transaction>) c -> {
//                while (c.next()) {
//                    if (c.wasAdded()) {
//                        addToAccountBalance(c.getAddedSubList());
//                    } else if (c.wasRemoved()) {
//                        subtractFromBalance(c.getRemoved());
//                    }
//                }
                resetAccountBalance();
            });
    }

    private void resetAccountBalance() {
        ObservableList<Transaction> list = TransactionList.getInstance().getList();
        for (Account a : getList()) {
            BigDecimal accountBalance = getBalance(Calendar.getInstance(), BigDecimal.ZERO, list);
            accountBalance = getCurrentBalance(a, accountBalance);
            a.setCurrentBalance(accountBalance);
        }
    }

    private void subtractFromBalance(List<? extends Transaction> addedSubList) {
        for (Transaction t : addedSubList) {
            for (Account a : getList()) {
                if (a.getName().equals(t.getAccount())) {
                    BigDecimal balance = a.getBdCurrentBalance();
                    if (a.getClassification().equals("Asset") || a.getClassification().equals("Equity")) {
                        balance = balance.subtract(t.getBigDecimalAmount());
                    } else {
                        balance = balance.add(t.getBigDecimalAmount());
                    }
                    a.setCurrentBalance(balance);
                }
            }
        }
    }

    private void addToAccountBalance(List<? extends Transaction> addedSubList) {
        for (Transaction t : addedSubList) {
            for (Account a : getList()) {
                if (a.getName().equals(t.getAccount())) {
                    BigDecimal balance = a.getBdCurrentBalance();
                    if (a.getClassification().equals("Asset") || a.getClassification().equals("Equity")) {
                        balance = balance.add(t.getBigDecimalAmount());
                    } else {
                        balance = balance.subtract(t.getBigDecimalAmount());
                    }
                    a.setCurrentBalance(balance);
                }
            }
        }
    }


    public BigDecimal getPrimaryAccountBalance(Calendar endDate) {
        for (Account a : getList()) {
            if (a.isPrimaryAccount()) {
                return getAccountBalance(endDate, a);
            }
        }
        return BigDecimal.ZERO;
    }

    public ObservableList<String> getAccountNameList() {
        ObservableList<String> list = FXCollections.observableArrayList();
        for (Account a : this.getList()) {
            list.add(a.getName());
        }
        return list;
    }

    private Account getItem(String name) {
        for (Account a : getList()) {
            if (a.getName().equals(name)) {
                return a;
            }
        }
        return null;
    }

    public boolean inCashFlow(String name) {
        Account a = getItem(name);
        return a != null && a.isInCashFlow();
    }
}
