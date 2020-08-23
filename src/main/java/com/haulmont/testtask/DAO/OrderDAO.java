package com.haulmont.testtask.DAO;

import com.haulmont.testtask.config.DataSourceConfig;
import com.haulmont.testtask.data.DTO.OrderDTO;
import com.haulmont.testtask.data.entities.Order;
import com.haulmont.testtask.exception.WrongGetException;
import org.hibernate.Session;

import javax.persistence.Query;
import java.sql.Date;
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

        Session session = DataSourceConfig.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Order order = new Order();
        order.setClient(clientDAO.getClient(client));
        order.setMaster(masterDAO.getMaster(master));
        order.setCreateDate(createDate);
        order.setFinishDate(finishDate);
        order.setPrice(Double.parseDouble(price));
        order.setDescription(descr);

        session.getTransaction().commit();
        session.close();

    }

    public void delOrder(String number) throws WrongGetException {

        Session session = DataSourceConfig.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Order order = session.find(Order.class, Long.parseLong(number));
        if (order == null){
            throw new WrongGetException("Нет заказа с таким номером!");
        }
        session.remove(order);
        session.getTransaction().commit();
        session.close();

    }

    public List<OrderDTO> getOrderDTOList(){

        Session session = DataSourceConfig.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Query query = session.createQuery("SELECT NEW com.haulmont.testtask.data.DTO.OrderDTO(o.id, c.last_name as client_surname, m.last_name as master_surname, o.create_date as date_create, o.finish_date as date_finish, o.price as price, o.status as status, o.descr as descr) FROM orders o inner join client c on o.client=c.id inner join master m on o.master=m.id");
        List<OrderDTO> orderList = query.getResultList();

        return orderList;
    }

    public Order getOrder(String id) throws WrongGetException {

        Session session = DataSourceConfig.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Order order = session.find(Order.class, Long.parseLong(id));
        if (order == null){
            throw new WrongGetException("Нет заказа с таким номером!");
        }
        session.close();
        return order;
    }

    public void updateOrder(Long id, String description, String price, Date finishDate, Long masterId, String status) throws WrongGetException {

        Session session = DataSourceConfig.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Order order = session.find(Order.class, id);
        if (order == null){
            throw new WrongGetException("Нет заказа с таким номером!");
        }
        order.setMaster(masterDAO.getMaster(String.valueOf(masterId)));
        order.setFinishDate(finishDate);
        order.setPrice(Double.parseDouble(price));
        order.setDescription(description);
        order.setStatus(status);

        session.getTransaction().commit();
        session.close();

    }
}
