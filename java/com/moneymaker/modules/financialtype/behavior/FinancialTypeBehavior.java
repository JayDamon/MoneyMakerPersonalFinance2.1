package com.moneymaker.modules.financialtype.behavior;

import com.moneymaker.main.UsernameData;
import com.moneymaker.modules.financialtype.Bean;
import com.moneymaker.utilities.ConnectionManager.ConnectionManagerUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Jay Damon on 7/21/2017.
 */
public abstract class FinancialTypeBehavior<T extends Bean> {
    private FinanceType type;
    T bean;

    FinancialTypeBehavior(T t) {
        this.bean = t;
        this.type = t.getFinanceType();
    }

    public abstract void update();

    public abstract boolean addToDB();

    public boolean beanExistsInDB() {
        if (this.bean.getID() != 0) {
            Connection conn = ConnectionManagerUser.getInstance().getConnection();
            final String sql = "SELECT count(1) RecordExists FROM " + this.type.getTableName() +
                    "WHERE ID = ?";
            try {
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, this.bean.getID());
                ResultSet rs = stmt.executeQuery();
                return rs.getInt("RecordExists") == 1;
            } catch (SQLException e) {
                return false;
            } finally {
                ConnectionManagerUser.getInstance().close();
            }
        } return false;
    }

    public void delete() {
        final Connection conn = ConnectionManagerUser.getInstance().getConnection();
        final String sql = "CALL moneymakerprocs.deleteItem(?, ?, ?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, UsernameData.getUserSchema());
            stmt.setInt(2, this.bean.getID());
            stmt.setString(3, this.type.getTableName());
            stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
