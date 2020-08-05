package com.haulmont.testtask.ui.window;

import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.*;

public class GetNumberWindow extends Window {
    static RegexpValidator numberValidator = new RegexpValidator("^[0-9]{1,4}$", "Wrong input");

    public static String getNumberWindow(String caption, String question){
        GetNumberWindow window = new GetNumberWindow();
        window.setCaption(caption);
        GridLayout gridLayout = new GridLayout(2,2);
        Label label = new Label(question);
        TextField textField = new TextField("Введите номер");
        textField.addValidator(numberValidator);

        Button send = new Button("ОК");
        final String[] result = {null};
        send.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if (textField.isValid()){
                    result[0] = textField.getValue();
                    window.close();
                }
            }
        });
        gridLayout.addComponent(label,0,0);
        gridLayout.addComponent(send, 0,1);
        window.setContent(gridLayout);
        return result[0];

    }
}
