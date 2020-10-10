package lixco.com.vantay;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class loadvantay {
	public static List<template> findAll(){
		List<template> ltl = new ArrayList<>();
		Connection connection = null;
		Statement statement = null;
		try {
			connection = DriverManager.getConnection("jdbc:sqlserver://192.168.1.236;databaseName=MITACOSQL", "sa", "LIXCO@admin2016");
			String sql = "select template.*, manhanvien, tennhanvien from template, nhanvien where template.machamcong = nhanvien.machamcong";
			statement = connection.createStatement();
			ResultSet res = statement.executeQuery(sql);
			while (res.next()) {                
                template tl = new template(res.getInt("MaChamCong"), 
                        res.getInt("FingerID"), res.getInt("Flag"), 
                        res.getString("FingerTemplate"),
                        res.getString("MaNhanVien"),
                        res.getString("TenNhanvien"));
                		
                ltl.add(tl);
            }
        } catch (SQLException ex) {
            Logger.getLogger(loadvantay.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(loadvantay.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(loadvantay.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
		}
		return ltl;
	}
}
