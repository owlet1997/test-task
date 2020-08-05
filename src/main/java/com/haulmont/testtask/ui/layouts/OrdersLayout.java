package com.haulmont.testtask.ui.layouts;

import com.haulmont.testtask.data.DAO.ClientDAO;
import com.haulmont.testtask.data.DAO.MasterDAO;
import com.haulmont.testtask.data.DAO.OrderDAO;
import com.haulmont.testtask.data.DTO.OrderDTO;
import com.haulmont.testtask.data.entities.Order;
import com.haulmont.testtask.ui.window.BaseWindow;
import com.haulmont.testtask.ui.window.GetNumberWindow;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.VerticalLayout;

import java.util.List;

public class OrdersLayout extends VerticalLayout {

    public OrdersLayout(){
        OrderDAO orderDAO = new OrderDAO();
        MasterDAO masterDAO = new MasterDAO();
        ClientDAO clientDAO = new ClientDAO();

        GridLayout gridLayout = new GridLayout(5,5);
        List<OrderDTO> orderList = orderDAO.getOrderDTOList("", "");

        Grid grid = getList(orderDAO);
        Button updateButton = new Button("Изменить заказ");
        Button addButton = new Button("Добавить заказ");
        Button deleteButton = new Button("Удалить заказ");

        updateButton.addClickListener((Button.ClickListener) clickEvent -> {
            String id = GetNumberWindow.getNumberWindow("Номер заказа","Введите номер заказа");
            BaseWindow baseWindow = new BaseWindow("Обновить заказ", orderDAO, id, masterDAO);
        });

        addButton.addClickListener((Button.ClickListener) clickEvent -> {
            BaseWindow baseWindow = new BaseWindow("Добавить заказ", orderDAO, masterDAO, clientDAO);

        });

        deleteButton.addClickListener((Button.ClickListener) clickEvent ->{
            String id = GetNumberWindow.getNumberWindow("Номер заказа","Введите номер заказа");
            BaseWindow baseWindow = new BaseWindow("Удалить заказ", orderDAO, id);
        });




    }

    private void refresh(OrderDAO orderDAO){

    }

    private Grid getList(OrderDAO orderDAO){
        List<OrderDTO> orderList = orderDAO.getOrderDTOList("", "");

        Grid grid = new Grid();
        grid.addColumn("Номер заказа");
        grid.addColumn("Клиент");
        grid.addColumn("Мастер");
        grid.addColumn("Дата начала работ");
        grid.addColumn("Дата окончания работ");
        grid.addColumn("Стоимость");
        grid.addColumn("Статус");

        orderList.forEach(e -> grid.addRow(e.getId(), e.getClientSurname(), e.getMasterSurname(),
                e.getCreateDate(), e.getFinishDate(), e.getPrice(), e.getPrice()));

        return grid;
    }

}
