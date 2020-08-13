package com.haulmont.testtask.ui.modals;

import com.haulmont.testtask.data.DAO.MasterDAO;
import com.haulmont.testtask.data.DTO.StatisticsDTO;
import com.haulmont.testtask.data.entities.Master;
import com.haulmont.testtask.data.exception.WrongGetException;
import com.haulmont.testtask.ui.window.BaseWindow;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.dussan.vaadin.dcharts.data.DataSeries;
import org.dussan.vaadin.dcharts.metadata.renderers.SeriesRenderers;
import org.dussan.vaadin.dcharts.options.SeriesDefaults;

import java.util.List;
import java.util.stream.Collectors;

public class ChangeMasterModal extends VerticalLayout implements ChangeInterface{
    static RegexpValidator numberValidator = new RegexpValidator("^[0-9]{1,4}$", "Wrong input");
    static RegexpValidator stringValidator = new RegexpValidator("^[А-ЯЁа-яёA-Za-z]{1,20}$", "Wrong input");

    public ChangeMasterModal() {
    }

    // добавить мастера
    public static ChangeMasterModal addMaster(MasterDAO masterDAO, BaseWindow baseWindow){
        ChangeMasterModal changeMasterPart = new ChangeMasterModal();
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
            if (nameField.isValid() && surnameField.isValid() &&
                    fNameField.isValid() && salaryField.isValid()){
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
    public static ChangeMasterModal deleteMaster(MasterDAO masterDAO, BaseWindow baseWindow){
        ChangeMasterModal changeMasterPart = new ChangeMasterModal();

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
            } catch (WrongGetException e){
                Notification.show("Ошибка удаления", "Нельзя удалить мастера с этим номером!",
                        Notification.Type.WARNING_MESSAGE);
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
    public static ChangeMasterModal updateMaster(MasterDAO masterDAO, BaseWindow window){
        ChangeMasterModal changeMasterPart = new ChangeMasterModal();

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
                    nameField.setValue(master.getName());
                    surnameField.setValue(master.getSurname());
                    fNameField.setValue(master.getFatherName());
                    salaryField.setValue(master.getSalary().toString());
                    Button updateButton = new Button("Сохранить изменения");

                    layout.addComponent(updateButton);
                    updateButton.setEnabled(false);

                    updateButton.addClickListener((Button.ClickListener) click -> {
                        if (nameField.isValid() && surnameField.isValid() &&
                                fNameField.isValid() && salaryField.isValid()){
                            updateButton.setEnabled(true);

                            masterDAO.updateMaster(master.getId(), nameField.getValue(),
                                    surnameField.getValue(),fNameField.getValue(), salaryField.getValue());
                            window.close();
                        }
                    });
                } catch (WrongGetException e){
                    Notification.show("Ошибка удаления", "Нельзя удалить мастера с этим номером!",
                            Notification.Type.WARNING_MESSAGE);
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
    public static ChangeMasterModal getStatistics(MasterDAO masterDAO, BaseWindow window){
        ChangeMasterModal changeMasterPart = new ChangeMasterModal();

        Grid statisticList = new Grid();
        statisticList.addColumn("Фамилия");
        statisticList.addColumn("Имя");
        statisticList.addColumn("Отчество");
        statisticList.addColumn("Количество заказов");
        statisticList.setSizeFull();




        List<StatisticsDTO> list = masterDAO.getStatistics();
        DataSeries dataSeries = new DataSeries()
                .add(list.stream().map(StatisticsDTO::getAmount).collect(Collectors.toList()));

        SeriesDefaults seriesDefaults = new SeriesDefaults()
                .setRenderer(SeriesRenderers.BAR);

        list.forEach(e -> statisticList.addRow(e.getSurname(), e.getName(), e.getFatherName(), String.valueOf(e.getAmount())));

        statisticList.setHeightByRows(list.size());

        Button closeButton = ChangeInterface.cancelButton(window);
        closeButton.setStyleName(ValoTheme.BUTTON_DANGER);

        changeMasterPart.addComponent(statisticList);
        changeMasterPart.addComponent(closeButton);
        return changeMasterPart;
    }







}
