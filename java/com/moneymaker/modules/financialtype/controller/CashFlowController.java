package com.moneymaker.modules.financialtype.controller;

import com.moneymaker.modules.financialtype.CashFlow;
import com.moneymaker.modules.financialtype.list.*;
import com.moneymaker.utilities.DateUtility;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.math.BigDecimal;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.ResourceBundle;

/**
 * Cash Flow created by Jay Damon on 9/19/2016.
 */
public class CashFlowController extends FinancialTypeController<CashFlow> {

    @FXML
    private ListView<String> listViewMonths;
    @FXML
    private ListView<Integer> listViewYears;

    public CashFlowController() {
        super("", "");
    }

    public void initialize(URL url, ResourceBundle rs) {
        Calendar today = DateUtility.getCalBeginningOfDay();
        int thisYearInt = today.get(Calendar.YEAR);
        String thisYear = String.valueOf(thisYearInt);
        String thisMonth = new DateFormatSymbols().getMonths()[today.get(Calendar.MONTH)];
        showCashFlow(today);
        addMonths(thisMonth);
        addYears(thisYear);

        listViewMonths.setOnMouseClicked(event -> updateVisibleCashFlow());
        listViewYears.setOnMouseClicked(event -> updateVisibleCashFlow());

        addListListeners();

    }

    private void addListListeners() {
        ArrayList<FinancialTypeList> lists = new ArrayList<>();
        lists.add(TransactionList.getInstance().activateList());
        lists.add(AccountList.getInstance().activateList());
        lists.add(GoalList.getInstance().activateList());
        lists.add(BudgetList.getInstance().activateList());
        lists.add(TransferList.getInstance().activateList());
        lists.add(RecurringTransactionList.getInstance().activateList());

        for (FinancialTypeList f : lists) {
            //noinspection unchecked
            f.getList().addListener((ListChangeListener) c -> updateVisibleCashFlow());
        }
    }

    @Override
    protected void launchContextMenu(double x, double y) {

    }

    private void updateVisibleCashFlow() {
        int selectedMonthInt = listViewMonths.getSelectionModel().getSelectedIndex();
        int selectedYearInt = listViewYears.getSelectionModel().getSelectedItem();
        if (selectedMonthInt != -1 && selectedYearInt != -1) {
            Calendar selectedDate = DateUtility.getCalBeginningOfDay();
            DateUtility.setCalDate(selectedDate, selectedYearInt, selectedMonthInt, 1);
            showCashFlow(selectedDate);
        }
    }

    private void showCashFlow(Calendar displayDate) {
        AccountList accountList = AccountList.getInstance().activateList();

        BigDecimal cohStarting = BigDecimal.ZERO; //coh = Cash on Hand
        BigDecimal cohCurrent = BigDecimal.ZERO;
        int currentTracker = 0;
        CashFlowList.getInstance().close();
        CashFlowList cashFlow = CashFlowList.getInstance().activateList(displayDate);

    //Sort cash Flow list which is the combined list of budget items and recurring transactions
        cashFlow.sortList();

    //Get balance on last day of previous time period (ptp)
        Calendar ptpDate = DateUtility.getCalBeginningOfDay();

        DateUtility.setCalDate(
                ptpDate,
                displayDate.get(Calendar.YEAR),
                displayDate.get(Calendar.MONTH) - 1,
                displayDate.getActualMaximum(Calendar.DAY_OF_MONTH));
        BigDecimal cohActual = accountList.getPrimaryAccountBalance(ptpDate) != null ?
                        accountList.getPrimaryAccountBalance(ptpDate) :
                        BigDecimal.ZERO;

        Calendar currentDate = DateUtility.getCalBeginningOfDay();  //Today's Date

    //Loop through Cash Flow list
        for (CashFlow c: cashFlow.getList()) {
//            System.out.println(c.getCategory());
            Calendar cashFlowDate = (Calendar)c.getCalDate().clone();  //Used to store date of this Cash Flow item
            cashFlowDate.set(Calendar.HOUR_OF_DAY, 0);
        //Compare the Cash Flow date to find out if the Cash Flow Date is before or after today's date
            boolean beforeToday = currentDate.compareTo(cashFlowDate) >= 0; // 0 is equal to, 1 already happened, -1 hasn't yet happened

            //The end date will be the last end date for this Cash Flow Item based on its frequency
            Calendar endDate = DateUtility.getCalBeginningOfDay();
            int lastDayOfMonth = cashFlowDate.getActualMaximum(Calendar.DAY_OF_MONTH);  //Get last day of month

            //Set end date as last day of month
            DateUtility.setCalDate(endDate,
                    cashFlowDate.get(Calendar.YEAR),
                    cashFlowDate.get(Calendar.MONTH)-1,
                    lastDayOfMonth);

            if (beforeToday) {
                switch (currentTracker) {
                    case 0:
                        cohStarting = cohStarting.add(c.getBdProjected());
                        cohActual = cohActual.add(c.getBdActual());
                        currentTracker++;
                        break;
                    case 1:
                        cohCurrent = BigDecimal.ZERO;
                        cohStarting = cohStarting.add(c.getBdProjected());
                        cohActual = cohActual.add(c.getBdActual());
//                        cohActual = cohActual.add(c.getBdActual());
                        currentTracker++;
                        break;

                    case 2:
                        cohCurrent = BigDecimal.ZERO;
                        cohStarting = cohStarting.add(c.getBdProjected());
                        cohActual = cohActual.add(c.getBdActual());
                        break;
                }
            } else //noinspection ConstantConditions
                if (!beforeToday) {
                if (cohCurrent.compareTo(BigDecimal.ZERO) == 0) {
                    cohCurrent = cohActual.add(c.getBdProjected());
                    cohActual = BigDecimal.ZERO;
                } else {
                    cohCurrent = cohCurrent.add(c.getBdProjected());
                }
                cohStarting = cohStarting.add(c.getBdProjected());
//                cohActual = cohActual.add(c.getBdActual());
//                cohCurrent = cohCurrent.add(c.getBdProjected());
            }
            c.setCohCurrent(cohCurrent);
            c.setCohStarting(cohStarting);
            c.setCohActual(cohActual);

            // ToDo how do i handle accounts.  Currently it pulls Capital One Balance, but i probably want to pull by checking with capital one accounted for
        }
        ObservableList<CashFlow> table = primaryTable.getItems();
        table.clear();
        table.addAll(cashFlow.getList());
    }

    private void addMonths(String thisMonth) {
        ObservableList<String> monthsList = FXCollections.observableArrayList();

        String[] months = new DateFormatSymbols().getMonths();
        Collections.addAll(monthsList, months);

        listViewMonths.getItems().clear();
        listViewMonths.setItems(monthsList);
        int count = 0;
        for (Object o : listViewMonths.getItems()) {
            if (o.equals(thisMonth)) {
                listViewMonths.getSelectionModel().select(count);
                listViewMonths.scrollTo(count);
                break;
            }
            count++;
        }
    }

    private void addYears(String thisYear) {
        ObservableList<Integer> yearList = FXCollections.observableArrayList();

        for (int i = 1980; i < 2050; i++) {
            yearList.add(i);
        }

        listViewYears.setItems(yearList);
        int count = 0;
        for(Integer o : listViewYears.getItems()) {
            if (o.toString().equals(thisYear)) {
                listViewYears.getSelectionModel().select(count);
                listViewYears.scrollTo(count);
                break;
            }
            count++;
        }
    }
}
