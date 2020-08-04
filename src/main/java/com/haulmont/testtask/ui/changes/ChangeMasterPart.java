package com.haulmont.testtask.ui.changes;

import com.haulmont.testtask.DAO.MasterDAO;
import com.haulmont.testtask.entities.Master;
import com.haulmont.testtask.exception.WrongDeleteException;
import com.haulmont.testtask.ui.base.BaseWindow;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.*;

import static com.haulmont.testtask.ui.util.Utility.*;

public class ChangeMasterPart extends VerticalLayout implements ChangeInterface{
    RegexpValidator numberValidator = new RegexpValidator("^[0-9]{1,4}$", "Wrong input");
    RegexpValidator stringValidator = new RegexpValidator("^[А-ЯЁа-яёA-Za-z]{1,20}$", "Wrong input");

    // добавить мастера
    public ChangeMasterPart(MasterDAO masterDAO, BaseWindow baseWindow){
        GridLayout gridLayout = new GridLayout(4,4);
        gridLayout.setSpacing(true);
        gridLayout.setSizeFull();

        TextField nameField = new TextField("Имя");
        TextField surnameField = new TextField("Фамилия");
        TextField fNameField = new TextField("Отчество");
        TextField salaryField = new TextField("Часовая ставка");


        nameField.addValidator(stringValidator);
        surnameField.addValidator(stringValidator);
        fNameField.addValidator(stringValidator);
        salaryField.addValidator(numberValidator);

        Button addButton = new Button("Добавить");
        addButton.addClickListener((Button.ClickListener) clickEvent -> {
            if (checkValidate(nameField.isValid(), surnameField.isValid(),
                    fNameField.isValid(), salaryField.isValid())){
                masterDAO.addMaster(nameField.getValue(),surnameField.getValue(), fNameField.getValue(),Long.parseLong(salaryField.getValue()));

                clearFields(nameField,surnameField,fNameField, salaryField);

                // TODO новый слой UI.getCurrent().setContent(new );
                }
        });

        Button closeButton = cancelButton(baseWindow);

        addGridComponents(nameField, surnameField, fNameField, salaryField,
                addButton, closeButton, gridLayout);

        addComponent(gridLayout);
    }

    // delete master
    public ChangeMasterPart(MasterDAO masterDAO, String id, BaseWindow baseWindow){
        Master master = masterDAO.getMaster(id);
        StringBuilder builder = new StringBuilder("Будет удален ");
        builder.append(master.getLastName()).append(" ").append(master.getFirstName()).append(" ").append(master.getFatherName()).append("\n Продолжить?");
        Label label = new Label(builder.toString());

        Button deleteButton = new Button("Удалить");
        deleteButton.addClickListener((Button.ClickListener) clickEvent -> {
            try {
                masterDAO.delMaster(id);
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
    public ChangeMasterPart(MasterDAO masterDAO, BaseWindow window, Master master){
        GridLayout gridLayout = new GridLayout(4,4);
        gridLayout.setSpacing(true);
        gridLayout.setSizeFull();

        TextField nameField = new TextField("Имя");
        TextField surnameField = new TextField("Фамилия");
        TextField fNameField = new TextField("Отчество");
        TextField salaryField = new TextField("Часовая ставка");

        nameField.addValidator(stringValidator);
        surnameField.addValidator(stringValidator);
        fNameField.addValidator(stringValidator);
        salaryField.addValidator(numberValidator);

        nameField.setValue(master.getFirstName());
        surnameField.setValue(master.getLastName());
        fNameField.setValue(master.getFatherName());
        salaryField.setValue(master.getSalary().toString());

        Button updateButton = new Button("Сохранить изменения");
        updateButton.addClickListener((Button.ClickListener) clickEvent -> {
            if (checkValidate(nameField.isValid(), surnameField.isValid(),
                    fNameField.isValid(), salaryField.isValid())){
                masterDAO.updateMaster(master.getId(), nameField.getValue(),
                        surnameField.getValue(),fNameField.getValue(), salaryField.getValue());
                clearFields(nameField,surnameField,fNameField, salaryField);
                // TODO новый слой UI.getCurrent().setContent(new );

            }
        });

        Button closeButton = cancelButton(window);

        addGridComponents(nameField, surnameField, fNameField, salaryField,
                            updateButton, closeButton, gridLayout);

        addComponent(gridLayout);

    }




}
