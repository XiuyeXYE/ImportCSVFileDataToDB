package com.xiuye.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.swing.JOptionPane;

import com.xiuye.jdbc.ColumnFormatCondition;
import com.xiuye.jdbc.constant.JDBCTypeConstant;

public class DBConfigUtil {

	private static Properties tableP = null;
	private static Properties jdbcP = null;
	static {
		try {
			tableP = new Properties();
			tableP.load(DBConfigUtil.class.getClassLoader().getResourceAsStream("tables_config.properties"));

			jdbcP = new Properties();
			jdbcP.load(DBConfigUtil.class.getClassLoader().getResourceAsStream("jdbc.properties"));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Collection<?> allTablesAliases() {

		Collection<?> values = null;
		values = tableP.values();

		return values;
	}

	public static Set<Entry<Object, Object>> tables() {
		return tableP.entrySet();
	}

	public static String user() {
		return jdbcP.getProperty("user", "scott");
	}

	public static String password() {
		return jdbcP.getProperty("password", "tiger");
	}

	public static String url() {
		return jdbcP.getProperty("url", "jdbc:oracle:thin:@localhost:1521:orcl");
	}

	public static String driverClass() {
		return jdbcP.getProperty("driverClass", "oracle.jdbc.OracleDriver");
	}

	private static Properties loadProperties(String fileName) {
		Properties p = null;
		try {
			p = new Properties();
			p.load(DBConfigUtil.class.getClassLoader().getResourceAsStream(fileName));

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "没有此表的配置文件,请配置...!", "错误", JOptionPane.ERROR_MESSAGE);
			// e.printStackTrace();
		}
		return p;
	}

	public static int columnsCount(String tableName) {
		Properties p = loadProperties("tables" + File.separator + tableName + ".properties");

		return p.size();
	}

	private static XYProperties loadXYProperties(String fileName) {
		XYProperties p = new XYProperties();
		// System.out.println(fileName);
		p.load(DBConfigUtil.class.getClassLoader().getResourceAsStream(fileName));
		return p;
	}

	public static String generateInsertSQL(String tableName) {
		XYProperties p = loadXYProperties("tables" + File.separator + tableName + ".properties");
		int columnsNum = p.size();
		String sql = "insert into " + tableName + "(";
		List<com.xiuye.util.XYProperties.Entry<String, String>> list = p.entryList();
		for (com.xiuye.util.XYProperties.Entry<String, String> entry : list) {
			sql += entry.getKey() + ",";
		}
		sql = sql.substring(0, sql.lastIndexOf(","));
		sql += ") values(";
		for (int i = 0; i < columnsNum; i++) {
			sql += "?,";
		}
		sql = sql.substring(0, sql.lastIndexOf(","));
		sql += ")";
		return sql;
	}

	public static List<ColumnFormatCondition> columnsFormatConditions(String tableName) {
		XYProperties p = loadXYProperties("tables" + File.separator + tableName + ".properties");
		List<com.xiuye.util.XYProperties.Entry<String, String>> list = p.entryList();
		List<ColumnFormatCondition> cfcs = new ArrayList<>();

		for (com.xiuye.util.XYProperties.Entry<String, String> entry : list) {

			ColumnFormatCondition cfc = new ColumnFormatCondition();
			cfc.setColumnName(entry.getKey());
			String value = entry.getValue();

			// System.out.println("Value := " + value);
			checkCondition(value, cfcs, cfc);

		}

		return cfcs;
	}

	private static void checkCondition(String value, List<ColumnFormatCondition> cfcs, ColumnFormatCondition cfc) {

		if (value != null && !value.isEmpty()) {
			if (value.contains("--")) {
				String[] ss = value.split("--");
				// System.out.println(ss[0]);
				// System.out.println(ss[1]);
				switch (ss[0]) {
				case JDBCTypeConstant.DATE:
					cfc.setDate(true);
					if (ss.length > 1) {
						cfc.setFormat(ss[1]);
					}
					break;
				case JDBCTypeConstant.DOUBLE:
					cfc.setDouble(true);
					break;
				case JDBCTypeConstant.FLOAT:
					cfc.setFloat(true);
					break;
				case JDBCTypeConstant.INT:
					cfc.setInt(true);
					break;
				case JDBCTypeConstant.LONG:
					cfc.setLong(true);
					break;
				case JDBCTypeConstant.STRING:
					cfc.setString(true);
					if (ss.length > 1) {
						cfc.setNullContent(ss[1]);
					}
					break;

				}
			} else {

				// System.out.println(value);
				switch (value) {

				case JDBCTypeConstant.DATE:
					cfc.setDate(true);

					break;
				case JDBCTypeConstant.DOUBLE:
					cfc.setDouble(true);
					break;
				case JDBCTypeConstant.FLOAT:
					cfc.setFloat(true);
					break;
				case JDBCTypeConstant.INT:
					cfc.setInt(true);
					break;
				case JDBCTypeConstant.LONG:
					cfc.setLong(true);
					break;
				case JDBCTypeConstant.STRING:
					cfc.setString(true);

					break;

				}
			}
		}

		cfcs.add(cfc);

	}

	public static String generateTruncateTableSQL(String tableName) {

		return "truncate table "+tableName;
	}

}
