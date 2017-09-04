package com.moneymaker.modules.financialtype.list;

import com.moneymaker.main.UsernameData;
import com.moneymaker.utilities.ConnectionManager.ConnectionManagerUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Jay Damon on 8/20/2017.
 */
public class TransactionCategoryList {

    private static HashMap<String, String> allTransactionCateogories = new HashMap<>();
    private static TransactionCategoryList instance = null;
    private boolean listActive = false;

    private TransactionCategoryList() {
    }

    public static TransactionCategoryList getInstance() {
        if (instance == null) {
            instance = new TransactionCategoryList();
        }
        return instance;
    }

    public TransactionCategoryList activateList() {
        if (!listActive) {
            createList();
            listActive = true;
        }
        return instance;
    }

    private void createList() {
        Connection conn = ConnectionManagerUser.getInstance().getConnection();
        try (
                PreparedStatement stmt = prepareCreateStatement(conn);
                ResultSet rs = stmt.executeQuery();
            ) {
            while (rs.next()) {
                allTransactionCateogories.put(
                        rs.getString("trancat_name"),
                        rs.getString("bud_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement prepareCreateStatement(Connection conn) throws SQLException {
        final String sql = "CALL moneymakerprocs.listTransactionCategories(?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, UsernameData.getUserSchema());
        return stmt;
    }
    public ObservableList<String> getTransactionCategories() {
        ObservableList<String> list = FXCollections.observableArrayList();
        for (Object o : allTransactionCateogories.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            list.add(pair.getKey().toString());
        }
        return list;
    }

    public ObservableList<String> getTransactionCategories(String budgetName) {
        ObservableList<String> list = FXCollections.observableArrayList();
        for (Object o : allTransactionCateogories.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            if (pair.getValue().equals(budgetName)) {
                list.add(pair.getKey().toString());
            }
        }
        return list;
    }
}
