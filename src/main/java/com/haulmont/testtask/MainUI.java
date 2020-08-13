package com.haulmont.testtask;

import com.haulmont.testtask.ui.layouts.ClientsLayout;
import com.haulmont.testtask.ui.layouts.MasterLayout;
import com.haulmont.testtask.ui.layouts.OrdersLayout;
import com.vaadin.annotations.Theme;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        TabSheet tabSheet = new TabSheet();

        VerticalLayout ordersLayout = new OrdersLayout();
        VerticalLayout masterLayout = new MasterLayout();
        VerticalLayout clientLayout = new ClientsLayout();

        tabSheet.addTab(ordersLayout, "Заказы");
        tabSheet.addTab(masterLayout, "Мастера");
        tabSheet.addTab(clientLayout, "Клиенты");
        UI.getCurrent().setContent(tabSheet);
    }


}