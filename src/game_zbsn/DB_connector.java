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
    
    public DB_connector(Connection con){
        this.con = con;
    }
    

    public String[] get_names(String type){
        String[] result = {"obiekty","hehe"};
        
        return result;
    }

    
}
