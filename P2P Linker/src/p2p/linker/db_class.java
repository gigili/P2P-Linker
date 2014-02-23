package p2p.linker;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;
        

/**
 *
 * @author Igor Ilic
 */
public class db_class {
 
    private final String server = "";
    private final String db = "";
    private final String db_user = "";
    private final String db_pass = "";
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
                ret = null;
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
                           "WHERE f.uid1 = '"+user_id+"' OR f.uid2 = '"+user_id+"'" ;
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
   
    public void addFriend(String user,String user_id) {
       Integer add_user_id;
       Integer us_id = Integer.parseInt(user_id);
        try {  
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://"+server+"/"+db,db_user,db_pass);
            st = conn.createStatement();
            
            String query = "SELECT * FROM users WHERE username = '" + user + "'";
            rs = st.executeQuery(query);
            if(!rs.first()){
                showMessageDialog(null,"Unable to find the requested username: " + user);
            }
            
            if(rs.first()){
                add_user_id = rs.getInt("id");
                Statement st2 = conn.createStatement();
                String q2 = "INSERT INTO friend_request (`from`,`to`) VALUES ('" + us_id + "','" + add_user_id + "')";
                st2.execute(q2);
                showMessageDialog(null, "Friend added successfully!");
            }
            conn.close();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(index.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    public void deleteFriend(String user_id, String del_user) {
        try {  
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://"+server+"/"+db,db_user,db_pass);
            st = conn.createStatement();
            
            String query = "SELECT * FROM users WHERE username = '" + del_user + "'";
            rs = st.executeQuery(query);
            if(!rs.first()){
                showMessageDialog(null,"Unable to find the requested username: " + del_user);
            }
            
            if(rs.first()){
                Integer del_user_id = rs.getInt("id");
                Integer my_user_id = Integer.parseInt(user_id);
                Statement st2 = conn.createStatement();
                String q2 = "DELETE FROM friends WHERE (uid1 = '" + my_user_id + "' AND uid2 = '" + del_user_id + "') OR (uid1 = '" + del_user_id + "' AND uid2 = '" + my_user_id + "')";
                st2.execute(q2);
                showMessageDialog(null, "Friend removed successfully!");
            }
            conn.close();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(index.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public void acceptFriend(String user_id,String username){
      try {  
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://"+server+"/"+db,db_user,db_pass);
            st = conn.createStatement();
            
            String query = "SELECT * FROM users WHERE username = '" + username + "'";
            rs = st.executeQuery(query);
            if(!rs.first()){
                showMessageDialog(null,"Unable to find the requested username: " + username);
            }
            
            if(rs.first()){
                Integer acc_user_id = rs.getInt("id");
                Integer my_user_id = Integer.parseInt(user_id);
                Statement st2 = conn.createStatement();
                String q2 = "INSERT INTO friends (uid1,uid2) VALUES ('" + my_user_id + "','" + acc_user_id + "')";
                st2.execute(q2);
                               
                String q3 = "DELETE FROM friend_request WHERE `to` = '" + user_id + "' AND `from` = '" + acc_user_id + "'";
                Statement st3 = conn.createStatement();
                st3.execute(q3);
                
                showMessageDialog(null, "Friend request accepted successfully!");
            }
            conn.close();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(index.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public ResultSet getRequests(String user_id) throws SQLException{
        try {  
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://"+server+"/"+db,db_user,db_pass);
            st = conn.createStatement();
            
            String query = "SELECT f.`from`,f.to,u.username FROM friend_request AS f " +
                           "JOIN users AS u ON f.from = u.id " +
                           "WHERE f.to = " + user_id + "";
            rs = st.executeQuery(query);
                       
            return rs;
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(index.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        } 
        conn.close();
        return rs;
    }
    
    public void denyRequest(String user_id,String username){
        try {  
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://"+server+"/"+db,db_user,db_pass);
            st = conn.createStatement();
            
            String query = "SELECT * FROM users WHERE username = '" + username + "'";
            rs = st.executeQuery(query);
            if(!rs.first()){
                showMessageDialog(null,"Unable to find the requested username: " + username);
            }
            
            if(rs.first()){
                Integer deny_user_id = rs.getInt("id");
                Integer my_user_id = Integer.parseInt(user_id);

                String q3 = "DELETE FROM friend_request WHERE `to` = '" + my_user_id + "' AND `from` = '" + deny_user_id + "'";
                Statement st3 = conn.createStatement();
                st3.execute(q3);
                
                showMessageDialog(null, "Friend request denied!");
            }
            conn.close();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(index.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    public void register(String f_name, String l_name, String email, String username, String password) {
        String full_name = f_name + " " + l_name;
        if(!"".equals(f_name) && !"".equals(l_name) && !"".equals(email) && !"".equals(username) && !"".equals(password) && check_email(email)){
            try {  
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://"+server+"/"+db,db_user,db_pass);
            st = conn.createStatement();
            
            String query = "INSERT INTO users (`name`,`email`,`username`,`password`) VALUES ('" + full_name + "','" + email + "','" + username + "',md5('"+password+"'))";
            if(st.execute(query)){
                showMessageDialog(null, "Account created successfully");
            }
            conn.close();
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(index.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }else{
            showMessageDialog(null,"All fields are required!");
        }
    }
    
    public Boolean check_email(String email){
        String[] split = email.split("@");
        if(!"".equals(split[1])){
            return true;
        }else{
            showMessageDialog(null, "Invalid email!","P2P Linker",ERROR_MESSAGE);
            return false;
        }
    }
    
    public ResultSet get_friend_requests_num(String id) throws SQLException{
       try {  
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://"+server+"/"+db,db_user,db_pass);
            st = conn.createStatement();
            
            String query = "SELECT COUNT(*) AS cnt FROM friend_request  WHERE `to` = '" + id + "'";
            rs = st.executeQuery(query);           
            return rs;
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(index.class.getName()).log(Level.SEVERE, null, ex);
        } 
        conn.close();
        return rs;
    }

    public void sendMessage(String name, String email, String subject, String message){
        try {  
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://"+server+"/"+db,db_user,db_pass);
            st = conn.createStatement();
            
            String query = "INSERT INTO feedback (`name`,`email`,`subject`,`message`) VALUES ('" + name + "','" + email + "','" + subject + "','"+ message +"')";
            if(!st.execute(query)){
                showMessageDialog(null, "Message sent successfully!");
            }
                        
            conn.close();
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(index.class.getName()).log(Level.SEVERE, null, ex);
            } 
    }
}