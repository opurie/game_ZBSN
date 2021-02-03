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
    public List<String> getPlayers() throws SQLException{
        List<String> data = new ArrayList<>();
        CallableStatement stmt = connection.prepareCall("{? = call get_players}");
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
    public void createPlayer(String name, String profession, String race)throws SQLException{
        CallableStatement stmt = connection.prepareCall("{call create_player(?, ?, ?)}");
        stmt.setString(1, name);
        stmt.setString(2, profession);
        stmt.setString(3, race);
        stmt.execute();
        stmt.close();
    }
    public void deletePlayer(int id) throws SQLException{
        CallableStatement stmt = connection.prepareCall("{call delete_player(?)}");
        stmt.setInt(1, id);
        stmt.execute();
        stmt.close();
    }
    public List<String> getMonsters() throws SQLException{
        List<String> data = new ArrayList<>();
        CallableStatement stmt = connection.prepareCall("{? = call get_monsters}");
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
    public void createMonster(String name, String item, String race)throws SQLException{
        CallableStatement stmt = connection.prepareCall("{call create_monster(?, ?, ?)}");
        stmt.setString(1, name);
        stmt.setString(2, item);
        stmt.setString(3, race);
        stmt.execute();
        stmt.close();
    }
    public void deleteMonster(int id) throws SQLException{
        CallableStatement stmt = connection.prepareCall("{call delete_monster(?)}");
        stmt.setInt(1, id);
        stmt.execute();
        stmt.close();
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
    
    public void createItem(String name, int strength, int agility, int intellect, int weight, String profession) throws SQLException {
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
    
    public String pickUpItem(int id, String name)throws SQLException{
        CallableStatement stmt = connection.prepareCall("{? = call pick_up(?, ?)}");
        stmt.registerOutParameter(1, Types.VARCHAR);
        stmt.setInt(2, id);
        stmt.setString(3, name);
        stmt.execute();
        String result = stmt.getString(1);
        stmt.close();
        return result;
    }
    
    public String dropItem(int id, String name)throws SQLException{
        CallableStatement stmt = connection.prepareCall("{? = call drop_item(?, ?)}");
        stmt.registerOutParameter(1, Types.VARCHAR);
        stmt.setInt(2, id);
        stmt.setString(3, name);
        stmt.execute();
        String result = stmt.getString(1);
        stmt.close();
        return result;
    }
    
    public void createQuest(String name, int exp, int id)throws SQLException{
        CallableStatement stmt = connection.prepareCall("{call create_quest(?, ?, ?)}");
        stmt.setString(1, name);
        stmt.setInt(2, exp);
        stmt.setInt(3, id);
        stmt.execute();
        stmt.close();
    }
    
    public void deleteQuest(String name)throws SQLException{
        CallableStatement stmt = connection.prepareCall("{call delete_quest(?)}");
        stmt.setString(1, name);
        stmt.execute();
        stmt.close();
    }
    
    public List<String> getQuests() throws SQLException {
        List<String> data = new ArrayList<>();
        CallableStatement stmt = connection.prepareCall("{? = call get_quests}");
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
    public String createClan(String name, int id, String headquater)throws SQLException{
        CallableStatement stmt = connection.prepareCall("{? = call create_clan(? ,? , ?)}");
        stmt.registerOutParameter(1, Types.VARCHAR);
        stmt.setString(2, name);
        stmt.setInt(3, id);
        stmt.setString(4, headquater);
        stmt.execute();
        String result = stmt.getString(1);
        stmt.close();
        return result;
    }
    
    public void deleteClan(String name) throws SQLException {
        CallableStatement stmt = connection.prepareCall("{call delete_clan(?)}");
        stmt.setString(1, name);
        stmt.execute();
        stmt.close();
    }
    
    public List<String> getClans() throws SQLException {
        List<String> data = new ArrayList<>();
        CallableStatement stmt = connection.prepareCall("{? = call get_clans}");
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
    
    public String joinClan(String name, int id)throws SQLException{
        CallableStatement stmt = connection.prepareCall("{? = call join_clan(? ,?)}");
        stmt.registerOutParameter(1, Types.VARCHAR);
        stmt.setString(2, name);
        stmt.setInt(3, id);
        stmt.execute();
        String result = stmt.getString(1);
        stmt.close();
        return result;
    }
    public String leaveClan(int id)throws SQLException{
        CallableStatement stmt = connection.prepareCall("{? = call leave_clan(?)}");
        stmt.registerOutParameter(1, Types.VARCHAR);
        stmt.setInt(2, id);
        stmt.execute();
        String result = stmt.getString(1);
        stmt.close();
        return result;
    }
    public int getId(String name){
        String[] s = name.split(". ");
        return Integer.parseInt(s[0]);
    }
}
