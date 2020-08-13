package com.haulmont.testtask.ui.layouts;

import com.haulmont.testtask.MainUI;
import com.haulmont.testtask.data.DAO.ClientDAO;
import com.haulmont.testtask.data.DAO.MasterDAO;
import com.haulmont.testtask.data.DAO.OrderDAO;
import com.haulmont.testtask.data.DTO.OrderDTO;
import com.haulmont.testtask.ui.window.BaseWindow;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrdersLayout extends VerticalLayout {

    public OrdersLayout(){
        OrderDAO orderDAO = OrderDAO.getInstance();
        MasterDAO masterDAO = MasterDAO.getInstance();
        ClientDAO clientDAO = ClientDAO.getInstance();

        Grid grid = getList(orderDAO);
        grid.setSizeFull();
        HorizontalLayout searchPanel = new HorizontalLayout();
        searchPanel.setMargin(true);

        Label name = new Label("Страница информации о заказах");
        name.setStyleName(ValoTheme.LABEL_COLORED);
        name.setStyleName(ValoTheme.LABEL_H2);

        HorizontalLayout buttonPanel = new HorizontalLayout();
        buttonPanel.setMargin(true);

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
            refresh(this, getSortedList(orderDAO, Optional.of(descr.getValue()),
                    Optional.of(client.getValue()), Optional.of(status.getValue())),
                    buttonPanel, searchPanel, name);
        });

        Button updateButton = new Button("Изменить заказ");
        Button addButton = new Button("Добавить заказ");
        Button deleteButton = new Button("Удалить заказ");

        updateButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
        addButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        deleteButton.setStyleName(ValoTheme.BUTTON_DANGER);

        buttonPanel.addComponent(updateButton);
        buttonPanel.addComponent(addButton);
        buttonPanel.addComponent(deleteButton);

        updateButton.addClickListener((Button.ClickListener) clickEvent -> {
            new BaseWindow("Обновить заказ", orderDAO, masterDAO);
            refresh();
        });

        addButton.addClickListener((Button.ClickListener) clickEvent -> {
            new BaseWindow("Добавить заказ", orderDAO, masterDAO, clientDAO);
            refresh();

        });

        deleteButton.addClickListener((Button.ClickListener) clickEvent ->{
            new BaseWindow("Удалить заказ", orderDAO);
           refresh();
        });

        addComponent(name);
        addComponent(buttonPanel);
        addComponent(searchPanel);
        addComponent(verticalLayout);
    }

    private Grid getList(OrderDAO orderDAO){
        List<OrderDTO> orderList = orderDAO.getOrderDTOList();

        return getComponents(orderList);
    }

    private Grid getSortedList(OrderDAO orderDAO, Optional<String> descr, Optional<String> client, Optional<String> status){

        List<OrderDTO> orderList = orderDAO.getOrderDTOList();

        List<OrderDTO> sortedList = orderList.stream().filter(e -> e.getClientSurname().contains(client.orElse("")))
                                                      .filter(e -> e.getDescription().contains(descr.orElse("")))
                                                      .filter(e -> e.getStatus().contains(status.orElse("")))
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

    private void refresh(){
        TabSheet tabSheet = new TabSheet();

        VerticalLayout ordersLayout = new OrdersLayout();
        VerticalLayout masterLayout = new MasterLayout();
        VerticalLayout clientLayout = new ClientsLayout();

        tabSheet.addTab(ordersLayout, "Заказы");
        tabSheet.addTab(masterLayout, "Мастера");
        tabSheet.addTab(clientLayout, "Клиенты");
        UI.getCurrent().setContent(tabSheet);

    }


    private void refresh(VerticalLayout layout, Grid grid, HorizontalLayout buttonPanel, HorizontalLayout searchPanel, Label label){
        layout.removeAllComponents();
        layout.addComponent(label);
        grid.setSizeFull();
        layout.addComponent(buttonPanel);
        layout.addComponent(searchPanel);
        layout.addComponent(grid);
    }

}
