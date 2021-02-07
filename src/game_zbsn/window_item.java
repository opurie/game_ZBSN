/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game_zbsn;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import static javax.swing.JList.VERTICAL;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import oracle.jdbc.OracleTypes;

/**
 *
 * @author User
 */
public class window_item extends JFrame implements ActionListener{
    private int windowHeight, windowWidth;
    private DBConnector dbConnector;
    //--------INSERTING--------------------------
    private JButton insertButton;
    private JTextField nameField, strengthField, agilityField,
                       intellectField, weightField;
    private JList professionList;
    private JLabel nameLabel, statsLabel, weightLabel, professionLabel, infoLabel;
    private List<String> professionData = new ArrayList<String>();
    //-------------------------------------------
    
    private JList namesList;
    //--------DELETING---------------------------
    private JButton deleteButton;
    private List<String> itemData = new ArrayList<String>();
    //-------------------------------------------
    
    //--------EDITING----------------------------
    private JButton bUpdate;
    //-------------------------------------------
    public window_item(int w, DBConnector dbConnector){
        this.windowHeight = w; this.windowWidth = w;
        this.dbConnector = dbConnector;
        setSize(this.windowWidth, this.windowHeight);
        setTitle("Items");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setVisible(true);
        setResizable(false);

        insert_init();
        delete_init();
        get_data();
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
        
        statsLabel = new JLabel("Statistics:");
        statsLabel.setBounds(20, 50, 100, 20);
        add(statsLabel);
        
        strengthField = new JTextField();
        strengthField.setBounds(90, 50, 30, 20);
        add(strengthField);
        strengthField.addActionListener(this);
        
        agilityField = new JTextField();
        agilityField.setBounds(123, 50, 30, 20);
        add(agilityField);
        agilityField.addActionListener(this);
        
        intellectField = new JTextField();
        intellectField.setBounds(156, 50, 30, 20);
        add(intellectField);
        intellectField.addActionListener(this);
        
        weightLabel = new JLabel("Weight:");
        weightLabel.setBounds(20, 75, 50, 20);
        add(weightLabel);
        
        weightField = new JTextField();
        weightField.setBounds(90, 75, 100, 20);
        add(weightField);
        weightField.addActionListener(this);
        
        professionList = new JList(professionData.toArray());
        professionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        professionList.setLayoutOrientation(VERTICAL);
        professionList.setVisibleRowCount(-1);
        add(professionList);
        
        professionLabel = new JLabel("Profession:");
        professionLabel.setBounds(20, 100, 100, 20);
        add(professionLabel);
        JScrollPane scroll_profession= new JScrollPane(professionList);
        scroll_profession.setPreferredSize(new Dimension(250, 100));
        scroll_profession.setBounds(90,100,130,60);
        add(scroll_profession);
        
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
        
        namesList = new JList(itemData.toArray());
        namesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        namesList.setLayoutOrientation(VERTICAL);
        namesList.setVisibleRowCount(1);
        namesList.addListSelectionListener((e) -> {
            JList list = (JList) e.getSource();
            if(list.getSelectedValue() != null) {
                String selected = list.getSelectedValue().toString();
                displayItem(selected);
            }
            });
        add(namesList);
        
        JScrollPane scroll_pc= new JScrollPane(namesList);
        scroll_pc.setPreferredSize(new Dimension(250, 100));
        scroll_pc.setBounds(60,200,150,70);
        add(scroll_pc);
        }
    public void get_data(){
        try {
            nameField.setText("");
            agilityField.setText("");
            intellectField.setText("");
            strengthField.setText("");
            weightField.setText("");
            
            professionData = dbConnector.getProfessions();
            itemData = dbConnector.getItems();
            
            namesList.setListData(itemData.toArray());
            professionList.setListData(professionData.toArray());
        } catch(SQLException ex) {
            Logger.getLogger(window_item.class.getName()).log(Level.SEVERE,
                                                            "Item get data error",ex);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == deleteButton){
            //Można usprawnić np. sprawdzić poprawność ale te dane są pobierane z Bazy więc nie powinno być błędu
            if(namesList.getSelectedValue() != null){
            try {
                    dbConnector.deleteItem(namesList.getSelectedValue().toString());
                    infoLabel.setText(namesList.getSelectedValue().toString() + " deleted");
            } catch(SQLException ex){
                    Logger.getLogger(window_item.class.getName()).log(Level.SEVERE,
                                                                "Delete item error",ex);
                    infoLabel.setText("Something gone wrong with deleting");
                    infoLabel.setForeground(Color.red);}
            }
            
        }
        if(source == insertButton){
            if(anyFieldEmpty()){
               System.out.println("rasa brak danych");
            } else {
                String name = nameField.getText();
                try {
                    
                    int strength = Integer.parseInt(strengthField.getText());
                    int agility = Integer.parseInt(agilityField.getText());
                    int intellect = Integer.parseInt(intellectField.getText());
                    int weight = Integer.parseInt(weightField.getText());
                    String profession = professionList.getSelectedValue().toString();
                    dbConnector.createItem(name, strength, agility, intellect, weight, profession);
                    infoLabel.setText("Item successfully created");
                } catch(SQLException | NumberFormatException ex){
                    Logger.getLogger(window_item.class.getName()).log(Level.SEVERE,
                                                            "SQL or int error item class",ex);
                    infoLabel.setText(name+" is used, insert unique name");
                    infoLabel.setForeground(Color.red);}
                //TODO separate exceptions
            }
        }
        if(source == bUpdate) {
            if(namesList.getSelectedValue() != null){
                String query = "UPDATE items SET";
                int i = 0;
                if(!nameField.getText().equals("")){
                    query += " i_name = '" + nameField.getText()+"'";
                    i++;
                }
                if(!agilityField.getText().equals("")){
                    if(i>0)
                        query += ", ";
                    query += " agility = " + agilityField.getText();
                    i++;
                }
                if(!strengthField.getText().equals("")){
                    if(i>0)
                        query += ", ";
                    query += " strength = " + strengthField.getText();
                    i++;
                }
                if(!intellectField.getText().equals("")){
                    if(i>0)
                        query += ", ";
                    query += " intellect = " + intellectField.getText();
                    i++;
                }
                if(!weightField.getText().equals("")){
                    if(i>0)
                        query += ", ";
                    query += " weight = " + weightField.getText();
                    i++;
                }
                if(professionList.getSelectedValue() != null){
                    if(i>0)
                        query += ", ";
                    query += " profession = \'" + professionList.getSelectedValue().toString() + "\'";
                    i++;
                }
                if(i>0){
                    query += " WHERE i_name LIKE '"+ namesList.getSelectedValue().toString()+"'";
                    try {
                        Statement stmt = dbConnector.getConnection().createStatement();
                        int changes;
                        changes = stmt.executeUpdate(query);
                        stmt.close();
                        infoLabel.setText("Item successfully updated");
                    } catch(SQLException ex){
                        Logger.getLogger(window_race.class.getName()).log(Level.SEVERE,
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
        return (nameField.getText().equals("") || strengthField.getText().equals("") || 
            agilityField.getText().equals("") || intellectField.getText().equals("")||
            professionList.getSelectedValue() == null);
        
    }
    
    private void displayItem(String name) {
        try {
            PreparedStatement statement = dbConnector.getConnection().prepareStatement(
                    "select * from items where i_name = ?");
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            rs.next();
            int s = rs.getInt("strength");
            int a = rs.getInt("agility");
            int in = rs.getInt("intellect");
            int w = rs.getInt("weight");
            String p = rs.getString("profession");
            nameField.setText(name);
            agilityField.setText(a + "");
            strengthField.setText(s + "");
            intellectField.setText(in + "");
            weightField.setText(w + "");
            for(int i = 0; i < professionList.getModel().getSize(); ++i) {
                if(professionList.getModel().getElementAt(i).equals(p)) {
                    professionList.setSelectedIndex(i);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(window_race.class.getName()).log(Level.SEVERE, "aaaaaaaa", ex);
        }
    }
}
