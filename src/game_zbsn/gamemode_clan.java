/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game_zbsn;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import static javax.swing.JList.VERTICAL;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
/**
 *
 * @author User
 */
public class gamemode_clan extends JFrame implements ActionListener{
    private int window_height,window_width;
    private DBConnector dbConnector;
        
    private JLabel lClanMembers, lClans, lPlayers, lClanName;
    private JButton bLeaveClan, bJoinClan;
    private JList listClanMembers, listClans, listPlayers;
    private JScrollPane scrollClans, scrollPlayers;
    
    private List<String> ClanMembersData = new ArrayList<>();
    private List<String> ClanData = new ArrayList<>();
    private List<String> PlayerData = new ArrayList<>();

    public gamemode_clan(int w, DBConnector dbConnector){
        this.window_height = w; this.window_width = w;
        this.dbConnector = dbConnector;
        setSize(this.window_width, this.window_height);
        setTitle("gamemode clans");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setVisible(true);
        
        getData();
        testInit();
    }
    public void getData(){
        try{
            ClanData = dbConnector.getClans();
            PlayerData = dbConnector.getPlayers();
        }catch(SQLException ex){Logger.getLogger(gamemode_clan.class.getName()).log(Level.SEVERE,
                                                                "get data error",ex);}
    }
    public void testInit(){
        listClans = new JList(ClanData.toArray());
        listClans.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listClans.setLayoutOrientation(VERTICAL);
        listClans.setVisibleRowCount(-1);
        add(listClans);
        
        lClans = new JLabel("Clans:");
        lClans.setBounds(20, 75, 100, 20);
        add(lClans);
        scrollClans = new JScrollPane(listClans);
        scrollClans.setPreferredSize(new Dimension(250, 100));
        scrollClans.setBounds(90, 75, 100, 150);
        add(scrollClans);
    }
    public String findClan(int id){
        String result = "";
        try{
            
            Statement stmt = dbConnector.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT clan_name FROM membership WHERE member_id ="+ Integer.toString(id) + ";");
            while(rs.next()){
                result = rs.getString("CLAN_NAME");
                if(!result.equals(""))
                    ClanMembersData = dbConnector.getClanMembers(result);   
            }
            rs.close();
            stmt.close();
        }catch(SQLException ex){Logger.getLogger(gamemode_clan.class.getName()).log(Level.SEVERE,
                                                                "findClan error",ex);
        }
        return result;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
