package com.haulmont.testtask.ui.base;

import com.haulmont.testtask.DAO.ClientDAO;
import com.haulmont.testtask.ui.changes.ChangeInterface;
import com.haulmont.testtask.ui.changes.ChangeMasterPart;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class BaseWindow extends Window {

    // обновить клиента
    public BaseWindow(String caption, ClientDAO dao) {
        getWindow(caption, "200px", "1000px", new );
    }

    private void getWindow(String caption, String height,
                           String width, ChangeInterface changeInterface){
        setCaption(caption);
        setContent((Component) changeInterface);
        center();
        setHeight(height);
        setWidth(width);
        setModal(true);
        UI.getCurrent().addWindow(this);

    }
}
