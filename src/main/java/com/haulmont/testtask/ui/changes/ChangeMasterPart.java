package com.haulmont.testtask.ui.changes;

import com.haulmont.testtask.DAO.MasterDAO;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.VerticalLayout;

public class ChangeMasterPart extends VerticalLayout implements ChangeInterface{
    RegexpValidator numberValidator = new RegexpValidator("^[0-9]{1,4}$", "Wrong input");
    RegexpValidator stringValidator = new RegexpValidator("^[А-ЯЁа-яёA-Za-z]{1,20}$", "Wrong input");
    RegexpValidator dateValidator = new RegexpValidator("(19|20)\\d\\d-((0[1-9]|1[012])-(0[1-9]|[12]\\d)|(0[13-9]|1[012])-30|(0[13578]|1[02])-31)",
            "Wrong date(enter YYYY-MM-DD format)!!!");

    // добавить мастера
    public ChangeMasterPart(MasterDAO masterDAO ){
        GridLayout gridLayout = new GridLayout()
    }
}
