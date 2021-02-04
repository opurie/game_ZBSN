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
public class window_clan extends JFrame implements ActionListener{
    private int window_height, window_width;
    private DBConnector dbConnector;
    
    //--------INSERTING--------------------------
    private JButton bInsert;
    private JTextField insert_name, insert_hq;
    private JList insert_creator;
    private JLabel lName, lCreator, lHq;
    private List<String> CreatorData = new ArrayList<>();
    //-------------------------------------------
    
    private JList ListOfNames;
    //--------DELETING---------------------------
    private JButton bDelete;
    private List<String> ClanData = new ArrayList<>();
    //-------------------------------------------
    
    //--------EDITING----------------------------
    private JButton bUpdate;
    //-------------------------------------------
    public window_clan(int w, DBConnector dbConnector){
        this.window_height = w; this.window_width = w;
        this.dbConnector = dbConnector;
        setSize(this.window_width, this.window_height);
        setTitle("Clans");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setVisible(true);
        
        insert_init();
        delete_init();
        get_data();
    }
    public void get_data(){
        try {
            CreatorData = dbConnector.getPlayers();
            ClanData = dbConnector.getClans();
            
            ListOfNames.setListData(ClanData.toArray());
            insert_creator.setListData(CreatorData.toArray());
        } catch(SQLException ex) {
            Logger.getLogger(window_clan.class.getName()).log(Level.SEVERE,
                                                            "Clan get data error",ex);
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
        
        
        lHq = new JLabel("Headquater:");
        lHq.setBounds(20, 50, 100, 20);
        add(lHq);
        
        insert_hq = new JTextField();
        insert_hq.setBounds(90, 50, 100, 20);
        insert_hq.addActionListener(this);
        add(insert_hq);
        
        insert_creator = new JList(CreatorData.toArray());
        insert_creator.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        insert_creator.setLayoutOrientation(VERTICAL);
        insert_creator.setVisibleRowCount(-1);
        add(insert_creator);
        
        lCreator = new JLabel("Creator:");
        lCreator.setBounds(20, 75, 100, 20);
        add(lCreator);
        JScrollPane scroll_creator = new JScrollPane(insert_creator);
        scroll_creator.setPreferredSize(new Dimension(250, 100));
        scroll_creator.setBounds(90, 75, 100, 50);
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
        
        ListOfNames = new JList(ClanData.toArray());
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
        Object source = e.getSource();
        if(source == bInsert){
            String result;
            if(insert_name.getText().equals("")||insert_hq.getText().equals("") ||insert_creator.getSelectedValue()==null){
                System.out.println("Clan brak danych");
            }
            else{
                try{
                    int id = dbConnector.getId(insert_creator.getSelectedValue().toString());
                   result = dbConnector.createClan(insert_name.getText(), id, insert_hq.getText());
                   System.out.println(result);
                }catch(SQLException ex){
                Logger.getLogger(window_clan.class.getName()).log(Level.SEVERE,
                                                            "Clan insert error",ex);}
            }
        }
        if(source == bDelete){
            if(ListOfNames.getSelectedValue() != null){
                String name = ListOfNames.getSelectedValue().toString();
                try{
                    dbConnector.deleteClan(name);
                }catch(SQLException ex){
                    Logger.getLogger(window_clan.class.getName()).log(Level.SEVERE,
                                                                "Delete clan error",ex);}
            }
        }
        get_data();
    }
    
}