/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game_zbsn;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import static javax.swing.JList.VERTICAL;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import oracle.jdbc.OracleTypes;

/**
 *
 * @author User
 */
public class window_pc extends JFrame implements ActionListener{
    private int windowHeight, windowWidth;
    private DBConnector dbConnector;
    //--------INSERTING--------------------------
    private JButton insertButton;
    private JTextField nameField;
    private JList raceList, professionList;
    private JLabel nameLabel, raceLabel, professionLabel, infoLabel;
    private List<String> professionData = new ArrayList<String>();
    private List<String> raceData = new ArrayList<String>();
    //-------------------------------------------
    
    
    private JList namesList;
    //--------DELETING---------------------------
    private JButton deleteButton;
    private List<String> playerData = new ArrayList<String>();
    //-------------------------------------------
    
    //--------EDITING----------------------------
    private JButton updateButton;
    //-------------------------------------------
    public window_pc(int w, DBConnector dbConnector){
        this.windowHeight = w; this.windowWidth = w;
        this.dbConnector = dbConnector;
        setSize(this.windowWidth, this.windowHeight);
        setTitle("Playable characters");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setVisible(true);
        setResizable(false);
        
        insert_init();
        delete_init();
        get_data();
        }
    public void get_data(){
        try{
            nameField.setText("");
            raceList.clearSelection();
            professionList.clearSelection();
            
            professionData = dbConnector.getProfessions();
            raceData = dbConnector.getRaces();
            playerData = dbConnector.getPlayers();
            
            namesList.setListData(playerData.toArray());
            raceList.setListData(raceData.toArray());
            professionList.setListData(professionData.toArray());
        }catch(SQLException ex){
            Logger.getLogger(window_pc.class.getName()).log(Level.SEVERE,
                                                            "get_data() error",ex);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == insertButton){
            if(nameField.getText().equals("")||professionList.getSelectedValue()==null ||
                    raceList.getSelectedValue()==null){
                System.out.println("pc brak danych");
                infoLabel.setText("Missing data");
            }
            else{
                try{
                   dbConnector.createPlayer(nameField.getText(), professionList.getSelectedValue().toString(), 
                           raceList.getSelectedValue().toString());
                   infoLabel.setText("Player successfully created");
                }catch(SQLException ex){
                Logger.getLogger(window_pc.class.getName()).log(Level.SEVERE,
                                                            "Player insert error",ex);
                infoLabel.setText("Something gone wrong with creating");}
            }
        }
        if(source == deleteButton){
            if(namesList.getSelectedValue() != null){
                String name = namesList.getSelectedValue().toString();
                int id = dbConnector.getId(name);
                try{
                    dbConnector.leaveClan(id);
                    dbConnector.deletePlayer(id);
                    infoLabel.setText("Player successfully deleted");

                }catch(SQLException ex){
                    Logger.getLogger(window_profession.class.getName()).log(Level.SEVERE,
                                                                "Delete profession error",ex);
                    infoLabel.setText("Something gone wrong with deleting");
                    infoLabel.setForeground(Color.red);}
            }
        }
        if(source ==  updateButton){
            if(namesList.getSelectedValue() != null){
                String query = "UPDATE players SET";
                int i = 0;
                if(!nameField.getText().equals("")){
                    query += " player_name = \'" + nameField.getText()+"\'";
                    i++;
                }
                if(raceList.getSelectedValue() != null){
                    if(i>0)
                        query += ", ";
                    query += " player_race = \'" + raceList.getSelectedValue().toString() + "\'";
                    i++;
                }
                if(professionList.getSelectedValue() != null){
                    if(i>0)
                        query += ", ";
                    query += " player_profession = \'" + professionList.getSelectedValue().toString() + "\'";
                    i++;
                }
                if(i>0){
                    query += " WHERE player_id = "+ dbConnector.getId(namesList.getSelectedValue().toString());
                    try {
                        System.out.println(query);
                        Statement stmt = dbConnector.getConnection().createStatement();
                        int changes;
                        changes = stmt.executeUpdate(query);
                        stmt.close();
                        infoLabel.setText("Player successfully updated");
                    } catch(SQLException ex){Logger.getLogger(window_race.class.getName()).log(Level.SEVERE,
                                                                "Update error",ex);
                                            infoLabel.setText("Something gone wrong with updating, change name");
                                            infoLabel.setForeground(Color.red);
                    }
                }
                
            }
        }
        get_data();
    }
    public void delete_init(){
        infoLabel = new JLabel("");
        infoLabel.setBounds(30, 310, 320, 20);
        infoLabel.setOpaque(true);
        infoLabel.setBackground(Color.WHITE);
        add(infoLabel);
        
        deleteButton = new JButton("Delete");
        deleteButton.setBounds(230, 200, 100, 30);
        add(deleteButton);
        deleteButton.addActionListener(this);
        
        updateButton = new JButton("Edit");
        updateButton.setBounds(230, 240, 100, 30);
        add(updateButton);
        updateButton.addActionListener(this);
        
        namesList = new JList(playerData.toArray());
        namesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        namesList.setLayoutOrientation(VERTICAL);
        namesList.setVisibleRowCount(1);
        namesList.addListSelectionListener((e) -> {
            JList list = (JList) e.getSource();
            if(list.getSelectedValue() != null) {
                String selected = list.getSelectedValue().toString();
                int id = dbConnector.getId(selected);
                displayPC(id);
            }
        });
        add(namesList);
        
        JScrollPane scroll_pc= new JScrollPane(namesList);
        scroll_pc.setPreferredSize(new Dimension(250, 100));
        scroll_pc.setBounds(60,200,150,70);
        add(scroll_pc);
        }
    public void insert_init(){
        insertButton = new JButton("Create");
        insertButton.setBounds(230, 85, 100, 30);
        add(insertButton);
        insertButton.addActionListener(this);
        
        nameLabel = new JLabel("Name:");
        nameLabel.setBounds(20, 25, 50, 20);
        add(nameLabel);
        
        nameField = new JTextField();
        nameField.setBounds(90, 25, 100, 20);
        add(nameField);
        nameField.addActionListener(this);
        
        raceList = new JList(raceData.toArray());
        raceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        raceList.setLayoutOrientation(VERTICAL);
        raceList.setVisibleRowCount(-1);
        add(raceList);
        
        raceLabel = new JLabel("Race:");      // add select name from races;
        raceLabel.setBounds(20, 50, 50, 20);
        add(raceLabel);
        JScrollPane scroll_race= new JScrollPane(raceList);
        scroll_race.setPreferredSize(new Dimension(250, 100));
        scroll_race.setBounds(90, 50, 130, 60);
        add(scroll_race);
        
        professionList = new JList(professionData.toArray());
        professionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        professionList.setLayoutOrientation(VERTICAL);
        professionList.setVisibleRowCount(-1);
        add(professionList);
        
        professionLabel = new JLabel("Profession:"); // add select name from professions;
        professionLabel.setBounds(20, 115, 100, 20);
        add(professionLabel);
        JScrollPane scroll_profession= new JScrollPane(professionList);
        scroll_profession.setPreferredSize(new Dimension(250, 100));
        scroll_profession.setBounds(90,115,130,60);
        add(scroll_profession);
    }
    
    private void displayPC(int id) {
        try {
            PreparedStatement statement = dbConnector.getConnection().prepareStatement(
                    "select * from players where player_id = ?");
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            
            String playerName = rs.getString("player_name");
            String race = rs.getString("player_race");
            String profession = rs.getString("player_profession");

            nameField.setText(playerName);

            for(int i = 0; i < raceList.getModel().getSize(); ++i) {
                String s = raceList.getModel().getElementAt(i).toString();
                if(s.equals(race)) {
                    raceList.setSelectedIndex(i);
                }
            }
            
            for(int i = 0; i < professionList.getModel().getSize(); ++i) {
                String s = professionList.getModel().getElementAt(i).toString();
                if(s.equals(profession)) {
                    professionList.setSelectedIndex(i);
                }
            }
        } catch(SQLException ex) {
            Logger.getLogger(window_race.class.getName()).log(Level.SEVERE, "aaaaaaaa", ex);
        }
    }
    
}
