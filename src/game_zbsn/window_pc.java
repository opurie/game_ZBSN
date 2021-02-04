/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game_zbsn;

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
    private JTextField insert_name;
    private JList insert_race, insert_profession;
    private JLabel lName, lRace, lProfession;
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
            ProfessionData = dbConnector.getProfessions();
            RaceData = dbConnector.getRaces();
            PlayerData = dbConnector.getPlayers();
            
            ListOfNames.setListData(PlayerData.toArray());
            insert_race.setListData(RaceData.toArray());
            insert_profession.setListData(ProfessionData.toArray());
        }catch(SQLException ex){
            Logger.getLogger(window_pc.class.getName()).log(Level.SEVERE,
                                                            "get_data() error",ex);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == bInsert){
            if(insert_name.getText().equals("")||insert_profession.getSelectedValue()==null ||
                    insert_race.getSelectedValue()==null){
                System.out.println("pc brak danych");
            }
            else{
                try{
                   dbConnector.createPlayer(insert_name.getText(), insert_profession.getSelectedValue().toString(), 
                           insert_race.getSelectedValue().toString());
                }catch(SQLException ex){
                Logger.getLogger(window_pc.class.getName()).log(Level.SEVERE,
                                                            "Player insert error",ex);}
            }
        }
        if(source == bDelete){
            if(ListOfNames.getSelectedValue() != null){
                String name = ListOfNames.getSelectedValue().toString();
                int id = dbConnector.getId(name);
                System.out.println("Usunieto pc "+id);
                try{
                    dbConnector.leaveClan(id);
                    dbConnector.deletePlayer(id);

                }catch(SQLException ex){
                    Logger.getLogger(window_profession.class.getName()).log(Level.SEVERE,
                                                                "Delete profession error",ex);}
            }
        }
        get_data();
    }
    public void delete_init(){
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
        
        insert_name = new JTextField();
        insert_name.setBounds(90, 25, 100, 20);
        add(insert_name);
        insert_name.addActionListener(this);
        
        insert_race = new JList(RaceData.toArray());
        insert_race.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        insert_race.setLayoutOrientation(VERTICAL);
        insert_race.setVisibleRowCount(-1);
        add(insert_race);
        
        lRace = new JLabel("Race:");      // add select name from races;
        lRace.setBounds(20, 50, 50, 20);
        add(lRace);
        JScrollPane scroll_race= new JScrollPane(insert_race);
        scroll_race.setPreferredSize(new Dimension(250, 100));
        scroll_race.setBounds(90, 50, 100, 30);
        add(scroll_race);
        
        insert_profession = new JList(ProfessionData.toArray());
        insert_profession.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        insert_profession.setLayoutOrientation(VERTICAL);
        insert_profession.setVisibleRowCount(-1);
        add(insert_profession);
        
        lProfession = new JLabel("Profession:"); // add select name from professions;
        lProfession.setBounds(20, 85, 100, 20);
        add(lProfession);
        JScrollPane scroll_profession= new JScrollPane(insert_profession);
        scroll_profession.setPreferredSize(new Dimension(250, 100));
        scroll_profession.setBounds(90,85,100,30);
        add(scroll_profession);
    }
}
