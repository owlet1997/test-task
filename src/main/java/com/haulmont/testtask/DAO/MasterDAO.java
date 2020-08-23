package com.haulmont.testtask.DAO;

import com.haulmont.testtask.data.DTO.StatisticsDTO;
import com.haulmont.testtask.config.DataSourceConfig;
import com.haulmont.testtask.data.entities.Master;
import com.haulmont.testtask.exception.WrongGetException;
import org.hibernate.Session;

import javax.persistence.Query;
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

        Session session = DataSourceConfig.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Master master = new Master();
        master.setName(name);
        master.setSurname(surname);
        master.setFatherName(fatherName);
        master.setSalary(salary);

        session.persist(master);
        session.getTransaction().commit();
        session.close();
    }

    public void delMaster(String number) throws WrongGetException {

        Session session = DataSourceConfig.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Master master = session.find(Master.class, Integer.parseInt(number));

        if (master==null){
            throw new WrongGetException("Нельзя удалить данного мастера!");
        }
        session.remove(master);

        session.getTransaction().commit();
        session.close();
    }

    public List<Master> getMasterList(){
        Session session = DataSourceConfig.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Query query = session.createQuery("SELECT * from master e");

        List<Master> masterList = query.getResultList();
        session.close();

        return masterList;
    }

    public void updateMaster(Long id, String fName, String lName, String fatherName, String salary){
        Session session = DataSourceConfig.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Master master = session.find(Master.class, id);
        master.setName(fName);
        master.setSurname(lName);
        master.setFatherName(fatherName);
        master.setSalary(Long.parseLong(salary));

        session.getTransaction().commit();
        session.close();

    }

    public Master getMaster(String id) throws WrongGetException {
        Session session = DataSourceConfig.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Master master = session.find(Master.class, Long.parseLong(id));
        if (master==null){
            throw new WrongGetException("Нет мастера с таким номером!");
        }
        session.close();
        return master;
    }

    public List<StatisticsDTO> getStatistics(){
        List<StatisticsDTO> dtoList = new ArrayList<>();
        Session session = DataSourceConfig.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Query query = session.createQuery("SELECT NEW com.haulmont.testtask.data.DTO.StatisticsDTO(m.first_name, m.last_name, m.father_name, count(m.id) as amount) FROM master m INNER JOIN orders o where m.id=o.master group by m.id");

        List<StatisticsDTO> list = query.getResultList();
        session.close();

        return list;
    }
}
