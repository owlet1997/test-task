package com.haulmont.testtask.data.DAO;

import com.haulmont.testtask.data.DTO.StatisticsDTO;
import com.haulmont.testtask.DataSourceConfig;
import com.haulmont.testtask.data.entities.Master;
import com.haulmont.testtask.data.exception.WrongDeleteException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MasterDAO {

    public void addMaster(String fName, String lName, String fatherName, Long salary) {

        String sql = "INSERT INTO master (first_name, last_name, father_name, salary) VALUES (?, ?, ?, ?) ";
        Connection con = DataSourceConfig.getInstance();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, fName);
            ps.setString(2, lName);
            ps.setString(3, fatherName);
            ps.setLong(4, salary);
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delMaster(String number) throws WrongDeleteException{
        String sql = "DELETE  FROM master WHERE id = ? ";
        Connection con = DataSourceConfig.getInstance();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(number));
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new WrongDeleteException("Нельзя удалить данного мастера!");

        }
    }

    public List<Master> getMasterList(String fName, String lName){
        String sql = "SELECT * FROM master WHERE first_name = ? AND last_name = ?";
        List<Master> masterList = new ArrayList<>();
        Connection con = DataSourceConfig.getInstance();
        try {
            if (fName==null) fName = "";
            if (lName==null) lName = "";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, fName);
            ps.setString(2, lName);

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Master master = new Master();
                master.setId(rs.getLong("id"));
                master.setFirstName(rs.getString("first_name"));
                master.setLastName(rs.getString("last_name"));
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
        String sql = "UPDATE master SET first_name = ?, last_name = ?, fatherName = ?, salary = ? WHERE id = ?";
        Connection con = DataSourceConfig.getInstance();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
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

    public Master getMaster(String id)  {
        String sql = "SELECT * FROM master WHERE id = ? ";
        Long masterId = Long.parseLong(id);

        Master master = new Master();

        Connection con = DataSourceConfig.getInstance();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setLong(1, masterId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                master.setId(rs.getLong("id"));
                master.setFirstName(rs.getString("first_name"));
                master.setLastName(rs.getString("last_name"));
                master.setFatherName(rs.getString("father_name"));
                master.setSalary(rs.getLong("salary"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
                }
        return master;
    }

    public List<StatisticsDTO> getStatistics(){
        String sql = "SELECT first_name, last_name, father_name, count(o.master) as amount FROM master m " +
                "INNER JOIN orders o on (m.id=o.master) group by o.master";

        Connection con = DataSourceConfig.getInstance();

        List<StatisticsDTO> list = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                StatisticsDTO statisticsDTO = new StatisticsDTO();
                statisticsDTO.setName(rs.getString("first_name"));
                statisticsDTO.setLastName(rs.getString("last_name"));
                statisticsDTO.setFatherName(rs.getString("father_name"));
                statisticsDTO.setCountAll(rs.getInt("amount"));
                list.add(statisticsDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
