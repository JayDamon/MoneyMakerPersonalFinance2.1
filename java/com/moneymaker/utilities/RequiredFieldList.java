package com.moneymaker.utilities;

import javafx.scene.control.*;

import javax.xml.soap.Text;
import java.util.ArrayList;

/**
 * Created by Jay Damon on 8/17/2017.
 */
public class RequiredFieldList {

    private ArrayList<Control> requiredFieldList;
    private final String whiteBackgroundStyle = "-fx-control-inner-background: white";
    private String labelWhiteBackgroundStyle = "-fx-background-color: white";

    public RequiredFieldList(ArrayList<Control> controls) {
        this.requiredFieldList = controls;
        formatRequiredFieldsOnInput();
    }

    private void formatRequiredFieldsOnInput() {
        if (requiredFieldList != null) {
            for (Control c : requiredFieldList) {
                if (c.getClass().equals(Label.class)) {
                    Label l = (Label) c;
                    setValueListener(l);
                } else if (c.getClass().equals(ComboBox.class)) {
                    ComboBox cmb = (ComboBox)c;
                    setValueListener(cmb);
                } else if (c.getClass().equals(TextField.class)) {
                    TextField txt = (TextField)c;
                    seValueListener(txt);
                } else if (c.getClass().equals(DatePicker.class)) {
                    DatePicker date = (DatePicker)c;
                    setValueListener(date);
                }
            }
        }
    }

    private void setValueListener(DatePicker d) {
        d.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue) && d.getValue() != null) {
                d.setStyle(whiteBackgroundStyle);
            }
        });
    }

    private void seValueListener(TextField t) {
        t.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue) && !t.getText().isEmpty()) {
                t.setStyle(whiteBackgroundStyle);
            }
        });
    }

    private void setValueListener(ComboBox c) {
        c.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (c.getItems().contains(newValue)) {
                c.setStyle(whiteBackgroundStyle);
            }
        });
    }

    private void setValueListener(Label l) {
        l.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue) && !l.getText().equals("")) {
                l.setStyle(labelWhiteBackgroundStyle);
            }
        });
    }

    private boolean controlIsComplete(Control c) {
        if(c.getClass().equals(TextField.class)) {
            TextField t = (TextField) c;
            return !t.getText().isEmpty();
        }
        if(c.getClass().equals(ComboBox.class)) {
            ComboBox cb = (ComboBox) c;
            return cb.getSelectionModel().getSelectedIndex() != -1 && cb.getSelectionModel().getSelectedItem() != null;
        }
        if(c.getClass().equals(DatePicker.class)) {
            DatePicker d = (DatePicker) c;
            return d.getValue() != null;
        }
        if(c.getClass().equals(Label.class)) {
            Label l = (Label) c;
            return !l.getText().equals("");
        }
        return false;
    }

    public boolean isComplete() {
        boolean isComplete = true;
        if (requiredFieldList != null) {
            for (Control c : requiredFieldList) {
                if (!controlIsComplete(c)) {
                    final String redBackgroundStyle;
                    if (!c.getClass().equals(Label.class)) {
                        redBackgroundStyle = "-fx-control-inner-background: red";
                    } else {
                        redBackgroundStyle = "-fx-background-color: red";
                    }
                    c.setStyle(redBackgroundStyle);
                    isComplete = false;
                }
            }
        }
        return isComplete;
    }

}
