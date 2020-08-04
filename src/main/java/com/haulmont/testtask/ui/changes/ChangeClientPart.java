package com.haulmont.testtask.ui.changes;

import com.haulmont.testtask.DAO.ClientDAO;
import com.haulmont.testtask.DAO.MasterDAO;
import com.haulmont.testtask.entities.Client;
import com.haulmont.testtask.entities.Master;
import com.haulmont.testtask.exception.WrongDeleteException;
import com.haulmont.testtask.ui.base.BaseWindow;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.*;

import static com.haulmont.testtask.ui.util.Utility.*;

public class ChangeClientPart extends VerticalLayout implements ChangeInterface {
    RegexpValidator numberValidator = new RegexpValidator("^[0-9]{1,4}$", "Wrong input");
    RegexpValidator stringValidator = new RegexpValidator("^[А-ЯЁа-яёA-Za-z]{1,20}$", "Wrong input");

    // добавить клиента
    public ChangeClientPart(ClientDAO clientDAO, BaseWindow baseWindow){
        GridLayout gridLayout = new GridLayout(4,4);
        gridLayout.setSpacing(true);
        gridLayout.setSizeFull();

        TextField nameField = new TextField("Имя");
        TextField surnameField = new TextField("Фамилия");
        TextField fNameField = new TextField("Отчество");
        TextField phoneField = new TextField("Номер телефона");


        nameField.addValidator(stringValidator);
        surnameField.addValidator(stringValidator);
        fNameField.addValidator(stringValidator);
        phoneField.addValidator(numberValidator);

        Button addButton = new Button("Добавить");
        addButton.addClickListener((Button.ClickListener) clickEvent -> {
            if (checkValidate(nameField.isValid(), surnameField.isValid(),
                    fNameField.isValid(), phoneField.isValid())){
                clientDAO.addClient(nameField.getValue(),surnameField.getValue(), fNameField.getValue(),phoneField.getValue());

                clearFields(nameField,surnameField,fNameField, phoneField);

                // TODO новый слой UI.getCurrent().setContent(new );
            }
        });

        Button closeButton = cancelButton(baseWindow);

        addGridComponents(nameField, surnameField, fNameField, phoneField,
                addButton, closeButton, gridLayout);

        addComponent(gridLayout);
    }

    // delete master
    public ChangeClientPart(ClientDAO clientDAO, String id, BaseWindow baseWindow){
        Client client = clientDAO.getClient(id);
        StringBuilder builder = new StringBuilder("Будет удален ");
        builder.append(client.getLastName()).append(" ").append(client.getFirstName()).append(" ").append(client.getFatherName()).append("\n Продолжить?");
        Label label = new Label(builder.toString());

        Button deleteButton = new Button("Удалить");
        deleteButton.addClickListener((Button.ClickListener) clickEvent -> {
            try {
                clientDAO.delClient(id);
            } catch (WrongDeleteException e) {
                e.printStackTrace();
                Notification.show("Ошибка удаления",
                        "Нельзя удалить заказ с этим номером!",
                        Notification.TYPE_HUMANIZED_MESSAGE);
                baseWindow.close();
            }
            // TODO новый слой UI.getCurrent().setContent(new );
        });

        Button closeButton = cancelButton(baseWindow);

        addComponent(label);
        addComponent(deleteButton);
        addComponent(closeButton);
    }

    // update master
    public ChangeClientPart(ClientDAO clientDAO, BaseWindow window, Client client){
        GridLayout gridLayout = new GridLayout(4,4);
        gridLayout.setSpacing(true);
        gridLayout.setSizeFull();

        TextField nameField = new TextField("Имя");
        TextField surnameField = new TextField("Фамилия");
        TextField fNameField = new TextField("Отчество");
        TextField phoneField = new TextField("Часовая ставка");

        nameField.addValidator(stringValidator);
        surnameField.addValidator(stringValidator);
        fNameField.addValidator(stringValidator);
        phoneField.addValidator(numberValidator);

        nameField.setValue(client.getFirstName());
        surnameField.setValue(client.getLastName());
        fNameField.setValue(client.getPhone());

        Button updateButton = new Button("Сохранить изменения");
        updateButton.addClickListener((Button.ClickListener) clickEvent -> {
            if (checkValidate(nameField.isValid(), surnameField.isValid(),
                    fNameField.isValid(), phoneField.isValid())){
                clientDAO.updateClient(client.getId(), nameField.getValue(),
                        surnameField.getValue(),fNameField.getValue(), phoneField.getValue());
                clearFields(nameField,surnameField,fNameField, phoneField);
                // TODO новый слой UI.getCurrent().setContent(new );

            }
        });

        Button closeButton = cancelButton(window);

        addGridComponents(nameField, surnameField, fNameField, phoneField,
                updateButton, closeButton, gridLayout);

        addComponent(gridLayout);

    }


}
