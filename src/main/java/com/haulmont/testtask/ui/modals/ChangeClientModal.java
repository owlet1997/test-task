package com.haulmont.testtask.ui.modals;

import com.haulmont.testtask.DAO.ClientDAO;
import com.haulmont.testtask.data.entities.Client;
import com.haulmont.testtask.exception.WrongGetException;
import com.haulmont.testtask.ui.window.BaseWindow;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.*;

public class ChangeClientModal extends VerticalLayout implements ChangeInterface {
    static RegexpValidator numberValidator = new RegexpValidator("^[0-9]{1,12}$", "Wrong input");
    static RegexpValidator stringValidator = new RegexpValidator("^[А-ЯЁа-яёA-Za-z]{1,20}$", "Wrong input");

    public ChangeClientModal() {
    }

    // добавить клиента
    public static ChangeClientModal addClient(ClientDAO clientDAO, BaseWindow baseWindow){
        ChangeClientModal changeClientPart = new ChangeClientModal();
        HorizontalLayout panel = new HorizontalLayout();
        HorizontalLayout buttonPanel = new HorizontalLayout();
        buttonPanel.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        TextField nameField = new TextField("Имя");
        TextField surnameField = new TextField("Фамилия");
        TextField fNameField = new TextField("Отчество");
        TextField phoneField = new TextField("Номер телефона");

        Button addButton = new Button("Добавить");

        nameField.addValidator(stringValidator);
        surnameField.addValidator(stringValidator);
        fNameField.addValidator(stringValidator);
        phoneField.addValidator(numberValidator);

        panel.addComponent(nameField);
        panel.addComponent(surnameField);
        panel.addComponent(fNameField);
        panel.addComponent(phoneField);

        addButton.addClickListener((Button.ClickListener) clickEvent -> {
            if (nameField.isValid() && surnameField.isValid() &&
                    fNameField.isValid() && phoneField.isValid()){
                clientDAO.addClient(nameField.getValue(),surnameField.getValue(), fNameField.getValue(),phoneField.getValue());
                baseWindow.close();
            }
        });

        Button closeButton = ChangeInterface.cancelButton(baseWindow);
        buttonPanel.addComponent(addButton);
        buttonPanel.addComponent(closeButton);

        changeClientPart.addComponent(panel);
        changeClientPart.addComponent(buttonPanel);
        return changeClientPart;
    }

    // delete client
    public static ChangeClientModal deleteClient(ClientDAO clientDAO, Client client, BaseWindow baseWindow){
        ChangeClientModal changeClientPart = new ChangeClientModal();

        VerticalLayout modal = new VerticalLayout();

        HorizontalLayout layout = new HorizontalLayout();
        layout.setCaption("Удалить клиента");

        Label label = new Label("Вы действительно хотит удалить клиента "
                + client.getSurname() + " " + client.getName() + "?");

        Button deleteButton = new Button("Да");
        deleteButton.addClickListener((Button.ClickListener) clickEvent -> {
            try{
                clientDAO.delClient(String.valueOf(client.getId()));
                baseWindow.close();

            } catch (WrongGetException e){
                Notification.show("Ошибка удаления", "Нельзя удалить клиента с этим номером!",
                        Notification.Type.WARNING_MESSAGE);
                baseWindow.close();
            }
        });
        Button closeButton = ChangeInterface.cancelButton(baseWindow);
        layout.addComponent(deleteButton);
        layout.addComponent(closeButton);
        modal.addComponent(label);
        modal.addComponent(layout);

        changeClientPart.addComponent(modal);
        return changeClientPart;
    }

    // update client
    public static ChangeClientModal updateClient(ClientDAO clientDAO, Client client, BaseWindow window){
         ChangeClientModal changeClientPart = new ChangeClientModal();

        HorizontalLayout panel = new HorizontalLayout();
        panel.setCaption("Обновить данные о клиенте");

        HorizontalLayout layout = new HorizontalLayout();

        TextField nameField = new TextField("Имя");
        TextField surnameField = new TextField("Фамилия");
        TextField fNameField = new TextField("Отчество");
        TextField phoneField = new TextField("Телефон");

        nameField.addValidator(stringValidator);
        surnameField.addValidator(stringValidator);
        fNameField.addValidator(stringValidator);

        layout.addComponent(nameField);
        layout.addComponent(surnameField);
        layout.addComponent(fNameField);
        layout.addComponent(phoneField);

        nameField.setValue(client.getName());
        surnameField.setValue(client.getSurname());
        fNameField.setValue(client.getFatherName());
        phoneField.setValue(client.getPhone());

        Button updateButton = new Button("Сохранить изменения");
        layout.addComponent(updateButton);

        updateButton.addClickListener((Button.ClickListener) click -> {
            if (nameField.isValid() && surnameField.isValid() &&
                    fNameField.isValid() && phoneField.isValid()){
                try {
                    clientDAO.updateClient(client.getId(), nameField.getValue(),
                            surnameField.getValue(),fNameField.getValue(), phoneField.getValue());
                } catch (WrongGetException e) {
                    e.printStackTrace();
                }
                window.close();
            }
        });

        Button closeButton = ChangeInterface.cancelButton(window);

        changeClientPart.addComponent(panel);
        changeClientPart.addComponent(layout);
        changeClientPart.addComponent(closeButton);
        return changeClientPart;

    }


}
