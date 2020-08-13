package com.haulmont.testtask.ui.modals;

import com.haulmont.testtask.data.DAO.ClientDAO;
import com.haulmont.testtask.data.entities.Client;
import com.haulmont.testtask.data.exception.WrongGetException;
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
        panel.addComponent(addButton);
        addButton.setEnabled(false);

        addButton.addClickListener((Button.ClickListener) clickEvent -> {
            if (nameField.isValid() && surnameField.isValid() &&
                    fNameField.isValid() && phoneField.isValid()){
                addButton.setEnabled(true);
                clientDAO.addClient(nameField.getValue(),surnameField.getValue(), fNameField.getValue(),phoneField.getValue());

                baseWindow.close();
            }
        });

        Button closeButton = ChangeInterface.cancelButton(baseWindow);

        changeClientPart.addComponent(panel);
        changeClientPart.addComponent(closeButton);
        return changeClientPart;
    }

    // delete client
    public static ChangeClientModal deleteClient(ClientDAO clientDAO, BaseWindow baseWindow){
        ChangeClientModal changeClientPart = new ChangeClientModal();

        HorizontalLayout layout = new HorizontalLayout();
        layout.setCaption("Удалить клиента");

        TextField numberField = new TextField("Введите код клиента");
        numberField.addValidator(numberValidator);

        Button deleteButton = new Button("Удалить");
        deleteButton.addClickListener((Button.ClickListener) clickEvent -> {
            try{
                if (numberField.isValid()){
                    clientDAO.delClient(numberField.getValue());
                    baseWindow.close();
                }
            } catch (WrongGetException e){
                Notification.show("Ошибка удаления", "Нельзя удалить клиента с этим номером!",
                        Notification.Type.WARNING_MESSAGE);
                numberField.clear();
            }
        });
        Button closeButton = ChangeInterface.cancelButton(baseWindow);
        layout.addComponent(numberField);
        layout.addComponent(deleteButton);
        layout.addComponent(closeButton);

        changeClientPart.addComponent(layout);
        return changeClientPart;
    }

    // update client
    public static ChangeClientModal updateClient(ClientDAO clientDAO, BaseWindow window){
         ChangeClientModal changeClientPart = new ChangeClientModal();

        HorizontalLayout panel = new HorizontalLayout();
        panel.setCaption("Обновить данные о клиенте");

        HorizontalLayout layout = new HorizontalLayout();

        TextField numberField = new TextField("Введите код клиента");
        numberField.addValidator(numberValidator);
        Button numberButton = new Button("Изменить данные клиента");

        panel.addComponent(numberField);
        panel.addComponent(numberButton);

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

        numberButton.addClickListener((Button.ClickListener) clickEvent-> {
            if (numberField.isValid()){
                try{
                    Client client = clientDAO.getClient(numberField.getValue());
                    layout.setVisible(true);
                    nameField.setValue(client.getName());
                    surnameField.setValue(client.getSurname());
                    fNameField.setValue(client.getFatherName());
                    phoneField.setValue(client.getPhone());
                    Button updateButton = new Button("Сохранить изменения");

                    layout.addComponent(updateButton);

                    updateButton.addClickListener((Button.ClickListener) click -> {
                        if (nameField.isValid() && surnameField.isValid() &&
                                fNameField.isValid() && phoneField.isValid()){
                            clientDAO.updateClient(client.getId(), nameField.getValue(),
                                    surnameField.getValue(),fNameField.getValue(), phoneField.getValue());
                            window.close();
                        }
                    });
                } catch (WrongGetException e){
                    Notification.show("Ошибка обновления", e.getMessage(),
                            Notification.Type.WARNING_MESSAGE);
                    numberField.clear();
                }

            }
        });

        Button closeButton = ChangeInterface.cancelButton(window);

        changeClientPart.addComponent(panel);
        changeClientPart.addComponent(layout);
        changeClientPart.addComponent(closeButton);
        return changeClientPart;

    }


}
