package com.haulmont.testtask.ui.layouts;

import com.haulmont.testtask.data.DAO.ClientDAO;
import com.haulmont.testtask.data.entities.Client;
import com.haulmont.testtask.ui.window.BaseWindow;
import com.vaadin.ui.*;

import java.util.List;

import static com.haulmont.testtask.MainUI.getButtons;

public class ClientsLayout extends VerticalLayout {
    public ClientsLayout(){
        ClientDAO clientDAO = new ClientDAO();
        HorizontalLayout buttons = getButtons();

        HorizontalLayout buttonPanel = new HorizontalLayout();

        Button updateButton = new Button("Изменить данные");
        Button addButton = new Button("Добавить клиента");
        Button deleteButton = new Button("Удалить клиента");

        Grid grid = getList(clientDAO);

        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(grid);


        updateButton.addClickListener((Button.ClickListener) clickEvent -> {
            new BaseWindow("Обновить клиента",clientDAO, "update");
            Grid grid1 = getList(clientDAO);
            refresh(layout, grid1);
        });

        addButton.addClickListener((Button.ClickListener) clickEvent -> {
            new BaseWindow("Добавить клиента", clientDAO, "add");
            Grid grid1 = getList(clientDAO);
            refresh(layout, grid1);
        });

        deleteButton.addClickListener((Button.ClickListener) clickEvent ->{
            new BaseWindow("Удалить клиента", clientDAO, "delete");
            Grid grid1 = getList(clientDAO);
            refresh(layout, grid1);
        });

        buttonPanel.addComponent(addButton);
        buttonPanel.addComponent(updateButton);
        buttonPanel.addComponent(deleteButton);

        addComponent(buttons);
        addComponent(buttonPanel);
        addComponent(layout);
    }

    private void refresh(VerticalLayout layout, Grid newGrid){
        layout.removeAllComponents();
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

        return grid;
    }
}
