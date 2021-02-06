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
    private int window_height, window_width;
    private DBConnector dbConnector;
    
    //--------INSERTING--------------------------
    private JButton bInsert;
    private JTextField InsertName;
    private JLabel lName, lInfo;
    //-------------------------------------------
    private JList ListOfNames;
    //--------DELETING---------------------------
    private JButton bDelete;
    private List<String> ProfessionData = new ArrayList<String>();
    //-------------------------------------------
    
    //--------EDITING----------------------------
    private JButton bUpdate;
    //-------------------------------------------

    public window_profession(int w, DBConnector dbConnector){
        this.window_height = w; this.window_width = w;
        this.dbConnector = dbConnector;
        setSize(this.window_width, this.window_height);
        setTitle("Professions");
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
        
        ListOfNames = new JList(ProfessionData.toArray());
        ListOfNames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListOfNames.setLayoutOrientation(VERTICAL);
        ListOfNames.setVisibleRowCount(1);
        ListOfNames.addListSelectionListener((e) -> {
            JList list = (JList) e.getSource();
            if(list.getSelectedValue() != null) {
                InsertName.setText(list.getSelectedValue().toString());
            } 
       });
        add(ListOfNames);
        
        JScrollPane scroll_pc= new JScrollPane(ListOfNames);
        scroll_pc.setPreferredSize(new Dimension(250, 100));
        scroll_pc.setBounds(60,200,150,70);
        add(scroll_pc);
        }

    public void get_data(){
        try{
            InsertName.setText("");
            ProfessionData = dbConnector.getProfessions();
            ListOfNames.setListData(ProfessionData.toArray());
        }catch(SQLException ex){
            Logger.getLogger(window_profession.class.getName()).log(Level.SEVERE,
                                                            "Get_professions error",ex);
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
                    dbConnector.deleteProfession(name);
                    lInfo.setText("Item successfully deleted");
                }catch(SQLException ex){
                    Logger.getLogger(window_profession.class.getName()).log(Level.SEVERE,
                                                                "Delete profession error",ex);
                    lInfo.setText("Something gone wrong with deleting");}
            }
        }
        if(source == bInsert){
            if(InsertName.getText().equals("")){
               System.out.println("Profession missing data");
               lInfo.setText("Missing data");
            }
            else{
                try{
                    dbConnector.createProfession(InsertName.getText());
                    lInfo.setText("Item successfully created");
                }catch(SQLException | NumberFormatException ex){
                    Logger.getLogger(window_profession.class.getName()).log(Level.SEVERE,
                                                            "Insert profession error",ex);
                    lInfo.setText("Something gone wrong with creating");
                    lInfo.setForeground(Color.red);}
            }
        }
        get_data();
    }
}
