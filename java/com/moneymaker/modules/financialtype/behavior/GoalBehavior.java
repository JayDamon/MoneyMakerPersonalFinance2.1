package com.moneymaker.modules.financialtype.behavior;

import com.moneymaker.main.UsernameData;
import com.moneymaker.modules.financialtype.Goal;
import com.moneymaker.modules.financialtype.list.GoalList;
import com.moneymaker.utilities.ConnectionManager.ConnectionManagerUser;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Jay Damon on 8/3/2017.
 */
public class GoalBehavior extends FinancialTypeBehavior<Goal> {

    public GoalBehavior(Goal g) {
        super(g);
    }

    @Override
    public void update() {
        Connection conn = ConnectionManagerUser.getInstance().getConnection();
        try (
                PreparedStatement stmt = createUpdateStatement(conn);
                ) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManagerUser.getInstance().close();
        }
    }

    private PreparedStatement createUpdateStatement(Connection conn) throws SQLException{

        final String sql = "CALL moneymakerprocs.updateGoals(?,?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, UsernameData.getUserSchema());
        stmt.setInt(2, this.bean.getID());
        stmt.setString(3, this.bean.getName());
        stmt.setInt(4, this.bean.getPriority());
        stmt.setString(5, this.bean.getType());
        stmt.setString(6, this.bean.getAccount());
        stmt.setDate(7, this.bean.getSQLStartDate());
        stmt.setDate(8, this.bean.getSQLEndDate());
        stmt.setBigDecimal(9, this.bean.getBDAmount());
        return stmt;
    }

    @Override
    public boolean addToDB() {
        Connection conn = ConnectionManagerUser.getInstance().getConnection();
        try (
            PreparedStatement stmt = createAddStatement(conn);
            ResultSet rs = stmt.executeQuery();
        ) {
            if (rs.next()) {
                this.bean.setId(rs.getInt("LAST_INSERT_ID()"));
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            ConnectionManagerUser.getInstance().close();
        }
    }

    private PreparedStatement createAddStatement(Connection conn) throws SQLException {
        final String sql = "CALL moneymakerprocs.addGoal(?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, UsernameData.getUserSchema());
        stmt.setString(2, this.bean.getName());
        stmt.setInt(3, this.bean.getPriority());
        stmt.setString(4, this.bean.getType());
        stmt.setString(5, this.bean.getAccount());
        stmt.setDate(6, this.bean.getSQLStartDate());
        stmt.setDate(7, this.bean.getSQLEndDate());
        stmt.setBigDecimal(8, this.bean.getBDAmount());
        return stmt;
    }
}
