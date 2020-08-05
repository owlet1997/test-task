package com.haulmont.testtask.data.DAO;

import com.haulmont.testtask.DataSourceConfig;
import com.haulmont.testtask.data.DTO.OrderDTO;
import com.haulmont.testtask.data.entities.Order;
import com.haulmont.testtask.data.enums.Status;
import com.haulmont.testtask.data.exception.WrongDeleteException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderDAO {

    private DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);

    public void addOrder(String client, String master, Date createDate,
                         Date finishDate, String price) {

        String sql = "INSERT INTO orders (client, master, create_date, finish_date," +
                " price, status) VALUES (?, ?, ?, ?, ?, ?) ";
        Connection con = DataSourceConfig.getInstance();

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, Long.parseLong(client));
            ps.setLong(2, Long.parseLong(master));
            ps.setDate(3, (java.sql.Date) createDate);
            ps.setDate(4, (java.sql.Date) finishDate);
            ps.setDouble(5, Double.parseDouble(price));
            ps.setString(6, Status.PLANNED.getDescription());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delOrder(String number) throws WrongDeleteException {
        String sql = "DELETE  FROM orders WHERE id = ? ";
        Connection con = DataSourceConfig.getInstance();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(number));
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new WrongDeleteException("Нельзя удалить заказ!");
        }
    }

    public List<Order> getOrdersList(String client, String master){
        String sql = "SELECT * FROM orders WHERE client = ? AND master = ?";
        List<Order> orderList = new ArrayList<>();
        Connection con = DataSourceConfig.getInstance();
        try {
            if (client==null) client = "";
            if (master==null) master = "";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, Long.parseLong(client));
            ps.setLong(2, Long.parseLong(client));

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Order order = new Order();
                order.setId(rs.getLong("id"));
                order.setClient(rs.getLong("client"));
                order.setMaster(rs.getLong("master"));
                order.setCreateDate((java.sql.Date) rs.getDate("create_date"));
                order.setFinishDate((java.sql.Date) rs.getDate("finish_date"));
                order.setPrice(rs.getDouble("price"));
                order.setStatus(Status.valueOf(rs.getString("status")));
                orderList.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderList;
    }

    public List<OrderDTO> getOrderDTOList(String client, String master){
        String sql = "SELECT * FROM orders WHERE client = ? AND master = ?";
        String sqlClientSurname = "SELECT last_name FROM client WHERE id = ?";
        String sqlMasterSurname = "SELECT last_name FROM master WHERE id = ?";
        List<OrderDTO> orderList = new ArrayList<>();
        Connection con = DataSourceConfig.getInstance();
        try {
            if (client==null) client = "";
            if (master==null) master = "";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, Long.parseLong(client));
            ps.setLong(2, Long.parseLong(client));

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                OrderDTO order = new OrderDTO();
                order.setId(rs.getLong("id"));
                PreparedStatement psClient = con.prepareStatement(sqlClientSurname);
                PreparedStatement psMaster = con.prepareStatement(sqlMasterSurname);
                psClient.setLong(1,rs.getLong("client"));
                psMaster.setLong(1,rs.getLong("master"));
                ResultSet rsClient = psClient.executeQuery();
                ResultSet rsMaster = psMaster.executeQuery();
                order.setClientSurname(rsClient.getString("last_name"));
                order.setMasterSurname(rsMaster.getString("last_name"));
                order.setCreateDate(rs.getDate("create_date").toString());
                order.setFinishDate(rs.getDate("finish_date").toString());
                order.setPrice(rs.getDouble("price"));
                order.setStatus(rs.getString("status"));
                orderList.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderList;
    }

    void updateOpder(String id, String finishDate, Double price, Status status){
        String sql = "UPDATE orders SET finish_date = ?, price = ?, status = ? WHERE id = ?";
        Connection con = DataSourceConfig.getInstance();
        Date fDate = null;
        try{
            fDate = format.parse(finishDate);
        } catch (ParseException e){
            e.printStackTrace();
        }
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDate(1, (java.sql.Date) fDate);
            ps.setDouble(2, price);
            ps.setString(3, status.getDescription());
            ps.setLong(4, Long.parseLong(id));
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

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
                order.setStatus(Status.valueOf(rs.getString("status")));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return order;
    }

    public void updateOrder(Long id, String description, String price, Date finishDate, Long masterId, String status){
        String sql = "UPDATE orders set description = ?, price = ?, finish_date = ?, master = ?, status = ? WHERE id = ?";
        Connection con = DataSourceConfig.getInstance();
        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, description);
            ps.setDouble(2, Double.parseDouble(price));
            ps.setDate(3, (java.sql.Date) finishDate);
            ps.setLong(4,masterId);
            ps.setString(5, status);
            ps.setLong(6, id);

            ps.execute();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}