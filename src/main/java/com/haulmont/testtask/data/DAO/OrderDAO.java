package com.haulmont.testtask.data.DAO;

import com.haulmont.testtask.DataSourceConfig;
import com.haulmont.testtask.data.DTO.OrderDTO;
import com.haulmont.testtask.data.entities.Order;
import com.haulmont.testtask.data.exception.WrongGetException;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

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
                         Date finishDate, String price, String descr) {

        String sql = "INSERT INTO orders (client, master, create_date, finish_date," +
                " price, status, descr) VALUES (?, ?, ?, ?, ?, ?, ?) ";
        Connection con = DataSourceConfig.getInstance();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, Long.parseLong(client));
            ps.setLong(2, Long.parseLong(master));
            ps.setDate(3, createDate);
            ps.setDate(4, finishDate);
            ps.setDouble(5, Double.parseDouble(price));
            ps.setString(6, "Запланирован");
            ps.setString(7, descr);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delOrder(String number) throws WrongGetException {
        String sql = "DELETE  FROM orders WHERE id = ? ";
        Connection con = DataSourceConfig.getInstance();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(number));
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new WrongGetException("Нельзя удалить заказ!");
        }
    }

    public List<OrderDTO> getOrderDTOList(){
        String sql = "SELECT o.id as id, c.last_name as client_surname, m.last_name as master_surname, o.create_date as date_create, " +
                "o.finish_date as date_finish, o.price as price, o.status as status, o.descr as descr" +
                " FROM orders o inner join client c on o.client=c.id " +
                "inner join master m on o.master=m.id";

        List<OrderDTO> orderList = new ArrayList<>();
        Connection con = DataSourceConfig.getInstance();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                    OrderDTO order = new OrderDTO();
                    order.setId(rs.getLong("id"));
                    order.setClientSurname(rs.getString("client_surname"));
                    order.setMasterSurname(rs.getString("master_surname"));
                    order.setCreateDate(rs.getDate("date_create").toString());
                    order.setFinishDate(rs.getDate("date_finish").toString());
                    order.setPrice(rs.getDouble("price"));
                    order.setStatus(rs.getString("status"));
                    order.setDescription(rs.getString("descr"));
                    orderList.add(order);
                }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderList;
    }

    public Order getOrder(String id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        Connection con = DataSourceConfig.getInstance();
        Order order = new Order();
        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, Long.parseLong(id));
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                order.setId(rs.getLong("id"));
                order.setClient(rs.getLong("client"));
                order.setMaster(rs.getLong("master"));
                order.setCreateDate(rs.getDate("create_date"));
                order.setFinishDate(rs.getDate("finish_date"));
                order.setPrice(rs.getDouble("price"));
                order.setStatus(rs.getString("status"));
                order.setDescription(rs.getString("descr"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return order;
    }

    public void updateOrder(Long id, String description, String price, Date finishDate, Long masterId, String status){
        String sql = "UPDATE orders set descr = ?, price = ?, finish_date = ?, master = ?, status = ? WHERE id = ?";
        Connection con = DataSourceConfig.getInstance();
        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, description);
            ps.setDouble(2, Double.parseDouble(price));
            ps.setDate(3, finishDate);
            ps.setLong(4,masterId);
            ps.setString(5, status);
            ps.setLong(6, id);

            ps.execute();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
