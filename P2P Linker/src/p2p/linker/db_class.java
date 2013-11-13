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
 * @author Igor Ilic
 */
public class db_class {
 
    private String server = "";
    private String db = "";
    private String db_user = "";
    private String db_pass = "";
    private Connection conn;
    private Statement st;    
    private ResultSet rs;
    
    public String[] check_login(String username,String password) throws SQLException{
        String[] ret = new String[2];
        
        try {            
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
        conn.close();
        return ret;
    }
    
    public ResultSet get_Links(String username) throws SQLException{
        try {
            
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://"+server+"/"+db,db_user,db_pass);
            st = conn.createStatement();
            
            String query = "SELECT * FROM links WHERE too = '"+username+"' AND open = '0'";
            rs = st.executeQuery(query);
            return rs;
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(index.class.getName()).log(Level.SEVERE, null, ex);
        }
        conn.close();
        return rs;
    }
    
    public void delete_Link(String username,String link) throws SQLException{
       try {
           
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://"+server+"/"+db,db_user,db_pass);
            st = conn.createStatement();
            
            String query = "DELETE FROM links WHERE too = '" + username + "' AND link = '" + link + "'";
            st.execute(query);
            showMessageDialog(null,"Link deleted successfully!");            
            get_Links(username);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(index.class.getName()).log(Level.SEVERE, null, ex);
        }
       conn.close();
    }

    ResultSet getFriends(String user_id) throws SQLException {
       try {  
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://"+server+"/"+db,db_user,db_pass);
            st = conn.createStatement();
            
            String query = "SELECT u.username,u.id,f.uid1,f.uid2 FROM users AS u \n" +
                           "JOIN friends AS f ON (f.uid1 = u.id OR f.uid2 = u.id)\n" +
                           "WHERE f.uid1 = 1 OR f.uid2 = 1" ;
            rs = st.executeQuery(query); 
            return rs;
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(index.class.getName()).log(Level.SEVERE, null, ex);
        }
       conn.close();
       return rs;
    }
    
    public void send_link(String username,String user,String link) throws SQLException{
       try {  
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://"+server+"/"+db,db_user,db_pass);
            st = conn.createStatement();
            
            String query = "INSERT INTO links (`from`,`too`,`link`) VALUES ('"+username+"','"+user+"','"+link+"')" ;
            st.execute(query); 
            showMessageDialog(null,"Link sent successfully!");
            conn.close();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(index.class.getName()).log(Level.SEVERE, null, ex);
        } 
       conn.close();
    }
    
    public void updateLinks(Integer x,String username) throws SQLException{
        try {  
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://"+server+"/"+db,db_user,db_pass);
            st = conn.createStatement();
            
            String query = "UPDATE links SET open = '" + x + "' WHERE too = '" + username + "'" ;
            st.execute(query);
            conn.close();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(index.class.getName()).log(Level.SEVERE, null, ex);
        } 
        conn.close();
    }
   

    
}
