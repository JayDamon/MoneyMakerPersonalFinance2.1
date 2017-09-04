package com.moneymaker.modules.financialtype.list;

import com.moneymaker.modules.financialtype.RecurringTransaction;
import com.moneymaker.modules.financialtype.behavior.FinanceType;
import com.moneymaker.utilities.DateUtility;
import com.moneymaker.utilities.TransactionType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

/**
 * Created by Jay Damon on 8/24/2017.
 */
public class RecurringTransactionList extends FinancialTypeList<RecurringTransaction> {

    private static RecurringTransactionList instance = null;

    private boolean listActive = false;

    private RecurringTransactionList() {
    }

    public static RecurringTransactionList getInstance() {
        if (instance == null) {
            instance = new RecurringTransactionList();
        }
        return instance;
    }

    @Override
    public RecurringTransactionList activateList() {
        if (!listActive) {
            this.setType(FinanceType.RECURRINGTRANSACTION);
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
    protected RecurringTransaction getItem(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID");
        String name = rs.getString("recName");
        String account = rs.getString("acc_name");
        String budget = rs.getString("budgetName");
        String frequency = rs.getString("freqtype_name");
        String occurrence = rs.getString("occtype_name");
        TransactionType type = TransactionType.getType(rs.getString("tran_type"));
        Calendar startDate = DateUtility.getCalDateFromSQL(rs.getDate("start_date"));
        Calendar endDate = DateUtility.getCalDateFromSQL(rs.getDate("end_date"));
        BigDecimal amount = rs.getBigDecimal("amount");

        return new RecurringTransaction(id, name, account, budget, frequency, occurrence, type, startDate, endDate, amount);
    }

    public ObservableList<String> getRecurringTransactionList() {
        ObservableList<String> recurringTransactions = FXCollections.observableArrayList();
        for (RecurringTransaction r : getList()) {
            recurringTransactions.add(r.getName());
        }
        return recurringTransactions;
    }

    public String getItem(String name) {
        for (RecurringTransaction rt : getList()) {
            if (rt.getName().equals(name)) {
                return rt.getName();
            }
        }
        return null;
    }
}
