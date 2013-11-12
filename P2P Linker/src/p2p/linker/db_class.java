/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package p2p.linker;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 *
 * @author Toshiba
 */
public class db_class {
 
    private String server = "";
    private String db = "";
    private String db_user = "";
    private String db_pass = "";
    private Connection conn;
    private Statement st;    
    private ResultSet rs;
    
    public String[] check_login(String username,String password){
        String[] ret = new String[2];
        
        try {
           
            //String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);  //paste data
            
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://"+server+"/"+db,db_user,db_pass);
            st = conn.createStatement();
            String query = "SELECT * FROM users WHERE username = '"+username+"' AND password = md5('"+password+"')";
            rs = st.executeQuery(query);
            
            if(!rs.next()){
                showMessageDialog(null,"Login incorrect!\nPleas try again");
            }else{
                ret[0] = rs.getString("username");
                ret[1] = rs.getString("id");
            }
            
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(index.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return ret;
    }
    
    public ResultSet get_Links(String id){
        try {
            
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://"+server+"/"+db,db_user,db_pass);
            st = conn.createStatement();
            
            String query = "SELECT * FROM links WHERE too = '"+id+"' AND open = '0'";
            rs = st.executeQuery(query);
            
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(index.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
    
    public void delete_Link(String us_id,String link){
       try {
           
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://"+server+"/"+db,db_user,db_pass);
            st = conn.createStatement();
            
            String query = "DELETE FROM links WHERE too = '" + us_id + "' AND link = '" + link + "'";
            if(st.execute(query)){
                showMessageDialog(null,"Link deleted successfully!");
            }else{
                showMessageDialog(null,"There was an error!\nPleas try again");
            }
            
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(index.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    ResultSet getFriends(String user_id) {
       try {  
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://"+server+"/"+db,db_user,db_pass);
            st = conn.createStatement();
            
            String query = "SELECT u.username,u.id,f.uid1,f.uid2 FROM users AS u \n" +
                           "JOIN friends AS f ON (f.uid1 = u.id OR f.uid2 = u.id)\n" +
                           "WHERE f.uid1 = 1 OR f.uid2 = 1" ;
            rs = st.executeQuery(query); 
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(index.class.getName()).log(Level.SEVERE, null, ex);
        }
       return rs;
    }
    
}
