package com.moneymaker.modules.budgetmanager;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Jay Damon on 10/8/2016.
 */
public class BudgetGraph {

    private final SimpleStringProperty budget = new SimpleStringProperty("");
    private final SimpleStringProperty plannedAmount = new SimpleStringProperty("");
    private final SimpleStringProperty actualAmount = new SimpleStringProperty("");

    public BudgetGraph(String budget, String plannedAmount, String actualAmount) {
        setBudget(budget);
        setPlannedAmount(plannedAmount);
        setActualAmount(actualAmount);
    }

    public String getBudget() {
        return budget.get();
    }

    private void setBudget(String budget) {
        this.budget.set(budget);
    }

    public String getPlannedAmount() {
        return plannedAmount.get();
    }

    private void setPlannedAmount(String plannedAmount) {
        this.plannedAmount.set(plannedAmount);
    }

    public String getActualAmount() {
        return actualAmount.get();
    }

    private void setActualAmount(String actualAmount) {
        this.actualAmount.set(actualAmount);
    }
}
