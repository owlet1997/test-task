package com.haulmont.testtask.ui.layouts;

import com.haulmont.testtask.data.DAO.MasterDAO;
import com.haulmont.testtask.data.entities.Master;
import com.haulmont.testtask.ui.window.BaseWindow;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;


public class MasterLayout extends VerticalLayout {

    public MasterLayout(){
        MasterDAO masterDAO = MasterDAO.getInstance();

         Label name = new Label("Страница информации о мастерах");
        name.setStyleName(ValoTheme.LABEL_COLORED);

        HorizontalLayout buttonPanel = new HorizontalLayout();
        buttonPanel.setMargin(true);

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();

        Grid grid = getList(masterDAO);
        grid.setSizeFull();

        Button updateButton = new Button("Изменить данные");
        updateButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
        Button addButton = new Button("Добавить мастера");
        addButton.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        Button deleteButton = new Button("Удалить мастера");
        deleteButton.setStyleName(ValoTheme.BUTTON_DANGER);
        Button statButton = new Button("Показать статистику по заказам");

        buttonPanel.addComponent(updateButton);
        buttonPanel.addComponent(addButton);
        buttonPanel.addComponent(deleteButton);
        buttonPanel.addComponent(statButton);

        updateButton.addClickListener((Button.ClickListener) clickEvent -> {
            new BaseWindow("Обновить данные о мастере", masterDAO, "update");
            refresh();
        });

        addButton.addClickListener((Button.ClickListener) clickEvent -> {
            new BaseWindow("Добавить мастера", masterDAO, "add");
            refresh();

        });

        deleteButton.addClickListener((Button.ClickListener) clickEvent ->{
            new BaseWindow("Удалить мастера", masterDAO, "delete");
            refresh();
        });

        statButton.addClickListener((Button.ClickListener) clickEvent -> {
           new BaseWindow("Статистика", masterDAO, "stat");
        });

        addComponent(name);
        addComponent(buttonPanel);
        layout.addComponent(grid);
        addComponent(layout);
    }

    private Grid getList(MasterDAO masterDAO){
        List<Master> masters = masterDAO.getMasterList();

        Grid grid = new Grid();
        grid.addColumn("Код");
        grid.addColumn("Фамилия");
        grid.addColumn("Имя");
        grid.addColumn("Отчество");
        grid.addColumn("Часовая ставка");

        masters.forEach(e -> grid.addRow(String.valueOf(e.getId()), e.getSurname(),
                e.getName(), e.getFatherName(), String.valueOf(e.getSalary())));

        return grid;
    }

    private void refresh(){
        TabSheet tabSheet = new TabSheet();

        VerticalLayout ordersLayout = new OrdersLayout();
        VerticalLayout masterLayout = new MasterLayout();
        VerticalLayout clientLayout = new ClientsLayout();

        tabSheet.addTab(masterLayout, "Мастера");
        tabSheet.addTab(ordersLayout, "Заказы");
        tabSheet.addTab(clientLayout, "Клиенты");
        UI.getCurrent().setContent(tabSheet);

    }
}
