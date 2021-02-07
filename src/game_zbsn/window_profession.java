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
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import oracle.jdbc.OracleTypes;

/**
 *
 * @author User
 */
public class window_profession extends JFrame implements ActionListener{
    private int windowHeight, windowWidth;
    private DBConnector dbConnector;
    
    //--------INSERTING--------------------------
    private JButton insertButton;
    private JTextField nameField;
    private JLabel nameLabel, infoLabel;
    //-------------------------------------------
    private JList namesList;
    //--------DELETING---------------------------
    private JButton deleteButton;
    private List<String> professionData = new ArrayList<String>();
    //-------------------------------------------
    
    //--------EDITING----------------------------
    private JButton updateButton;
    //-------------------------------------------

    public window_profession(int w, DBConnector dbConnector){
        this.windowHeight = w; this.windowWidth = w;
        this.dbConnector = dbConnector;
        setSize(this.windowWidth, this.windowHeight);
        setTitle("Professions");
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
        
        namesList = new JList(professionData.toArray());
        namesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        namesList.setLayoutOrientation(VERTICAL);
        namesList.setVisibleRowCount(1);
        namesList.addListSelectionListener((e) -> {
            JList list = (JList) e.getSource();
            if(list.getSelectedValue() != null) {
                nameField.setText(list.getSelectedValue().toString());
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
            professionData = dbConnector.getProfessions();
            namesList.setListData(professionData.toArray());
        }catch(SQLException ex){
            Logger.getLogger(window_profession.class.getName()).log(Level.SEVERE,
                                                            "Get_professions error",ex);
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
                    dbConnector.deleteProfession(name);
                    infoLabel.setText("Item successfully deleted");
                }catch(SQLException ex){
                    Logger.getLogger(window_profession.class.getName()).log(Level.SEVERE,
                                                                "Delete profession error",ex);
                    infoLabel.setText("Something gone wrong with deleting");}
            }
        }
        if(source == insertButton){
            if(nameField.getText().equals("")){
               System.out.println("Profession missing data");
               infoLabel.setText("Missing data");
            }
            else{
                String name = nameField.getText();
                try{
                    dbConnector.createProfession(name);
                    infoLabel.setText("Item successfully created");
                }catch(SQLException | NumberFormatException ex){
                    Logger.getLogger(window_profession.class.getName()).log(Level.SEVERE,
                                                            "Insert profession error",ex);
                    infoLabel.setText(name+" is used, insert unique name");
                    infoLabel.setForeground(Color.red);}
            }
        }
        get_data();
    }
}
