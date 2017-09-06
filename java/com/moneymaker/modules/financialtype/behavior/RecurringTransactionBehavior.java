package com.moneymaker.modules.financialtype.behavior;

import com.moneymaker.main.UsernameData;
import com.moneymaker.modules.financialtype.RecurringTransaction;
import com.moneymaker.utilities.ConnectionManager.ConnectionManagerUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Jay Damon on 8/24/2017.
 */
public class RecurringTransactionBehavior extends FinancialTypeBehavior<RecurringTransaction> {

    public RecurringTransactionBehavior(RecurringTransaction recurringTransaction) {
        super(recurringTransaction);
    }

    @Override
    public void update() {
        Connection conn = ConnectionManagerUser.getInstance().getConnection();
        try (
                PreparedStatement stmt = prepareUpdateStatement(conn)
                ) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManagerUser.getInstance().close();
        }
    }

    private PreparedStatement prepareUpdateStatement(Connection conn) throws SQLException {
        final String sql = "CALL moneymakerprocs.updateRecurringTransaction(?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, UsernameData.getUserSchema());
        stmt.setInt(2, this.bean.getID());
        stmt.setString(3, this.bean.getName());
        stmt.setString(4, this.bean.getAccount());
        stmt.setString(5, this.bean.getBudget());
        stmt.setString(6, this.bean.getFrequency());
        stmt.setString(7, this.bean.getOccurrence());
        stmt.setString(8, this.bean.getType());
        stmt.setDate(9, this.bean.getSQLStartDate());
        stmt.setDate(10, this.bean.getSQLEndDate());
        stmt.setBigDecimal(11, this.bean.getBdAmount());
        return stmt;
    }

    @Override
    public boolean addToDB() {
        Connection conn = ConnectionManagerUser.getInstance().getConnection();
        try (
                PreparedStatement stmt = prepareAddStatement(conn);
                ResultSet rs = stmt.executeQuery()
        ) {
            if (rs.next()) {
                this.bean.setId(rs.getInt("LAST_INSERT_ID()"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManagerUser.getInstance().close();
        }
        return false;
    }

    private PreparedStatement prepareAddStatement(Connection conn) throws SQLException {
        final String sql = "CALL moneymakerprocs.addRecurringTransaction(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, UsernameData.getUserSchema());
        stmt.setString(2, this.bean.getName());
        stmt.setString(3, this.bean.getAccount());
        stmt.setString(4, this.bean.getBudget());
        stmt.setString(5, this.bean.getFrequency());
        stmt.setString(6, this.bean.getOccurrence());
        stmt.setString(7, this.bean.getType());
        stmt.setDate(8, this.bean.getSQLStartDate());
        stmt.setDate(9, this.bean.getSQLEndDate());
        stmt.setBigDecimal(10, this.bean.getBdAmount());
        return stmt;
    }
}
