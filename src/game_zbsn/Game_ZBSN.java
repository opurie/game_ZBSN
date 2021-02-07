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
    
    private int windowHeight = 350, windowWidth = 320;
    private int gamemodeScale = 500, createScale = 400;
    private JButton gamemodeButton, exitButton, connectButton;
    //-----------CREATING--------------- 
    private JButton createButton;
    private JRadioButton createPlayer;
    private JRadioButton createClan;
    private JRadioButton createMonster;
    private JRadioButton createQuest;
    private JRadioButton createItem;
    private JRadioButton createRace;
    private JRadioButton createProfession;
    private ButtonGroup createGroup;
    //---------------------------------- 

    //-----------GAMEMODE--------------- 
    private JButton PVPButton, equipmentButton, clanButton, questButton;
    //----------------------------------
    
    //-----------FRAMES-----------------
    private JFrame secondaryWindow = null;
    //----------------------------------
    window_pc pc = null;
    public Game_ZBSN(){
        setSize(windowWidth, windowHeight);
        setTitle("PUT GAMES");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        
        createButtons();
        createGamemodes();
        
        connectButton = new JButton("Connect");
        connectButton.setBounds(20, 20, 100, 30);
        add(connectButton);
        connectButton.addActionListener(this);
        
        ConLabel = new JLabel("Connect with Database...");
        ConLabel.setForeground(Color.GRAY);
        ConLabel.setBounds(10, 50, 150, 30);
        add(ConLabel);
        
        exitButton = new JButton("Exit game");
        exitButton.setBounds(190, 270, 100, 20);
        add(exitButton);
        exitButton.addActionListener(this);
    }
    public void createGamemodes(){
        //bpvp, bpve, bClan, bQuest;
        //bpvp = new JButton("PvP");
       // bpvp.setBounds(190, 40, 100, 30);
        //add(bpvp);
        //bpvp.addActionListener(this);
        
        equipmentButton = new JButton("Equipment");
        equipmentButton.setBounds(190, 75, 100, 30);
        add(equipmentButton);
        equipmentButton.addActionListener(this);
        
        clanButton = new JButton("Clans");
        clanButton.setBounds(190, 110, 100, 30);
        add(clanButton);
        clanButton.addActionListener(this);
        
        questButton = new JButton("Quests");
        questButton.setBounds(190, 145, 100, 30);
        add(questButton);
        questButton.addActionListener(this);
    }
    
    //buttons for 
    public void createButtons(){
        createButton = new JButton("Create/Delete");
        createButton.setBounds(20,265, 140, 30);
        add(createButton);
        createButton.addActionListener(this);
        
        createPlayer = new JRadioButton("Playable Characters", true);  add(createPlayer);
        createPlayer.setBounds(20, 240, 150, 20);
        createMonster = new JRadioButton("Monsters", false);       add(createMonster);
        createMonster.setBounds(20, 220, 100, 20);
        createClan = new JRadioButton("Clans",false);              add(createClan);
        createClan.setBounds(20, 200, 100, 20);
        createQuest = new JRadioButton("Quests", false);           add(createQuest);
        createQuest.setBounds(20, 180, 100, 20);
        createItem = new JRadioButton("Items", false);             add(createItem);
        createItem.setBounds(20, 160, 100, 20);
        createRace = new JRadioButton("Races", false);             add(createRace);
        createRace.setBounds(20, 140, 100, 20);
        createProfession = new JRadioButton("Professions", false); add(createProfession);
        createProfession.setBounds(20, 120, 100, 20);
        
        
        createGroup = new ButtonGroup();
        createGroup.add(createPlayer);        
        createGroup.add(createMonster);   createGroup.add(createItem);
        createGroup.add(createClan);      createGroup.add(createRace);
        createGroup.add(createQuest);     createGroup.add(createProfession);
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
        if(source == connectButton && connection == null){
     
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
        if(connection !=null){
            //gamemode buttons actions
            //if(source == bpvp){
             //   window_cleaner(SecondaryWindow);
              //  SecondaryWindow = new gamemode_pvp(gamemode_scale, dbConnector);
            //}
            if(source == equipmentButton){
                window_cleaner(secondaryWindow);
                secondaryWindow = new gamemode_equipment(gamemodeScale, dbConnector);
            }
            if(source == clanButton){
                window_cleaner(secondaryWindow);
                secondaryWindow = new gamemode_clan(gamemodeScale, dbConnector);
            }
            if(source == questButton){
                window_cleaner(secondaryWindow);
                secondaryWindow = new gamemode_quest(gamemodeScale, dbConnector);
            }
            //Create/delete button actions
            if(source == createButton){
                window_cleaner(secondaryWindow);
                if (createPlayer.isSelected()){
                    System.out.println("Open PC window");
                    secondaryWindow = new window_pc(createScale, dbConnector);
                }
                else if(createMonster.isSelected()){
                    System.out.println("Open monster window");
                    secondaryWindow = new window_monster(createScale, dbConnector);
                }
                else if(createClan.isSelected()){
                    System.out.println("Open clan window");
                    secondaryWindow = new window_clan(createScale, dbConnector);
                }
                else if(createQuest.isSelected()){
                    System.out.println("Open quest window");
                    secondaryWindow = new window_quest(createScale, dbConnector);
                }
                else if(createItem.isSelected()){
                    System.out.println("Open item window");
                    secondaryWindow = new window_item(createScale, dbConnector);
                }
                else if(createRace.isSelected()){
                    System.out.println("Open race window");
                    secondaryWindow = new window_race(createScale, dbConnector);
                }
                else if(createProfession.isSelected()){
                    System.out.println("Open profession window");
                    secondaryWindow = new window_profession(createScale, dbConnector);
                }
            }
        }
        
        if(source == exitButton) {
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
