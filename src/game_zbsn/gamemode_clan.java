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
    private int windowHeight, windowWidth;
    private DBConnector dbConnector;
        
    private List<String> clanMembersData = new ArrayList<>();
    private List<String> clanData = new ArrayList<>();
    private List<String> playerData = new ArrayList<>();

    private JLabel clanMembersLabel, clansLabel, playersLabel, clanNameLabel, levelLabel, HQLabel, infoLabel;
    private JButton leaveClanButton, joinClanButton, updateButton;
    private JList clanMembersList = new JList(), clansList = new JList(clanData.toArray()), 
                            playersList = new JList(playerData.toArray());
    private JScrollPane scrollClans, scrollPlayers, scrollClanMembers;
    
    
    public gamemode_clan(int w, DBConnector dbConnector){
        this.windowHeight = w; this.windowWidth = w;
        this.dbConnector = dbConnector;
        setSize(this.windowWidth, this.windowHeight);
        setTitle("gamemode clans");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setVisible(true);
        setResizable(false);
        
        
        
        testInit();
        initButtons();
        Updateable();
        getData();
    }
    public void getData(){
        try{
            clanData = dbConnector.getClans();
            playerData = dbConnector.getPlayers();
            
            clansList.setListData(clanData.toArray());
            playersList.setListData(playerData.toArray());
        }catch(SQLException ex){Logger.getLogger(gamemode_clan.class.getName()).log(Level.SEVERE,
                                                                "get data error",ex);}
    }
    public void Updateable(){
        clanMembersList.setListData(clanMembersData.toArray());
        clanMembersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        clanMembersList.setLayoutOrientation(VERTICAL);
        clanMembersList.setVisibleRowCount(-1);
        add(clanMembersList);
        scrollClanMembers = new JScrollPane(clanMembersList);
        scrollClanMembers.setPreferredSize(new Dimension(250, 100));
        scrollClanMembers.setBounds(30, 240, 150, 150);
        add(scrollClanMembers);
        
    }
    public void initButtons(){
        updateButton = new JButton("Update clan");
        updateButton.setBounds(210, 290, 120, 30);
        updateButton.addActionListener(this);
        add(updateButton);
        
        joinClanButton = new JButton("Join clan");
        joinClanButton.setBounds(310, 150, 100, 30);
        joinClanButton.addActionListener(this);
        add(joinClanButton);
        
        leaveClanButton = new JButton("Leave clan");
        leaveClanButton.setBounds(210, 330, 120, 30);
        leaveClanButton.addActionListener(this);
        add(leaveClanButton);
    }
    public void testInit(){
        infoLabel = new JLabel("");
        infoLabel.setBounds(20, 400, 430, 20);
        infoLabel.setOpaque(true);
        infoLabel.setBackground(Color.WHITE);
        add(infoLabel);
        
        clanMembersLabel = new JLabel("Members of selected Clan:");
        clanMembersLabel.setBounds(30,205,200,20);
        add(clanMembersLabel);
        HQLabel = new JLabel("Headquater:");
        HQLabel.setBounds(200, 240, 150, 20);
        add(HQLabel);
        levelLabel = new JLabel("Clan level:");
        levelLabel.setBounds(200, 260, 150, 20);
        add(levelLabel);
        
        clansList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        clansList.setLayoutOrientation(VERTICAL);
        clansList.setVisibleRowCount(1);
        clansList.addListSelectionListener((e) ->{
                JList list = (JList) e.getSource();
                String selected = list.getSelectedValue().toString();
                System.out.println(selected);
                try{
                    clanMembersData = dbConnector.getClanMembers(clansList.getSelectedValue().toString());
                    clanMembersList.setListData(clanMembersData.toArray());}
                catch(SQLException ex){Logger.getLogger(gamemode_clan.class.getName()).log(Level.SEVERE,
                                                                        "listener listClans error",ex);}
                try{
                    int lvl=0; 
                    String HQ="";
                    Statement stmt = dbConnector.getConnection().createStatement();
                    ResultSet rs = stmt.executeQuery("select clan_level, headquater from clans where clan_name = '"+selected+"'");
                    while(rs.next()){
                        lvl = rs.getInt("clan_level");
                        HQ = rs.getString("HEADQUATER");
                    }
                    HQLabel.setText("Headquater: " + HQ);
                    levelLabel.setText("Clan level: "+ Integer.toString(lvl));
                }catch(SQLException ex){Logger.getLogger(gamemode_clan.class.getName()).log(Level.SEVERE,
                                                                        "listener cv  vbnvnvblistClans error",ex);}
             
            }
            );
            add(clansList);
        
        clansLabel = new JLabel("Clans:");
        clansLabel.setBounds(30, 30, 100, 20);
        add(clansLabel);
        scrollClans = new JScrollPane(clansList);
        scrollClans.setPreferredSize(new Dimension(250, 100));
        scrollClans.setBounds(30, 55, 100, 150);
        add(scrollClans);
        
        clanNameLabel = new JLabel("");
        clanNameLabel.setForeground(Color.BLACK);
        clanNameLabel.setBounds(275, 55, 200, 40);
        add(clanNameLabel);
        
        
        playersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        playersList.setLayoutOrientation(VERTICAL);
        playersList.setVisibleRowCount(1);
        playersList.addListSelectionListener((e)->{
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
                clanNameLabel.setText(founder + clanName);
                
                rs.close();
                stmt.close();
            }catch(SQLException ex){Logger.getLogger(gamemode_clan.class.getName()).log(Level.SEVERE, "playerlist", ex);
            clanNameLabel.setText(list.getSelectedValue().toString()+"- without a clan");}
        
        
        });
        add(playersList);
        
        playersLabel = new JLabel("All Players:");
        playersLabel.setBounds(140, 30, 100, 20);
        add(playersLabel);
        scrollPlayers = new JScrollPane(playersList);
        scrollPlayers.setPreferredSize(new Dimension(250, 100));
        scrollPlayers.setBounds(140, 55, 130, 150);
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
        if(source == joinClanButton){
            String result;
            if(playersList.getSelectedValue()==null ||clansList.getSelectedValue()==null){
                System.out.println("Join clan missing data");
            }
            else{
                try{
                    int id = dbConnector.getId(playersList.getSelectedValue().toString());
                    String name = clansList.getSelectedValue().toString();
                    result = dbConnector.joinClan(name, id);
                    System.out.println(result);
                    infoLabel.setText(result);
                }catch(SQLException ex){Logger.getLogger(gamemode_clan.class.getName()).log(Level.SEVERE,
                                                                "Join clan error",ex);
                                        infoLabel.setText("Something gone wrong with joining clan");
                                        infoLabel.setForeground(Color.red);}
            }
        
        }
        if(source == leaveClanButton){
            String result;
            if(clanMembersList.getSelectedValue()==null)
                System.out.println("Leave clan missing data");
            else{
                try{
                    result = dbConnector.leaveClan(dbConnector.getId( clanMembersList.getSelectedValue().toString()));
                    System.out.println(result);
                    infoLabel.setText(result);
                }catch(SQLException ex){Logger.getLogger(gamemode_clan.class.getName()).log(Level.SEVERE,
                                                                "Leave clan error",ex);
                                        infoLabel.setText("Something gone wrong with leaving a clan");
                                        infoLabel.setForeground(Color.red);}
            }
        }
        if(source == updateButton){
            if(clansList.getSelectedValue() == null){}
            else{
                String name = clansList.getSelectedValue().toString();
                try{
                    Statement stmt = dbConnector.getConnection().createStatement();
                    ResultSet rs = stmt.executeQuery("UPDATE clans set clan_level = clan_level + 1 where clan_name like '"+name+"'");
                    infoLabel.setText("Succesfully upgraded "+name+" to next level");
                    rs.close();
                    stmt.close();
                }catch(SQLException ex){
                    infoLabel.setText(name + " was not upgraded to next level");
                    infoLabel.setForeground(Color.red);}
            }
        }
        getData();
    }
    
}
