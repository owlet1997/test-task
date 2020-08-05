package com.haulmont.testtask.data.DAO;

import com.haulmont.testtask.DataSourceConfig;
import com.haulmont.testtask.data.entities.Client;
import com.haulmont.testtask.data.exception.WrongDeleteException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    private static ClientDAO clientDAO;

    public static ClientDAO getInstance(){
        if (clientDAO == null) {
            clientDAO = new ClientDAO();
        }
        return clientDAO;
    }

    public void addClient(String fName, String lName, String fatherName, String phone) {

        String sql = "INSERT INTO client (first_name, last_name, father_name, phone) VALUES (?, ?, ?, ?) ";
        if (fatherName == null) fatherName = "";
        Connection con = DataSourceConfig.getInstance();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, fName);
            ps.setString(2, lName);
            ps.setString(3, fatherName);
            ps.setString(4, phone);
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delClient(String number) throws WrongDeleteException {
        String sql = "DELETE  FROM client WHERE id = ? ";
        Connection con = DataSourceConfig.getInstance();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.valueOf(number));
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new WrongDeleteException("Нельзя удалить этого клиента!");
        }
    }

    public List<Client> getClientList(String fName, String lName){
        String sql = "SELECT * FROM client WHERE first_name = ? AND last_name = ?";
        List<Client> clientList = new ArrayList<>();
        Connection con = DataSourceConfig.getInstance();
        try {
            if (fName==null) fName = "";
            if (lName==null) lName = "";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, fName);
            ps.setString(2, lName);

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Client client = new Client();
                client.setId(rs.getLong("id"));
                client.setFirstName(rs.getString("first_name"));
                client.setLastName(rs.getString("last_name"));
                client.setFatherName(rs.getString("father_name"));
                client.setPhone(rs.getString("phone"));
                clientList.add(client);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
         return clientList;
    }

     public void updateClient(Long id, String fName, String lName, String fatherName, String phone){
        String sql = "UPDATE client SET first_name = ?, last_name = ?, fatherName = ?, phone = ? WHERE id = ?";
        Connection con = DataSourceConfig.getInstance();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, fName);
            ps.setString(2, lName);
            ps.setString(3, fatherName);
            ps.setString(4, phone);
            ps.setLong(5,id);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Client getClient(String id) {
        String sql = "SELECT * FROM client WHERE id = ? ";
        Long clientId = Long.parseLong(id);

        Client client = new Client();

        Connection con = DataSourceConfig.getInstance();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, clientId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                client.setId(rs.getLong("id"));
                client.setFirstName(rs.getString("first_name"));
                client.setLastName(rs.getString("last_name"));
                client.setFatherName(rs.getString("father_name"));
                client.setPhone(rs.getString("salary"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return client;
    }
}
