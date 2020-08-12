package com.haulmont.testtask.ui.layouts;

import com.haulmont.testtask.data.DAO.ClientDAO;
import com.haulmont.testtask.data.entities.Client;
import com.haulmont.testtask.ui.window.BaseWindow;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

public class ClientsLayout extends VerticalLayout {

    public ClientsLayout(){
        ClientDAO clientDAO = new ClientDAO();

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
            refresh(this, buttonPanel, name, getList(clientDAO));
        });

        addButton.addClickListener((Button.ClickListener) clickEvent -> {
            new BaseWindow("Добавить клиента", clientDAO, "add");
            refresh(this, buttonPanel, name, getList(clientDAO));
        });

        deleteButton.addClickListener((Button.ClickListener) clickEvent ->{
            new BaseWindow("Удалить клиента", clientDAO, "delete");
            refresh(this, buttonPanel, name, getList(clientDAO));
        });

        addComponent(name);
        addComponent(buttonPanel);
        addComponent(grid);
    }

    private void refresh(VerticalLayout layout, HorizontalLayout panel,
                         Label label, Grid newGrid){
        layout.removeAllComponents();
        layout.addComponent(label);
        layout.addComponent(panel);
        newGrid.setSizeFull();
        layout.addComponent(newGrid);
    }


    private Grid getList(ClientDAO clientDAO){
        List<Client> clients = clientDAO.getClientList();

        Grid grid = new Grid();
        grid.addColumn("Код");
        grid.addColumn("Фамилия");
        grid.addColumn("Имя");
        grid.addColumn("Отчество");
        grid.addColumn("Телефон");

        clients.forEach(e -> grid.addRow(String.valueOf(e.getId()), e.getLastName(),
                e.getFirstName(), e.getFatherName(), e.getPhone()));

        grid.setHeightByRows(clients.size());
        return grid;
    }
}
