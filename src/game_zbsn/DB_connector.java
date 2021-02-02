/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game_zbsn;

import java.sql.*;

/**
 *
 * @author User
 */
public class DB_connector {
    private Connection con = null;
    
    //służy do wyciągania id przy graczu i potworze. "2. Edward" returns int(2)
    public int getId(String name){
        String[] s = name.split(".");
        return Integer.parseInt(s[0]);
    }
    public DB_connector(Connection con){
        this.con = con;
    }
    

    public String[] get_names(String type){
        String[] result = {"obiekty","hehe"};
        
        return result;
    }

    
}
