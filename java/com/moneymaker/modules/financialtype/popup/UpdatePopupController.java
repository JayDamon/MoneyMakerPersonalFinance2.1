package com.moneymaker.modules.financialtype.popup;

import com.moneymaker.utilities.gui.PopupController;
import com.moneymaker.modules.financialtype.Bean;
import javafx.fxml.Initializable;

/**
 * Created by Jay Damon on 7/24/2017.
 */
public abstract class UpdatePopupController<T extends Bean> extends PopupController implements Initializable {

    protected T itemToUpdate;

    public void setItemToUpdate(T bean) {
        this.itemToUpdate = bean;
        setControlValues();
    }

    protected abstract void setControlValues();

}
