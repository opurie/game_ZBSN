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
public class window_monster extends JFrame implements ActionListener{
    private int window_height, window_width;
    private Connection con;
    
    //--------INSERTING--------------------------
    private JButton bInsert;
    private JTextField insert_name;
    private JList insert_race, insert_profession, insert_item;
    private JLabel lName, lRace, lProfession, lItem;
    private static String[] test_race={"dziekan","piwo","student3.0"};
    private static String[] test_prof={"inf","air","mcdonald"};
    private static String[] test_item={"Nothing", "Axe", "Spoon", "Sword", "Banana"};
    //-------------------------------------------
    
    private JList list_of_pc;
    //--------DELETING---------------------------
    private JButton bDelete;
    private static String[] delete_string={"monster1","monster2","monster3","monster4", "monster1"};
    //-------------------------------------------
    
    //--------EDITING----------------------------
    private JButton bUpdate;
    //-------------------------------------------
    public window_monster(int w, Connection con){
        this.window_height = w; this.window_width = w;
        this.con = con;
        setSize(this.window_width, this.window_height);
        setTitle("Monsters");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setVisible(true);

        insert_init();
        delete_init();
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
        
        list_of_pc = new JList(delete_string);
        list_of_pc.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list_of_pc.setLayoutOrientation(VERTICAL);
        list_of_pc.setVisibleRowCount(1);
        add(list_of_pc);
        
        JScrollPane scroll_pc= new JScrollPane(list_of_pc);
        scroll_pc.setPreferredSize(new Dimension(250, 100));
        scroll_pc.setBounds(60,200,150,70);
        add(scroll_pc);
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
         
        insert_race = new JList(test_race);
        insert_race.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        insert_race.setLayoutOrientation(VERTICAL);
        insert_race.setVisibleRowCount(-1);
        add(insert_race);
        
        lRace = new JLabel("Race:");      // add select name from races;
        lRace.setBounds(20, 50, 50, 20);
        add(lRace);
        JScrollPane scroll_race= new JScrollPane(insert_race);
        scroll_race.setPreferredSize(new Dimension(250, 100));
        scroll_race.setBounds(90, 50, 100, 30);
        add(scroll_race);
        
        insert_profession = new JList(test_prof);
        insert_profession.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        insert_profession.setLayoutOrientation(VERTICAL);
        insert_profession.setVisibleRowCount(-1);
        add(insert_profession);
        
        lProfession = new JLabel("Profession:"); // add select name from professions;
        lProfession.setBounds(20, 85, 100, 20);
        add(lProfession);
        JScrollPane scroll_profession= new JScrollPane(insert_profession);
        scroll_profession.setPreferredSize(new Dimension(250, 100));
        scroll_profession.setBounds(90,85,100,30);
        add(scroll_profession);
        
        insert_item = new JList(test_item);
        insert_item.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        insert_item.setLayoutOrientation(VERTICAL);
        insert_item.setVisibleRowCount(-1);
        add(insert_item);
        
        lItem = new JLabel("Items:");
        lItem.setBounds(20, 120, 100, 20);
        add(lItem);
        JScrollPane scroll_item = new JScrollPane(insert_item);
        scroll_item.setPreferredSize(new Dimension(250,100));
        scroll_item.setBounds(90, 120, 100, 60);
        add(scroll_item);
        
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource(); 
    }
    
}