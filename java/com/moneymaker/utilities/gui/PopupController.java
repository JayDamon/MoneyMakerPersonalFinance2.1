package com.moneymaker.utilities.gui;

import com.moneymaker.utilities.RequiredFieldList;
import com.moneymaker.utilities.AutoCompleteComboBoxListener;
import com.moneymaker.utilities.DateUtility;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

/**
 * Created by Jay Damon on 7/24/2017.
 */
public abstract class PopupController implements Initializable {

    protected static final String FX_CONTROL_INNER_BACKGROUND_WHITE = "-fx-control-inner-background: white";

    @FXML
    protected Pane primaryPane;

    @FXML
    private Button buttonConfirm = new Button();
    public Button buttonExit = new Button();

    protected RequiredFieldList requiredFieldList;

    public void initialize(URL url, ResourceBundle rs) {
        fillPrePopulatedControls();
        addAutoCompleteListener();
        setRequiredFields();
        setButtonConfirmListener();
    }

    protected abstract ArrayList<ComboBox> comboBoxesForAutoComplete();

    private void addAutoCompleteListener() {
        if (comboBoxesForAutoComplete() != null) {
            for (ComboBox c : comboBoxesForAutoComplete()) {
                new AutoCompleteComboBoxListener(c);
            }
        }
    }

    protected abstract void fillPrePopulatedControls();

    protected abstract ArrayList<Control> requiredFieldList();

    private void setRequiredFields() {
        requiredFieldList = new RequiredFieldList(requiredFieldList());
    }

    private void setButtonConfirmListener() {
        buttonConfirm.setOnMouseClicked(e -> {
            if (requiredFieldList.isComplete()) {
                confirmOperation();
                exitWindow();
            }
        });
    }

    protected abstract void confirmOperation();

    @FXML
    protected void exitWindow() {
        Stage stage = (Stage)primaryPane.getScene().getWindow();
        stage.close();
    }

    protected Calendar datePickerToCalendar(DatePicker picker) {
        if (picker.getValue() != null) {
            return DateUtility.parseStringCalendar(picker.getValue().toString());
        } return null;
    }

    protected void addDateToDatePicker(Calendar c, DatePicker d) {
        if (c != null) {
            int day = c.get(Calendar.DAY_OF_MONTH);
            int month = c.get(Calendar.MONTH) + 1;
            int year = c.get(Calendar.YEAR);
            d.setValue(LocalDate.of(year, month, day));
        }
    }
}
