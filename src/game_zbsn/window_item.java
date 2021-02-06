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
    private int window_height, window_width;
    private DBConnector dbConnector;
    //--------INSERTING--------------------------
    private JButton bInsert;
    private JTextField InsertName, InsertStrength, InsertAgility,
                       InsertIntellect, InsertWeight;
    private JList InsertProfession;
    private JLabel lName, lStatistics, lWeight, lProfession, lInfo;
    private List<String> ProfessionData = new ArrayList<String>();
    //-------------------------------------------
    
    private JList ListOfNames;
    //--------DELETING---------------------------
    private JButton bDelete;
    private List<String> ItemData = new ArrayList<String>();
    //-------------------------------------------
    
    //--------EDITING----------------------------
    private JButton bUpdate;
    //-------------------------------------------
    public window_item(int w, DBConnector dbConnector){
        this.window_height = w; this.window_width = w;
        this.dbConnector = dbConnector;
        setSize(this.window_width, this.window_height);
        setTitle("Items");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setVisible(true);

        insert_init();
        delete_init();
        get_data();
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
        
        lStatistics = new JLabel("Statistics:");
        lStatistics.setBounds(20, 50, 100, 20);
        add(lStatistics);
        
        InsertStrength = new JTextField();
        InsertStrength.setBounds(90, 50, 30, 20);
        add(InsertStrength);
        InsertStrength.addActionListener(this);
        
        InsertAgility = new JTextField();
        InsertAgility.setBounds(123, 50, 30, 20);
        add(InsertAgility);
        InsertAgility.addActionListener(this);
        
        InsertIntellect = new JTextField();
        InsertIntellect.setBounds(156, 50, 30, 20);
        add(InsertIntellect);
        InsertIntellect.addActionListener(this);
        
        lWeight = new JLabel("Weight:");
        lWeight.setBounds(20, 75, 50, 20);
        add(lWeight);
        
        InsertWeight = new JTextField();
        InsertWeight.setBounds(90, 75, 100, 20);
        add(InsertWeight);
        InsertWeight.addActionListener(this);
        
        InsertProfession = new JList(ProfessionData.toArray());
        InsertProfession.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        InsertProfession.setLayoutOrientation(VERTICAL);
        InsertProfession.setVisibleRowCount(-1);
        add(InsertProfession);
        
        lProfession = new JLabel("Profession:");
        lProfession.setBounds(20, 100, 100, 20);
        add(lProfession);
        JScrollPane scroll_profession= new JScrollPane(InsertProfession);
        scroll_profession.setPreferredSize(new Dimension(250, 100));
        scroll_profession.setBounds(90,100,100,60);
        add(scroll_profession);
        
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
        
        ListOfNames = new JList(ItemData.toArray());
        ListOfNames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListOfNames.setLayoutOrientation(VERTICAL);
        ListOfNames.setVisibleRowCount(1);
        ListOfNames.addListSelectionListener((e) -> {
            JList list = (JList) e.getSource();
            if(list.getSelectedValue() != null) {
                String selected = list.getSelectedValue().toString();
                displayItem(selected);
            }
            });
        add(ListOfNames);
        
        JScrollPane scroll_pc= new JScrollPane(ListOfNames);
        scroll_pc.setPreferredSize(new Dimension(250, 100));
        scroll_pc.setBounds(60,200,150,70);
        add(scroll_pc);
        }
    public void get_data(){
        try {
            InsertName.setText("");
            InsertAgility.setText("");
            InsertIntellect.setText("");
            InsertStrength.setText("");
            InsertWeight.setText("");
            
            ProfessionData = dbConnector.getProfessions();
            ItemData = dbConnector.getItems();
            
            ListOfNames.setListData(ItemData.toArray());
            InsertProfession.setListData(ProfessionData.toArray());
        } catch(SQLException ex) {
            Logger.getLogger(window_item.class.getName()).log(Level.SEVERE,
                                                            "Item get data error",ex);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == bDelete){
            //Można usprawnić np. sprawdzić poprawność ale te dane są pobierane z Bazy więc nie powinno być błędu
            if(ListOfNames.getSelectedValue() != null){
            try {
                    dbConnector.deleteItem(ListOfNames.getSelectedValue().toString());
                    lInfo.setText(ListOfNames.getSelectedValue().toString() + " deleted");
            } catch(SQLException ex){
                    Logger.getLogger(window_item.class.getName()).log(Level.SEVERE,
                                                                "Delete item error",ex);
                    lInfo.setText("Something gone wrong with deleting");
                    lInfo.setForeground(Color.red);}
            }
            
        }
        if(source == bInsert){
            if(anyFieldEmpty()){
               System.out.println("rasa brak danych");
            } else {
                try {
                    String name = InsertName.getText();
                    int strength = Integer.parseInt(InsertStrength.getText());
                    int agility = Integer.parseInt(InsertAgility.getText());
                    int intellect = Integer.parseInt(InsertIntellect.getText());
                    int weight = Integer.parseInt(InsertWeight.getText());
                    String profession = InsertProfession.getSelectedValue().toString();
                    dbConnector.createItem(name, strength, agility, intellect, weight, profession);
                    lInfo.setText("Item successfully created");
                } catch(SQLException | NumberFormatException ex){
                    Logger.getLogger(window_item.class.getName()).log(Level.SEVERE,
                                                            "SQL or int error item class",ex);
                    lInfo.setText("Something gone wrong with creating item");
                    lInfo.setForeground(Color.red);}
                //TODO separate exceptions
            }
        }
        get_data();
    }
        
    private boolean anyFieldEmpty() {
        return (InsertName.getText().equals("") || InsertStrength.getText().equals("") || 
            InsertAgility.getText().equals("") || InsertIntellect.getText().equals("")||
            InsertProfession.getSelectedValue() == null);
        
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
            InsertName.setText(name);
            InsertAgility.setText(a + "");
            InsertStrength.setText(s + "");
            InsertIntellect.setText(in + "");
            InsertWeight.setText(w + "");
            for(int i = 0; i < InsertProfession.getModel().getSize(); ++i) {
                if(InsertProfession.getModel().getElementAt(i).equals(p)) {
                    InsertProfession.setSelectedIndex(i);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(window_race.class.getName()).log(Level.SEVERE, "aaaaaaaa", ex);
        }
    }
}
