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
    private int window_height, window_width;
    private DBConnector dbConnector;
    
    //--------INSERTING--------------------------
    private JButton bInsert;
    private JTextField InsertName, InsertExp;
    private JList InsertCreator;
    private JLabel lName, lExp, lCreator, lInfo;
    private List<String> CreatorData = new ArrayList<>();
    //-------------------------------------------
    
    private JList ListOfNames;
    //--------DELETING---------------------------
    private JButton bDelete;
    
    private List<String> QuestData = new ArrayList<>();
    //-------------------------------------------
    
    //--------EDITING----------------------------
    private JButton bUpdate;
    //-------------------------------------------
    public window_quest(int w, DBConnector dbConnector){
        this.window_height = w; this.window_width = w;
        this.dbConnector = dbConnector;
        setSize(this.window_width, this.window_height);
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
            InsertName.setText("");
            InsertExp.setText("");
            
            QuestData = dbConnector.getQuests();
            CreatorData = dbConnector.getPlayers();
            
            InsertCreator.setListData(CreatorData.toArray());
            ListOfNames.setListData(QuestData.toArray());
        }catch(SQLException ex){
            Logger.getLogger(window_quest.class.getName()).log(Level.SEVERE,
                                                            "get_data() error",ex);
        }
    }
    public void insert_init(){
        lInfo = new JLabel("");
        lInfo.setBounds(30, 310, 320, 20);
        lInfo.setOpaque(true);
        lInfo.setBackground(Color.WHITE);
        add(lInfo);
        
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
        
        lExp = new JLabel("Exp:");
        lExp.setBounds(20, 50, 50, 20);
        add(lExp);
        
        InsertExp = new JTextField();
        InsertExp.setBounds(90, 50, 100, 20);
        add(InsertExp);
        InsertExp.addActionListener(this);
        
        InsertCreator = new JList(CreatorData.toArray());
        InsertCreator.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        InsertCreator.setLayoutOrientation(VERTICAL);
        InsertCreator.setVisibleRowCount(-1);
        add(InsertCreator);
        
        lCreator = new JLabel("Creator:");
        lCreator.setBounds(20, 85, 100, 20);
        add(lCreator);
        JScrollPane scroll_creator = new JScrollPane(InsertCreator);
        scroll_creator.setPreferredSize(new Dimension(250, 100));
        scroll_creator.setBounds(90, 85, 130, 100);
        add(scroll_creator);
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
        
        ListOfNames = new JList(QuestData.toArray());
        ListOfNames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListOfNames.setLayoutOrientation(VERTICAL);
        ListOfNames.setVisibleRowCount(1);
        ListOfNames.addListSelectionListener((e) -> {
            JList list = (JList) e.getSource();
            if(list.getSelectedValue() != null) {
                String selected = list.getSelectedValue().toString();
                displayQuest(selected);
            }
        });
        add(ListOfNames);
        
        JScrollPane scroll_pc= new JScrollPane(ListOfNames);
        scroll_pc.setPreferredSize(new Dimension(250, 100));
        scroll_pc.setBounds(60,200,150,70);
        add(scroll_pc);
        }
    @Override

    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        if(button == bInsert) {
            if(!anyFieldEmpty()) {
                String name = InsertName.getText();
                try {
                    
                    float exp = Float.parseFloat(InsertExp.getText());
                    int creator = dbConnector.getId(InsertCreator.getSelectedValue().toString());
                    dbConnector.createQuest(name, exp, creator);
                    lInfo.setText("Quest successfully created");
                } catch(SQLException ex) {
                    Logger.getLogger(window_race.class.getName()).log(Level.SEVERE,
                                                                "Create quest error",ex);
                    lInfo.setText(name+" is used, insert unique name");
                    lInfo.setForeground(Color.red);
                }
            } else {
                lInfo.setText("Missing data");
                System.out.println("field empty");
            }
        } else if(button == bDelete) {
            String name = InsertName.getText();
            if(ListOfNames.getSelectedValue()!=null) {
                try {
                    dbConnector.deleteQuest(ListOfNames.getSelectedValue().toString());
                    lInfo.setText("Quest successfully deleted");
                } catch(SQLException ex) {
                    Logger.getLogger(window_race.class.getName()).log(Level.SEVERE,
                                                                "Delete quest error",ex);
                    lInfo.setText("Something gone wrong with deleting");
                }
            } else {
                System.out.println("field empty");
            }
        }
        if(button ==  bUpdate){
            if(ListOfNames.getSelectedValue() != null){
                String query = "UPDATE quests SET";
                int i = 0;
                if(!InsertName.getText().equals("")){
                    query += " q_name = '" + InsertName.getText()+"'";
                    i++;
                }
                if(!InsertExp.getText().equals("")){
                    if(i>0)
                        query += ", ";
                    query += " experience_points = " + InsertExp.getText();
                    i++;
                }
                if(InsertCreator.getSelectedValue() != null){
                    if(i>0)
                        query += ", ";
                    query += " creator_id = " + dbConnector.getId(InsertCreator.getSelectedValue().toString());
                    i++;
                }
                if(i>0){
                    query += " WHERE q_name LIKE '"+ ListOfNames.getSelectedValue().toString()+"'";
                    try{
                        Statement stmt = dbConnector.getConnection().createStatement();
                        int changes;
                        changes = stmt.executeUpdate(query);
                        stmt.close();
                        lInfo.setText("Quest successfully updated");
                    }catch(SQLException ex){Logger.getLogger(window_race.class.getName()).log(Level.SEVERE,
                                                                "Update error",ex);
                                            lInfo.setText("Something gone wrong with updating, change name");
                                            lInfo.setForeground(Color.red);
                    }
                }
                
            }
        }
        get_data();
    }
    
    private boolean anyFieldEmpty() {
        return InsertName.getText().equals("") || InsertExp.getText().equals("") ||
                InsertCreator.getSelectedValue() == null;
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
            
            InsertName.setText(questName);
            InsertExp.setText(exp + "");
            
            for(int i = 0; i < InsertCreator.getModel().getSize(); ++i) {
                String s = InsertCreator.getModel().getElementAt(i).toString();
                if(dbConnector.getId(s) == id) {
                    InsertCreator.setSelectedIndex(i);
                }
            }
            
        } catch(SQLException ex) {
            Logger.getLogger(window_race.class.getName()).log(Level.SEVERE, "aaaaaaaa", ex);
        }
    }
}