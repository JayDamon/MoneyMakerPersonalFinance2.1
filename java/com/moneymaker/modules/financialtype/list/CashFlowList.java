package com.moneymaker.modules.financialtype.list;

import com.moneymaker.main.UsernameData;
import com.moneymaker.modules.financialtype.CashFlow;
import com.moneymaker.modules.financialtype.Transaction;
import com.moneymaker.utilities.ConnectionManager.ConnectionManagerUser;
import com.moneymaker.utilities.DateUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;

/**
 * Created by Jay Damon on 8/25/2017.
 */
public class CashFlowList extends FinancialTypeList<CashFlow> {

    private static CashFlowList instance = null;

    private boolean listActive = false;

    private CashFlowList() {
    }

    public static CashFlowList getInstance() {
        if (instance == null) {
            instance = new CashFlowList();
        }
        return instance;
    }

    @Override
    public CashFlowList activateList() {
        if (!listActive) {
            this.list = activateCashFlowList(DateUtility.getCalBeginningOfDay());
            this.listActive = true;
        }
        return instance;
    }

    public CashFlowList activateList(Calendar selectedDate) {
        if (!listActive) {
            this.list = activateCashFlowList(selectedDate);
            this.listActive = true;
        }
        return instance;
    }

    public void close() {
        this.list = null;
        this.listActive = false;
    }

    private ObservableList<CashFlow> activateCashFlowList(Calendar selectedDate) {
        selectedDate.set(Calendar.DAY_OF_MONTH, 1);
        Connection conn = ConnectionManagerUser.getInstance().getConnection();
        ObservableList<CashFlow> list = FXCollections.observableArrayList();
        ConnectionManagerUser.getInstance().suspendClose();
        list.addAll(
                getListFromDB(conn, selectedDate, false));
        list.addAll(
                getListFromDB(conn, selectedDate, true));

        ConnectionManagerUser.getInstance().activateClose();
        ConnectionManagerUser.getInstance().close();
        return list;
    }

    private ObservableList<CashFlow> getListFromDB(Connection conn, Calendar selectedDate, boolean recurringTransactions) {
        ObservableList<CashFlow> list = FXCollections.observableArrayList();
        try (
                PreparedStatement stmt = prepareStatement(conn, selectedDate, recurringTransactions);
                ResultSet rs = stmt.executeQuery()
        ) {
                if (recurringTransactions) {
                    list = getRecurringTransactionItem(rs, selectedDate);
                } else {
                    list = getMonthlyBudgetRemainingItem(rs, selectedDate);
                }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManagerUser.getInstance().close();
        }
        return list;
    }

    private ObservableList<CashFlow> getRecurringTransactionItem(ResultSet rs, Calendar selectedDate) throws SQLException {
        ObservableList<CashFlow> list = FXCollections.observableArrayList();
        while (rs.next()) {
            String account = rs.getString("Account");
            String budget = rs.getString("Budget");
            String category = rs.getString("Category");
            BigDecimal projected = rs.getBigDecimal("Amount");
            Calendar startDate = DateUtility.getCalDateFromSQL(rs.getDate("Start Date"));
            int frequencyDays = rs.getInt("Frequency");
            String occurrence = rs.getString("Occurrence");
            int occurrenceType = rs.getInt("Occurrence Type");
            double numberOfDaysBetweenDates;
            if (startDate != null) {
                numberOfDaysBetweenDates = ChronoUnit.DAYS.between(startDate.toInstant(), selectedDate.toInstant());
            } else continue;

            double numberOfDaysForFirstOccurrenceInMonth = Math.ceil(numberOfDaysBetweenDates / frequencyDays) * frequencyDays;
            Calendar o = (Calendar)startDate.clone();
            startDate.add(Calendar.DATE, (int) numberOfDaysForFirstOccurrenceInMonth);
//            System.out.println("Start Date: " + o.getTime() + " " + category + " | " + numberOfDaysForFirstOccurrenceInMonth + " (" + numberOfDaysBetweenDates + " / " + frequencyDays + ") * " + frequencyDays + " | Start Date: " + startDate.getTime());
            list.addAll(addNewCashFlow(selectedDate, account, budget, category, projected, startDate, frequencyDays, occurrence, occurrenceType));
        }
        return list;
    }

