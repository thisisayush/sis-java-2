/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package luciferproject;

import java.sql.*;
import java.util.ArrayList;
/**
 *
 * @author Administrator
 */

class ConnectionConfig{
    public final String url, user, pass;
    
    ConnectionConfig(String url, String user, String pass){
        this.url = url;
        this.user = user;
        this.pass = pass;
    }
}

class StudentData{
    public final int id;
    public final String name, enroll;
    
    StudentData(int id, String name, String enroll){
        this.id = id;
        this.name = name;
        this.enroll = enroll;
    }
}

public class DB {
    
    Connection con;
    ConnectionConfig cc = new ConnectionConfig("", "", "");
    
    private final String table_name;
    private final String insert_query, fetch_query;
    public final int COL_id, COL_name, COL_enroll;
    
    PreparedStatement insert_st, update_st, fetch_st;
    
    private final String driver_name;
    
    public DB() throws ClassNotFoundException{
        
        this.driver_name = "";
        this.table_name = "";
        
        Class.forName(this.driver_name);
        
        this.COL_id = 1;
        this.COL_name = 2;
        this.COL_enroll = 3;
        
        this.insert_query = "INSERT INTO " + this.table_name + " VALUES(?,?,?)";
        this.fetch_query = "SELECT * FROM " + this.table_name;
    }
    
    public void connect(){
        try{
            this.con = DriverManager.getConnection(cc.url, cc.user, cc.pass);
            this.insert_st = con.prepareStatement(this.insert_query);
            this.fetch_st = con.prepareStatement(this.fetch_query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        }catch(SQLException e){
            System.out.println("Unable to Connect to Server! Err: " + e.toString());
        }
    }
    
    public int insert(String name, String enroll) throws SQLException{
        int next_id = get_next_id();
        this.insert_st.setInt(this.COL_id, next_id);
        this.insert_st.setString(this.COL_name, name);
        this.insert_st.setString(this.COL_enroll, enroll);
        this.insert_st.executeUpdate();
        return next_id;
    }
    
    public ArrayList<StudentData> fetchAll() throws SQLException{
        ResultSet rs = this.fetch_st.executeQuery();
        ArrayList<StudentData> s = new ArrayList<>();
        
        while(rs.next()){
            s.add(
                    new StudentData(
                            rs.getInt(this.COL_id), 
                            rs.getString(this.COL_name), 
                            rs.getString(this.COL_enroll)
                    )
            );
        }
        return s;
    }
    
    public StudentData fetch(int id) throws SQLException, RecordNotFoundException{
        ResultSet rs = this.fetch_st.executeQuery();
        
        if(!rs.absolute(id)){
            throw new RecordNotFoundException("Record with id " + Integer.toString(id) + " not found!");
        }
        
        return new StudentData(
                rs.getInt(this.COL_id), 
                rs.getString(this.COL_name), 
                rs.getString(this.COL_enroll)
        );
    }
    
    private int get_next_id() throws SQLException{
        ResultSet rs = this.fetch_st.executeQuery();
        rs.last();
        return rs.getInt(1) + 1;
    }

    public class RecordNotFoundException extends Exception{
        public RecordNotFoundException(String s) {
            super(s);
        }
    }
    
}
