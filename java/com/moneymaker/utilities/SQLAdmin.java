package com.moneymaker.utilities;

import com.moneymaker.utilities.ConnectionManager.ConnectionManagerAdmin;

import java.sql.*;

/**
 * Created for MoneyMaker by Jay Damon on 10/25/2016.
 */
public class SQLAdmin {

    public static boolean userExists(String username, String password) {
        try (
                PreparedStatement stmt = prepareUserExistsStatement(username, password);
                ResultSet rs = stmt.executeQuery()
                ) {
            while (rs.next()) {
                int result = rs.getInt("correct");
                if (result != 0) {
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            ConnectionManagerAdmin.getInstance().close();
        }
    }

    private static PreparedStatement prepareUserExistsStatement(String username, String password) throws SQLException {
        Connection conn = ConnectionManagerAdmin.getInstance().getConnection();
        final String sql = "CALL money_maker_stored_procedures.findUserExists(?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        stmt.setString(2, password);
        return stmt;
    }

    public static boolean userExists(String username) {

        try (
                PreparedStatement stmtFindDuplicates = prepareDuplicateTransactionStatement(username);
                ResultSet rs = stmtFindDuplicates.executeQuery()
        ) {
            if (rs.next()) {
                int result = rs.getInt("duplicateuser");
                return result != 0;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            ConnectionManagerAdmin.getInstance().close();
        }
    }

    private static PreparedStatement prepareDuplicateTransactionStatement(String username) throws SQLException {
        Connection conn = ConnectionManagerAdmin.getInstance().getConnection();
        final String sql = "CALL money_maker_stored_procedures.findDuplicateUser(?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        return stmt;
    }

    public static void createNewUser(String username, String password, String firstName, String lastName, String email, String userSchema) {
        Connection conn = ConnectionManagerAdmin.getInstance().getConnection();
        final String sql = "CALL money_maker_stored_procedures.createuser(?,?,?,?,?,?)";
        try (
                PreparedStatement stmt = conn.prepareStatement(sql);
                ) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, firstName);
            stmt.setString(4, lastName);
            stmt.setString(5, email);
            stmt.setString(6, userSchema);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManagerAdmin.getInstance().close();
        }
    }

    public static String getUserSchema(String username, String password) {
        try (
                PreparedStatement stmt = prepareUserSchemaStatement(username, password);
                ResultSet rs = stmt.executeQuery()
                ) {
            if (rs.next()) {
                return rs.getObject("userSchema", String.class);
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            ConnectionManagerAdmin.getInstance().close();
        }
    }

    private static PreparedStatement prepareUserSchemaStatement(String username, String password) throws SQLException {
        Connection conn = ConnectionManagerAdmin.getInstance().getConnection();
        String sql = "CALL money_maker_stored_procedures.getUserSchema(?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        stmt.setString(2, password);
        return  stmt;
    }
}
