package com.haulmont.testtask.DAO;

import com.haulmont.testtask.config.DataSourceConfig;
import com.haulmont.testtask.data.entities.Client;
import com.haulmont.testtask.exception.WrongGetException;
import org.hibernate.Session;

import javax.persistence.Query;
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

        Session session = DataSourceConfig.getSessionFactory().getCurrentSession();

        session.beginTransaction();

        Client client = new Client();
        client.setName(fName);
        client.setSurname(lName);
        client.setFatherName(fatherName);
        client.setPhone(phone);

        session.persist(client);
        session.getTransaction().commit();
        session.close();
    }

    public void delClient(String number) throws WrongGetException {
        Session session = DataSourceConfig.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Client client = session.find(Client.class, Long.parseLong(number));

        if (client == null){
            throw new WrongGetException("Нельзя удалить этого клиента!");
        }
        session.remove(client);
        session.getTransaction().commit();
        session.close();
    }

    public List<Client> getClientList(){
        Session session = DataSourceConfig.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Query query = session.createQuery("select * from client e");
        List<Client> list = query.getResultList();

        return list;
    }

     public void updateClient(Long id, String fName, String lName, String fatherName, String phone) throws WrongGetException {
         Session session = DataSourceConfig.getSessionFactory().getCurrentSession();
         session.beginTransaction();

         Client client = session.find(Client.class, id);
         if (client == null){
             throw new WrongGetException("Нельзя удалить этого клиента!");
         }
         client.setName(fName);
         client.setSurname(lName);
         client.setFatherName(fatherName);
         client.setPhone(phone);

         session.getTransaction().commit();
         session.close();
    }

    public Client getClient(String id) throws WrongGetException {
        Session session = DataSourceConfig.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Client client = session.find(Client.class, id);
        session.close();

        return client;
    }
}
