package com.moneymaker.utilities;

import com.moneymaker.modules.budgetmanager.BudgetGraph;
import com.moneymaker.main.UsernameData;
import com.moneymaker.utilities.ConnectionManager.ConnectionManagerUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created for MoneyMaker by Jay Damon on 8/27/2016.
 */
public class SQLMethods {

    public static ArrayList<String> listFrequency() {
        ArrayList<String> frequencyList = new ArrayList<>();
        try (
                PreparedStatement stmt = prepareListStatement("CALL moneymakerprocs.ListFrequency(?)");
                ResultSet rs = stmt.executeQuery()
        ) {
            rs.beforeFirst();
            while (rs.next()) {
                String freqtype = rs.getObject("freqtype_name", String.class);
                frequencyList.add(freqtype);
            }
            return frequencyList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            ConnectionManagerUser.getInstance().close();
        }
    }

    public static ArrayList<String> listOccurrence() {
        ArrayList<String> occurrenceList = new ArrayList<>();
        try (
                PreparedStatement stmt = prepareListStatement("CALL moneymakerprocs.ListOccurrence(?)");
                ResultSet rs = stmt.executeQuery()
        ) {
            rs.beforeFirst();
            while (rs.next()) {
                String occType = rs.getObject("occtype_name", String.class);
                occurrenceList.add(occType);
            }
            return occurrenceList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            ConnectionManagerUser.getInstance().close();
        }
    }

    public static ArrayList<String> listTranType() {
        ArrayList<String> tranTypeList = new ArrayList<>();
        try (
                PreparedStatement stmt = prepareListStatement("CALL moneymakerprocs.ListTranType(?)");
                ResultSet rs = stmt.executeQuery()
        ) {
            rs.beforeFirst();
            while (rs.next()) {
                String occType = rs.getObject("tran_type", String.class);
                tranTypeList.add(occType);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tranTypeList;
    }
    private static PreparedStatement prepareListStatement(final String sql) throws SQLException {
        Connection conn = ConnectionManagerUser.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, UsernameData.getUserSchema());
        return stmt;
    }

    public static ObservableList<BudgetGraph> graphBudgets(String startDate, String endDate) {
        ObservableList<BudgetGraph> budgetGraph = FXCollections.observableArrayList();

        try (
                PreparedStatement stmt = prepareGraphBudgetsStatement(startDate, endDate);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                String budget = rs.getObject("Budget", String.class);
                String plannedAmount = rs.getObject("Planned", String.class);
                String actualAmount = rs.getObject("Actual", String.class);

                if (plannedAmount == null) {
                    plannedAmount = "0.0000";
                }

                if (actualAmount == null) {
                    actualAmount = "0.0000";
                }

                budgetGraph.add(new BudgetGraph(budget, plannedAmount, actualAmount));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return budgetGraph;
    }

    private static PreparedStatement prepareGraphBudgetsStatement(String startDate, String endDate) throws SQLException {
        Connection conn = ConnectionManagerUser.getInstance().getConnection();
        final String sql = "CALL moneymakerprocs.graphBudgets(?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, UsernameData.getUserSchema());
        stmt.setString(2, startDate);
        stmt.setString(3, endDate);
        return stmt;

    }
}
