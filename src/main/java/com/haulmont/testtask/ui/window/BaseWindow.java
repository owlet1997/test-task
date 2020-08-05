package com.haulmont.testtask.ui.window;

import com.haulmont.testtask.data.DAO.ClientDAO;
import com.haulmont.testtask.data.DAO.MasterDAO;
import com.haulmont.testtask.data.DAO.OrderDAO;
import com.haulmont.testtask.data.entities.Client;
import com.haulmont.testtask.data.entities.Master;
import com.haulmont.testtask.data.enums.Status;
import com.haulmont.testtask.ui.changes.ChangeClientPart;
import com.haulmont.testtask.ui.changes.ChangeInterface;
import com.haulmont.testtask.ui.changes.ChangeMasterPart;
import com.haulmont.testtask.ui.changes.ChangeOrderPart;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class BaseWindow extends Window {

    // обновить клиента
    public BaseWindow(String caption, ClientDAO dao, Client client) {
        getWindow(caption, "200px", "1000px", new ChangeClientPart(dao, this,client));
    }

    // добавить клиента
    public BaseWindow(String caption, ClientDAO dao) {
        getWindow(caption, "200px", "1000px", new ChangeClientPart(dao, this));
    }

    // удалить клиента
    public BaseWindow(String caption, ClientDAO dao, String id) {
        getWindow(caption, "200px", "1000px", new ChangeClientPart(dao, id,this));
    }

    // добавить мастера
    public BaseWindow(String caption, MasterDAO dao){
        getWindow(caption, "200px", "1000px", new ChangeMasterPart(dao,this));
    }

    // удалить мастера
    public BaseWindow(String caption, MasterDAO dao, String id){
        getWindow(caption, "200px", "1000px", new ChangeMasterPart(dao, id, this));
    }

    // обновить мастера
    public BaseWindow(String caption, MasterDAO dao, Master master){
        getWindow(caption, "200px", "1000px",new ChangeMasterPart(dao, this, master));
    }

    public BaseWindow(String caption, MasterDAO masterDAO, Status status){
        getWindow(caption, "500px", "1000px", new ChangeMasterPart(masterDAO, this, Status.PLANNED));
    }

    // добавить заказ
    public BaseWindow(String caption, OrderDAO orderDAO, MasterDAO masterDAO, ClientDAO clientDAO){
        getWindow(caption, "200px", "1000px",new ChangeOrderPart(orderDAO,masterDAO,clientDAO,this));
    }

    // удалить заказ
    public BaseWindow(String caption, OrderDAO orderDAO, String id){
        getWindow(caption, "200px", "1000px",new ChangeOrderPart(orderDAO, id, this));
    }

//    обновить заказ
    public BaseWindow(String caption, OrderDAO orderDAO, String id, MasterDAO masterDAO){
        getWindow(caption,"200px", "1000px", new ChangeOrderPart(orderDAO, masterDAO, id, this));
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
