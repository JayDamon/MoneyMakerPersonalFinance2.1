package com.moneymaker.modules.financialtype.list;

import com.moneymaker.main.UsernameData;
import com.moneymaker.utilities.ConnectionManager.ConnectionManagerUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Jay Damon on 8/24/2017.
 */
public class AccountTypeList {
    private static ArrayList<String> goalTypes = new ArrayList<>();
    private boolean listActive = false;
    private static AccountTypeList instance = null;

    private AccountTypeList() {
    }

    public static AccountTypeList getInstance() {
        if (instance == null) {
            instance = new AccountTypeList();
        }
        return instance;
    }

    public AccountTypeList activateList() {
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
                goalTypes.add(rs.getString("acctype_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement prepareCreateStatement(Connection conn) throws SQLException {
        final String sql = "CALL moneymakerprocs.listAccountTypes(?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, UsernameData.getUserSchema());
        return stmt;
    }

    public ArrayList<String> getList() {
        return goalTypes;
    }
}
