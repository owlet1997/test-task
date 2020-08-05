package com.haulmont.testtask.ui.changes;

import com.haulmont.testtask.ui.window.BaseWindow;
import com.vaadin.ui.Button;

public interface ChangeInterface {

    default Button cancelButton(BaseWindow window){
        Button button = new Button("Отменить");
        button.addClickListener((Button.ClickListener) clickEvent -> {
            window.close();
        });

        return button;
    }


}
