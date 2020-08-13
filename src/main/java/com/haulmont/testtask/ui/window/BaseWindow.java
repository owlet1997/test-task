package com.haulmont.testtask.ui.window;

import com.haulmont.testtask.data.DAO.ClientDAO;
import com.haulmont.testtask.data.DAO.MasterDAO;
import com.haulmont.testtask.data.DAO.OrderDAO;
import com.haulmont.testtask.data.entities.Client;
import com.haulmont.testtask.ui.modals.ChangeInterface;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

import static com.haulmont.testtask.ui.modals.ChangeClientModal.*;
import static com.haulmont.testtask.ui.modals.ChangeMasterModal.*;
import static com.haulmont.testtask.ui.modals.ChangeOrderModal.*;

public class BaseWindow extends Window {

    public BaseWindow(String caption, ClientDAO dao, Client client, String flag) {
        switch (flag){
            case "update":
                getWindow(caption, updateClient(dao, client,this));
                break;
            case "delete":
                getWindow(caption, deleteClient(dao, client,this));
                break;
        }
    }
    public BaseWindow(String caption,  ClientDAO dao){
        getWindow(caption,  addClient(dao, this));
    }

    public BaseWindow(String caption, MasterDAO dao, String flag){
        switch (flag){
            case "delete":
                getWindow(caption,  deleteMaster(dao, this));
                break;
            case "add":
                getWindow(caption,  addMaster(dao,this));
                break;
            case "update":
                getWindow(caption,  updateMaster(dao, this));
                break;
            case "stat":
                getWindow(caption,  getStatistics(dao, this));
                break;
        }

    }

    // добавить заказ
    public BaseWindow(String caption, OrderDAO orderDAO, MasterDAO masterDAO, ClientDAO clientDAO){
        getWindow(caption,  addOrder(orderDAO,masterDAO,clientDAO,this));
    }

    // удалить заказ
    public BaseWindow(String caption, OrderDAO orderDAO){
        getWindow(caption,  deleteOrder(orderDAO, this));
    }

//    обновить заказ
    public BaseWindow(String caption, OrderDAO orderDAO, MasterDAO masterDAO){
        getWindow(caption,updateOrder(orderDAO, masterDAO, this));
    }

    private void getWindow(String caption, ChangeInterface changeInterface){
        setCaption(caption);
        setContent((Component) changeInterface);
        center();
        setModal(true);
        UI.getCurrent().addWindow(this);
    }
}
