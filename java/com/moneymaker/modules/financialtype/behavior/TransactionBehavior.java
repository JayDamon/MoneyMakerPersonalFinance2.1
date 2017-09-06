package com.moneymaker.modules.financialtype.behavior;

import com.moneymaker.main.UsernameData;
import com.moneymaker.modules.financialtype.Transaction;
import com.moneymaker.modules.transactionmanager.DuplicateTransactionWindowController;
import com.moneymaker.utilities.ConnectionManager.ConnectionManagerUser;
import com.moneymaker.utilities.DateUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

/**
 * Created by Jay Damon on 7/23/2017.
 */
public class TransactionBehavior extends FinancialTypeBehavior<Transaction> {

    public TransactionBehavior(Transaction t) {
        super(t);
    }

    @Override
    public void update() {
        Connection conn = ConnectionManagerUser.getInstance().getConnection();
        executeUpdate(conn);
    }

    private void executeUpdate(Connection conn) {
        final String sql;
        if (this.bean.getBudget().equals("Goal")) {
            sql = "CALL moneymakerprocs.updateTransactionWithGoal(?,?,?,?,?,?,?,?,?)";
        }   else {
            sql = "CALL moneymakerprocs.updateTransaction(?,?,?,?,?,?,?,?,?)";
        }

        try (
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, UsernameData.getUserSchema());
            stmt.setInt(2, this.bean.getID());
            stmt.setString(3, this.bean.getDescription());
            stmt.setString(4, this.bean.getAccount());
            stmt.setString(5, this.bean.getBudget());
            stmt.setString(6, this.bean.getCategory());
            stmt.setString(7, this.bean.getRecurring());
            stmt.setDate(8, this.bean.getSqlDate());
            stmt.setBigDecimal(9, this.bean.getBigDecimalAmount());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManagerUser.getInstance().close();
        }
    }

    @Override
    public boolean addToDB() {
        Connection conn = ConnectionManagerUser.getInstance().getConnection();
        if (!this.beanExistsInDB() && addNewTransaction(conn)) {

            try (
                    PreparedStatement stmt = prepareAddStatement(conn);
                    ResultSet rs = stmt.executeQuery()
            ){
                if (rs.next()) {
                    this.bean.setId(rs.getInt("ID"));
                    this.bean.setTimeStamp(DateUtility.getCalDateFromSQL(rs.getDate("TimeStampAdded")));
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            } finally {
                ConnectionManagerUser.getInstance().close();
            }
        } else {
            ConnectionManagerUser.getInstance().close();
            return false;
        }
        return false;
    }

    private PreparedStatement prepareAddStatement(Connection conn) throws SQLException {
        final String sql = "CALL moneymakerprocs.addTransaction(?,?,?,?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, UsernameData.getUserSchema());
        stmt.setString(2, this.bean.getAccount());
        stmt.setString(3, this.bean.getBudget());
        stmt.setString(4, this.bean.getCategory());
        stmt.setDate(5, this.bean.getSqlDate());
        stmt.setString(6, this.bean.getDescription());
        stmt.setBigDecimal(7, this.bean.getBigDecimalAmount());
        return stmt;
    }

    private boolean addNewTransaction(Connection conn) {

        ObservableList<Transaction> duplicates = FXCollections.observableArrayList();
        try (
                PreparedStatement stmt = createDuplicatePreparedStatement(conn);
                ResultSet rs = stmt.executeQuery()
        ) {
            if (rs.next()) {
                do {
                    Calendar transactionDate = DateUtility.getCalDateFromSQL(rs.getDate("tran_date"));
                    String transactionDesc = rs.getObject("tran_description", String.class);
                    BigDecimal transactionAmount = rs.getBigDecimal("tran_amount").setScale(2, RoundingMode.CEILING);
                    Calendar transactionTimeAdded = DateUtility.getCalDateFromSQL(rs.getDate("tran_time_added"));
                    duplicates.add(new Transaction(0, "Account", "Budget", "Category", "Recurring", transactionDate, transactionDesc, transactionAmount, transactionTimeAdded));
                } while (rs.next());
                return askUserToAdd(duplicates);
            } else {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean askUserToAdd(ObservableList<Transaction> t) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/transactions/transactions/duplicateTransactionWindow.fxml"));
            AnchorPane newWindow = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Duplicate Transaction Found");
            Scene scene = new Scene(newWindow);
            stage.setScene(scene);
            DuplicateTransactionWindowController dw = loader.getController();
            dw.listDuplicateTransactions(t);
            stage.showAndWait();
            return dw.getAddTransaction();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    private PreparedStatement createDuplicatePreparedStatement(Connection conn) throws SQLException {
        String userSchema = UsernameData.getUserSchema();
        String sql = "CALL moneymakerprocs.findDuplicateTransactions(?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, userSchema);
        stmt.setDate(2, this.bean.getSqlDate());
        stmt.setString(3, this.bean.getDescription());
        stmt.setBigDecimal(4, this.bean.getBigDecimalAmount());
        return stmt;
    }
}
