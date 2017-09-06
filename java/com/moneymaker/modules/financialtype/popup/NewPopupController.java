package com.moneymaker.modules.financialtype.popup;

import com.moneymaker.modules.financialtype.list.FinancialTypeList;
import com.moneymaker.utilities.gui.PopupController;

/**
 * Created by Jay Damon on 8/4/2017.
 */
public abstract class NewPopupController<T extends FinancialTypeList> extends PopupController {

    protected T list;

    public void setListToAddTo(T list) {
        this.list = list;
    }

}
