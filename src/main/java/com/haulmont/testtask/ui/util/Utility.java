package com.haulmont.testtask.ui.util;

import com.vaadin.ui.Field;

public class Utility {

    public static boolean checkValidate(Boolean ... booleans){
        boolean result = true;
        for (Boolean aBoolean : booleans) {
            if (!aBoolean) {
                result = false;
                break;
            }
        }
        return result;
    }

    public static void clearFields(Field ... components){
        for (Field component : components) {
            component.clear();
        }
    }


}
