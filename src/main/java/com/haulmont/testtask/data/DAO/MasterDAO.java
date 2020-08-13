package com.haulmont.testtask.data.DAO;

import com.haulmont.testtask.data.DTO.StatisticsDTO;
import com.haulmont.testtask.DataSourceConfig;
import com.haulmont.testtask.data.entities.Master;
import com.haulmont.testtask.data.exception.WrongGetException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MasterDAO {

    private final String INSERT = "INSERT INTO master (first_name, last_name, father_name, salary) VALUES (?, ?, ?, ?) ";

    private final String DELETE = "DELETE  FROM master WHERE id = ? ";

    private final String SELECT_ALL = "SELECT * FROM master";

    private final String SELECT_ONE = "SELECT * FROM master WHERE id = ? ";
    private final String SELECT_STAT =  "SELECT m.first_name, m.last_name, m.father_name, " +
            "count(m.id) as amount FROM master m INNER JOIN orders o on (m.id=o.master) group by m.id";

    private final String UPDATE = "UPDATE master SET first_name = ?, last_name = ?, father_name = ?, salary = ? WHERE id = ?";

    private static MasterDAO masterDAO;

    private MasterDAO(){}

    public static MasterDAO getInstance() {
        if (masterDAO == null){
            masterDAO = new MasterDAO();
        }
        return masterDAO;
    }

    public void addMaster(String fName, String lName, String fatherName, Long salary) {

        Connection con = DataSourceConfig.getInstance();
        try {
            PreparedStatement ps = con.prepareStatement(INSERT);
            ps.setString(1, fName);
            ps.setString(2, lName);
            ps.setString(3, fatherName);
            ps.setLong(4, salary);
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delMaster(String number) throws WrongGetException {
        Connection con = DataSourceConfig.getInstance();
        try {
            PreparedStatement ps = con.prepareStatement(DELETE);
            ps.setInt(1, Integer.parseInt(number));
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new WrongGetException("Нельзя удалить данного мастера!");

        }
    }

    public List<Master> getMasterList(){
        List<Master> masterList = new ArrayList<>();
        Connection con = DataSourceConfig.getInstance();
        try {
            PreparedStatement ps = con.prepareStatement(SELECT_ALL);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Master master = new Master();
                master.setId(rs.getLong("id"));
                master.setName(rs.getString("first_name"));
                master.setSurname(rs.getString("last_name"));
                master.setFatherName(rs.getString("father_name"));
                master.setSalary(rs.getLong("salary"));
                masterList.add(master);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return masterList;
    }

    public void updateMaster(Long id, String fName, String lName, String fatherName, String salary){

        Connection con = DataSourceConfig.getInstance();
        try {
            PreparedStatement ps = con.prepareStatement(UPDATE);
            ps.setString(1, fName);
            ps.setString(2, lName);
            ps.setString(3, fatherName);
            ps.setLong(4, Long.parseLong(salary));
            ps.setLong(5, id);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Master getMaster(String id) throws WrongGetException {
        long masterId = Long.parseLong(id);

        Master master = new Master();

        Connection con = DataSourceConfig.getInstance();
        try {
            PreparedStatement ps = con.prepareStatement(SELECT_ONE);
            ps.setLong(1, masterId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                master.setId(rs.getLong("id"));
                master.setName(rs.getString("first_name"));
                master.setSurname(rs.getString("last_name"));
                master.setFatherName(rs.getString("father_name"));
                master.setSalary(rs.getLong("salary"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new WrongGetException("Нет мастера с таким кодом!");
        }
        return master;
    }

    public List<StatisticsDTO> getStatistics(){
       Connection con = DataSourceConfig.getInstance();

        List<StatisticsDTO> list = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(SELECT_STAT);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                StatisticsDTO statisticsDTO = new StatisticsDTO();
                statisticsDTO.setName(rs.getString("first_name"));
                statisticsDTO.setSurname(rs.getString("last_name"));
                statisticsDTO.setFatherName(rs.getString("father_name"));
                statisticsDTO.setAmount(rs.getInt("amount"));
                list.add(statisticsDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
