/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game_zbsn;
import java.awt.Color;
import java.sql.*;
import java.awt.Dimension;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
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
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import oracle.jdbc.OracleTypes;

/**
 *
 * @author User
 */
public class window_race extends JFrame implements ActionListener{
    private int windowHeight, windowWidth;
    private DBConnector dbConnector;
    
    //--------INSERTING--------------------------
    private JButton insertButton;
    private JTextField nameField, strengthField, agilityField,
                       intellectField;
    private JLabel nameLabel, statsLabel, weightLabel, infoLabel;
    //-------------------------------------------
    private JList namesList;
    //--------DELETING---------------------------
    private JButton deleteButton;
    private List<String> raceData = new ArrayList<String>();
    //-------------------------------------------
    
    //--------EDITING----------------------------
    private JButton updateButton;
    //-------------------------------------------

    public window_race(int w, DBConnector dbConnector){
        this.windowHeight = w; this.windowWidth = w;
        this.dbConnector = dbConnector;
        setSize(this.windowWidth, this.windowHeight);
        setTitle("Races");
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
        
        namesList = new JList(raceData.toArray());
            namesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            namesList.setLayoutOrientation(VERTICAL);
            namesList.setVisibleRowCount(1);
            namesList.addListSelectionListener((e) -> {
                JList list = (JList) e.getSource();
                if(list.getSelectedValue() != null) {
                    String selected = list.getSelectedValue().toString();
                    displayRace(selected);
                }
            });
            add(namesList);

            JScrollPane scroll_pc= new JScrollPane(namesList);
            scroll_pc.setPreferredSize(new Dimension(250, 100));
            scroll_pc.setBounds(60,200,150,70);
            add(scroll_pc);
        
        }
    public void get_data(){
        try{
            nameField.setText("");
            strengthField.setText("");
            agilityField.setText("");
            intellectField.setText("");
            raceData = dbConnector.getRaces();
            namesList.setListData(raceData.toArray());
        }catch(SQLException ex){
            Logger.getLogger(window_race.class.getName()).log(Level.SEVERE,
                                                            "Get_race error",ex);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == deleteButton){
            //Można usprawnić np. sprawdzić poprawność ale te dane są pobierane z Bazy więc nie powinno być błędu
            if(namesList.getSelectedValue() != null){
                String name = namesList.getSelectedValue().toString();
                try{
                    dbConnector.deleteRace(name);
                    infoLabel.setText("Race successfully deleted");
                }catch(SQLException ex){
                    Logger.getLogger(window_race.class.getName()).log(Level.SEVERE,
                                                                "Delete race error",ex);
                    infoLabel.setText("Something gone wrong with deleting");}
            }
        }
        if(source == insertButton){
            if(nameField.getText().equals("") || strengthField.getText().equals("") || 
               agilityField.getText().equals("") || intellectField.getText().equals("")){
               System.out.println("rasa brak danych");
               infoLabel.setText("Missing data");
            }
            else{
                String name = nameField.getText();
                try{
                    int strength = Integer.parseInt(strengthField.getText());
                    int agility = Integer.parseInt(agilityField.getText());
                    int intellect = Integer.parseInt(intellectField.getText());
                    dbConnector.createRace(name, strength, agility, intellect);
                    infoLabel.setText("Race successfully created");
                }catch(SQLException | NumberFormatException ex){
                    Logger.getLogger(window_race.class.getName()).log(Level.SEVERE,
                                                            "SQL or int error race class",ex);
                    infoLabel.setText(name+" is used, insert unique name");
                    infoLabel.setForeground(Color.red);}

            }
        }
        if(source ==  updateButton){
            if(namesList.getSelectedValue() != null){
                String query = "UPDATE races SET";
                int i = 0;
                if(!agilityField.getText().equals("")){
                    if(i>0)
                        query += ", ";
                    query += " agility = " + agilityField.getText();
                    i++;}
                if(!strengthField.getText().equals("")){
                    if(i>0)
                        query += ", ";
                    query += " strength = " + strengthField.getText();
                    i++;}
                if(!intellectField.getText().equals("")){
                    if(i>0)
                        query += ", ";
                    query += " intellect = " + intellectField.getText();
                    i++;}
                if(i>0){
                    query += " WHERE r_name LIKE '"+ namesList.getSelectedValue().toString()+"'";
                    try{
                        Statement stmt = dbConnector.getConnection().createStatement();
                        int changes;
                        changes = stmt.executeUpdate(query);
                        stmt.close();
                        infoLabel.setText("Race successfully updated");
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
    
    private void displayRace(String name) {
        try {
            PreparedStatement statement = dbConnector.getConnection().prepareStatement(
                    "select * from races where r_name = ?");
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            rs.next();
            int s = rs.getInt("strength");
            int a = rs.getInt("agility");
            int i = rs.getInt("intellect");
            nameField.setText(name);
            agilityField.setText(a + "");
            strengthField.setText(s + "");
            intellectField.setText(i + "");
        } catch (SQLException ex) {
            Logger.getLogger(window_race.class.getName()).log(Level.SEVERE, "aaaaaaaa", ex);
        }
    }
    
}
