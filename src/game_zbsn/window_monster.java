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
    private int windowHeight, windowWidth;
    private DBConnector dbConnector;
    
    //--------INSERTING--------------------------
    private JButton insertButton;
    private JTextField nameField;
    private JList raceList, itemList;
    private JLabel nameLabel, raceLabel, itemLabel, infoLabel;
    private List<String> raceData = new ArrayList<>();
    private List<String> itemData = new ArrayList<>();
    //-------------------------------------------
    
    private JList nameList;
    //--------DELETING---------------------------
    private JButton deleteButton;
    private List<String> monsterData = new ArrayList<>();
    
    //-------------------------------------------
    
    //--------EDITING----------------------------
    private JButton updateButton;
    //-------------------------------------------
    public window_monster(int w, DBConnector dbConnector){
        this.windowHeight = w; this.windowWidth = w;
        this.dbConnector = dbConnector;
        setSize(this.windowWidth, this.windowHeight);
        setTitle("Monsters");
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
            itemList.clearSelection();
            
            raceData = dbConnector.getRaces();
            itemData = dbConnector.getItems();
            monsterData = dbConnector.getMonsters();
            System.out.println("lol");
            
            nameList.setListData(monsterData.toArray());
            raceList.setListData(raceData.toArray());
            itemList.setListData(itemData.toArray());
        }catch(SQLException ex){
            Logger.getLogger(window_race.class.getName()).log(Level.SEVERE,
                                                            "Get_race error",ex);
        }
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
        
        nameList = new JList(monsterData.toArray());
        nameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        nameList.setLayoutOrientation(VERTICAL);
        nameList.setVisibleRowCount(1);
        nameList.addListSelectionListener((e) -> {
            JList list = (JList) e.getSource();
            if(list.getSelectedValue() != null) {
                String selected = list.getSelectedValue().toString();
                displayMonster(dbConnector.getId(selected));
            }
            });
        add(nameList);
        
        JScrollPane scroll_pc= new JScrollPane(nameList);
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
        scroll_race.setBounds(90, 50, 130, 70);
        add(scroll_race);
        
        itemList = new JList(itemData.toArray());
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemList.setLayoutOrientation(VERTICAL);
        itemList.setVisibleRowCount(-1);
        add(itemList);
        
        itemLabel = new JLabel("Items:");
        itemLabel.setBounds(20, 125, 100, 20);
        add(itemLabel);
        JScrollPane scroll_item = new JScrollPane(itemList);
        scroll_item.setPreferredSize(new Dimension(250,100));
        scroll_item.setBounds(90, 125, 130, 70);
        add(scroll_item);
        
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == insertButton){
            if(nameField.getText().equals("")||raceList.getSelectedValue()==null||itemList.getSelectedValue()==null){
                System.out.println("pc brak danych");
                infoLabel.setText("Missing data");
            }
            else{
                try{
                   dbConnector.createMonster(nameField.getText(), itemList.getSelectedValue().toString(), raceList.getSelectedValue().toString());
                   infoLabel.setText("Monster successfully created");
                }catch(SQLException ex){
                Logger.getLogger(window_monster.class.getName()).log(Level.SEVERE,
                                                            "Monster insert error",ex);
                infoLabel.setText("Something gone wrong with creating");
                infoLabel.setForeground(Color.red);}
            }
        }
        if(source == deleteButton){
            if(nameList.getSelectedValue() != null){
                String name = nameList.getSelectedValue().toString();
                int id = dbConnector.getId(name);
                try{
                    dbConnector.deleteMonster(id);
                    infoLabel.setText("Monster deleted");
                }catch(SQLException ex){
                    Logger.getLogger(window_monster.class.getName()).log(Level.SEVERE,
                                                                "Delete monster error",ex);
                    infoLabel.setText("Something gone wrong with deleting");
                    infoLabel.setForeground(Color.red);}
            }
        }
        if(source ==  updateButton){
            if(nameList.getSelectedValue() != null){
                String query = "UPDATE monsters SET";
                int i = 0;
                if(!nameField.getText().equals("")){
                    query += " monster_name = \'" + nameField.getText()+"\'";
                    i++;
                }
                if(raceList.getSelectedValue() != null){
                    if(i>0)
                        query += ", ";
                    query += " monster_race = \'" + raceList.getSelectedValue().toString() + "\'";
                    i++;
                }
                if(itemList.getSelectedValue() != null){
                    if(i>0)
                        query += ", ";
                    query += " owned_item = \'" + itemList.getSelectedValue().toString() + "\'";
                    i++;
                }
                if(i>0){
                    query += " WHERE monster_id = "+ dbConnector.getId(nameList.getSelectedValue().toString());
                    try {
                        Statement stmt = dbConnector.getConnection().createStatement();
                        int changes;
                        changes = stmt.executeUpdate(query);
                        stmt.close();
                        infoLabel.setText("Monster successfully updated");
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
            
            nameField.setText(monsterName);
            
            for(int i = 0; i < raceList.getModel().getSize(); ++i) {
                String s = raceList.getModel().getElementAt(i).toString();
                if(s.equals(race)) {
                    raceList.setSelectedIndex(i);
                }
            }
            
            for(int i = 0; i < itemList.getModel().getSize(); ++i) {
                String s = itemList.getModel().getElementAt(i).toString();
                if(s.equals(item)) {
                    itemList.setSelectedIndex(i);
                }
            }
            
        } catch(SQLException ex) {
            Logger.getLogger(window_race.class.getName()).log(Level.SEVERE, "aaaaaaaa", ex);
        }
    }
    
}