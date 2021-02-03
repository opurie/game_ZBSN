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
public class window_quest extends JFrame implements ActionListener{
    private int window_height, window_width;
    private DBConnector dbConnector;
    
    //--------INSERTING--------------------------
    private JButton bInsert;
    private JTextField insert_name, insert_exp;
    private JList InsertCreator;
    private JLabel lName, lExp, lCreator;
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
        setVisible(true);

        get_data();
        insert_init();
        delete_init();
    }
    public void get_data(){
        try{
            QuestData = dbConnector.getQuests();
            CreatorData = dbConnector.getPlayers();
        }catch(SQLException ex){
            Logger.getLogger(window_quest.class.getName()).log(Level.SEVERE,
                                                            "get_data() error",ex);
        }
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
        
        lExp = new JLabel("Exp:");
        lExp.setBounds(20, 50, 50, 20);
        add(lExp);
        
        insert_exp = new JTextField();
        insert_exp.setBounds(90, 50, 100, 20);
        add(insert_exp);
        insert_exp.addActionListener(this);
        
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
        scroll_creator.setBounds(90, 85, 100, 50);
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
                try {
                    String name = insert_name.getText();
                    int exp = Integer.parseInt(insert_exp.getText());
                    int creator = dbConnector.getId(InsertCreator.getSelectedValue().toString());
                    dbConnector.createQuest(name, exp, creator);
                } catch(SQLException ex) {
                    Logger.getLogger(window_race.class.getName()).log(Level.SEVERE,
                                                                "Create quest error",ex);
                }
            } else {
                System.out.println("field empty");
            }
        } else if(button == bDelete) {
            String name = insert_name.getText();
            if(ListOfNames.getSelectedValue()!=null) {
                try {
                    dbConnector.deleteQuest(ListOfNames.getSelectedValue().toString());
                } catch(SQLException ex) {
                    Logger.getLogger(window_race.class.getName()).log(Level.SEVERE,
                                                                "Delete quest error",ex);
                }
            } else {
                System.out.println("field empty");
            }
        }
    }
    
    private boolean anyFieldEmpty() {
        return insert_name.getText().equals("") || insert_exp.getText().equals("") ||
                InsertCreator.getSelectedValue() == null;
    }
    
}