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
    
    //--------INSERTING--------------------------
    private JButton bInsert;
    private JTextField insert_name;
    private JList insert_race, insert_profession;
    private JLabel lName, lRace, lProfession;
    private List<String> ProfessionData = new ArrayList<String>();
    private List<String> RaceData = new ArrayList<String>();
    private static String[] test_race={"dziekan","piwo","student3.0"};
    private static String[] test_prof={"inf","air","mcdonald"};
    //-------------------------------------------
    
    
    private JList list_of_pc;
    //--------DELETING---------------------------
    private JButton bDelete;
    private List<String> PlayerData = new ArrayList<String>();
    private static String[] delete_string={"character1","character2","character3","character4", "character1"};
    //-------------------------------------------
    
    //--------EDITING----------------------------
    private JButton bUpdate;
    //-------------------------------------------
    
    private Connection con;
    public window_pc(int w, Connection con){
        this.window_height = w; this.window_width = w;
        this.con = con;
        setSize(this.window_width, this.window_height);
        setTitle("Playable characters");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setVisible(true);
        
        get_data();
        insert_init();
        delete_init();
        }
    public void get_data(){
        try{
            CallableStatement stmt = this.con.prepareCall("{? = call get_races}");
            stmt.registerOutParameter(1, OracleTypes.REF_CURSOR);
            stmt.execute();
            ResultSet result = (ResultSet)stmt.getObject(1);
            while(result.next()){
                RaceData.add(result.getString(1));
            }
            result.close();
            stmt.close();
        }catch(SQLException ex){
            Logger.getLogger(window_race.class.getName()).log(Level.SEVERE,
                                                            "Player get_races error",ex);
        }
        try{
            CallableStatement stmt = this.con.prepareCall("{? = call get_professions}");
            stmt.registerOutParameter(1, OracleTypes.REF_CURSOR);
            stmt.execute();
            ResultSet result = (ResultSet)stmt.getObject(1);
            while(result.next()){
                ProfessionData.add(result.getString(1));
            }
            result.close();
            stmt.close();
        }catch(SQLException ex){
            Logger.getLogger(window_item.class.getName()).log(Level.SEVERE,
                                                            "Player get_professions error",ex);
        }
        try{
            CallableStatement stmt = this.con.prepareCall("{? = call get_players}");
            stmt.registerOutParameter(1, OracleTypes.REF_CURSOR);
            stmt.execute();
            ResultSet result = (ResultSet)stmt.getObject(1);
            while(result.next()){
                PlayerData.add(result.getString(1));
            }
            result.close();
            stmt.close();
            
        }catch(SQLException ex){
            Logger.getLogger(window_pc.class.getName()).log(Level.SEVERE,
                                                            "get_player error",ex);
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
                    CallableStatement stmt = this.con.prepareCall("{call create_player(?, ?, ?)}");
                    stmt.setString(1, insert_name.getText());
                    stmt.setString(2, insert_profession.getSelectedValue().toString());
                    stmt.setString(3, insert_race.getSelectedValue().toString());
                    stmt.execute();
                    stmt.close();
                }catch(SQLException ex){
                Logger.getLogger(window_pc.class.getName()).log(Level.SEVERE,
                                                            "Player insert error",ex);}
            }
        
        }
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
        
        list_of_pc = new JList(PlayerData.toArray());
        list_of_pc.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list_of_pc.setLayoutOrientation(VERTICAL);
        list_of_pc.setVisibleRowCount(1);
        add(list_of_pc);
        
        JScrollPane scroll_pc= new JScrollPane(list_of_pc);
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
