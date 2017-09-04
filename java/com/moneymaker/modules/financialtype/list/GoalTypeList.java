package com.moneymaker.modules.financialtype.list;

import com.moneymaker.main.UsernameData;
import com.moneymaker.utilities.ConnectionManager.ConnectionManagerUser;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jay Damon on 8/20/2017.
 */
public class GoalTypeList {
    private static ArrayList<String> goalTypes = new ArrayList<>();
    private boolean listActive = false;
    private static GoalTypeList instance = null;

    private GoalTypeList() {
    }

    public static GoalTypeList getInstance() {
        if (instance == null) {
            instance = new GoalTypeList();
        }
        return instance;
    }

    public GoalTypeList activateList() {
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
                goalTypes.add(rs.getString("goalTypeName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement prepareCreateStatement(Connection conn) throws SQLException {
        final String sql = "CALL moneymakerprocs.listGoalType(?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, UsernameData.getUserSchema());
        return stmt;
    }

    public ArrayList<String> getGoalTypes() {
        return goalTypes;
    }
}
