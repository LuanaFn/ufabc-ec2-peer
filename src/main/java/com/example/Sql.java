package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Sql  {
	
	public Connection con;
	public Sql() throws Exception {
		this.con = createConnection();
	}
	
	 public static Connection createConnection() throws Exception{
	  	         
	     return DriverManager.getConnection("jdbc:mysql://localhost:3306/peer", "root", "");
	         
	    }
	 
	 public void executaInsert(String query) throws SQLException {
		PreparedStatement ps = con.prepareStatement(query);
		ps.execute();
	 }
	 public ResultSet executaSelect(String query) throws SQLException {
			PreparedStatement ps = con.prepareStatement(query);
			   ResultSet rs =ps.executeQuery();
			return rs;
			   
		 }

}
