package com.xiuye.jdbc;

public class ColumnFormatCondition {

	private boolean isLong;
	private boolean isInt;
	private boolean isDate;
	private boolean isDouble;
	private boolean isFloat;
	private boolean isString;

	private String columnName;

	private String format;
	private String nullContent;


	public String getColumnName() {
		return columnName;
	}


	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}


	public String getNullContent() {
		return nullContent;
	}


	public void setNullContent(String nullContent) {
		this.nullContent = nullContent;
	}


	public boolean isLong() {
		return isLong;
	}


	public void setLong(boolean isLong) {
		this.isLong = isLong;
	}


	public boolean isInt() {
		return isInt;
	}


	public void setInt(boolean isInt) {
		this.isInt = isInt;
	}


	public boolean isDate() {
		return isDate;
	}


	public void setDate(boolean isDate) {
		this.isDate = isDate;
	}


	public boolean isDouble() {
		return isDouble;
	}


	public void setDouble(boolean isDouble) {
		this.isDouble = isDouble;
	}


	public boolean isFloat() {
		return isFloat;
	}


	public void setFloat(boolean isFloat) {
		this.isFloat = isFloat;
	}


	public boolean isString() {
		return isString;
	}


	public void setString(boolean isString) {
		this.isString = isString;
	}


	public String getFormat() {
		return format;
	}


	public void setFormat(String format) {
		this.format = format;
	}


	@Override
	public String toString() {
		return "ColumnFormatCondition [isLong=" + isLong + ", isInt=" + isInt + ", isDate=" + isDate + ", isDouble="
				+ isDouble + ", isFloat=" + isFloat + ", isString=" + isString + ", columnName=" + columnName
				+ ", format=" + format + ", nullContent=" + nullContent + "]";
	}





}
