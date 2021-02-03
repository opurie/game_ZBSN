/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game_zbsn;
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
    private int window_height, window_width;
    private DBConnector dbConnector;
    
    //--------INSERTING--------------------------
    private JButton bInsert;
    private JTextField InsertName, InsertStrength, InsertAgility,
                       InsertIntellect;
    private JLabel lName, lStatistics, lWeight;
    //-------------------------------------------
    private JList ListOfNames;
    //--------DELETING---------------------------
    private JButton bDelete;
    private List<String> RaceData = new ArrayList<String>();
    //-------------------------------------------
    
    //--------EDITING----------------------------
    private JButton bUpdate;
    //-------------------------------------------

    public window_race(int w, DBConnector dbConnector){
        this.window_height = w; this.window_width = w;
        this.dbConnector = dbConnector;
        setSize(this.window_width, this.window_height);
        setTitle("Monsters");
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
        
        ListOfNames = new JList(RaceData.toArray());
            ListOfNames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            ListOfNames.setLayoutOrientation(VERTICAL);
            ListOfNames.setVisibleRowCount(1);
            add(ListOfNames);

            JScrollPane scroll_pc= new JScrollPane(ListOfNames);
            scroll_pc.setPreferredSize(new Dimension(250, 100));
            scroll_pc.setBounds(60,200,150,70);
            add(scroll_pc);
        
        }
    public void get_data(){
        try{
            RaceData = dbConnector.getRaces();
        }catch(SQLException ex){
            Logger.getLogger(window_race.class.getName()).log(Level.SEVERE,
                                                            "Get_race error",ex);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == bDelete){
            //Można usprawnić np. sprawdzić poprawność ale te dane są pobierane z Bazy więc nie powinno być błędu
            if(ListOfNames.getSelectedValue() != null){
                String name = ListOfNames.getSelectedValue().toString();
                try{
                    dbConnector.deleteRace(name);
                }catch(SQLException ex){
                    Logger.getLogger(window_race.class.getName()).log(Level.SEVERE,
                                                                "Delete race error",ex);}
            }
        }
        if(source == bInsert){
            if(InsertName.getText().equals("") || InsertStrength.getText().equals("") || 
               InsertAgility.getText().equals("") || InsertIntellect.getText().equals("")){
               System.out.println("rasa brak danych");
            }
            else{
                try{
                    String name = InsertName.getText();
                    int strength = Integer.parseInt(InsertStrength.getText());
                    int agility = Integer.parseInt(InsertAgility.getText());
                    int intellect = Integer.parseInt(InsertIntellect.getText());
                    dbConnector.createRace(name, strength, agility, intellect);
                }catch(SQLException | NumberFormatException ex){
                    Logger.getLogger(window_race.class.getName()).log(Level.SEVERE,
                                                            "SQL or int error race class",ex);}
            }
        }
    } 
}
