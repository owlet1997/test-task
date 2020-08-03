package com.haulmont.testtask.ui.util;

import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;

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

    public static void addGridComponents(Component tf1, Component tf2,
                                   Component tf3, Component tf4,
                                   Component tf5, Component tf6,
                                   GridLayout gridLayout){

        gridLayout.addComponent(tf1, 0,0);
        gridLayout.addComponent(tf2,1, 0);
        gridLayout.addComponent(tf3, 2, 0);
        gridLayout.addComponent(tf4, 3, 0);

        gridLayout.addComponent(tf5, 2, 1);
        gridLayout.addComponent(tf6, 3, 0);

    }


}
