package com.haulmont.testtask.ui.changes;

import com.haulmont.testtask.DAO.ClientDAO;
import com.haulmont.testtask.DAO.MasterDAO;
import com.haulmont.testtask.DAO.OrderDAO;
import com.haulmont.testtask.entities.Client;
import com.haulmont.testtask.entities.Master;
import com.haulmont.testtask.ui.base.BaseWindow;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.*;
import sun.awt.X11.XBaseWindow;

import java.util.List;

import static com.haulmont.testtask.ui.util.Utility.checkValidate;
import static com.haulmont.testtask.ui.util.Utility.clearFields;

public class ChangeOrderPart extends VerticalLayout implements ChangeInterface {
    RegexpValidator numberValidator = new RegexpValidator("^[0-9]{1,4}$", "Wrong input");
    RegexpValidator stringValidator = new RegexpValidator("^[А-ЯЁа-яёA-Za-z]{1,20}$", "Wrong input");

    // add order
    public ChangeOrderPart(OrderDAO orderDAO, MasterDAO masterDAO, ClientDAO clientDAO, BaseWindow window){
        GridLayout gridLayout = new GridLayout(7,4);
        gridLayout.setSpacing(true);
        gridLayout.setSizeFull();

        TextField descrField = new TextField("Описание");
        TextField priceField = new TextField("Цена");
        DateField createField = new DateField("Дата создания");
        DateField finishField = new DateField("Дата сдачи");

        ComboBox selectClient = new ComboBox("Заказчик");
        ComboBox selectMaster = new ComboBox("Исполнитель");

        priceField.addValidator(numberValidator);

        List<Client> clientList = clientDAO.getClientList("", "");
        List<Master> masterList = masterDAO.getMasterList("","");


        clientList.forEach(e -> selectClient.addItem(e.getId()));
        masterList.forEach(e -> selectMaster.addItem(e.getId()));

        Button addButton = new Button("Добавить заказ");
        addButton.addClickListener((Button.ClickListener) clickEvent -> {

            if (checkValidate(priceField.isValid(), selectClient.isValid(), selectMaster.isValid())){
                orderDAO.addOrder((String) selectClient.getValue(), (String) selectMaster.getValue(),
                        createField.getValue(), finishField.getValue(),priceField.getValue());

                clearFields(priceField, selectClient,selectMaster, descrField,createField,finishField);
                // TODO новый слой UI.getCurrent().setContent(new );

            }
            Button cancelButton = cancelButton(window);


            addComponent(gridLayout);
        });





    }

}
