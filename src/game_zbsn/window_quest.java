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
public class window_quest extends JFrame implements ActionListener{
    private int windowHeight, windowWidth;
    private DBConnector dbConnector;
    
    //--------INSERTING--------------------------
    private JButton insertButton;
    private JTextField nameField, expField;
    private JList creatorList;
    private JLabel nameLabel, expLabel, creatorLabel, infoLabel;
    private List<String> creatorData = new ArrayList<>();
    //-------------------------------------------
    
    private JList namesList;
    //--------DELETING---------------------------
    private JButton deleteButton;
    
    private List<String> questData = new ArrayList<>();
    //-------------------------------------------
    
    //--------EDITING----------------------------
    private JButton bUpdate;
    //-------------------------------------------
    public window_quest(int w, DBConnector dbConnector){
        this.windowHeight = w; this.windowWidth = w;
        this.dbConnector = dbConnector;
        setSize(this.windowWidth, this.windowHeight);
        setTitle("Quests");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        setVisible(true);


        insert_init();
        delete_init();
        get_data();
    }
    public void get_data(){
        try{
            nameField.setText("");
            expField.setText("");
            
            questData = dbConnector.getQuests();
            creatorData = dbConnector.getPlayers();
            
            creatorList.setListData(creatorData.toArray());
            namesList.setListData(questData.toArray());
        }catch(SQLException ex){
            Logger.getLogger(window_quest.class.getName()).log(Level.SEVERE,
                                                            "get_data() error",ex);
        }
    }
    public void insert_init(){
        infoLabel = new JLabel("");
        infoLabel.setBounds(30, 310, 320, 20);
        infoLabel.setOpaque(true);
        infoLabel.setBackground(Color.WHITE);
        add(infoLabel);
        
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
        
        expLabel = new JLabel("Exp:");
        expLabel.setBounds(20, 50, 50, 20);
        add(expLabel);
        
        expField = new JTextField();
        expField.setBounds(90, 50, 100, 20);
        add(expField);
        expField.addActionListener(this);
        
        creatorList = new JList(creatorData.toArray());
        creatorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        creatorList.setLayoutOrientation(VERTICAL);
        creatorList.setVisibleRowCount(-1);
        add(creatorList);
        
        creatorLabel = new JLabel("Creator:");
        creatorLabel.setBounds(20, 85, 100, 20);
        add(creatorLabel);
        JScrollPane scroll_creator = new JScrollPane(creatorList);
        scroll_creator.setPreferredSize(new Dimension(250, 100));
        scroll_creator.setBounds(90, 85, 130, 100);
        add(scroll_creator);
    }
    public void delete_init(){
        deleteButton = new JButton("Delete");
        deleteButton.setBounds(230, 200, 100, 30);
        add(deleteButton);
        deleteButton.addActionListener(this);
        
        bUpdate = new JButton("Edit");
        bUpdate.setBounds(230, 240, 100, 30);
        add(bUpdate);
        bUpdate.addActionListener(this);
        
        namesList = new JList(questData.toArray());
        namesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        namesList.setLayoutOrientation(VERTICAL);
        namesList.setVisibleRowCount(1);
        namesList.addListSelectionListener((e) -> {
            JList list = (JList) e.getSource();
            if(list.getSelectedValue() != null) {
                String selected = list.getSelectedValue().toString();
                displayQuest(selected);
            }
        });
        add(namesList);
        
        JScrollPane scroll_pc= new JScrollPane(namesList);
        scroll_pc.setPreferredSize(new Dimension(250, 100));
        scroll_pc.setBounds(60,200,150,70);
        add(scroll_pc);
        }
    @Override

    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        if(button == insertButton) {
            if(!anyFieldEmpty()) {
                String name = nameField.getText();
                try {
                    
                    float exp = Float.parseFloat(expField.getText());
                    int creator = dbConnector.getId(creatorList.getSelectedValue().toString());
                    dbConnector.createQuest(name, exp, creator);
                    infoLabel.setText("Quest successfully created");
                } catch(SQLException ex) {
                    Logger.getLogger(window_race.class.getName()).log(Level.SEVERE,
                                                                "Create quest error",ex);
                    infoLabel.setText(name+" is used, insert unique name");
                    infoLabel.setForeground(Color.red);
                }
            } else {
                infoLabel.setText("Missing data");
                System.out.println("field empty");
            }
        } else if(button == deleteButton) {
            String name = nameField.getText();
            if(namesList.getSelectedValue()!=null) {
                try {
                    dbConnector.deleteQuest(namesList.getSelectedValue().toString());
                    infoLabel.setText("Quest successfully deleted");
                } catch(SQLException ex) {
                    Logger.getLogger(window_race.class.getName()).log(Level.SEVERE,
                                                                "Delete quest error",ex);
                    infoLabel.setText("Something gone wrong with deleting");
                }
            } else {
                System.out.println("field empty");
            }
        }
        if(button ==  bUpdate){
            if(namesList.getSelectedValue() != null){
                String query = "UPDATE quests SET";
                int i = 0;
                if(!expField.getText().equals("")){
                    if(i>0)
                        query += ", ";
                    query += " experience_points = " + expField.getText();
                    i++;
                }
                if(i>0){
                    query += " WHERE q_name LIKE '"+ namesList.getSelectedValue().toString()+"'";
                    try{
                        Statement stmt = dbConnector.getConnection().createStatement();
                        int changes;
                        changes = stmt.executeUpdate(query);
                        stmt.close();
                        infoLabel.setText("Quest successfully updated");
                    }catch(SQLException ex){Logger.getLogger(window_race.class.getName()).log(Level.SEVERE,
                                                                "Update error",ex);
                                            infoLabel.setText("Something gone wrong with updating, change name");
                                            infoLabel.setForeground(Color.red);
                    }
                }
                
            }
        }
        get_data();
    }
    
    private boolean anyFieldEmpty() {
        return nameField.getText().equals("") || expField.getText().equals("") ||
                creatorList.getSelectedValue() == null;
    }
    
    private void displayQuest(String name) {
        try {
            PreparedStatement statement = dbConnector.getConnection().prepareStatement(
                    "select q.creator_id as id, q.experience_points as exp, "
                            + "q.q_name as q_name "
                            + "from quests q join players p on q.creator_id = p.player_id "
                            + "where q.q_name = ?");
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            rs.next();
            
            float exp = rs.getFloat("exp");
            int id = rs.getInt("id");
            System.out.println(id);
            String questName = rs.getString("q_name");
            
            nameField.setText(questName);
            expField.setText(exp + "");
            
            for(int i = 0; i < creatorList.getModel().getSize(); ++i) {
                String s = creatorList.getModel().getElementAt(i).toString();
                if(dbConnector.getId(s) == id) {
                    creatorList.setSelectedIndex(i);
                }
            }
            
        } catch(SQLException ex) {
            Logger.getLogger(window_race.class.getName()).log(Level.SEVERE, "aaaaaaaa", ex);
        }
    }
}