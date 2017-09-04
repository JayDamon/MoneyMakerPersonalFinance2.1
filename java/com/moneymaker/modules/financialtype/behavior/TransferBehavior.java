package com.moneymaker.modules.financialtype.behavior;

import com.moneymaker.main.UsernameData;
import com.moneymaker.modules.financialtype.Transaction;
import com.moneymaker.modules.financialtype.Transfer;
import com.moneymaker.modules.financialtype.list.TransactionList;
import com.moneymaker.utilities.ConnectionManager.ConnectionManagerUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Jay Damon on 8/20/2017.
 */
public class TransferBehavior extends FinancialTypeBehavior<Transfer> {

    public TransferBehavior(Transfer t) {
        super(t);
    }

    @Override
    public void update() {
        Connection conn = ConnectionManagerUser.getInstance().getConnection();
        try (
                PreparedStatement stmt = prepareStatement(conn);
                ) {
            stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement prepareStatement(Connection conn) throws SQLException {
        final String sql = "CALL moneymakerprocs.updateTransfer(?,?,?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, UsernameData.getUserSchema());
        stmt.setInt(2, this.bean.getID());
        stmt.setDate(3, this.bean.getSqlDate());
        stmt.setInt(4, this.bean.getFromTransactionID());
        stmt.setInt(5, this.bean.getToTransactionID());
        stmt.setBigDecimal(6, this.bean.getBdAmount());
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
                this.bean.setId(rs.getInt("ID"));
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            ConnectionManagerUser.getInstance().close();
        }
        return false;
    }

    private PreparedStatement prepareAddStatement(Connection conn) throws SQLException {
        final String sql = "CALL moneymakerprocs.addTransfer(?,?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, UsernameData.getUserSchema());
        stmt.setDate(2, this.bean.getSqlDate());
        stmt.setInt(3, this.bean.getFromTransactionID());
        stmt.setInt(4, this.bean.getToTransactionID());
        stmt.setBigDecimal(5, this.bean.getBdAmount());
        return stmt;
    }

    @Override
    public void delete() {
        super.delete();
        TransactionList list = TransactionList.getInstance().activateList();
        Transaction fromTransaction = list.getItem(this.bean.getFromTransactionID());
        Transaction toTransaction = list.getItem(this.bean.getToTransactionID());
        list.getList().remove(fromTransaction);
        list.getList().remove(toTransaction);
        fromTransaction.getBehavior().delete();
        toTransaction.getBehavior().delete();
    }
}
