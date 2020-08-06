package com.haulmont.testtask.ui.changes;

import com.haulmont.testtask.data.DAO.ClientDAO;
import com.haulmont.testtask.data.entities.Client;
import com.haulmont.testtask.data.exception.WrongDeleteException;
import com.haulmont.testtask.ui.window.BaseWindow;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.*;

import static com.haulmont.testtask.ui.util.Utility.*;

public class ChangeClientPart extends VerticalLayout implements ChangeInterface {
    static RegexpValidator numberValidator = new RegexpValidator("^[0-9]{1,4}$", "Wrong input");
    static RegexpValidator stringValidator = new RegexpValidator("^[А-ЯЁа-яёA-Za-z]{1,20}$", "Wrong input");

    public ChangeClientPart() {
    }

    // добавить клиента
    public static ChangeClientPart addClient(ClientDAO clientDAO, BaseWindow baseWindow){
        ChangeClientPart changeClientPart = new ChangeClientPart();
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

        addButton.addClickListener((Button.ClickListener) clickEvent -> {
            if (checkValidate(nameField.isValid(), surnameField.isValid(),
                    fNameField.isValid(), phoneField.isValid())){
                clientDAO.addClient(nameField.getValue(),surnameField.getValue(), fNameField.getValue(),phoneField.getValue());

                clearFields(nameField,surnameField,fNameField, phoneField);
                baseWindow.close();
            }
        });

        Button closeButton = ChangeInterface.cancelButton(baseWindow);

        changeClientPart.addComponent(panel);
        changeClientPart.addComponent(closeButton);
        return changeClientPart;
    }

    // delete client
    public static ChangeClientPart deleteClient(ClientDAO clientDAO, BaseWindow baseWindow){
        ChangeClientPart changeClientPart = new ChangeClientPart();

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
            } catch (WrongDeleteException e){
                Notification.show("Ошибка удаления", "Нельзя удалить мастера с этим номером!",
                        Notification.TYPE_HUMANIZED_MESSAGE);
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
    public static ChangeClientPart updateClient(ClientDAO clientDAO, BaseWindow window){
         ChangeClientPart changeClientPart = new ChangeClientPart();

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
                Client client = clientDAO.getClient(numberField.getValue());
                layout.setVisible(true);
                nameField.setValue(client.getFirstName());
                surnameField.setValue(client.getLastName());
                fNameField.setValue(client.getFatherName());
                phoneField.setValue(client.getPhone());
                Button updateButton = new Button("Сохранить изменения");

                layout.addComponent(updateButton);

                updateButton.addClickListener((Button.ClickListener) click -> {
                    if (checkValidate(nameField.isValid(), surnameField.isValid(),
                            fNameField.isValid(), phoneField.isValid())){
                        clientDAO.updateClient(client.getId(), nameField.getValue(),
                                surnameField.getValue(),fNameField.getValue(), phoneField.getValue());
                        clearFields(nameField,surnameField,fNameField, phoneField);
                        window.close();
                    }
                });
            }
        });

        Button closeButton = ChangeInterface.cancelButton(window);

        changeClientPart.addComponent(panel);
        changeClientPart.addComponent(layout);
        changeClientPart.addComponent(closeButton);
        return changeClientPart;

    }


}
