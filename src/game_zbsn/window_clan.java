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
    private JTextField InsertName, InsertHQ;
    private JList InsertCreator;
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
            InsertName.setText("");
            InsertHQ.setText("");
            InsertCreator.clearSelection();
            
            CreatorData = dbConnector.getPlayers();
            ClanData = dbConnector.getClans();
            
            ListOfNames.setListData(ClanData.toArray());
            InsertCreator.setListData(CreatorData.toArray());
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
        
        InsertName = new JTextField();
        InsertName.setBounds(90, 25, 100, 20);
        add(InsertName);
        InsertName.addActionListener(this);
        
        
        lHq = new JLabel("Headquater:");
        lHq.setBounds(20, 50, 100, 20);
        add(lHq);
        
        InsertHQ = new JTextField();
        InsertHQ.setBounds(90, 50, 100, 20);
        InsertHQ.addActionListener(this);
        add(InsertHQ);
        
        InsertCreator = new JList(CreatorData.toArray());
        InsertCreator.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        InsertCreator.setLayoutOrientation(VERTICAL);
        InsertCreator.setVisibleRowCount(-1);
        add(InsertCreator);
        
        lCreator = new JLabel("Creator:");
        lCreator.setBounds(20, 75, 100, 20);
        add(lCreator);
        JScrollPane scroll_creator = new JScrollPane(InsertCreator);
        scroll_creator.setPreferredSize(new Dimension(250, 100));
        scroll_creator.setBounds(90, 75, 100, 60);
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
        ListOfNames.addListSelectionListener((e) -> {
            JList list = (JList) e.getSource();
            if(list.getSelectedValue() != null) {
                String selected = list.getSelectedValue().toString();
                displayClan(selected);
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
        Object source = e.getSource();
        if(source == bInsert){
            String result;
            if(InsertName.getText().equals("")||InsertHQ.getText().equals("") ||InsertCreator.getSelectedValue()==null){
                System.out.println("Clan brak danych");
            }
            else{
                try{
                    int id = dbConnector.getId(InsertCreator.getSelectedValue().toString());
                   result = dbConnector.createClan(InsertName.getText(), id, InsertHQ.getText());
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
    
    private void displayClan(String name) {
        try {
            PreparedStatement statement = dbConnector.getConnection().prepareStatement(
                        "select c.clan_name as name, c.headquater as hq, m.member_id as id "
                                + "from clans c join membership m on c.clan_name = m.clan_name "
                                + "where c.clan_name = ? and m.founder = \'Y\'");
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            rs.next();
            
            String clanName = rs.getString("name");
            String hq = rs.getString("hq");
            int id = rs.getInt("id");
            
            InsertName.setText(clanName);
            InsertHQ.setText(hq);
            
            for(int i = 0; i < InsertCreator.getModel().getSize(); ++i) {
                String s = InsertCreator.getModel().getElementAt(i).toString();
                if(dbConnector.getId(s) == id) {
                    InsertCreator.setSelectedIndex(i);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(window_race.class.getName()).log(Level.SEVERE, "aaaaaaaa", ex);
        }
     }
     
}