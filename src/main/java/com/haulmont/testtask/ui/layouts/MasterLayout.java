package com.haulmont.testtask.ui.layouts;

import com.haulmont.testtask.data.DAO.MasterDAO;
import com.haulmont.testtask.data.entities.Master;
import com.haulmont.testtask.ui.window.BaseWindow;
import com.vaadin.ui.*;

import java.util.List;

import static com.haulmont.testtask.MainUI.getButtons;

public class MasterLayout extends VerticalLayout {

    public MasterLayout(){
        MasterDAO masterDAO = new MasterDAO();
        HorizontalLayout buttons = getButtons();

        HorizontalLayout buttonPanel = new HorizontalLayout();
        buttonPanel.setCaption("Страница информации о мастерах");

        VerticalLayout layout = new VerticalLayout();
        Grid grid = getList(masterDAO);
        grid.setSizeFull();

        Button updateButton = new Button("Изменить данные");
        Button addButton = new Button("Добавить мастера");
        Button deleteButton = new Button("Удалить мастера");
        Button statButton = new Button("Показать статистику по заказам");

        buttonPanel.addComponent(updateButton);
        buttonPanel.addComponent(addButton);
        buttonPanel.addComponent(deleteButton);
        buttonPanel.addComponent(statButton);

        updateButton.addClickListener((Button.ClickListener) clickEvent -> {
            new BaseWindow("Обновить данные о мастере", masterDAO, "update");
            Grid grid1 = getList(masterDAO);
            refresh(layout, grid1);
        });

        addButton.addClickListener((Button.ClickListener) clickEvent -> {
            new BaseWindow("Добавить мастера", masterDAO, "add");
            Grid grid1 = getList(masterDAO);
            refresh(layout, grid1);

        });

        deleteButton.addClickListener((Button.ClickListener) clickEvent ->{
            new BaseWindow("Удалить мастера", masterDAO, "delete");
            Grid grid1 = getList(masterDAO);
            refresh(layout, grid1);
        });

        statButton.addClickListener((Button.ClickListener) clickEvent -> {
           new BaseWindow("Статистика", masterDAO, "stat");
        });

        addComponent(buttons);
        addComponent(buttonPanel);
        layout.addComponent(grid);
        addComponent(layout);
    }

    private void refresh(VerticalLayout layout, Grid newGrid){
        layout.removeAllComponents();
        layout.addComponent(newGrid);
    }

    private Grid getList(MasterDAO masterDAO){
        List<Master> masters = masterDAO.getMasterList();

        Grid grid = new Grid();
        grid.addColumn("Код");
        grid.addColumn("Фамилия");
        grid.addColumn("Имя");
        grid.addColumn("Отчество");
        grid.addColumn("Часовая ставка");

        masters.forEach(e -> grid.addRow(String.valueOf(e.getId()), e.getLastName(),
                e.getFirstName(), e.getFatherName(), String.valueOf(e.getSalary())));

        return grid;
    }
}
