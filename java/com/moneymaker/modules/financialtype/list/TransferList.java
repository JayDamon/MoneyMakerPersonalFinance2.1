package com.moneymaker.modules.financialtype.list;

import com.moneymaker.modules.financialtype.Transfer;
import com.moneymaker.modules.financialtype.behavior.FinanceType;
import com.moneymaker.utilities.DateUtility;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

/**
 * Created by Jay Damon on 8/20/2017.
 */
public class TransferList extends FinancialTypeList<Transfer> {

    private static TransferList instance = null;

    private boolean listActive = false;

    private TransferList() {
    }

    public static TransferList getInstance() {
        if (instance == null) {
            instance = new TransferList();
        }
        return instance;
    }

    @Override
    public TransferList activateList() {
        if (!listActive) {
            this.setType(FinanceType.TRANSFER);
            super.activateList();
            listActive = true;
        }
        return instance;
    }

    public void close() {
        if (instance != null) {
            instance = null;
        }
    }

    @Override
    protected void sortList() {

    }

    @Override
    protected Transfer getItem(ResultSet rs) throws SQLException {
        int transferID = rs.getInt("ID");
        Calendar transferDate = DateUtility.getCalDateFromSQL(rs.getDate("transferDate"));
        String transactionCategory = rs.getObject("transactionCategory", String.class);
        String fromAccountName = rs.getObject("fromAccountName", String.class);
        String toAccountName = rs.getObject("toAccountName", String.class);
        BigDecimal transferAmount = rs.getBigDecimal("transferAmount");
        int fromTransactionID = rs.getInt("fromTransactionID");
        int toTransactionID = rs.getInt("toTransactionID");

        return new Transfer(transferID, transferDate, transactionCategory, fromAccountName,
                toAccountName, transferAmount, fromTransactionID, toTransactionID);
    }
}
