package com.moneymaker.modules.financialtype.behavior;

import com.moneymaker.main.UsernameData;
import com.moneymaker.modules.financialtype.Account;
import com.moneymaker.utilities.ConnectionManager.ConnectionManagerUser;

import java.sql.*;

/**
 * Created by Jay Damon on 8/24/2017.
 */
public class AccountBehavior extends FinancialTypeBehavior<Account> {

    public AccountBehavior(Account a) {
        super(a);
    }

    @Override
    public void update() {
        Connection conn = ConnectionManagerUser.getInstance().getConnection();

        try (
            PreparedStatement stmt = prepareUpsateStatement(conn);
        ){
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManagerUser.getInstance().close();
        }
    }

    private PreparedStatement prepareUpsateStatement(Connection conn) throws SQLException {
        final String sql = "CALL moneymakerprocs.updateAccount(?,?,?,?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, UsernameData.getUserSchema());
        stmt.setString(2, this.bean.getType());
        stmt.setString(3, this.bean.getName());
        stmt.setBigDecimal(4, this.bean.getBdStartingBalance());
        stmt.setBoolean(5, this.bean.isPrimaryAccount());
        stmt.setBoolean(6, this.bean.isInCashFlow());
        stmt.setInt(7, this.bean.getID());
        return stmt;
    }

    @Override
    public boolean addToDB() {
        Connection conn = ConnectionManagerUser.getInstance().getConnection();
        try (
                PreparedStatement stmt = prepareAddStatement(conn);
                ResultSet rs = stmt.executeQuery();
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
        final String sql = "CALL moneymakerprocs.addAccount(?,?,?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, UsernameData.getUserSchema());
        stmt.setString(2, this.bean.getType());
        stmt.setString(3, this.bean.getName());
        stmt.setBigDecimal(4, this.bean.getBdStartingBalance());
        stmt.setBoolean(5, this.bean.isPrimaryAccount());
        stmt.setBoolean(6, this.bean.isInCashFlow());
        return stmt;
    }
}
