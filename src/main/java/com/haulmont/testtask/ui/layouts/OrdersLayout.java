package com.haulmont.testtask.ui.layouts;

import com.haulmont.testtask.data.DAO.ClientDAO;
import com.haulmont.testtask.data.DAO.MasterDAO;
import com.haulmont.testtask.data.DAO.OrderDAO;
import com.haulmont.testtask.data.DTO.OrderDTO;
import com.haulmont.testtask.ui.window.BaseWindow;
import com.vaadin.ui.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.haulmont.testtask.MainUI.getButtons;

public class OrdersLayout extends VerticalLayout {

    public OrdersLayout(){
        OrderDAO orderDAO = new OrderDAO();
        MasterDAO masterDAO = new MasterDAO();
        ClientDAO clientDAO = new ClientDAO();
        HorizontalLayout buttons = getButtons();

        Grid grid = getList(orderDAO);
        grid.setSizeFull();
        HorizontalLayout searchPanel = new HorizontalLayout();
        HorizontalLayout buttonPanel = new HorizontalLayout();

        TextField descr = new TextField("Описание");
        TextField client = new TextField("Клиент");
        TextField status = new TextField("Статус");
        Button sort = new Button("Применить");
        sort.setHeight("60px");

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setCaption("Список заказов");
        verticalLayout.addComponent(grid);

        searchPanel.addComponent(descr);
        searchPanel.addComponent(client);
        searchPanel.addComponent(status);
        searchPanel.addComponent(sort);

        sort.addClickListener((Button.ClickListener) clickEvent -> {
            Grid grid1 = getSortedList(orderDAO, descr.getValue(), client.getValue(), status.getValue());
            refresh(verticalLayout,grid1);
        });

        Button updateButton = new Button("Изменить заказ");
        Button addButton = new Button("Добавить заказ");
        Button deleteButton = new Button("Удалить заказ");

        buttonPanel.addComponent(updateButton);
        buttonPanel.addComponent(addButton);
        buttonPanel.addComponent(deleteButton);

        updateButton.addClickListener((Button.ClickListener) clickEvent -> {
            new BaseWindow("Обновить заказ", orderDAO, masterDAO);
            Grid grid1 = getList(orderDAO);
            refresh(verticalLayout, grid1);
        });

        addButton.addClickListener((Button.ClickListener) clickEvent -> {
            new BaseWindow("Добавить заказ", orderDAO, masterDAO, clientDAO);
            Grid grid1 = getList(orderDAO);
            refresh(verticalLayout, grid1);

        });

        deleteButton.addClickListener((Button.ClickListener) clickEvent ->{
            new BaseWindow("Удалить заказ", orderDAO);
            Grid grid1 = getList(orderDAO);
            refresh(verticalLayout, grid1);
        });

        addComponent(buttons);

        addComponent(buttonPanel);
        addComponent(searchPanel);
        addComponent(verticalLayout);
    }

    private void refresh(VerticalLayout layout, Grid newGrid){
    layout.removeAllComponents();
    layout.addComponent(newGrid);
    }

    private Grid getList(OrderDAO orderDAO){
        List<OrderDTO> orderList = orderDAO.getOrderDTOList();

        return getComponents(orderList);
    }

    private Grid getSortedList(OrderDAO orderDAO, String descr, String client, String status){
        if (descr==null) descr = "";
        if (client==null) client = "";
        if (status==null) status = "";
        List<OrderDTO> orderList = orderDAO.getOrderDTOList();

        String finalClient = client;
        String finalDescr = descr;
        String finalStatus = status;
        List<OrderDTO> sortedList = orderList.stream().filter(e -> e.getClientSurname().contains(finalClient))
                                                      .filter(e -> e.getDescription().contains(finalDescr))
                                                      .filter(e -> e.getStatus().contains(finalStatus))
                                                      .collect(Collectors.toList());
        return getComponents(sortedList);
    }

    private Grid getComponents(List<OrderDTO> sortedList) {
        Grid grid = new Grid();
        grid.addColumn("Номер заказа");
        grid.addColumn("Описание");
        grid.addColumn("Клиент");
        grid.addColumn("Мастер");
        grid.addColumn("Дата начала работ");
        grid.addColumn("Дата окончания работ");
        grid.addColumn("Стоимость");
        grid.addColumn("Статус");

        sortedList.forEach(e -> grid.addRow(String.valueOf(e.getId()), e.getDescription(), e.getClientSurname(), e.getMasterSurname(),
                e.getCreateDate(), e.getFinishDate(), String.valueOf(e.getPrice()), e.getStatus()));

        grid.setSizeFull();
        grid.setHeightByRows(20);
        return grid;
    }

}
