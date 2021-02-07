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
public class window_clan extends JFrame implements ActionListener{
    private int windowHeight, windowWidth;
    private DBConnector dbConnector;
    
    //--------INSERTING--------------------------
    private JButton insertButton;
    private JTextField nameField, HQField;
    private JList creatorList;
    private JLabel nameLabel, creatorLabel, HQLabel, infoLabel;
    private List<String> creatorData = new ArrayList<>();
    //-------------------------------------------
    
    private JList namesList;
    //--------DELETING---------------------------
    private JButton deleteButton;
    private List<String> clanData = new ArrayList<>();
    //-------------------------------------------
    
    //--------EDITING----------------------------
    private JButton updateButton;
    //-------------------------------------------
    public window_clan(int w, DBConnector dbConnector){
        this.windowHeight = w; this.windowWidth = w;
        this.dbConnector = dbConnector;
        setSize(this.windowWidth, this.windowHeight);
        setTitle("Clans");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setVisible(true);
        setResizable(false);
        
        insert_init();
        delete_init();
        get_data();
    }
    public void get_data(){
        try {
            nameField.setText("");
            HQField.setText("");
            creatorList.clearSelection();
            
            creatorData = dbConnector.getPlayers();
            clanData = dbConnector.getClans();
            
            namesList.setListData(clanData.toArray());
            creatorList.setListData(creatorData.toArray());
        } catch(SQLException ex) {
            Logger.getLogger(window_clan.class.getName()).log(Level.SEVERE,
                                                            "Clan get data error",ex);
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
        
        
        HQLabel = new JLabel("Headquater:");
        HQLabel.setBounds(20, 50, 100, 20);
        add(HQLabel);
        
        HQField = new JTextField();
        HQField.setBounds(90, 50, 100, 20);
        HQField.addActionListener(this);
        add(HQField);
        
        creatorList = new JList(creatorData.toArray());
        creatorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        creatorList.setLayoutOrientation(VERTICAL);
        creatorList.setVisibleRowCount(-1);
        add(creatorList);
        
        creatorLabel = new JLabel("Creator:");
        creatorLabel.setBounds(20, 75, 100, 20);
        add(creatorLabel);
        JScrollPane scroll_creator = new JScrollPane(creatorList);
        scroll_creator.setPreferredSize(new Dimension(250, 100));
        scroll_creator.setBounds(90, 75, 130, 100);
        add(scroll_creator);
    }
    public void delete_init(){
        deleteButton = new JButton("Delete");
        deleteButton.setBounds(230, 200, 100, 30);
        add(deleteButton);
        deleteButton.addActionListener(this);
        
        updateButton = new JButton("Edit");
        updateButton.setBounds(230, 240, 100, 30);
        add(updateButton);
        updateButton.addActionListener(this);
        
        namesList = new JList(clanData.toArray());
        namesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        namesList.setLayoutOrientation(VERTICAL);
        namesList.setVisibleRowCount(1);
        namesList.addListSelectionListener((e) -> {
            JList list = (JList) e.getSource();
            if(list.getSelectedValue() != null) {
                String selected = list.getSelectedValue().toString();
                displayClan(dbConnector.getId(selected));
            }
        });
        add(namesList);
        
        JScrollPane scroll_pc= new JScrollPane(namesList);
        scroll_pc.setPreferredSize(new Dimension(250, 100));
        scroll_pc.setBounds(60,200,150,100);
        add(scroll_pc);
        }
    @Override
     public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == insertButton){
            String result;
            if(nameField.getText().equals("")||HQField.getText().equals("") ||creatorList.getSelectedValue()==null){
                System.out.println("Clan brak danych");
            }
            else{
                String name = nameField.getText();
                try{
                    int id = dbConnector.getId(creatorList.getSelectedValue().toString());
                   result = dbConnector.createClan(name, id, HQField.getText());
                   System.out.println(result);
                   infoLabel.setText(result);
                }catch(SQLException ex){
                Logger.getLogger(window_clan.class.getName()).log(Level.SEVERE,
                                                            "Clan insert error",ex);
                infoLabel.setText(name+" is used, insert unique name");
                infoLabel.setForeground(Color.red);}
            }
        }
        if(source == deleteButton){
            if(namesList.getSelectedValue() != null){
                int id = dbConnector.getId(namesList.getSelectedValue().toString());
                String name = namesList.getSelectedValue().toString();
                try{
                    dbConnector.deleteClan(id);
                    infoLabel.setText(name + " deleted");
                }catch(SQLException ex){
                    Logger.getLogger(window_clan.class.getName()).log(Level.SEVERE,
                                                                "Delete clan error",ex);
                    infoLabel.setText("Something gone wrong with deleting clan");}
            }
        }
        if(source ==  updateButton){ //FIXME constraint violated
            if(namesList.getSelectedValue() != null){
                String query = "UPDATE clans SET";
                int i = 0;
                if(!nameField.getText().equals("")){
                    query += " clan_name = \'" + nameField.getText()+"\'";
                    i++;
                }
                if(!HQField.getText().equals("")){
                    if(i>0)
                        query += ", ";
                    query += " headquater = \'" + HQField.getText() + "\'";
                    i++;
                }
                if(i>0){
                    query += " WHERE clan_id = "+ dbConnector.getId(namesList.getSelectedValue().toString());
                    try{
                        Statement stmt = dbConnector.getConnection().createStatement();
                        int changes;
                        changes = stmt.executeUpdate(query);
                        stmt.close();
                        infoLabel.setText("Clan successfully updated");
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
    
    private void displayClan(int cid) {
        try {
            PreparedStatement statement = dbConnector.getConnection().prepareStatement(
                        "select c.clan_name as name, c.headquater as hq, m.member_id as id "
                                + "from clans c join membership m on c.clan_id = m.clan_id "
                                + "where c.clan_id = ? and m.founder = \'Y\'");
            statement.setInt(1, cid);
            ResultSet rs = statement.executeQuery();
            rs.next();
            
            String clanName = rs.getString("name");
            String hq = rs.getString("hq");
            int id = rs.getInt("id");
            
            nameField.setText(clanName);
            HQField.setText(hq);
            
            for(int i = 0; i < creatorList.getModel().getSize(); ++i) {
                String s = creatorList.getModel().getElementAt(i).toString();
                if(dbConnector.getId(s) == id) {
                    creatorList.setSelectedIndex(i);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(window_race.class.getName()).log(Level.SEVERE, "aaaaaaaa", ex);
        }
     }
     
}