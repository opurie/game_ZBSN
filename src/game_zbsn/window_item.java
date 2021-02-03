/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game_zbsn;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
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
    private JTextField insert_name, insert_strength, insert_agility,
                       insert_intellect, insert_weight;
    private JList insert_profession;
    private JLabel lName, lStatistics, lWeight, lProfession;
    private List<String> profession_data = new ArrayList<String>();
    //-------------------------------------------
    
    private JList list_of_pc;
    //--------DELETING---------------------------
    private JButton bDelete;
    private List<String> item_data = new ArrayList<String>();
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
        
        get_data();
        insert_init();
        delete_init();
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
        
        lStatistics = new JLabel("Statistics:");
        lStatistics.setBounds(20, 50, 100, 20);
        add(lStatistics);
        
        insert_strength = new JTextField();
        insert_strength.setBounds(90, 50, 30, 20);
        add(insert_strength);
        insert_strength.addActionListener(this);
        
        insert_agility = new JTextField();
        insert_agility.setBounds(123, 50, 30, 20);
        add(insert_agility);
        insert_agility.addActionListener(this);
        
        insert_intellect = new JTextField();
        insert_intellect.setBounds(156, 50, 30, 20);
        add(insert_intellect);
        insert_intellect.addActionListener(this);
        
        lWeight = new JLabel("Weight:");
        lWeight.setBounds(20, 75, 50, 20);
        add(lWeight);
        
        insert_weight = new JTextField();
        insert_weight.setBounds(90, 75, 100, 20);
        add(insert_weight);
        insert_weight.addActionListener(this);
        
        insert_profession = new JList(profession_data.toArray());
        insert_profession.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        insert_profession.setLayoutOrientation(VERTICAL);
        insert_profession.setVisibleRowCount(-1);
        add(insert_profession);
        
        lProfession = new JLabel("Profession:");
        lProfession.setBounds(20, 100, 100, 20);
        add(lProfession);
        JScrollPane scroll_profession= new JScrollPane(insert_profession);
        scroll_profession.setPreferredSize(new Dimension(250, 100));
        scroll_profession.setBounds(90,100,100,30);
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
        
        list_of_pc = new JList(item_data.toArray());
        list_of_pc.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list_of_pc.setLayoutOrientation(VERTICAL);
        list_of_pc.setVisibleRowCount(1);
        add(list_of_pc);
        
        JScrollPane scroll_pc= new JScrollPane(list_of_pc);
        scroll_pc.setPreferredSize(new Dimension(250, 100));
        scroll_pc.setBounds(60,200,150,70);
        add(scroll_pc);
        }
    public void get_data(){
        try {
            profession_data = dbConnector.getProfessions();
        } catch(SQLException ex) {
            Logger.getLogger(window_item.class.getName()).log(Level.SEVERE,
                                                            "Item get_professions error",ex);
        } try {
            item_data = dbConnector.getItems();
        } catch(SQLException ex) {
            Logger.getLogger(window_item.class.getName()).log(Level.SEVERE,
                                                            "Item get_items error",ex);
            }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == bDelete){
            //Można usprawnić np. sprawdzić poprawność ale te dane są pobierane z Bazy więc nie powinno być błędu
            String name = list_of_pc.getSelectedValue().toString();
            try {
                dbConnector.deleteItem(name);
                item_data.remove(list_of_pc.getSelectedValue().toString());
            } catch(SQLException ex){
                Logger.getLogger(window_item.class.getName()).log(Level.SEVERE,
                                                            "Delete item error",ex);}
            
        }
        if(source == bInsert){
            if(insert_name.getText().equals("") || insert_strength.getText().equals("") || 
               insert_agility.getText().equals("") || insert_intellect.getText().equals("")||
                    insert_profession.getSelectedValue() == null){
               System.out.println("rasa brak danych");
            } else {
                try {
                    String name = insert_name.getText();
                    int strength = Integer.parseInt(insert_strength.getText());
                    int agility = Integer.parseInt(insert_agility.getText());
                    int intellect = Integer.parseInt(insert_intellect.getText());
                    int weight = Integer.parseInt(insert_weight.getText());
                    String profession = insert_profession.getSelectedValue().toString();
                    dbConnector.createItem(name, strength, agility, intellect, weight, profession);
                } catch(SQLException | NumberFormatException ex){
                    Logger.getLogger(window_item.class.getName()).log(Level.SEVERE,
                                                            "SQL or int error item class",ex);}
                //TODO separate exceptions
            }
        }
    }
    
}
