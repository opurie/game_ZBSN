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
    private int windowHeight, windowWidth;
    private DBConnector dbConnector;
    
    private List<String> playerData = new ArrayList<>();
    private List<String> questData = new ArrayList<>();
    private List<String> playerQuestData = new ArrayList<>();
    
    private JLabel playersLabel, questsLabel, playerQuestLabel, expLabel, creatorLabel, doneLabel, infoLabel;
    private JButton takeTaskButton, submitTaskButton;
    private JList questsList = new JList(questData.toArray()), playersList= new JList(playerData.toArray()), 
                                        playerQuestList = new JList(playerQuestData.toArray());
    private JScrollPane scrollQuest, scrollPlayer, scrollPlayerQuest;
    
    public gamemode_quest(int w, DBConnector dbConnector){
        this.windowHeight = w; this.windowWidth = w;
        this.dbConnector = dbConnector;
        setSize(this.windowWidth, this.windowHeight);
        setTitle("gamemode quests");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setVisible(true);
        setResizable(false);
        
        initButtons();
        init();
        getData();
    }
    public void getData(){
        try{
            playerData = dbConnector.getPlayers();
            questData = dbConnector.getQuests();
            
            questsList.setListData(questData.toArray());
            playersList.setListData(playerData.toArray());
        }catch(SQLException ex){
        System.out.println("getData error");}
    }
    public void initButtons(){
        infoLabel = new JLabel("");
        infoLabel.setBounds(20, 400, 430, 20);
        infoLabel.setOpaque(true);
        infoLabel.setBackground(Color.WHITE);
        add(infoLabel);
        
        expLabel = new JLabel("Experience:");
        expLabel.setBounds(300, 50, 200, 20);
        add(expLabel);
        creatorLabel = new JLabel("Client:");
        creatorLabel.setBounds(300, 70, 200, 20);
        add(creatorLabel);
        
        takeTaskButton = new JButton("Take task");
        takeTaskButton.setBounds(300, 100, 100, 30);
        takeTaskButton.addActionListener(this);
        add(takeTaskButton);
    
        submitTaskButton = new JButton("Submit task");
        submitTaskButton.setBounds(300, 240, 100, 30);
        submitTaskButton.addActionListener(this);
        add(submitTaskButton);
    }
    public void init(){
        playerQuestLabel = new JLabel("Taken quests");
        playerQuestLabel.setBounds(30, 205, 200, 20);
        add(playerQuestLabel);
        
        playerQuestList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        playerQuestList.setLayoutOrientation(VERTICAL);
        playerQuestList.setVisibleRowCount(1);
        add(playerQuestList);
        scrollPlayerQuest = new JScrollPane(playerQuestList);
        scrollPlayerQuest.setPreferredSize(new Dimension(250,100));
        scrollPlayerQuest.setBounds(30, 225, 250, 170);
        add(scrollPlayerQuest);
        
        playersLabel = new JLabel("All players");
        playersLabel.setBounds(30, 30, 100, 20);
        add(playersLabel);
        
        playersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        playersList.setLayoutOrientation(VERTICAL);
        playersList.setVisibleRowCount(1);
        playersList.addListSelectionListener((e)->{
            JList list =(JList) e.getSource();
            String selected = list.getSelectedValue().toString();
            int id = dbConnector.getId(selected);
            try{
                playerQuestData = dbConnector.getPlayerQuests(id);
            }catch(SQLException ex){}
            playerQuestList.setListData(playerQuestData.toArray());
        });
        add(playersList);
        scrollPlayer = new JScrollPane(playersList);
        scrollPlayer.setPreferredSize(new Dimension(250,100));
        scrollPlayer.setBounds(30, 55, 100, 150);
        add(scrollPlayer);
        
        questsLabel = new JLabel("All quests");
        questsLabel.setBounds(140, 30, 100, 20);
        add(questsLabel);
        
        questsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        questsList.setLayoutOrientation(VERTICAL);
        questsList.setVisibleRowCount(1);
        questsList.addListSelectionListener((e)-> {
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
                expLabel.setText("Experience: "+ Float.toString(experience));
                creatorLabel.setText("Client: "+ Integer.toString(client));
            }catch(SQLException ex){}
        });
        add(questsList);
        scrollQuest = new JScrollPane(questsList);
        scrollQuest.setPreferredSize(new Dimension(250,100));
        scrollQuest.setBounds(140, 55, 140, 150);
        add(scrollQuest);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == takeTaskButton){
            String result="";
            if(playersList.getSelectedValue()==null || questsList.getSelectedValue()==null){
                result = "missing data";
            }
            else{
                try{
                    int id = dbConnector.getId(playersList.getSelectedValue().toString());
                    String name = questsList.getSelectedValue().toString();
                    result = dbConnector.takeTask(id, name);
                    infoLabel.setText(result);
                }catch(SQLException ex){
                    infoLabel.setText("Something gone wrong with taking task");
                    infoLabel.setForeground(Color.red);}
            }
            System.out.println(result);
        }
        if(source == submitTaskButton){
            String result="";
            if(playersList.getSelectedValue()==null || playerQuestList.getSelectedValue()==null){
                result = "missing data";
            }
            else{
                try{
                    int id = dbConnector.getId(playersList.getSelectedValue().toString());
                    String name = playerQuestList.getSelectedValue().toString();
                    
                    result = dbConnector.submitTask(id, dbConnector.getQuest(name));
                    infoLabel.setText(result);
                    playerData = dbConnector.getPlayers();
                    playersList.setListData(playerData.toArray());
                }catch(SQLException ex){
                    infoLabel.setText("Something gone wrong with submiting task");
                    infoLabel.setForeground(Color.red);}
            }
            
            System.out.println(result);
        }
    }
    
}
