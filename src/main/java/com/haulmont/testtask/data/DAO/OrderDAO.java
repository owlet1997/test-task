package com.haulmont.testtask.data.DAO;

import com.haulmont.testtask.DataSourceConfig;
import com.haulmont.testtask.data.DTO.OrderDTO;
import com.haulmont.testtask.data.entities.Order;
import com.haulmont.testtask.data.exception.WrongGetException;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    MasterDAO masterDAO = MasterDAO.getInstance();
    ClientDAO clientDAO = ClientDAO.getInstance();

    private static OrderDAO orderDAO;

    private OrderDAO(){

    }
    public static OrderDAO getInstance() {
        if (orderDAO == null){
            orderDAO = new OrderDAO();
        }
        return orderDAO;
    }

    public void addOrder(String client, String master, Date createDate,
                         Date finishDate, String price, String descr) throws WrongGetException {

        EntityManager manager = DataSourceConfig.getInstance();
        manager.getTransaction().begin();

        Order order = new Order();
        order.setClient(clientDAO.getClient(client));
        order.setMaster(masterDAO.getMaster(master));
        order.setCreateDate(createDate);
        order.setFinishDate(finishDate);
        order.setPrice(Double.parseDouble(price));
        order.setDescription(descr);

        manager.getTransaction().commit();
        manager.close();

    }

    public void delOrder(String number) throws WrongGetException {

        EntityManager manager = DataSourceConfig.getInstance();
        manager.getTransaction().begin();

        Order order = manager.find(Order.class, Long.parseLong(number));
        if (order == null){
            throw new WrongGetException("Нет заказа с таким номером!");
        }
        manager.remove(order);
        manager.getTransaction().commit();
        manager.close();

    }

    public List<OrderDTO> getOrderDTOList(){

        EntityManager manager = DataSourceConfig.getInstance();
        manager.getTransaction().begin();

        Query query = manager.createQuery("SELECT NEW com.haulmont.testtask.data.DTO.OrderDTO(o.id, c.last_name as client_surname, m.last_name as master_surname, o.create_date as date_create, o.finish_date as date_finish, o.price as price, o.status as status, o.descr as descr) FROM orders o inner join client c on o.client=c.id inner join master m on o.master=m.id");
        List<OrderDTO> orderList = query.getResultList();

        return orderList;
    }

    public Order getOrder(String id) throws WrongGetException {

        EntityManager manager = DataSourceConfig.getInstance();
        manager.getTransaction().begin();

        Order order = manager.find(Order.class, Long.parseLong(id));
        if (order == null){
            throw new WrongGetException("Нет заказа с таким номером!");
        }
        manager.close();
        return order;
    }

    public void updateOrder(Long id, String description, String price, Date finishDate, Long masterId, String status) throws WrongGetException {

        EntityManager manager = DataSourceConfig.getInstance();
        manager.getTransaction().begin();

        Order order = manager.find(Order.class, id);
        if (order == null){
            throw new WrongGetException("Нет заказа с таким номером!");
        }
        order.setMaster(masterDAO.getMaster(String.valueOf(masterId)));
        order.setFinishDate(finishDate);
        order.setPrice(Double.parseDouble(price));
        order.setDescription(description);
        order.setStatus(status);

        manager.getTransaction().commit();
        manager.close();

    }
}
