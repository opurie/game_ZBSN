/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game_zbsn;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Level;


/**
 *
 * @author User
 */
public class Game_ZBSN extends JFrame implements ActionListener {
    private Connection con; private JLabel con_label;
    
    private int window_height = 350, window_width = 320;
    private int gamemode_scale = 500, create_scale = 400;
    private JButton bGamemode, bExit, bConnect;
    //-----------CREATING--------------- 
    private JButton bCreate;
    private JRadioButton create_pc;
    private JRadioButton create_clan;
    private JRadioButton create_monster;
    private JRadioButton create_quest;
    private JRadioButton create_item;
    private JRadioButton create_race;
    private JRadioButton create_profession;
    private ButtonGroup create_group;
    //---------------------------------- 

    //-----------GAMEMODE--------------- 
    private JButton bpvp, bpve, bClan, bQuest;
    //----------------------------------
    
    //-----------FRAMES-----------------
    private JFrame secondary_window = null;
    //----------------------------------
    window_pc pc = null;
    public Game_ZBSN(){
        setSize(window_width, window_height);
        setTitle("PUT GAMES");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(null);
        
        creating_method();
        gamemode_method();
        
        bConnect = new JButton("Connect");
        bConnect.setBounds(window_width-300, window_height - 330, 100, 30);
        add(bConnect);
        bConnect.addActionListener(this);
        
        con_label = new JLabel("Connect with Database...");
        con_label.setForeground(Color.GRAY);
        con_label.setBounds(window_width-310, window_height - 300, 150, 30);
        add(con_label);
        
        bExit = new JButton("Exit game");
        bExit.setBounds(window_width - 130, window_height - 80, 100, 20);
        add(bExit);
        bExit.addActionListener(this);
    }
    public void gamemode_method(){
        //bpvp, bpve, bClan, bQuest;
        bpvp = new JButton("PvP");
        bpvp.setBounds(window_width - 130, 40, 100, 30);
        add(bpvp);
        bpvp.addActionListener(this);
        
        bpve = new JButton("PvE");
        bpve.setBounds(window_width - 130, 75, 100, 30);
        add(bpve);
        bpve.addActionListener(this);
        
        bClan = new JButton("Clans");
        bClan.setBounds(window_width - 130, 110, 100, 30);
        add(bClan);
        bClan.addActionListener(this);
        
        bQuest = new JButton("Quests");
        bQuest.setBounds(window_width - 130, 145, 100, 30);
        add(bQuest);
        bQuest.addActionListener(this);
    }
    
    //buttons for 
    public void creating_method(){
        bCreate = new JButton("Create/Delete");
        bCreate.setBounds(20,window_height - 85, 140, 30);
        add(bCreate);
        bCreate.addActionListener(this);
        
        create_pc = new JRadioButton("Playable Characters", true);  add(create_pc);
        create_pc.setBounds(20, window_height - 110, 150, 20);
        create_monster = new JRadioButton("Monsters", false);       add(create_monster);
        create_monster.setBounds(20, window_height - 130, 100, 20);
        create_clan = new JRadioButton("Clans",false);              add(create_clan);
        create_clan.setBounds(20, window_height - 150, 100, 20);
        create_quest = new JRadioButton("Quests", false);           add(create_quest);
        create_quest.setBounds(20, window_height - 170, 100, 20);
        create_item = new JRadioButton("Items", false);             add(create_item);
        create_item.setBounds(20, window_height - 190, 100, 20);
        create_race = new JRadioButton("Races", false);             add(create_race);
        create_race.setBounds(20, window_height - 210, 100, 20);
        create_profession = new JRadioButton("Professions", false); add(create_profession);
        create_profession.setBounds(20, window_height - 230, 100, 20);
        
        
        create_group = new ButtonGroup();
        create_group.add(create_pc);        
        create_group.add(create_monster);   create_group.add(create_item);
        create_group.add(create_clan);      create_group.add(create_race);
        create_group.add(create_quest);     create_group.add(create_profession);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Game_ZBSN game = new Game_ZBSN();
        game.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        //bpvp, bpve, bClan, bQuest;
        if(source == bConnect && con == null){
            String connectionString = "jdbc:oracle:thin:@//admlab2.cs.put.poznan.pl:1521/"+
                               "dblab02_students.cs.put.poznan.pl";
            Properties connectionProps = new Properties();
            connectionProps.put("user","inf141299");
            connectionProps.put("password", "inf141299");
            try{
                con = DriverManager.getConnection(connectionString, connectionProps);
                System.out.println("Połączono z bazą danych");
                con_label.setText("Connected!");
                con_label.setForeground(Color.green);
                con_label.setBounds(window_width-290, window_height - 300, 150, 30);
            }catch(SQLException ex){
                Logger.getLogger(Game_ZBSN.class.getName()).log(Level.SEVERE,
                                                            "Nie udało się połączyć z bazą danych",ex);
                con_label.setText("Connection failed...");
                con_label.setForeground(Color.red);
                con_label.setBounds(window_width-310, window_height - 300, 150, 30);
                con = null;
            }
        }
        if(/*con != null*/ true){
            //gamemode buttons actions
            if(source == bpvp){
                window_cleaner(secondary_window);
                secondary_window = new gamemode_pvp(gamemode_scale, con);
            }
            if(source == bpve){
                window_cleaner(secondary_window);
                secondary_window = new gamemode_pve(gamemode_scale, con);
            }
            if(source == bClan){
                window_cleaner(secondary_window);
                secondary_window = new gamemode_clan(gamemode_scale, con);
            }
            if(source == bQuest){
                window_cleaner(secondary_window);
                secondary_window = new gamemode_quest(gamemode_scale, con);
            }
            //Create/delete button actions
            if(source == bCreate){
                window_cleaner(secondary_window);
                if (create_pc.isSelected()){
                    System.out.println("Open PC window");
                    secondary_window = new window_pc(create_scale, con);
                }
                else if(create_monster.isSelected()){
                    System.out.println("Open monster window");
                    secondary_window = new window_monster(create_scale, con);
                }
                else if(create_clan.isSelected()){
                    System.out.println("Open clan window");
                    secondary_window = new window_clan(create_scale, con);
                }
                else if(create_quest.isSelected()){
                    System.out.println("Open quest window");
                    secondary_window = new window_quest(create_scale, con);
                }
                else if(create_item.isSelected()){
                    System.out.println("Open item window");
                    secondary_window = new window_item(create_scale, con);
                }
                else if(create_race.isSelected()){
                    System.out.println("Open race window");
                    secondary_window = new window_race(create_scale, con);
                }
                else if(create_profession.isSelected()){
                    System.out.println("Open profession window");
                    secondary_window = new window_profession(create_scale, con);
                }
            }
        }
        
        if(source == bExit) {
            System.exit(0);
        }        
    }
    
    //closing window
    public void window_cleaner(JFrame window){
        if(window != null){
            window.setVisible(false);
            window=null;
        }
    }
}
