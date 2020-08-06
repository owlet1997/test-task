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
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponent(getButtons());
        verticalLayout.setSizeFull();
        verticalLayout.setMargin(true);

        setContent(verticalLayout);
    }

    public static HorizontalLayout getButtons(){
        Button showMasters = new Button("Информация о мастерах");
        Button showClients = new Button("Информация о клиентах");
        Button showOrders = new Button("Информация о заказах");

        showMasters.addClickListener((Button.ClickListener) clickEvent ->{
            UI.getCurrent().setContent(new MasterLayout());
        });

        showClients.addClickListener((Button.ClickListener) clickListener ->{
            UI.getCurrent().setContent(new ClientsLayout());
        });

        showOrders.addClickListener((Button.ClickListener) clickListener -> {
            UI.getCurrent().setContent(new OrdersLayout());
        });

        HorizontalLayout layout = new HorizontalLayout();
        layout.addComponent(showMasters);
        layout.addComponent(showClients);
        layout.addComponent(showOrders);

        return layout;

    }
}