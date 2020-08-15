package com.haulmont.testtask.data.DAO;

import com.haulmont.testtask.DataSourceConfig;
import com.haulmont.testtask.data.entities.Client;
import com.haulmont.testtask.data.entities.Master;
import com.haulmont.testtask.data.exception.WrongGetException;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

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

        EntityManager manager = DataSourceConfig.getInstance();
        manager.getTransaction().begin();

        Client client = new Client();
        client.setName(fName);
        client.setSurname(lName);
        client.setFatherName(fatherName);
        client.setPhone(phone);

        manager.persist(client);
        manager.getTransaction().commit();
        manager.close();
    }

    public void delClient(String number) throws WrongGetException {
        EntityManager manager = DataSourceConfig.getInstance();
        manager.getTransaction().begin();

        Client client = manager.find(Client.class, Long.parseLong(number));

        if (client == null){
            throw new WrongGetException("Нельзя удалить этого клиента!");
        }
        manager.remove(client);
        manager.getTransaction().commit();
        manager.close();
    }

    public List<Client> getClientList(){
        EntityManager manager = DataSourceConfig.getInstance();
        manager.getTransaction().begin();

        Query query = manager.createQuery("select  * from client e");
        List<Client> list = query.getResultList();

        return list;
    }

     public void updateClient(Long id, String fName, String lName, String fatherName, String phone) throws WrongGetException {
         EntityManager manager = DataSourceConfig.getInstance();
         manager.getTransaction().begin();

         Client client = manager.find(Client.class, id);
         if (client == null){
             throw new WrongGetException("Нельзя удалить этого клиента!");
         }
         client.setName(fName);
         client.setSurname(lName);
         client.setFatherName(fatherName);
         client.setPhone(phone);

         manager.getTransaction().commit();
         manager.close();
    }

    public Client getClient(String id) throws WrongGetException {
        EntityManager manager = DataSourceConfig.getInstance();
        manager.getTransaction().begin();

        Client client = manager.find(Client.class, id);
        manager.close();

        return client;
    }
}
