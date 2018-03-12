package com.moneymaker.modules.financialtype;

import com.moneymaker.modules.financialtype.behavior.FinanceType;
import com.moneymaker.modules.financialtype.behavior.FinancialTypeBehavior;
import javafx.beans.property.SimpleIntegerProperty;

import java.beans.Beans;

/**
 * Created by Jay Damon on 7/20/2017.
 */

public abstract class Bean extends Beans {
    private FinancialTypeBehavior behavior;
    private final SimpleIntegerProperty id = new SimpleIntegerProperty(0);
    private final FinanceType type;

    Bean(FinanceType type) {
        this.type = type;
    }

    public int getID() {
        return id.get();
    }

    public void setId(int id) {
        idProperty().set(id);
    }

    void setBehavior(FinancialTypeBehavior t) {
        this.behavior = t;
    }

    public FinanceType getFinanceType() {
        return this.type;
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public FinancialTypeBehavior getBehavior() {
        return this.behavior;
    }
}
