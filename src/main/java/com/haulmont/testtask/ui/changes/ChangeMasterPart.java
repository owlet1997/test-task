package com.haulmont.testtask.ui.changes;

import com.haulmont.testtask.data.DAO.MasterDAO;
import com.haulmont.testtask.data.DTO.StatisticsDTO;
import com.haulmont.testtask.data.entities.Master;
import com.haulmont.testtask.data.exception.WrongDeleteException;
import com.haulmont.testtask.ui.window.BaseWindow;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

import static com.haulmont.testtask.ui.util.Utility.*;

public class ChangeMasterPart extends VerticalLayout implements ChangeInterface{
    static RegexpValidator numberValidator = new RegexpValidator("^[0-9]{1,4}$", "Wrong input");
    static RegexpValidator stringValidator = new RegexpValidator("^[А-ЯЁа-яёA-Za-z]{1,20}$", "Wrong input");

    public ChangeMasterPart() {
    }

    // добавить мастера
    public static ChangeMasterPart addMaster(MasterDAO masterDAO, BaseWindow baseWindow){
        ChangeMasterPart changeMasterPart = new ChangeMasterPart();
        HorizontalLayout panel = new HorizontalLayout();
        panel.setCaption("Добавление мастера");

        TextField nameField = new TextField("Имя");
        TextField surnameField = new TextField("Фамилия");
        TextField fNameField = new TextField("Отчество");
        TextField salaryField = new TextField("Часовая ставка");

        nameField.addValidator(stringValidator);
        surnameField.addValidator(stringValidator);
        fNameField.addValidator(stringValidator);
        salaryField.addValidator(numberValidator);

        panel.addComponent(nameField);
        panel.addComponent(surnameField);
        panel.addComponent(fNameField);
        panel.addComponent(salaryField);

        Button addButton = new Button("Добавить");
        addButton.addClickListener((Button.ClickListener) clickEvent -> {
            if (checkValidate(nameField.isValid(), surnameField.isValid(),
                    fNameField.isValid(), salaryField.isValid())){
                masterDAO.addMaster(surnameField.getValue(), nameField.getValue(), fNameField.getValue(),Long.parseLong(salaryField.getValue()));
                Notification.show("Успешно!", "Мастер успешно добавлен!",
                        Notification.TYPE_HUMANIZED_MESSAGE);
                baseWindow.close();
                }
        });

        Button closeButton = ChangeInterface.cancelButton(baseWindow);

        changeMasterPart.addComponent(panel);
        changeMasterPart.addComponent(addButton);
        changeMasterPart.addComponent(closeButton);
        return changeMasterPart;
    }

    // delete master
    public static ChangeMasterPart deleteMaster(MasterDAO masterDAO, BaseWindow baseWindow){
        ChangeMasterPart changeMasterPart = new ChangeMasterPart();

        HorizontalLayout layout = new HorizontalLayout();
        layout.setCaption("Удалить мастера");

        TextField numberField = new TextField("Введите код мастера");
        numberField.addValidator(numberValidator);

        Button deleteButton = new Button("Удалить");
        deleteButton.addClickListener((Button.ClickListener) clickEvent -> {
            try{
                if (numberField.isValid()){
                    masterDAO.delMaster(numberField.getValue());
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

        changeMasterPart.addComponent(layout);
        return changeMasterPart;
    }

    // update master
    public static ChangeMasterPart updateMaster(MasterDAO masterDAO, BaseWindow window){
        ChangeMasterPart changeMasterPart = new ChangeMasterPart();

        HorizontalLayout panel = new HorizontalLayout();
        panel.setCaption("Обновить данные о мастере");

        HorizontalLayout layout = new HorizontalLayout();

        TextField numberField = new TextField("Введите код мастера");
        numberField.addValidator(numberValidator);
        Button numberButton = new Button("Изменить данные мастера");

        panel.addComponent(numberField);
        panel.addComponent(numberButton);

        TextField nameField = new TextField("Имя");
        TextField surnameField = new TextField("Фамилия");
        TextField fNameField = new TextField("Отчество");
        TextField salaryField = new TextField("Часовая ставка");

        nameField.addValidator(stringValidator);
        surnameField.addValidator(stringValidator);
        fNameField.addValidator(stringValidator);
        salaryField.addValidator(numberValidator);

        layout.addComponent(nameField);
        layout.addComponent(surnameField);
        layout.addComponent(fNameField);
        layout.addComponent(salaryField);

        numberButton.addClickListener((Button.ClickListener) clickEvent-> {
            if (numberField.isValid()){
                try {
                    Master master = masterDAO.getMaster(numberField.getValue());
                    layout.setVisible(true);
                    nameField.setValue(master.getFirstName());
                    surnameField.setValue(master.getLastName());
                    fNameField.setValue(master.getFatherName());
                    salaryField.setValue(master.getSalary().toString());
                    Button updateButton = new Button("Сохранить изменения");

                    layout.addComponent(updateButton);

                    updateButton.addClickListener((Button.ClickListener) click -> {
                        if (checkValidate(nameField.isValid(), surnameField.isValid(),
                                fNameField.isValid(), salaryField.isValid())){
                            masterDAO.updateMaster(master.getId(), nameField.getValue(),
                                    surnameField.getValue(),fNameField.getValue(), salaryField.getValue());
                            clearFields(nameField,surnameField,fNameField, salaryField);
                            window.close();
                        }
                    });
                } catch (WrongDeleteException e){
                    Notification.show("Ошибка удаления", "Нельзя удалить мастера с этим номером!",
                            Notification.TYPE_HUMANIZED_MESSAGE);
                    numberField.clear();
                }
            }
        });

        Button closeButton = ChangeInterface.cancelButton(window);

        changeMasterPart.addComponent(panel);
        changeMasterPart.addComponent(layout);
        changeMasterPart.addComponent(closeButton);
        return changeMasterPart;
    }

    // get statistics
    public static ChangeMasterPart getStatistics(MasterDAO masterDAO, BaseWindow window){
        ChangeMasterPart changeMasterPart = new ChangeMasterPart();

        Grid statisticList = new Grid();
        statisticList.addColumn("Фамилия");
        statisticList.addColumn("Имя");
        statisticList.addColumn("Отчество");
        statisticList.addColumn("Количество заказов");
        statisticList.setSizeFull();

        List<StatisticsDTO> list = masterDAO.getStatistics();

        list.forEach(e -> statisticList.addRow(e.getLastName(), e.getName(), e.getFatherName(), String.valueOf(e.getCountAll())));

        statisticList.setHeightByRows(list.size());

        Button closeButton = ChangeInterface.cancelButton(window);
        closeButton.setStyleName(ValoTheme.BUTTON_DANGER);

        changeMasterPart.addComponent(statisticList);
        changeMasterPart.addComponent(closeButton);
        return changeMasterPart;
    }







}
