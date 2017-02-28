package com.xiuye.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.xiuye.jdbc.Connections;


public class JDBCConnectionUtil {

	public static Connection getConnection(String driveClassName,String url,String user,String password) throws ClassNotFoundException, SQLException {

		Class.forName(driveClassName);
		Connection conn = DriverManager.getConnection(url,user,password);
		Connections.addConnection(conn);
		return conn;

	}
	public static Connection getConnection(String driveClassName,String url) throws ClassNotFoundException, SQLException {

		Class.forName(driveClassName);
		Connection conn = DriverManager.getConnection(url);
		Connections.addConnection(conn);
		return conn;

	}

	public static PreparedStatement preparedStatement(String sql,String driveClassName,String url,String user,String password) throws ClassNotFoundException, SQLException{

		Connection connection = getConnection(driveClassName,url,user,password);
		return connection.prepareStatement(sql);
	}
	public static PreparedStatement preparedStatement(String sql,String driveClassName,String url) throws ClassNotFoundException, SQLException{

		Connection connection = getConnection(driveClassName,url);
		return connection.prepareStatement(sql);
	}




}