    private static ObservableList<CashFlow> addNewCashFlow(Calendar selectedDate, String account, String budget, String category, BigDecimal projected, Calendar startDate, int frequencyDays, String occurrence, int occurrenceType) {
        int monthOfFirstOccurrence = startDate.get(Calendar.MONTH);
        int yearOfFirstOccurrence = startDate.get(Calendar.YEAR);
        Calendar currentStartDate = setStartDate(selectedDate, startDate, occurrence, occurrenceType);
        ObservableList<CashFlow> list = FXCollections.observableArrayList();
        while (monthOfFirstOccurrence == selectedDate.get(Calendar.MONTH) && yearOfFirstOccurrence == selectedDate.get(Calendar.YEAR)) {
            Calendar cashFlowDate = (Calendar)currentStartDate.clone();
            Calendar actualStartDate = (Calendar)currentStartDate.clone();

            Calendar endDate = (Calendar)currentStartDate.clone();
            setEndDate(frequencyDays, endDate);

            if (frequencyDays == 30 || frequencyDays == 60 || frequencyDays == 90 || frequencyDays == 180 ||
                    frequencyDays == 270 || frequencyDays == 365) {
                Calendar newStartDate = new GregorianCalendar(yearOfFirstOccurrence, monthOfFirstOccurrence, 1);
                DateUtility.setCalDate(endDate, yearOfFirstOccurrence, monthOfFirstOccurrence, 1);
                actualStartDate = (Calendar)endDate.clone();
                endDate.set(Calendar.DAY_OF_MONTH, newStartDate.getActualMaximum(Calendar.DAY_OF_MONTH));
            }

            AmountAndDate amountAndDate = getActualTransactionAmount(category, actualStartDate, endDate, cashFlowDate);
            BigDecimal actualAmount = amountAndDate.amount;
            Calendar date = amountAndDate.date;

            list.add(new CashFlow(account, budget, date, category, projected, actualAmount));
            currentStartDate.add(Calendar.DATE, frequencyDays);
            monthOfFirstOccurrence = currentStartDate.get(Calendar.MONTH);
            yearOfFirstOccurrence = currentStartDate.get(Calendar.YEAR);
        }
        return list;
    }

    private static void setEndDate(int frequencyDays, Calendar endDate) {
        if (frequencyDays == 7 || frequencyDays == 14) {
            endDate.add(Calendar.DATE, frequencyDays - 1);
        } else {
            endDate.add(Calendar.DATE, frequencyDays);
        }
    }

    private static Calendar setStartDate(Calendar selectedDate, Calendar startDate, String occurrence, int occurrenceType) {
        Calendar currentStartDate = DateUtility.getCalBeginningOfDay();
        switch (occurrenceType) {
            case 1: // Specific Date
                currentStartDate = (Calendar)selectedDate.clone();
                currentStartDate.set(Calendar.DAY_OF_MONTH, startDate.get(Calendar.DAY_OF_MONTH));
                break;
            case 2: // End of Month
                currentStartDate.set(Calendar.DAY_OF_MONTH, currentStartDate.getActualMaximum(Calendar.DAY_OF_MONTH));
                break;
            case 3: // First of Month
                currentStartDate = (Calendar)selectedDate.clone();
                currentStartDate.set(Calendar.DAY_OF_MONTH, 1);
                break;
            case 4: // Specific day of the week
                currentStartDate = DateUtility.getWeekDayStartDate((Calendar)startDate.clone(), occurrence);  // Get date of the first occurrence of that day of the week
                break;
            case 5: // Rolling date
                //TODO need formula for this
        }
        return currentStartDate;
    }

