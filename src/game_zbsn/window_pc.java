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
    private int window_height, window_width;
    private DBConnector dbConnector;
    //--------INSERTING--------------------------
    private JButton bInsert;
    private JTextField InsertName;
    private JList InsertRace, InsertProfession;
    private JLabel lName, lRace, lProfession, lInfo;
    private List<String> ProfessionData = new ArrayList<String>();
    private List<String> RaceData = new ArrayList<String>();
    //-------------------------------------------
    
    
    private JList ListOfNames;
    //--------DELETING---------------------------
    private JButton bDelete;
    private List<String> PlayerData = new ArrayList<String>();
    //-------------------------------------------
    
    //--------EDITING----------------------------
    private JButton bUpdate;
    //-------------------------------------------
    public window_pc(int w, DBConnector dbConnector){
        this.window_height = w; this.window_width = w;
        this.dbConnector = dbConnector;
        setSize(this.window_width, this.window_height);
        setTitle("Playable characters");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setVisible(true);
        
        
        insert_init();
        delete_init();
        get_data();
        }
    public void get_data(){
        try{
            InsertName.setText("");
            InsertRace.clearSelection();
            InsertProfession.clearSelection();
            
            ProfessionData = dbConnector.getProfessions();
            RaceData = dbConnector.getRaces();
            PlayerData = dbConnector.getPlayers();
            
            ListOfNames.setListData(PlayerData.toArray());
            InsertRace.setListData(RaceData.toArray());
            InsertProfession.setListData(ProfessionData.toArray());
        }catch(SQLException ex){
            Logger.getLogger(window_pc.class.getName()).log(Level.SEVERE,
                                                            "get_data() error",ex);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == bInsert){
            if(InsertName.getText().equals("")||InsertProfession.getSelectedValue()==null ||
                    InsertRace.getSelectedValue()==null){
                System.out.println("pc brak danych");
                lInfo.setText("Missing data");
            }
            else{
                try{
                   dbConnector.createPlayer(InsertName.getText(), InsertProfession.getSelectedValue().toString(), 
                           InsertRace.getSelectedValue().toString());
                   lInfo.setText("Player successfully created");
                }catch(SQLException ex){
                Logger.getLogger(window_pc.class.getName()).log(Level.SEVERE,
                                                            "Player insert error",ex);
                lInfo.setText("Something gone wrong with creating");}
            }
        }
        if(source == bDelete){
            if(ListOfNames.getSelectedValue() != null){
                String name = ListOfNames.getSelectedValue().toString();
                int id = dbConnector.getId(name);
                try{
                    dbConnector.leaveClan(id);
                    dbConnector.deletePlayer(id);
                    lInfo.setText("Player successfully deleted");

                }catch(SQLException ex){
                    Logger.getLogger(window_profession.class.getName()).log(Level.SEVERE,
                                                                "Delete profession error",ex);
                    lInfo.setText("Something gone wrong with deleting");
                    lInfo.setForeground(Color.red);}
            }
        }
        get_data();
    }
    public void delete_init(){
        lInfo = new JLabel("");
        lInfo.setBounds(30, 310, 320, 20);
        lInfo.setOpaque(true);
        lInfo.setBackground(Color.WHITE);
        add(lInfo);
        
        bDelete = new JButton("Delete");
        bDelete.setBounds(230, 200, 100, 30);
        add(bDelete);
        bDelete.addActionListener(this);
        
        bUpdate = new JButton("Edit");
        bUpdate.setBounds(230, 240, 100, 30);
        add(bUpdate);
        bUpdate.addActionListener(this);
        
        ListOfNames = new JList(PlayerData.toArray());
        ListOfNames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListOfNames.setLayoutOrientation(VERTICAL);
        ListOfNames.setVisibleRowCount(1);
        ListOfNames.addListSelectionListener((e) -> {
            JList list = (JList) e.getSource();
            if(list.getSelectedValue() != null) {
                String selected = list.getSelectedValue().toString();
                int id = dbConnector.getId(selected);
                displayPC(id);
            }
        });
        add(ListOfNames);
        
        JScrollPane scroll_pc= new JScrollPane(ListOfNames);
        scroll_pc.setPreferredSize(new Dimension(250, 100));
        scroll_pc.setBounds(60,200,150,70);
        add(scroll_pc);
        }
    public void insert_init(){
        bInsert = new JButton("Create");
        bInsert.setBounds(230, 85, 100, 30);
        add(bInsert);
        bInsert.addActionListener(this);
        
        lName = new JLabel("Name:");
        lName.setBounds(20, 25, 50, 20);
        add(lName);
        
        InsertName = new JTextField();
        InsertName.setBounds(90, 25, 100, 20);
        add(InsertName);
        InsertName.addActionListener(this);
        
        InsertRace = new JList(RaceData.toArray());
        InsertRace.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        InsertRace.setLayoutOrientation(VERTICAL);
        InsertRace.setVisibleRowCount(-1);
        add(InsertRace);
        
        lRace = new JLabel("Race:");      // add select name from races;
        lRace.setBounds(20, 50, 50, 20);
        add(lRace);
        JScrollPane scroll_race= new JScrollPane(InsertRace);
        scroll_race.setPreferredSize(new Dimension(250, 100));
        scroll_race.setBounds(90, 50, 100, 60);
        add(scroll_race);
        
        InsertProfession = new JList(ProfessionData.toArray());
        InsertProfession.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        InsertProfession.setLayoutOrientation(VERTICAL);
        InsertProfession.setVisibleRowCount(-1);
        add(InsertProfession);
        
        lProfession = new JLabel("Profession:"); // add select name from professions;
        lProfession.setBounds(20, 115, 100, 20);
        add(lProfession);
        JScrollPane scroll_profession= new JScrollPane(InsertProfession);
        scroll_profession.setPreferredSize(new Dimension(250, 100));
        scroll_profession.setBounds(90,115,100,60);
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

            InsertName.setText(playerName);

            for(int i = 0; i < InsertRace.getModel().getSize(); ++i) {
                String s = InsertRace.getModel().getElementAt(i).toString();
                if(s.equals(race)) {
                    InsertRace.setSelectedIndex(i);
                }
            }
            
            for(int i = 0; i < InsertProfession.getModel().getSize(); ++i) {
                String s = InsertProfession.getModel().getElementAt(i).toString();
                if(s.equals(profession)) {
                    InsertProfession.setSelectedIndex(i);
                }
            }
        } catch(SQLException ex) {
            Logger.getLogger(window_race.class.getName()).log(Level.SEVERE, "aaaaaaaa", ex);
        }
    }
    
}
