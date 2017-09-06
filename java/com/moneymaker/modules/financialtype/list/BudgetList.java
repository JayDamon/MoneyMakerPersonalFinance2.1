package com.moneymaker.modules.financialtype.list;

import com.moneymaker.modules.financialtype.Budget;
import com.moneymaker.modules.financialtype.Goal;
import com.moneymaker.modules.financialtype.behavior.FinanceType;
import com.moneymaker.utilities.DateUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

/**
 * Created by Jay Damon on 8/17/2017.
 */
public class BudgetList extends FinancialTypeList<Budget> {

    private static BudgetList instance = null;

    private final ObservableList<Budget> allBudgets = FXCollections.observableArrayList();

    private boolean listActive = false;

    private BudgetList() {
    }

    public static BudgetList getInstance() {
        if (instance == null) {
            instance = new BudgetList();
        }
        return instance;
    }

    @Override
    public BudgetList activateList() {
        if (!listActive) {
            this.setType(FinanceType.BUDGET);
            super.activateList();
            this.addGoalAmount();
            this.listActive = true;
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
    protected Budget getItem(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID");
        String name = rs.getString("bud_name");
        Calendar startDate = DateUtility.getCalDateFromSQL(rs.getDate("bud_start_date"));
        Calendar endDate = DateUtility.getCalDateFromSQL(rs.getDate("bud_end_date"));
        String freqType = rs.getString("freqtype_name");
        BigDecimal amount = rs.getBigDecimal("bud_amount");
        boolean generic = rs.getInt("generic") == 1;
        boolean inUse = rs.getInt("inuse") == 1;
        Budget b = new Budget(id, name, startDate, endDate, freqType, amount, generic, inUse);
        allBudgets.add(b);
        if (inUse) {
            return b;
        } else {
            return null;
        }
    }

    private void addGoalAmount() {
        for (Budget b : this.getAllBudgets()) {
            if (b.getName().equals("Goal")) {
                b.setAmount(sumGoalAmount());
            }
        }
    }

    private BigDecimal sumGoalAmount() {

        BigDecimal floatGoals = BigDecimal.ZERO;

        for (Goal g: GoalList.getInstance().activateList().getList()) {

            Calendar goalStartDate = g.getCalStartDate();
            Calendar goalEndDate = DateUtility.getCalBeginningOfDay();
            if (g.getCalEndDate() != null) {
                goalEndDate = g.getCalEndDate();
            }

            BigDecimal goalAmount = g.getBDAmount();

            int startMonth = goalStartDate.get(Calendar.MONTH);
            int startYear = goalStartDate.get(Calendar.YEAR);
            int endMonth = goalEndDate.get(Calendar.MONTH);
            int endYear = goalEndDate.get(Calendar.YEAR);
            int monthsBetween;
            if (startYear != endYear) {
                monthsBetween = (endYear - startYear) * 12;
                monthsBetween += ((12-startMonth) + endMonth);
            } else {
                monthsBetween = endMonth - startMonth;
            }
            if (monthsBetween != 0) {
                goalAmount = goalAmount.divide(BigDecimal.valueOf(monthsBetween), RoundingMode.CEILING);
                floatGoals = floatGoals.add(goalAmount).setScale(2, RoundingMode.CEILING);
//                floatGoals += goalAmount / monthsBetween;
            }
        }
        return floatGoals;
    }

    public ObservableList<Budget> getAllBudgets() {
        return allBudgets;
    }

    public ObservableList<String> getActiveBudgets() {
        ObservableList<String> activeBudgets = FXCollections.observableArrayList();
        for (Budget b : this.getList()) {
            activeBudgets.add(b.getName());
        }
        return activeBudgets;
    }

    public ObservableList<String> getInactiveBudgets() {
        ObservableList<String> inactiveBudgets = FXCollections.observableArrayList();
        for (Budget b : this.getAllBudgets()) {
            if (!b.inUse()) {
                inactiveBudgets.add(b.getName());
            }
        }
        return inactiveBudgets;
    }

    public void updateUncategorizedNumber() {
        for (Budget b : this.getList()) {
            b.setUncategorizedTransactions();
        }
    }
}
