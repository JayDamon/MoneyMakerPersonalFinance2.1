package com.moneymaker.modules.financialtype.list;

import com.moneymaker.modules.financialtype.Goal;
import com.moneymaker.modules.financialtype.behavior.FinanceType;
import com.moneymaker.utilities.DateUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Comparator;

/**
 * Created by Jay Damon on 8/3/2017.
 */
public class GoalList extends FinancialTypeList<Goal> {

    private static GoalList instance = null;

    private boolean listActive = false;

    private GoalList() {
    }

    public static GoalList getInstance() {
        if (instance == null) {
            instance = new GoalList();
        }
        return instance;
    }

    @Override
    public GoalList activateList() {
        if (!listActive) {
            this.setType(FinanceType.GOAL);
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
    public void sortList() {
        ObservableList<Goal> g = this.getList();
        Comparator<Goal> comparator = Comparator.comparingInt(Goal::getPriority);
        FXCollections.sort(g, comparator);
    }

    public void sortList(TableView<Goal> tableView) {
        ObservableList<Goal> g = tableView.getItems();
        Comparator<Goal> comparator = Comparator.comparingInt(Goal::getPriority);
        FXCollections.sort(g, comparator);
    }

    protected Goal getItem(ResultSet rs) throws SQLException {

        int id = rs.getInt("ID");
        String name = rs.getObject("goalName", String.class);
        int priority = rs.getInt("goalPriority");
        String typeName = rs.getObject("goalTypeName", String.class);
        String accountName = rs.getObject("acc_name", String.class);
        Calendar startDate = DateUtility.getCalDateFromSQL(rs.getDate("startDate"));
        Calendar endDate = DateUtility.getCalDateFromSQL(rs.getDate("endDate"));
        BigDecimal amount = rs.getBigDecimal("goalAmount").setScale(2, RoundingMode.CEILING);
        BigDecimal actualAmount = rs.getBigDecimal("goalActualAmount");

        return new Goal(id, name, priority, typeName, accountName, startDate, endDate, amount, actualAmount);
    }

    public ObservableList<String> getGoalNameList() {
        ObservableList<String> list = FXCollections.observableArrayList();
        for (Goal g : this.getList()) {
            list.add(g.getName());
        }
        return list;
    }

    public void addPriority(int priority) {
        boolean priorityFound = false;
        ObservableList<Goal> list = GoalList.getInstance().activateList().getList();
        for (Goal g : list) {
            if (g.getPriority() == priority) {
                priorityFound = true;
                g.setPriority(g.getPriority() + 1);
                g.getBehavior().update();
            } else if (priorityFound) {
                g.setPriority(g.getPriority() + 1);
                g.getBehavior().update();
            }
        }
    }

    public void updatePriorityList(int newPriority, int oldPriority) {
        ObservableList<Goal> list = GoalList.getInstance().activateList().getList();
        if (newPriority < oldPriority) {
            for (Goal g : list) {
                if (g.getPriority() >= newPriority && g.getPriority() < oldPriority) {
                    g.setPriority(g.getPriority() + 1);
                    g.getBehavior().update();
                }
            }
        } else if (newPriority > oldPriority) {
            for (Goal g : list) {
                if (g.getPriority() <= newPriority && g.getPriority() > oldPriority) {
                    g.setPriority(g.getPriority() - 1);
                    g.getBehavior().update();
                }
            }
        }
    }
}
