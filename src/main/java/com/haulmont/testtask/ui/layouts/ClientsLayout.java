package com.haulmont.testtask.ui.layouts;

import com.haulmont.testtask.data.DAO.ClientDAO;
import com.haulmont.testtask.data.entities.Client;
import com.haulmont.testtask.ui.window.BaseWindow;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

public class ClientsLayout extends VerticalLayout {

    public ClientsLayout(){
        ClientDAO clientDAO = ClientDAO.getInstance();

        Label name = new Label("Страница информации о клиентах");
        name.setStyleName(ValoTheme.LABEL_COLORED);
        name.setStyleName(ValoTheme.LABEL_H2);

        HorizontalLayout buttonPanel = new HorizontalLayout();
        buttonPanel.setMargin(true);

        Button updateButton = new Button("Изменить данные");
        Button addButton = new Button("Добавить клиента");
        Button deleteButton = new Button("Удалить клиента");

        updateButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
        addButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        deleteButton.setStyleName(ValoTheme.BUTTON_DANGER);

        buttonPanel.addComponent(addButton);
        buttonPanel.addComponent(updateButton);
        buttonPanel.addComponent(deleteButton);

        Grid grid = getList(clientDAO);
        grid.setSizeFull();

        updateButton.addClickListener((Button.ClickListener) clickEvent -> {
            new BaseWindow("Обновить клиента",clientDAO, "update");
            refresh();
        });

        addButton.addClickListener((Button.ClickListener) clickEvent -> {
            new BaseWindow("Добавить клиента", clientDAO, "add");
            refresh();
        });

        deleteButton.addClickListener((Button.ClickListener) clickEvent ->{
            new BaseWindow("Удалить клиента", clientDAO, "delete");
            refresh();
        });

        addComponent(name);
        addComponent(buttonPanel);
        addComponent(grid);
    }

    private Grid getList(ClientDAO clientDAO){
        List<Client> clients = clientDAO.getClientList();

        Grid grid = new Grid();
        grid.addColumn("Код");
        grid.addColumn("Фамилия");
        grid.addColumn("Имя");
        grid.addColumn("Отчество");
        grid.addColumn("Телефон");

        clients.forEach(e -> grid.addRow(String.valueOf(e.getId()), e.getSurname(),
                e.getName(), e.getFatherName(), e.getPhone()));

        grid.setHeightByRows(clients.size());
        return grid;
    }

    private void refresh(){
        TabSheet tabSheet = new TabSheet();

        VerticalLayout ordersLayout = new OrdersLayout();
        VerticalLayout masterLayout = new MasterLayout();
        VerticalLayout clientLayout = new ClientsLayout();

        tabSheet.addTab(clientLayout, "Клиенты");
        tabSheet.addTab(ordersLayout, "Заказы");
        tabSheet.addTab(masterLayout, "Мастера");

        UI.getCurrent().setContent(tabSheet);

    }
}
