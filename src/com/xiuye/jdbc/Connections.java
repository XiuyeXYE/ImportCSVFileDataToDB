package com.xiuye.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Connections {

	private static final List<Connection> conns = new ArrayList<>();

	public static void addConnection(Connection conn){
		conns.add(conn);
	}
	public static void closeAllConnections(){
		for(Connection conn : conns){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
	public static void clear(){
		closeAllConnections();
		conns.clear();
	}
}
