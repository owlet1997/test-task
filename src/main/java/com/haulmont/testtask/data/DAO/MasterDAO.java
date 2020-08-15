package com.haulmont.testtask.data.DAO;

import com.haulmont.testtask.data.DTO.StatisticsDTO;
import com.haulmont.testtask.DataSourceConfig;
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

public class MasterDAO {

    private static MasterDAO masterDAO;

    private MasterDAO(){}

    public static MasterDAO getInstance() {
        if (masterDAO == null){
            masterDAO = new MasterDAO();
        }
        return masterDAO;
    }

    public void addMaster(String name, String surname, String fatherName, Long salary) {

        EntityManager manager = DataSourceConfig.getInstance();
        manager.getTransaction().begin();

        Master master = new Master();
        master.setName(name);
        master.setSurname(surname);
        master.setFatherName(fatherName);
        master.setSalary(salary);

        manager.persist(master);
        manager.getTransaction().commit();
        manager.close();
    }

    public void delMaster(String number) throws WrongGetException {

        EntityManager manager = DataSourceConfig.getInstance();
        manager.getTransaction().begin();

        Master master = manager.find(Master.class, Integer.parseInt(number));

        if (master==null){
            throw new WrongGetException("Нельзя удалить данного мастера!");
        }
        manager.remove(master);

        manager.getTransaction().commit();
        manager.close();
    }

    public List<Master> getMasterList(){
        EntityManager manager = DataSourceConfig.getInstance();
        manager.getTransaction().begin();

        Query query = manager.createQuery("SELECT * from master e");

        List<Master> masterList = query.getResultList();
        manager.close();

        return masterList;
    }

    public void updateMaster(Long id, String fName, String lName, String fatherName, String salary){
        EntityManager manager = DataSourceConfig.getInstance();
        manager.getTransaction().begin();

        Master master = manager.find(Master.class, id);
        master.setName(fName);
        master.setSurname(lName);
        master.setFatherName(fatherName);
        master.setSalary(Long.parseLong(salary));

        manager.getTransaction().commit();
        manager.close();

    }

    public Master getMaster(String id) throws WrongGetException {
        EntityManager manager = DataSourceConfig.getInstance();
        manager.getTransaction().begin();

        Master master = manager.find(Master.class, Long.parseLong(id));
        if (master==null){
            throw new WrongGetException("Нет мастера с таким номером!");
        }
        manager.close();
        return master;
    }

    public List<StatisticsDTO> getStatistics(){
        List<StatisticsDTO> dtoList = new ArrayList<>();
        EntityManager manager = DataSourceConfig.getInstance();
        manager.getTransaction().begin();

        Query query = manager.createQuery("SELECT NEW com.haulmont.testtask.data.DTO.StatisticsDTO(m.first_name, m.last_name, m.father_name, count(m.id) as amount) FROM master m INNER JOIN orders o where m.id=o.master group by m.id");

        List<StatisticsDTO> list = query.getResultList();
        manager.close();

        return list;
    }
}
