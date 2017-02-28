package com.xiuye.handler;


public class CsvFileHandler {


	public static String[] lineStr2ColumnsData(String line){
		line = line.replace("\"", "");
		String []columnDatas = line.split(",");
		return columnDatas;
	}


}
