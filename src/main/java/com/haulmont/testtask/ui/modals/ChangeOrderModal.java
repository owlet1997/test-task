package com.haulmont.testtask.ui.modals;

import com.haulmont.testtask.data.DAO.ClientDAO;
import com.haulmont.testtask.data.DAO.MasterDAO;
import com.haulmont.testtask.data.DAO.OrderDAO;
import com.haulmont.testtask.data.entities.Client;
import com.haulmont.testtask.data.entities.Master;
import com.haulmont.testtask.data.entities.Order;
import com.haulmont.testtask.data.exception.WrongGetException;
import com.haulmont.testtask.ui.window.BaseWindow;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

public class ChangeOrderModal extends VerticalLayout implements ChangeInterface {
    static RegexpValidator numberValidator = new RegexpValidator("^[0-9]{1,4}$", "Wrong input");
    static RegexpValidator priceValidator = new RegexpValidator("^[0-9.]{1,12}$", "Wrong input");

    static RegexpValidator stringValidator = new RegexpValidator("^[А-ЯЁа-яёA-Za-z]{1,20}$", "Wrong input");

    private final static String[] statuses = new String[]{"Запланирован", "Выполнен", "Принят клиентом"};


    public ChangeOrderModal() {}

    // add order
    public static ChangeOrderModal addOrder(OrderDAO orderDAO, MasterDAO masterDAO, ClientDAO clientDAO, BaseWindow window){
        ChangeOrderModal changeOrderPart = new ChangeOrderModal();
        HorizontalLayout panel = new HorizontalLayout();
        panel.setCaption("Добавление заказа");

        TextField descrField = new TextField("Описание");
        TextField priceField = new TextField("Цена");
        DateField createField = new DateField("Дата создания");
        DateField finishField = new DateField("Дата сдачи");

        ComboBox selectClient = new ComboBox("Заказчик");
        ComboBox selectMaster = new ComboBox("Исполнитель");

        priceField.addValidator(priceValidator);

        List<Client> clientList = clientDAO.getClientList();
        List<Master> masterList = masterDAO.getMasterList();

        clientList.forEach(e -> selectClient.addItem(String.valueOf(e.getId())));
        masterList.forEach(e -> selectMaster.addItem(String.valueOf(e.getId())));

        Button addButton = new Button("Добавить заказ");
        addButton.addClickListener((Button.ClickListener) clickEvent -> {

            if (priceField.isValid() && selectClient.isValid() && selectMaster.isValid() && descrField.isValid()){
                orderDAO.addOrder((String) selectClient.getValue(), (String) selectMaster.getValue(),
                        new Date(createField.getValue().getTime()), new Date(finishField.getValue().getTime()),priceField.getValue(), descrField.getValue());

                window.close();

            }
        });
        Button cancelButton = ChangeInterface.cancelButton(window);
        panel.addComponent(descrField);
        panel.addComponent(selectClient);
        panel.addComponent(selectMaster);
        panel.addComponent(createField);
        panel.addComponent(finishField);
        panel.addComponent(priceField);

        changeOrderPart.addComponent(panel);
        changeOrderPart.addComponent(addButton);
        changeOrderPart.addComponent(cancelButton);
        return changeOrderPart;
    }

    //delete order
    public static ChangeOrderModal deleteOrder(OrderDAO orderDAO, BaseWindow window){
        ChangeOrderModal changeOrderPart = new ChangeOrderModal();

        HorizontalLayout layout = new HorizontalLayout();
        layout.setCaption("Удалить заказ");

        TextField numberField = new TextField("Введите номер заказа");
        numberField.addValidator(numberValidator);

        Button deleteButton = new Button("Удалить");
        deleteButton.addClickListener((Button.ClickListener) clickEvent -> {
            try{
                if (numberField.isValid()){
                    orderDAO.delOrder(numberField.getValue());
                    window.close();
                }
            } catch (WrongGetException e){
                Notification.show("Ошибка удаления", "Нельзя удалить заказ с этим номером!",
                        Notification.Type.WARNING_MESSAGE);
                numberField.clear();
            }
        });
        Button closeButton = ChangeInterface.cancelButton(window);
        closeButton.setStyleName(ValoTheme.BUTTON_DANGER);

        layout.addComponent(numberField);
        layout.addComponent(deleteButton);
        layout.addComponent(closeButton);

        changeOrderPart.addComponent(layout);
        return changeOrderPart;
    }


    // update order
    public static ChangeOrderModal updateOrder(OrderDAO orderDAO, MasterDAO masterDAO, BaseWindow window){
        ChangeOrderModal changeOrderPart = new ChangeOrderModal();
        window.setHeightUndefined();
        window.setWindowMode(WindowMode.NORMAL);
        HorizontalLayout panel = new HorizontalLayout();
        panel.setCaption("Обновить заказ");
        panel.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        HorizontalLayout updatePanel = new HorizontalLayout();
        panel.setCaption("Детали заказа");

        TextField descrField = new TextField("Описание");
        TextField priceField = new TextField("Цена");
        DateField finishField = new DateField("Дата сдачи");
        ComboBox selectMaster = new ComboBox("Исполнитель");
        ComboBox selectStatus = new ComboBox("Статус");

        List<Master> masterList = masterDAO.getMasterList();
        List<String> statusList = Arrays.asList(statuses);

        masterList.forEach(e->selectMaster.addItem(e.getId()));
        statusList.forEach(e->selectStatus.addItem(e));

        priceField.addValidator(numberValidator);

        TextField numberField = new TextField("Введите номер заказа");
        numberField.addValidator(numberValidator);
        Button numberButton = new Button("Изменить данный заказ");

        panel.addComponent(numberField);
        panel.addComponent(numberButton);

        updatePanel.addComponent(descrField);
        updatePanel.addComponent(priceField);
        updatePanel.addComponent(finishField);
        updatePanel.addComponent(selectMaster);
        updatePanel.addComponent(selectStatus);
        updatePanel.setVisible(false);

        Button updateButton = new Button("Сохранить изменения");

        numberButton.addClickListener((Button.ClickListener) clickEvent-> {
            if (numberField.isValid()){
                Order order = orderDAO.getOrder(numberField.getValue());
                updatePanel.setVisible(true);
                descrField.setValue(order.getDescription());
                priceField.setValue(order.getPrice().toString());
                finishField.setValue(order.getFinishDate());
                selectMaster.setValue(order.getMaster());
                selectStatus.setValue(order.getStatus());
                updatePanel.addComponent(updateButton);

                updateButton.addClickListener((Button.ClickListener) click -> {
                    if (descrField.isValid() && priceField.isValid() &&
                            finishField.isValid() && selectMaster.isValid() && selectStatus.isValid()){
                        orderDAO.updateOrder(order.getId(), descrField.getValue(),
                                priceField.getValue(),new Date(finishField.getValue().getTime()), (Long) selectMaster.getValue(), (String) selectStatus.getValue());
                        Notification.show("Успешно!", "Заказ был успешно обновлен!", Notification.Type.HUMANIZED_MESSAGE);
                        window.close();
                    }
                });
            }
        });
        Button closeButton = ChangeInterface.cancelButton(window);

        changeOrderPart.addComponent(panel);
        changeOrderPart.addComponent(updatePanel);
        changeOrderPart.addComponent(closeButton);
        return changeOrderPart;
    }

}
