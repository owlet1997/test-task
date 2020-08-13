package com.haulmont.testtask.data.DAO;

import com.haulmont.testtask.DataSourceConfig;
import com.haulmont.testtask.data.entities.Client;
import com.haulmont.testtask.data.exception.WrongGetException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {
    private final String INSERT = "INSERT INTO client (first_name, last_name, father_name, phone) VALUES (?, ?, ?, ?) ";

    private final String DELETE = "DELETE  FROM client WHERE id = ? ";

    private final String SELECT_ALL = "SELECT * FROM client";

    private final String SELECT_ONE = "SELECT * FROM client WHERE id = ?";

    private final String UPDATE = "UPDATE client SET first_name = ?, last_name = ?, fatherName = ?, phone = ? WHERE id = ?";


    private static ClientDAO clientDAO;

    private ClientDAO(){
    }

    public static ClientDAO getInstance(){
        if (clientDAO == null) {
            clientDAO = new ClientDAO();
        }
        return clientDAO;
    }

    public void addClient(String fName, String lName, String fatherName, String phone) {

        if (fatherName == null) fatherName = "";
        Connection con = DataSourceConfig.getInstance();
        try {
            PreparedStatement ps = con.prepareStatement(INSERT);
            ps.setString(1, fName);
            ps.setString(2, lName);
            ps.setString(3, fatherName);
            ps.setString(4, phone);
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delClient(String number) throws WrongGetException {
        Connection con = DataSourceConfig.getInstance();
        try {
            PreparedStatement ps = con.prepareStatement(DELETE);
            ps.setInt(1, Integer.valueOf(number));
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new WrongGetException("Нельзя удалить этого клиента!");
        }
    }

    public List<Client> getClientList(){
        List<Client> clientList = new ArrayList<>();
        Connection con = DataSourceConfig.getInstance();
        try {
            PreparedStatement ps = con.prepareStatement(SELECT_ALL);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Client client = new Client();
                client.setId(rs.getLong("id"));
                client.setName(rs.getString("first_name"));
                client.setSurname(rs.getString("last_name"));
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
        Connection con = DataSourceConfig.getInstance();
        try {
            PreparedStatement ps = con.prepareStatement(UPDATE);
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

    public Client getClient(String id) throws WrongGetException {
        Long clientId = Long.parseLong(id);

        Client client = new Client();

        Connection con = DataSourceConfig.getInstance();
        try {
            PreparedStatement ps = con.prepareStatement(SELECT_ONE);
            ps.setLong(1, clientId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                client.setId(rs.getLong("id"));
                client.setName(rs.getString("first_name"));
                client.setSurname(rs.getString("last_name"));
                client.setFatherName(rs.getString("father_name"));
                client.setPhone(rs.getString("salary"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new WrongGetException("Нет клиента с таки номером!");
        }
        return client;
    }
}
