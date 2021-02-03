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
    private Connection connection;
    private DBConnector dbConnector;
    private JLabel ConLabel;
    
    private int window_height = 350, window_width = 320;
    private int gamemode_scale = 500, create_scale = 400;
    private JButton bGamemode, bExit, bConnect;
    //-----------CREATING--------------- 
    private JButton bCreate;
    private JRadioButton CreatePC;
    private JRadioButton CreateClan;
    private JRadioButton CreateMonster;
    private JRadioButton CreateQuest;
    private JRadioButton CreateItem;
    private JRadioButton CreateRace;
    private JRadioButton CreateProfession;
    private ButtonGroup CreateGroup;
    //---------------------------------- 

    //-----------GAMEMODE--------------- 
    private JButton bpvp, bpve, bClan, bQuest;
    //----------------------------------
    
    //-----------FRAMES-----------------
    private JFrame SecondaryWindow = null;
    //----------------------------------
    window_pc pc = null;
    public Game_ZBSN(){
        setSize(window_width, window_height);
        setTitle("PUT GAMES");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        
        creating_method();
        gamemode_method();
        
        bConnect = new JButton("Connect");
        bConnect.setBounds(20, 20, 100, 30);
        add(bConnect);
        bConnect.addActionListener(this);
        
        ConLabel = new JLabel("Connect with Database...");
        ConLabel.setForeground(Color.GRAY);
        ConLabel.setBounds(10, 50, 150, 30);
        add(ConLabel);
        
        bExit = new JButton("Exit game");
        bExit.setBounds(190, 270, 100, 20);
        add(bExit);
        bExit.addActionListener(this);
    }
    public void gamemode_method(){
        //bpvp, bpve, bClan, bQuest;
        bpvp = new JButton("PvP");
        bpvp.setBounds(190, 40, 100, 30);
        add(bpvp);
        bpvp.addActionListener(this);
        
        bpve = new JButton("PvE");
        bpve.setBounds(190, 75, 100, 30);
        add(bpve);
        bpve.addActionListener(this);
        
        bClan = new JButton("Clans");
        bClan.setBounds(190, 110, 100, 30);
        add(bClan);
        bClan.addActionListener(this);
        
        bQuest = new JButton("Quests");
        bQuest.setBounds(190, 145, 100, 30);
        add(bQuest);
        bQuest.addActionListener(this);
    }
    
    //buttons for 
    public void creating_method(){
        bCreate = new JButton("Create/Delete");
        bCreate.setBounds(20,265, 140, 30);
        add(bCreate);
        bCreate.addActionListener(this);
        
        CreatePC = new JRadioButton("Playable Characters", true);  add(CreatePC);
        CreatePC.setBounds(20, 240, 150, 20);
        CreateMonster = new JRadioButton("Monsters", false);       add(CreateMonster);
        CreateMonster.setBounds(20, 220, 100, 20);
        CreateClan = new JRadioButton("Clans",false);              add(CreateClan);
        CreateClan.setBounds(20, 200, 100, 20);
        CreateQuest = new JRadioButton("Quests", false);           add(CreateQuest);
        CreateQuest.setBounds(20, 180, 100, 20);
        CreateItem = new JRadioButton("Items", false);             add(CreateItem);
        CreateItem.setBounds(20, 160, 100, 20);
        CreateRace = new JRadioButton("Races", false);             add(CreateRace);
        CreateRace.setBounds(20, 140, 100, 20);
        CreateProfession = new JRadioButton("Professions", false); add(CreateProfession);
        CreateProfession.setBounds(20, 120, 100, 20);
        
        
        CreateGroup = new ButtonGroup();
        CreateGroup.add(CreatePC);        
        CreateGroup.add(CreateMonster);   CreateGroup.add(CreateItem);
        CreateGroup.add(CreateClan);      CreateGroup.add(CreateRace);
        CreateGroup.add(CreateQuest);     CreateGroup.add(CreateProfession);
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
        if(source == bConnect && connection == null){
     
            try{
                dbConnector = new DBConnector("inf141299", "inf141299");
                connection = dbConnector.connect();
                System.out.println("Połączono z bazą danych");
                ConLabel.setText("Connected!");
                ConLabel.setForeground(Color.green);
                ConLabel.setBounds(30, 50, 150, 30);
            }catch(SQLException ex){
                Logger.getLogger(Game_ZBSN.class.getName()).log(Level.SEVERE,
                                                            "Nie udało się połączyć z bazą danych",ex);
                ConLabel.setText("Connection failed...");
                ConLabel.setForeground(Color.red);
                ConLabel.setBounds(10, 50, 150, 30);
                connection = null;
            }
        }
        if(/*con != null*/ true){
            //gamemode buttons actions
            if(source == bpvp){
                window_cleaner(SecondaryWindow);
                SecondaryWindow = new gamemode_pvp(gamemode_scale, connection);
            }
            if(source == bpve){
                window_cleaner(SecondaryWindow);
                SecondaryWindow = new gamemode_pve(gamemode_scale, connection);
            }
            if(source == bClan){
                window_cleaner(SecondaryWindow);
                SecondaryWindow = new gamemode_clan(gamemode_scale, connection);
            }
            if(source == bQuest){
                window_cleaner(SecondaryWindow);
                SecondaryWindow = new gamemode_quest(gamemode_scale, connection);
            }
            //Create/delete button actions
            if(source == bCreate){
                window_cleaner(SecondaryWindow);
                if (CreatePC.isSelected()){
                    System.out.println("Open PC window");
                    SecondaryWindow = new window_pc(create_scale, dbConnector);
                }
                else if(CreateMonster.isSelected()){
                    System.out.println("Open monster window");
                    SecondaryWindow = new window_monster(create_scale, dbConnector);
                }
                else if(CreateClan.isSelected()){
                    System.out.println("Open clan window");
                    SecondaryWindow = new window_clan(create_scale, connection);
                }
                else if(CreateQuest.isSelected()){
                    System.out.println("Open quest window");
                    SecondaryWindow = new window_quest(create_scale, connection);
                }
                else if(CreateItem.isSelected()){
                    System.out.println("Open item window");
                    SecondaryWindow = new window_item(create_scale, connection);
                }
                else if(CreateRace.isSelected()){
                    System.out.println("Open race window");
                    SecondaryWindow = new window_race(create_scale, dbConnector);
                }
                else if(CreateProfession.isSelected()){
                    System.out.println("Open profession window");
                    SecondaryWindow = new window_profession(create_scale, dbConnector);
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