    private ObservableList<CashFlow> getMonthlyBudgetRemainingItem(ResultSet rs, Calendar selectedDate) throws SQLException {
        ObservableList<CashFlow> list = FXCollections.observableArrayList();
        while (rs.next()) {
            String budget = rs.getString("Budget");
//            Calendar date = DateUtility.getCalDateFromSQL(rs.getDate("Date"));
            BigDecimal remaining = rs.getBigDecimal("Amount");
            BigDecimal amount = rs.getBigDecimal("Budget Amount");
            BigDecimal projected;

            if (remaining == null) {
                projected = amount;
            } else {
                projected = remaining;
            }

            BigDecimal actual = getMonthlyBudgetActual(budget, selectedDate);

            if (projected.compareTo(BigDecimal.ZERO) != 0) {
                list.add(new CashFlow(budget, selectedDate, projected, actual));
            }
        }
        return list;
    }

    private BigDecimal getMonthlyBudgetActual(String budget, Calendar date) {
        ObservableList<Transaction> transactions = TransactionList.getInstance().activateList().getList();
        AccountList accounts = AccountList.getInstance().activateList();
        BigDecimal amount = BigDecimal.ZERO;
        if (transactions != null) {
            for (Transaction t : transactions) {
                Calendar tCal = t.getCalendar();
                if (tCal.get(Calendar.MONTH) == date.get(Calendar.MONTH) &&
                        tCal.get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
                        t.getBudget().equals(budget) &&
                        t.getRecurring().equals("") &&
                        accounts.inCashFlow(t.getAccount())) {
                    amount = amount.add(t.getBigDecimalAmount());
                }
            }
        }
        return amount;
    }

    private static AmountAndDate getActualTransactionAmount(String description, Calendar startDate, Calendar endDate, Calendar projectedDate) {
        ObservableList<Transaction> transactions = TransactionList.getInstance().activateList().getList();
        AccountList accounts = AccountList.getInstance().activateList();
        Calendar date = (Calendar)projectedDate.clone();
        BigDecimal amount = BigDecimal.ZERO;
        for (Transaction t : transactions) {
            if (t.getCalendar().compareTo(startDate) >= 0 &&
                    t.getCalendar().compareTo(endDate) <= 0 &&
                    t.getRecurring().equals(description) &&
                    accounts.inCashFlow(t.getAccount())) {
                date = t.getCalendar().compareTo(date) < 0 ? t.getCalendar() : date;
                amount = amount.add(t.getBigDecimalAmount());
            }
        }
        AmountAndDate amountAndDate = new AmountAndDate();
        amountAndDate.amount = amount;
        amountAndDate.date = date;
        return amountAndDate;
    }

    private PreparedStatement prepareStatement(Connection conn, Calendar selectedDate, boolean recurringTransactions) throws SQLException {
        final String sql;
        if (recurringTransactions) {
            sql = "CALL moneymakerprocs.viewRecurringTransactionsForCashFlow(?,?)";
//            System.out.println(sql + " | " + UsernameData.getUserSchema() + " | " + DateUtility.getSQLDate(selectedDate));
        } else {
            sql = "CALL moneymakerprocs.viewMonthlyBudgetRemaining(?,?)";
//            System.out.println(sql + " | " + UsernameData.getUserSchema() + " | " + DateUtility.getSQLDate(selectedDate));
        }
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, UsernameData.getUserSchema());
        stmt.setDate(2, DateUtility.getSQLDate(selectedDate));
        return stmt;
    }

    @Override
    public void sortList() {
        getList().sort(Comparator.comparing(CashFlow::getCalDate));
    }

    @Override
    protected CashFlow getItem(ResultSet rs) throws SQLException {

        return null;
    }

    public static class AmountAndDate {
        Calendar date;
        BigDecimal amount;
    }
}
