package com.haulmont.testtask.ui.window;

import com.haulmont.testtask.data.DAO.ClientDAO;
import com.haulmont.testtask.data.DAO.MasterDAO;
import com.haulmont.testtask.data.DAO.OrderDAO;
import com.haulmont.testtask.ui.changes.ChangeInterface;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

import static com.haulmont.testtask.ui.changes.ChangeClientPart.*;
import static com.haulmont.testtask.ui.changes.ChangeMasterPart.*;
import static com.haulmont.testtask.ui.changes.ChangeOrderPart.*;

public class BaseWindow extends Window {

    public BaseWindow(String caption, ClientDAO dao, String flag) {
        switch (flag){
            case "update":
                getWindow(caption, "400px", "1500px", updateClient(dao, this));
                break;
            case "add":
                getWindow(caption, "400px", "1500px", addClient(dao, this));
                break;
            case "delete":
                getWindow(caption, "400px", "1500px", deleteClient(dao, this));
                break;
        }
    }

    public BaseWindow(String caption, MasterDAO dao, String flag){
        switch (flag){
            case "delete":
                getWindow(caption, "400px", "1500px", deleteMaster(dao, this));
                break;
            case "add":
                getWindow(caption, "400px", "1500px", addMaster(dao,this));
                break;
            case "update":
                getWindow(caption, "400px", "1500px", updateMaster(dao, this));
                break;
            case "stat":
                getWindow(caption, "500px", "1500px", getStatistics(dao, this));
                break;
        }

    }

    // добавить заказ
    public BaseWindow(String caption, OrderDAO orderDAO, MasterDAO masterDAO, ClientDAO clientDAO){
        getWindow(caption, "400px", "1500px", addOrder(orderDAO,masterDAO,clientDAO,this));
    }

    // удалить заказ
    public BaseWindow(String caption, OrderDAO orderDAO){
        getWindow(caption, "400px", "1500px", deleteOrder(orderDAO, this));
    }

//    обновить заказ
    public BaseWindow(String caption, OrderDAO orderDAO, MasterDAO masterDAO){
        getWindow(caption,"400px", "1500px", updateOrder(orderDAO, masterDAO, this));
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
