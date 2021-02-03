/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game_zbsn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import oracle.jdbc.OracleTypes;

/**
 *
 * @author User
 */
public class DBConnector {
    private Connection connection = null;
    private String name, pass;
    
    public DBConnector(String name, String pass){
        this.name = name;
        this.pass = pass;
    }
    
    public String[] getNames(String type){
        String[] result = {"obiekty","hehe"};
        
        return result;
    }

    public Connection connect() throws SQLException{
        String connectionString = "jdbc:oracle:thin:@//admlab2.cs.put.poznan.pl:1521/"+
                               "dblab02_students.cs.put.poznan.pl";
        Properties connectionProps = new Properties();
        connectionProps.put("user", name);
        connectionProps.put("password", pass);
        Connection con = DriverManager.getConnection(connectionString, connectionProps);
        connection = con;
        return con;
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    public List<String> getProfessions() throws SQLException{
        List<String> data = new ArrayList<>();
        CallableStatement stmt = connection.prepareCall("{? = call get_professions}");
        stmt.registerOutParameter(1, OracleTypes.REF_CURSOR);
        stmt.execute();
        ResultSet result = (ResultSet)stmt.getObject(1);
        while(result.next()){
            data.add(result.getString(1));
        }
        result.close();
        stmt.close();
        return data;
    }
    
    public void deleteProfession(String profession) throws SQLException{
        CallableStatement stmt = connection.prepareCall("{call delete_profession(?)}");
        stmt.setString(1, profession);
        stmt.execute();
        stmt.close();
    }
    
    public void createProfession(String profession) throws SQLException{
        CallableStatement stmt = connection.prepareCall("{call create_profession(?)}");
        stmt.setString(1, profession);
        stmt.execute();
        stmt.close();
    }
    
    public List<String> getRaces() throws SQLException {
        List<String> data = new ArrayList<>();
        CallableStatement stmt = connection.prepareCall("{? = call get_races}");
        stmt.registerOutParameter(1, OracleTypes.REF_CURSOR);
        stmt.execute();
        ResultSet result = (ResultSet)stmt.getObject(1);
        while(result.next()){
            data.add(result.getString(1));
        }
        result.close();
        stmt.close();
        return data;
    }
    
    public void deleteRace(String name) throws SQLException {
        CallableStatement stmt = connection.prepareCall("{call delete_race(?)}");
        stmt.setString(1, name);
        stmt.execute();
        stmt.close();
    }
    
    public void createRace(String name, int strength, int agility, int intellect) throws SQLException {
        CallableStatement stmt = connection.prepareCall("{call create_race(?, ?, ?, ?)}");
        stmt.setString(1, name);
        stmt.setInt(2, strength);
        stmt.setInt(3, agility);
        stmt.setInt(4, intellect);
        stmt.execute();
        stmt.close();
    }
    
    public List<String> getItems() throws SQLException {
        List<String> data = new ArrayList<>();
        CallableStatement stmt = connection.prepareCall("{? = call get_items}");
        stmt.registerOutParameter(1, OracleTypes.REF_CURSOR);
        stmt.execute();
        ResultSet result = (ResultSet)stmt.getObject(1);
        while(result.next()){
            data.add(result.getString(1));
        }
        result.close();
        stmt.close();
        return data;
    }
    
    public void deleteItem(String name) throws SQLException {
        CallableStatement stmt = connection.prepareCall("{call delete_item(?)}");
        stmt.setString(1, name);
        stmt.execute();
        stmt.close();
    }
    
    public void createItem(String name, int strength, int agility, int intellect, 
            int weight, String profession) throws SQLException {
        CallableStatement stmt = connection.prepareCall("{call create_item(?, ?, ?, ?, ?, ?)}");
        stmt.setString(1, name);
        stmt.setInt(2, strength);
        stmt.setInt(3, agility);
        stmt.setInt(4, intellect);
        stmt.setInt(5, weight);
        stmt.setString(6, profession);
        stmt.execute();
        stmt.close();
    }
}
