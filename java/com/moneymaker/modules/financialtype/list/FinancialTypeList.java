package com.moneymaker.modules.financialtype.list;

import com.moneymaker.main.UsernameData;
import com.moneymaker.modules.financialtype.Bean;
import com.moneymaker.modules.financialtype.behavior.FinanceType;
import com.moneymaker.utilities.ConnectionManager.ConnectionManagerUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Jay Damon on 7/23/2017.
 */
public abstract class FinancialTypeList<T extends Bean> {
    ObservableList<T> list = FXCollections.observableArrayList();
    private FinanceType type;

    public FinancialTypeList activateList() {
        this.list = getListFromDB();
        sortList();
        return this;
    }

    protected void setType(FinanceType t) {
        this.type = t;
    }

    public ObservableList<T> getList() {
        return this.list;
    }

    protected abstract void sortList();

    private ObservableList<T> getListFromDB() {
        ObservableList<T> list = FXCollections.observableArrayList();
        Connection conn = ConnectionManagerUser.getInstance().getConnection();
        try (
                PreparedStatement stmt = createPreparedStatement(conn);
                ResultSet rs = stmt.executeQuery();
        ) {
            rs.beforeFirst();
            while (rs.next()) {
                T t = getItem(rs);
                if (t != null) {
                    list.add(t);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManagerUser.getInstance().close();
        }
        return list;
    }

    private PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
        String userSchema = UsernameData.getUserSchema();
        String sql = "CALL moneymakerprocs.view" + this.type.getTableName() + "(?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, userSchema);
        return stmt;
    }

    protected abstract T getItem(ResultSet rs) throws SQLException;

    public T getItem(int id) {
        for (T t : list) {
            if (t.getID() == id) {
                return t;
            }
        }
        return null;
    }

}
