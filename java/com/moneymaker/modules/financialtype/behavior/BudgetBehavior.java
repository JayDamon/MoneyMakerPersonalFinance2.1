package com.moneymaker.modules.financialtype.behavior;

import com.moneymaker.main.UsernameData;
import com.moneymaker.modules.financialtype.Budget;
import com.moneymaker.utilities.ConnectionManager.ConnectionManagerUser;

import java.sql.*;

/**
 * Created by Jay Damon on 8/17/2017.
 */
public class BudgetBehavior extends FinancialTypeBehavior<Budget> {

    public BudgetBehavior(Budget b) {
        super(b);
    }

    @Override
    public void update() {
        Connection conn = ConnectionManagerUser.getInstance().getConnection();
        try (
                PreparedStatement stmt =  prepareUpdateStatement(conn);
                ) {
            stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement prepareUpdateStatement(Connection conn) throws SQLException {
        final String sql = "CALL moneymakerprocs.updateBudget(?,?,?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, UsernameData.getUserSchema());
        stmt.setInt(2, this.bean.getID());
        stmt.setDate(3, this.bean.getSQLStartDate());
        stmt.setDate(4, this.bean.getSQLEndDate());
        stmt.setString(5, this.bean.getFrequency());
        stmt.setBigDecimal(6, this.bean.getBdAmount());
        return stmt;
    }

    @Override
    public boolean addToDB() {
        Connection conn = ConnectionManagerUser.getInstance().getConnection();
        if (this.bean.isGeneric()) {
            activateBudget(conn);
        } else {
            addBudget(conn);
        }
        return false;
    }

    private void activateBudget(Connection conn) {
        final String sql = "CALL moneymakerprocs.activateBudget(?,?)";
        try (
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setString(1, UsernameData.getUserSchema());
            stmt.setString(2, this.bean.getName());
            stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addBudget(Connection conn) {
        try (
                PreparedStatement stmt = getAddPreparedStatement(conn);
                ResultSet rs = stmt.executeQuery();
        ) {
            if (rs.next()) {
                this.bean.setId(rs.getInt("LAST_INSERT_ID()"));
                this.bean.setInUse(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement getAddPreparedStatement(Connection conn) throws SQLException {
        final String sql = "CALL moneymakerprocs.addBudgetName(?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, UsernameData.getUserSchema());
        stmt.setString(2, this.bean.getName());
        return stmt;
    }

    @Override
    public void delete() {
        if (this.bean.isGeneric()) {
            resetGenericBudget();
        } else {
            super.delete();
        }
    }

    private void resetGenericBudget() {
        Connection conn = ConnectionManagerUser.getInstance().getConnection();
        try (
            PreparedStatement stmt = createBudGenPreparedStatement(conn);
        ) {
            stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement createBudGenPreparedStatement(Connection conn) throws SQLException {
        final String sql =  "CALL moneymakerprocs.resetGenericBudget(?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, UsernameData.getUserSchema());
        stmt.setString(2, this.bean.getName());
        return stmt;
    }

    private PreparedStatement getIsGenericStatment(Connection conn) throws SQLException{
        final String sql = "CALL moneymakerprocs.getBudgetGeneric(?,?);";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, UsernameData.getUserSchema());
        stmt.setString(2, this.bean.getName());

        return stmt;
    }


}
