package com.haulmont.testtask.DAO;

import com.haulmont.testtask.DataSourceConfig;
import com.haulmont.testtask.entities.Order;
import com.haulmont.testtask.enums.Status;

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

    public void addOrder(String client, String master, String createDate,
                         String finishDate, Double price, Status status) {

        String sql = "INSERT INTO orders (client, master, create_date, finish_date," +
                " price, status) VALUES (?, ?, ?, ?, ?, ?) ";
        Connection con = DataSourceConfig.getInstance();
        Date crDate = null;
        Date fshDate = null;
        try {
            crDate = format.parse(createDate);
            fshDate = format.parse(finishDate);
        } catch ( ParseException e){
            e.printStackTrace();
        }
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, Long.parseLong(client));
            ps.setLong(2, Long.parseLong(master));
            ps.setDate(3, (java.sql.Date) crDate);
            ps.setDate(4, (java.sql.Date) fshDate);
            ps.setDouble(5, price);
            ps.setString(6, status.getDescription());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delOrder(String number){
        String sql = "DELETE  FROM orders WHERE id = ? ";
        Connection con = DataSourceConfig.getInstance();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(number));
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
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
}
