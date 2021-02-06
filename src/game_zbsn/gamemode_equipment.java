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
    private int window_height,window_width;
    private DBConnector dbConnector;
    
    private List<String> ItemData = new ArrayList<>();
    private List<String> EquipmentData = new ArrayList<>();
    private List<String> PlayerData = new ArrayList<>();

    private JLabel lItems, lPlayers, lEquipment, lStats, lWeight, lCapacity, lInfo;
    private JButton bPick, bDrop, bUpgradeEq;
    private JList listItems = new JList(), listEquipment = new JList(EquipmentData.toArray()), 
                            listPlayers = new JList(PlayerData.toArray());
    private JScrollPane scrollItems, scrollPlayers, scrollEquipments;
    
    
    public gamemode_equipment(int w, DBConnector dbConnector){
        this.window_height = w; this.window_width = w;
        this.dbConnector = dbConnector;
        setSize(this.window_width, this.window_height);
        setTitle("gamemode equipment");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setVisible(true);
        
        getData();
        initButtons();
        updateable();
        initLists();
    }
    public void initButtons(){
        lInfo = new JLabel("");
        lInfo.setBounds(20, 400, 430, 20);
        lInfo.setOpaque(true);
        lInfo.setBackground(Color.WHITE);
        add(lInfo);
        
        bPick = new JButton("Pick item");
        bPick.setBounds(310, 100, 100, 30);
        bPick.addActionListener(this);
        add(bPick);
        
        bDrop = new JButton("Drop item");
        bDrop.setBounds(310, 290, 120, 30);
        bDrop.addActionListener(this);
        add(bDrop);
        
        bUpgradeEq = new JButton("Upgrade Eq");
        bUpgradeEq.setBounds(310, 330, 120, 30);
        bUpgradeEq.addActionListener(this);
        add(bUpgradeEq);
    }
    public void getData(){
        try{
            ItemData = dbConnector.getItems();
            PlayerData = dbConnector.getPlayers();
            listItems.setListData(ItemData.toArray());
            listPlayers.setListData(PlayerData.toArray());
        }catch(SQLException ex){}
    }
    public void updateable(){
        lEquipment = new JLabel("Inventory: Name - weight - profession - count");
        lEquipment.setBounds(30, 215, 300, 20);
        add(lEquipment);
        listEquipment.setListData(EquipmentData.toArray());
        listEquipment.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listEquipment.setLayoutOrientation(VERTICAL);
        listEquipment.setVisibleRowCount(-1);
        add(listEquipment);
        scrollEquipments = new JScrollPane(listEquipment);
        scrollEquipments.setPreferredSize(new Dimension(250, 100));
        scrollEquipments.setBounds(30, 240, 250, 150);
        add(scrollEquipments);
    }
    public void initLists(){
        lWeight = new JLabel("Weight:");
        lWeight.setBounds(270, 55, 200, 20);
        add(lWeight);
        
        lCapacity = new JLabel("Inventory capacity:");
        lCapacity.setBounds(300, 230, 200, 20);
        add(lCapacity);
        listPlayers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listPlayers.setLayoutOrientation(VERTICAL);
        listPlayers.setVisibleRowCount(1);
        listPlayers.addListSelectionListener((e)->{
            JList list = (JList) e.getSource();
            String selected = list.getSelectedValue().toString();
            int id = dbConnector.getId(selected);
            float weight=0;
            try{
                EquipmentData = dbConnector.getEquipment(id);
                listEquipment.setListData(EquipmentData.toArray());
            }catch(SQLException ex){};
            try{
                Statement stmt = dbConnector.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery("select capacity_eq from equipments where owner_id ='"+id+"'");
                while(rs.next()){
                    weight = rs.getFloat("capacity_eq");}
                rs.close();
                stmt.close();
                lCapacity.setText("Inventory capacity: "+Float.toString(weight));
            }catch(SQLException ex){};
        });
        
        add(listPlayers);
        lPlayers = new JLabel("Players:");
        lPlayers.setBounds(30, 30, 100, 20);
        add(lPlayers);
        scrollPlayers = new JScrollPane(listPlayers);
        scrollPlayers.setPreferredSize(new Dimension(250, 100));
        scrollPlayers.setBounds(30, 55, 100, 150);
        add(scrollPlayers);
        
        listItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listItems.setLayoutOrientation(VERTICAL);
        listItems.setVisibleRowCount(1);
        listItems.addListSelectionListener((e)->{
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
                lWeight.setText("Weight: "+Float.toString(w));
                rs.close();
                stmt.close();
            }catch(SQLException ex){}
        });
        
        add(listItems);
        lItems = new JLabel("Items:");
        lItems.setBounds(140, 30, 100, 20);
        add(lItems);
        scrollItems = new JScrollPane(listItems);
        scrollItems.setPreferredSize(new Dimension(250, 100));
        scrollItems.setBounds(140, 55, 100, 150);
        add(scrollItems);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == bDrop){
            String result="";
            if(listPlayers.getSelectedValue()==null || listEquipment.getSelectedValue()==null)
                System.out.println("drop missing data");
            else{
                try{
                    int id = dbConnector.getId(listPlayers.getSelectedValue().toString());
                    String name = dbConnector.getItemName(listEquipment.getSelectedValue().toString());
                    result = dbConnector.dropItem(id, name);
                    lInfo.setText(result);
                    System.out.println(result);
                
                }catch(SQLException ex){
                    lInfo.setText("Something gone wrong with dropping");
                    lInfo.setForeground(Color.red);}
            }
        }
        if(source == bPick){
            String result="";
            if(listPlayers.getSelectedValue()==null || listItems.getSelectedValue()==null){
                System.out.println("pick missing data");}
            else{
                try{
                    int id = dbConnector.getId(listPlayers.getSelectedValue().toString());
                    String name = listItems.getSelectedValue().toString();
                    result = dbConnector.pickUpItem(id, name);
                    System.out.println(result);
                    lInfo.setText(result);
                }catch(SQLException ex){
                    lInfo.setText("Something gone wrong with picking");
                    lInfo.setForeground(Color.red);}
            }
        }
        if(source == bUpgradeEq){
            if(listPlayers.getSelectedValue()==null)
                System.out.println("upgradeeq missing data");
            else{
                try{
                    int id = dbConnector.getId(listPlayers.getSelectedValue().toString());
                    PreparedStatement stmt = dbConnector.getConnection().prepareStatement(
                                        "update equipments set capacity_eq = capacity_eq +10 where owner_id=?");
                    stmt.setInt(1, id);
                    stmt.execute();
                    stmt.close();
                    lInfo.setText("Equipment successfully upgraded +10");
                }catch(SQLException ex){
                    lInfo.setText("Something gone wrong with upgrading");
                    lInfo.setForeground(Color.red);}
            }
        }
        getData();
    }
}
