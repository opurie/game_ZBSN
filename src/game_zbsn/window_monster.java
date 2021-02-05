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
import javax.swing.JLabel;
import javax.swing.JList;
import static javax.swing.JList.VERTICAL;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

/**
 *
 * @author User
 */
public class window_monster extends JFrame implements ActionListener{
    private int window_height, window_width;
    private DBConnector dbConnector;
    
    //--------INSERTING--------------------------
    private JButton bInsert;
    private JTextField InsertName;
    private JList InsertRace, InsertItem;
    private JLabel lName, lRace, lItem;
    private List<String> RaceData = new ArrayList<>();
    private List<String> ItemData = new ArrayList<>();
    //-------------------------------------------
    
    private JList ListOfNames;
    //--------DELETING---------------------------
    private JButton bDelete;
    private List<String> MonsterData = new ArrayList<>();
    
    //-------------------------------------------
    
    //--------EDITING----------------------------
    private JButton bUpdate;
    //-------------------------------------------
    public window_monster(int w, DBConnector dbConnector){
        this.window_height = w; this.window_width = w;
        this.dbConnector = dbConnector;
        setSize(this.window_width, this.window_height);
        setTitle("Monsters");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setVisible(true);

        insert_init();
        delete_init();
        get_data();
    }
    public void get_data(){
        try{
            RaceData = dbConnector.getRaces();
            ItemData = dbConnector.getItems();
            MonsterData = dbConnector.getMonsters();
            
            ListOfNames.setListData(MonsterData.toArray());
            InsertRace.setListData(RaceData.toArray());
            InsertItem.setListData(ItemData.toArray());
        }catch(SQLException ex){
            Logger.getLogger(window_race.class.getName()).log(Level.SEVERE,
                                                            "Get_race error",ex);
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
        
        ListOfNames = new JList(MonsterData.toArray());
        ListOfNames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListOfNames.setLayoutOrientation(VERTICAL);
        ListOfNames.setVisibleRowCount(1);
        ListOfNames.addListSelectionListener((e) -> {
            JList list = (JList) e.getSource();
            if(list.getSelectedValue() != null) {
                String selected = list.getSelectedValue().toString();
                displayMonster(dbConnector.getId(selected));
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
        
        InsertItem = new JList(ItemData.toArray());
        InsertItem.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        InsertItem.setLayoutOrientation(VERTICAL);
        InsertItem.setVisibleRowCount(-1);
        add(InsertItem);
        
        lItem = new JLabel("Items:");
        lItem.setBounds(20, 115, 100, 20);
        add(lItem);
        JScrollPane scroll_item = new JScrollPane(InsertItem);
        scroll_item.setPreferredSize(new Dimension(250,100));
        scroll_item.setBounds(90, 115, 100, 60);
        add(scroll_item);
        
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == bInsert){
            if(InsertName.getText().equals("")||InsertRace.getSelectedValue()==null||InsertItem.getSelectedValue()==null){
                System.out.println("pc brak danych");
            }
            else{
                try{
                   dbConnector.createMonster(InsertName.getText(), InsertItem.getSelectedValue().toString(), InsertRace.getSelectedValue().toString());
                }catch(SQLException ex){
                Logger.getLogger(window_monster.class.getName()).log(Level.SEVERE,
                                                            "Monster insert error",ex);}
            }
        }
        if(source == bDelete){
            if(ListOfNames.getSelectedValue() != null){
                String name = ListOfNames.getSelectedValue().toString();
                int id = dbConnector.getId(name);
                System.out.println("Monster deleted "+id);
                try{
                    dbConnector.deleteMonster(id);
                }catch(SQLException ex){
                    Logger.getLogger(window_monster.class.getName()).log(Level.SEVERE,
                                                                "Delete monster error",ex);}
            }
        }
        get_data();
    }
    
    private void displayMonster(int id) {
        try {
            PreparedStatement statement = dbConnector.getConnection().prepareStatement(
                    "select * from monsters where monster_id = ?");
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            
            String monsterName = rs.getString("monster_name");
            String race = rs.getString("monster_race");
            String item = rs.getString("owned_item");
            
            InsertName.setText(monsterName);
            
            for(int i = 0; i < InsertRace.getModel().getSize(); ++i) {
                String s = InsertRace.getModel().getElementAt(i).toString();
                if(s.equals(race)) {
                    InsertRace.setSelectedIndex(i);
                }
            }
            
            for(int i = 0; i < InsertItem.getModel().getSize(); ++i) {
                String s = InsertItem.getModel().getElementAt(i).toString();
                if(s.equals(item)) {
                    InsertItem.setSelectedIndex(i);
                }
            }
            
        } catch(SQLException ex) {
            Logger.getLogger(window_race.class.getName()).log(Level.SEVERE, "aaaaaaaa", ex);
        }
    }
    
}