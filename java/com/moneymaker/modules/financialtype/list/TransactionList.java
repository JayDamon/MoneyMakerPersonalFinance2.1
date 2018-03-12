package com.moneymaker.modules.financialtype.list;

import com.moneymaker.modules.financialtype.Transaction;
import com.moneymaker.modules.financialtype.behavior.FinanceType;
import com.moneymaker.utilities.DateUtility;
import com.moneymaker.utilities.TransactionType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Calendar;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Created by Jay Damon on 7/18/2017.
 */
public class TransactionList extends FinancialTypeList<Transaction> {

    private static TransactionList instance = null;
    private boolean listActive = false;

    private TransactionList() {
    }

    public static TransactionList getInstance() {
        if (instance == null) {
            instance = new TransactionList();
        }
        return instance;
    }

    @Override
    public TransactionList activateList() {
        if (!listActive) {
            this.setType(FinanceType.TRANSACTION);
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

    protected Transaction getItem(ResultSet rs) throws SQLException {
        int transactionID = rs.getInt("ID");
        String transactionAccount = rs.getObject("Account Name", String.class);
        String transactionBudget = rs.getObject("Budget Name", String.class);
        String transactionCategory = rs.getObject("Category", String.class);
        String transactionRecurring = rs.getObject("Recurring Transaction", String.class);
        Calendar transactionDate = DateUtility.getCalDateFromSQL(rs.getDate("Date"));
        String transactionDescription = rs.getObject("Description", String.class);
        BigDecimal transactionAmount = rs.getBigDecimal("Amount");

        transactionDescription = transactionDescription.replaceAll("\\s+","");
        return new Transaction(transactionID, transactionAccount, transactionBudget, transactionCategory,
                transactionRecurring, transactionDate, transactionDescription, transactionAmount, null);
    }

    protected void sortList() {
        ObservableList<Transaction> l = this.getList();
        sortList(l);
    }

    private void sortList(ObservableList<Transaction> t) {
        t.sort(Comparator.comparing(t2 -> t2.getCalendar()));
    }

    public ObservableList<Transaction> getUncategorizedTransactions(String budget) {
        ObservableList<Transaction> list = FXCollections.observableArrayList();
        for (Transaction t : this.getList()) {
            if ((t.getCategory() == null  || t.getCategory().equals("")) && t.getBudget().equals(budget)) {
                list.add(t);
            }
        }
        if (list.size() > 0) {
            return list;
        } else {
            return null;
        }
    }

    public ObservableList<Transaction> getListByAccount(String account) {
        ObservableList<Transaction> list = FXCollections.observableArrayList();
        for (Transaction t : this.getList()) {
            if (t.getAccount().equals(account)) {
                list.add(t);
            }
        }
        if (list.size() > 0) {
            return list;
        } else {
            return null;
        }
    }

    public ObservableList<Transaction> getList(String account, String category) {
        ObservableList<Transaction> list = FXCollections.observableArrayList();
        for (Transaction t : this.getList()) {
            if (t.getAccount() != null && t.getCategory() != null && t.getAccount().equals(account) && t.getCategory().equals(category)) {
                list.add(t);
            }
        }
        if (list.size() > 0) {
            return list;
        } else {
            return null;
        }
    }

    public ObservableList<Transaction> getExpenses() {
        ObservableList<Transaction> expenses = FXCollections.observableArrayList();
        for (Transaction t : getList()) {
            if (t.getTransactionType().equals(TransactionType.EXPENSE) || t.getTransactionType().equals(TransactionType.EXPENSE_TRANSFER)) {
                expenses.add(t);
            }
        }
        sortList(expenses);
        return expenses;
    }

    public ObservableList<Transaction> getIncome() {
        ObservableList<Transaction> income = FXCollections.observableArrayList();
        for (Transaction t : getList()) {
            if (t.getTransactionType().equals(TransactionType.INCOME) || t.getTransactionType().equals(TransactionType.INCOME_TRANSFER)) {
                income.add(t);
            }
        }
        sortList(income);
        return income;
    }

    public ObservableList<Transaction> getTransferExpenses() {
        ObservableList<Transaction> expenses = FXCollections.observableArrayList();
        for (Transaction t : getList()) {
            if ((t.getBudget().equals(FinanceType.TRANSFER.specialBehavior()) && t.getTransactionType().equals(TransactionType.EXPENSE)) || t.getTransactionType().equals(TransactionType.EXPENSE_TRANSFER)) {
                expenses.add(t);
            }
        }
        sortList(expenses);
        return expenses;
    }

    public ObservableList<Transaction> getTransferIncome() {
        ObservableList<Transaction> income = FXCollections.observableArrayList();
        for (Transaction t : getList()) {
            if ((t.getBudget().equals(FinanceType.TRANSFER.specialBehavior()) && t.getTransactionType().equals(TransactionType.INCOME)) || t.getTransactionType().equals(TransactionType.INCOME_TRANSFER)) {
                income.add(t);
            }
        }
        sortList(income);
        return income;
    }

    public ObservableList<Transaction> sortListFromTableViewCriteria(ObservableList<TableColumn<Transaction, ?>> criteria) {
        ObservableList<Transaction> transactions = getList();
        if (criteria.size() == 1 && criteria.get(0).getText().equals("Date")) {
            System.out.println(criteria.get(0).getText());
//            transactions.sort(Comparator.comparing(Transaction::getCalendar).reversed());.
            transactions.sort(Comparator.comparing((Transaction t) -> t.getCalendar()).reversed());
            return transactions;
        }
        return null;
    }
}
