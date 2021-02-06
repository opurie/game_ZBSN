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

/**
 *
 * @author User
 */
public class gamemode_quest extends JFrame implements ActionListener{
    private int window_height,window_width;
    private DBConnector dbConnector;
    
    private List<String> PlayerData = new ArrayList<>();
    private List<String> QuestData = new ArrayList<>();
    private List<String> PlayerQuestData = new ArrayList<>();
    
    private JLabel lPlayers, lQuests, lPlayerQuest, lEXP, lCreator, lDone, lInfo;
    private JButton bTakeTheTask, bSubmitTask;
    private JList listQuests = new JList(QuestData.toArray()), listPlayers= new JList(PlayerData.toArray()), 
                                        listPlayerQuest = new JList(PlayerQuestData.toArray());
    private JScrollPane scrollQuest, scrollPlayer, scrollPlayerQuest;
    
    public gamemode_quest(int w, DBConnector dbConnector){
        this.window_height = w; this.window_width = w;
        this.dbConnector = dbConnector;
        setSize(this.window_width, this.window_height);
        setTitle("gamemode quests");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setVisible(true);
        
        initButtons();
        init();
        getData();
    }
    public void getData(){
        try{
            PlayerData = dbConnector.getPlayers();
            QuestData = dbConnector.getQuests();
            
            listQuests.setListData(QuestData.toArray());
            listPlayers.setListData(PlayerData.toArray());
        }catch(SQLException ex){
        System.out.println("getData error");}
    }
    public void initButtons(){
        lInfo = new JLabel("");
        lInfo.setBounds(20, 400, 430, 20);
        lInfo.setOpaque(true);
        lInfo.setBackground(Color.WHITE);
        add(lInfo);
        
        lEXP = new JLabel("Experience:");
        lEXP.setBounds(300, 50, 200, 20);
        add(lEXP);
        lCreator = new JLabel("Client:");
        lCreator.setBounds(300, 70, 200, 20);
        add(lCreator);
        
        bTakeTheTask = new JButton("Take task");
        bTakeTheTask.setBounds(300, 100, 100, 30);
        bTakeTheTask.addActionListener(this);
        add(bTakeTheTask);
    
        bSubmitTask = new JButton("Submit task");
        bSubmitTask.setBounds(300, 240, 100, 30);
        bSubmitTask.addActionListener(this);
        add(bSubmitTask);
    }
    public void init(){
        lPlayerQuest = new JLabel("Taken quests");
        lPlayerQuest.setBounds(30, 205, 200, 20);
        add(lPlayerQuest);
        
        listPlayerQuest.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listPlayerQuest.setLayoutOrientation(VERTICAL);
        listPlayerQuest.setVisibleRowCount(1);
        add(listPlayerQuest);
        scrollPlayerQuest = new JScrollPane(listPlayerQuest);
        scrollPlayerQuest.setPreferredSize(new Dimension(250,100));
        scrollPlayerQuest.setBounds(30, 225, 250, 170);
        add(scrollPlayerQuest);
        
        lPlayers = new JLabel("All players");
        lPlayers.setBounds(30, 30, 100, 20);
        add(lPlayers);
        
        listPlayers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listPlayers.setLayoutOrientation(VERTICAL);
        listPlayers.setVisibleRowCount(1);
        listPlayers.addListSelectionListener((e)->{
            JList list =(JList) e.getSource();
            String selected = list.getSelectedValue().toString();
            int id = dbConnector.getId(selected);
            try{
                PlayerQuestData = dbConnector.getPlayerQuests(id);
            }catch(SQLException ex){}
            listPlayerQuest.setListData(PlayerQuestData.toArray());
        });
        add(listPlayers);
        scrollPlayer = new JScrollPane(listPlayers);
        scrollPlayer.setPreferredSize(new Dimension(250,100));
        scrollPlayer.setBounds(30, 55, 100, 150);
        add(scrollPlayer);
        
        lQuests = new JLabel("All quests");
        lQuests.setBounds(140, 30, 100, 20);
        add(lQuests);
        
        listQuests.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listQuests.setLayoutOrientation(VERTICAL);
        listQuests.setVisibleRowCount(1);
        listQuests.addListSelectionListener((e)-> {
            JList list = (JList) e.getSource();
            String selected = list.getSelectedValue().toString();
            try{
                float experience=0;
                int client=0;
                String done="";
                Statement stmt = dbConnector.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery("Select creator_id, experience_points from quests where q_name like '"+selected+"'");
                while(rs.next()){
                    client = rs.getInt("creator_id");
                    experience = rs.getFloat("experience_points");
                }
                rs.close();
                stmt.close();
                lEXP.setText("Experience: "+ Float.toString(experience));
                lCreator.setText("Client: "+ Integer.toString(client));
            }catch(SQLException ex){}
        });
        add(listQuests);
        scrollQuest = new JScrollPane(listQuests);
        scrollQuest.setPreferredSize(new Dimension(250,100));
        scrollQuest.setBounds(140, 55, 140, 150);
        add(scrollQuest);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == bTakeTheTask){
            String result="";
            if(listPlayers.getSelectedValue()==null || listQuests.getSelectedValue()==null){
                result = "missing data";
            }
            else{
                try{
                    int id = dbConnector.getId(listPlayers.getSelectedValue().toString());
                    String name = listQuests.getSelectedValue().toString();
                    result = dbConnector.TakeTheTask(id, name);
                    lInfo.setText(result);
                }catch(SQLException ex){
                    lInfo.setText("Something gone wrong with taking task");
                    lInfo.setForeground(Color.red);}
            }
            System.out.println(result);
        }
        if(source == bSubmitTask){
            String result="";
            if(listPlayers.getSelectedValue()==null || listQuests.getSelectedValue()==null){
                result = "missing data";
            }
            else{
                try{
                    int id = dbConnector.getId(listPlayers.getSelectedValue().toString());
                    String name = listQuests.getSelectedValue().toString();
                    result = dbConnector.SubmitTask(id, name);
                    lInfo.setText(result);
                }catch(SQLException ex){
                    lInfo.setText("Something gone wrong with submiting task");
                    lInfo.setForeground(Color.red);}
            }
            
            System.out.println(result);
        }
    }
    
}
