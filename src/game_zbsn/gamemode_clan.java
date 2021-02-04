/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game_zbsn;

import java.awt.Color;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
/**
 *
 * @author User
 */
public class gamemode_clan extends JFrame implements ActionListener{
    private int window_height,window_width;
    private DBConnector dbConnector;
        
    private List<String> ClanMembersData = new ArrayList<>();
    private List<String> ClanData = new ArrayList<>();
    private List<String> PlayerData = new ArrayList<>();

    private JLabel lClanMembers, lClans, lPlayers, lClanName;
    private JButton bLeaveClan, bJoinClan;
    private JList listClanMembers = new JList(), listClans = new JList(ClanData.toArray()), 
                            listPlayers = new JList(PlayerData.toArray());
    private JScrollPane scrollClans, scrollPlayers, scrollClanMembers;
    
    
    public gamemode_clan(int w, DBConnector dbConnector){
        this.window_height = w; this.window_width = w;
        this.dbConnector = dbConnector;
        setSize(this.window_width, this.window_height);
        setTitle("gamemode clans");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setVisible(true);
        
        
        testInit();
        initButtons();
        Updateable();
        getData();
    }
    public void getData(){
        try{
            ClanData = dbConnector.getClans();
            PlayerData = dbConnector.getPlayers();
            
            listClans.setListData(ClanData.toArray());
            listPlayers.setListData(PlayerData.toArray());
        }catch(SQLException ex){Logger.getLogger(gamemode_clan.class.getName()).log(Level.SEVERE,
                                                                "get data error",ex);}
    }
    public void Updateable(){
        listClanMembers.setListData(ClanMembersData.toArray());
        listClanMembers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listClanMembers.setLayoutOrientation(VERTICAL);
        listClanMembers.setVisibleRowCount(-1);
        add(listClanMembers);
        scrollClanMembers = new JScrollPane(listClanMembers);
        scrollClanMembers.setPreferredSize(new Dimension(250, 100));
        scrollClanMembers.setBounds(30, 240, 150, 150);
        add(scrollClanMembers);
        
    }
    public void initButtons(){
        bJoinClan = new JButton("Join clan");
        bJoinClan.setBounds(310, 100, 100, 30);
        bJoinClan.addActionListener(this);
        add(bJoinClan);
        
        bLeaveClan = new JButton("Leave clan");
        bLeaveClan.setBounds(310, 150, 100, 30);
        bLeaveClan.addActionListener(this);
        add(bLeaveClan);
    }
    public void testInit(){
        lClanMembers = new JLabel("Members of");
        lClanMembers.setBounds(30,205,100,20);
        JLabel lCM = new JLabel("selected Clan:");
        lCM.setBounds(30, 220, 100, 20);
        add(lClanMembers); add(lCM);
        
        
            listClans.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            listClans.setLayoutOrientation(VERTICAL);
            listClans.setVisibleRowCount(1);
            listClans.addListSelectionListener((e) ->{
                JList list = (JList) e.getSource();
                String selected = list.getSelectedValue().toString();
                System.out.println(selected);
                try{
                    ClanMembersData = dbConnector.getClanMembers(listClans.getSelectedValue().toString());
                    listClanMembers.setListData(ClanMembersData.toArray());}
                catch(SQLException ex){Logger.getLogger(gamemode_clan.class.getName()).log(Level.SEVERE,
                                                                        "listener listClans error",ex);}
             
            }
            );
            add(listClans);
        
        lClans = new JLabel("Clans:");
        lClans.setBounds(30, 30, 100, 20);
        add(lClans);
        scrollClans = new JScrollPane(listClans);
        scrollClans.setPreferredSize(new Dimension(250, 100));
        scrollClans.setBounds(30, 55, 100, 150);
        add(scrollClans);
        
        lClanName = new JLabel("");
        lClanName.setForeground(Color.BLACK);
        lClanName.setBounds(250, 55, 200, 20);
        add(lClanName);
        
        
        listPlayers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listPlayers.setLayoutOrientation(VERTICAL);
        listPlayers.setVisibleRowCount(1);
        listPlayers.addListSelectionListener((e)->{
            JList list = (JList) e.getSource();
            String selected = list.getSelectedValue().toString();
            int id = dbConnector.getId(selected);
            System.out.println(id);
            
            try{
                PreparedStatement stmt = dbConnector.getConnection().prepareStatement(
                "select founder, clan_name from membership where member_id = ?");
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                String founder = rs.getString("founder");
                String clanName = rs.getString("clan_name");
                if(founder.equals("Y"))
                    founder = "Founder of the ";
                else if(founder.equals("N"))
                    founder = "Member of the ";
                lClanName.setText(founder + clanName);
                
                rs.close();
                stmt.close();
            }catch(SQLException ex){Logger.getLogger(gamemode_clan.class.getName()).log(Level.SEVERE, "playerlist", ex);
            lClanName.setText(list.getSelectedValue().toString()+" - without a clan");}
        
        
        });
        add(listPlayers);
        
        lPlayers = new JLabel("All Players:");
        lPlayers.setBounds(140, 30, 100, 20);
        add(lPlayers);
        scrollPlayers = new JScrollPane(listPlayers);
        scrollPlayers.setPreferredSize(new Dimension(250, 100));
        scrollPlayers.setBounds(140, 55, 100, 150);
        add(scrollPlayers);
    }
    public String findClan(int id){
        String result = "";
        try{
            
            Statement stmt = dbConnector.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT clan_name FROM membership WHERE member_id ="+ Integer.toString(id));
            while(rs.next()){
                result = rs.getString("CLAN_NAME"); 
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
        Object source = e.getSource();
        if(source == bJoinClan){
            String result;
            if(listPlayers.getSelectedValue()==null ||listClans.getSelectedValue()==null){
                System.out.println("Join clan missing data");
            }
            else{
                try{
                    int id = dbConnector.getId(listPlayers.getSelectedValue().toString());
                    String name = listClans.getSelectedValue().toString();
                    result = dbConnector.joinClan(name, id);
                    System.out.println(result);
                }catch(SQLException ex){Logger.getLogger(gamemode_clan.class.getName()).log(Level.SEVERE,
                                                                "Join clan error",ex);}
            }
        
        }
        if(source == bLeaveClan){
            String result;
            if(listClanMembers.getSelectedValue()==null)
                System.out.println("Leave clan missing data");
            else{
                try{
                    result = dbConnector.leaveClan(dbConnector.getId( listClanMembers.getSelectedValue().toString()));
                    System.out.println(result);
                }catch(SQLException ex){Logger.getLogger(gamemode_clan.class.getName()).log(Level.SEVERE,
                                                                "Leave clan error",ex);}
            }
        
        }
        getData();
    }
    
}
