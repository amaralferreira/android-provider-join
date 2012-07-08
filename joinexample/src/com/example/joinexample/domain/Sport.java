package com.example.joinexample.domain;

import java.util.Date;

import android.provider.BaseColumns;

public class Sport {
	
	public static final String TABLE_NAME = "sport";
	public static final String KEY_NAME = "name";
	public static final String KEY_PERIOD_TYPE = "periodType";
	public static final String KEY_UPDATED_DT = "updatedDt";
	public static final String ALIAS_JOINER = "_";
	
	public static final String[] TABLE_COLUMNS = {BaseColumns._ID, KEY_NAME, KEY_PERIOD_TYPE, KEY_UPDATED_DT};
	private String name;
	private Date updatedDt;
	private String periodType;
	
	public Sport(String name, String periodType,  Date updatedDt) {
		
		this.name = name;
		this.updatedDt = updatedDt;
		this.periodType = periodType;
	}
	
	public static String[] getQualifiedColumns() {
		String[] qualifiedColumns = new String[TABLE_COLUMNS.length];
		for (int i = 0; i < TABLE_COLUMNS.length; i++) {
			qualifiedColumns[i] = addPrefix(TABLE_COLUMNS[i]);
		}
		
		return qualifiedColumns;
	}
	
	/**
	 * Concatenates the table name, ".", then the columns name.
	 * 
	 * @param column
	 * @return prefixed or table-qualified column
	 */
	public static String addPrefix(String column) {
		return TABLE_NAME + "." + column;
	}
	
	/**
	 * Concatenates the table name, "_", then the columns name.
	 * 
	 * @param column
	 * @return
	 */
	public static String addAliasPrefix(String column) {
		return TABLE_NAME + ALIAS_JOINER + column;
	}
	
	public String getName() {
		return name;
	}

	public String getPeriodType() {
		return periodType;
	}
	
	public Date getUpdatedDt() {
		return updatedDt;
	}
}
