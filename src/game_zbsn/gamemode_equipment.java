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
import javax.swing.JFrame;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import static javax.swing.JList.VERTICAL;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 *
 * @author User
 */
public class gamemode_equipment extends JFrame implements ActionListener{
    private int windowHeight, windowWidth;
    private DBConnector dbConnector;
    
    private List<String> itemData = new ArrayList<>();
    private List<String> equipmentData = new ArrayList<>();
    private List<String> playerData = new ArrayList<>();

    private JLabel itemsLabel, playersLabel, equipmentLabel, statsLabel, weightLabel, capacityLabel, infoLabel;
    private JButton pickButton, dropButton, upgradeEquipmentButton;
    private JList itemsList = new JList(), equipmentList = new JList(equipmentData.toArray()), 
                            playersList = new JList(playerData.toArray());
    private JScrollPane scrollItems, scrollPlayers, scrollEquipments;
    
    
    public gamemode_equipment(int w, DBConnector dbConnector){
        this.windowHeight = w; this.windowWidth = w;
        this.dbConnector = dbConnector;
        setSize(this.windowWidth, this.windowHeight);
        setTitle("gamemode equipment");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setVisible(true);
        setResizable(false);
        
        getData();
        initButtons();
        updateable();
        initLists();
    }
    public void initButtons(){
        infoLabel = new JLabel("");
        infoLabel.setBounds(20, 400, 430, 20);
        infoLabel.setOpaque(true);
        infoLabel.setBackground(Color.WHITE);
        add(infoLabel);
        
        pickButton = new JButton("Pick item");
        pickButton.setBounds(310, 100, 100, 30);
        pickButton.addActionListener(this);
        add(pickButton);
        
        dropButton = new JButton("Drop item");
        dropButton.setBounds(310, 290, 120, 30);
        dropButton.addActionListener(this);
        add(dropButton);
        
        upgradeEquipmentButton = new JButton("Upgrade Eq");
        upgradeEquipmentButton.setBounds(310, 330, 120, 30);
        upgradeEquipmentButton.addActionListener(this);
        add(upgradeEquipmentButton);
    }
    public void getData(){
        try{
            itemData = dbConnector.getItems();
            playerData = dbConnector.getPlayers();
            itemsList.setListData(itemData.toArray());
            playersList.setListData(playerData.toArray());
        }catch(SQLException ex){}
    }
    public void updateable(){
        equipmentLabel = new JLabel("Inventory: Name - weight - profession - count");
        equipmentLabel.setBounds(30, 215, 300, 20);
        add(equipmentLabel);
        equipmentList.setListData(equipmentData.toArray());
        equipmentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        equipmentList.setLayoutOrientation(VERTICAL);
        equipmentList.setVisibleRowCount(-1);
        add(equipmentList);
        scrollEquipments = new JScrollPane(equipmentList);
        scrollEquipments.setPreferredSize(new Dimension(250, 100));
        scrollEquipments.setBounds(30, 240, 250, 150);
        add(scrollEquipments);
    }
    public void initLists(){
        weightLabel = new JLabel("Weight:");
        weightLabel.setBounds(270, 55, 200, 20);
        add(weightLabel);
        
        capacityLabel = new JLabel("Inventory capacity:");
        capacityLabel.setBounds(300, 230, 200, 20);
        add(capacityLabel);
        playersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        playersList.setLayoutOrientation(VERTICAL);
        playersList.setVisibleRowCount(1);
        playersList.addListSelectionListener((e)->{
            JList list = (JList) e.getSource();
            String selected = list.getSelectedValue().toString();
            int id = dbConnector.getId(selected);
            float weight = 0;
            try{
                equipmentData = dbConnector.getEquipment(id);
                equipmentList.setListData(equipmentData.toArray());
            }catch(SQLException ex){};
            try{
                Statement stmt = dbConnector.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery("select capacity_eq from equipments where owner_id =\'" + id + "\'");
                while(rs.next()){
                    weight = rs.getFloat("capacity_eq");}
                rs.close();
                stmt.close();
                capacityLabel.setText("Inventory capacity: " + Float.toString(weight));
            }catch(SQLException ex){};
        });
        
        add(playersList);
        playersLabel = new JLabel("Players:");
        playersLabel.setBounds(30, 30, 100, 20);
        add(playersLabel);
        scrollPlayers = new JScrollPane(playersList);
        scrollPlayers.setPreferredSize(new Dimension(250, 100));
        scrollPlayers.setBounds(30, 55, 100, 150);
        add(scrollPlayers);
        
        itemsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemsList.setLayoutOrientation(VERTICAL);
        itemsList.setVisibleRowCount(1);
        itemsList.addListSelectionListener((e)->{
            JList list = (JList) e.getSource();
            String selected = list.getSelectedValue().toString();
            float w=0;
            try{
                PreparedStatement stmt = dbConnector.getConnection().prepareStatement(
                "select weight from items where i_name = ?");
                stmt.setString(1, selected);
                ResultSet rs = stmt.executeQuery();
                while(rs.next())
                    w = rs.getFloat("weight");
                weightLabel.setText("Weight: "+Float.toString(w));
                rs.close();
                stmt.close();
            }catch(SQLException ex){}
        });
        
        add(itemsList);
        itemsLabel = new JLabel("Items:");
        itemsLabel.setBounds(140, 30, 100, 20);
        add(itemsLabel);
        scrollItems = new JScrollPane(itemsList);
        scrollItems.setPreferredSize(new Dimension(250, 100));
        scrollItems.setBounds(140, 55, 100, 150);
        add(scrollItems);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == dropButton){
            String result="";
            if(playersList.getSelectedValue()==null || equipmentList.getSelectedValue()==null)
                System.out.println("drop missing data");
            else{
                try{
                    int id = dbConnector.getId(playersList.getSelectedValue().toString());
                    String name = dbConnector.getItemName(equipmentList.getSelectedValue().toString());
                    result = dbConnector.dropItem(id, name);
                    infoLabel.setText(result);
                    System.out.println(result);
                
                }catch(SQLException ex){
                    infoLabel.setText("Something gone wrong with dropping");
                    infoLabel.setForeground(Color.red);}
            }
        }
        if(source == pickButton){
            String result="";
            if(playersList.getSelectedValue()==null || itemsList.getSelectedValue()==null){
                System.out.println("pick missing data");}
            else{
                try{
                    int id = dbConnector.getId(playersList.getSelectedValue().toString());
                    String name = itemsList.getSelectedValue().toString();
                    result = dbConnector.pickUpItem(id, name);
                    System.out.println(result);
                    infoLabel.setText(result);
                }catch(SQLException ex){
                    infoLabel.setText("Something gone wrong with picking");
                    infoLabel.setForeground(Color.red);}
            }
        }
        if(source == upgradeEquipmentButton){
            if(playersList.getSelectedValue()==null)
                System.out.println("upgradeeq missing data");
            else{
                try{
                    int id = dbConnector.getId(playersList.getSelectedValue().toString());
                    PreparedStatement stmt = dbConnector.getConnection().prepareStatement(
                                        "update equipments set capacity_eq = capacity_eq +10 where owner_id=?");
                    stmt.setInt(1, id);
                    stmt.execute();
                    stmt.close();
                    infoLabel.setText("Equipment successfully upgraded +10");
                }catch(SQLException ex){
                    infoLabel.setText("Something gone wrong with upgrading");
                    infoLabel.setForeground(Color.red);}
            }
        }
        getData();
    }
}
