package com.example.joinexample.domain;

import java.util.Date;

import android.provider.BaseColumns;

public class Team {

	public static final String TABLE_NAME = "team";
	public static final String KEY_NAME = "name";
	public static final String KEY_SPORT_ID = "sportId";
	public static final String KEY_UPDATED_DT = "updatedDt";
	
	public static final String[] TABLE_COLUMNS = {BaseColumns._ID, KEY_NAME, KEY_SPORT_ID, KEY_UPDATED_DT};

	private String name;
	private Long sportId;
	private Date updatedDt;
	
	public Team(String name, Long sportId, Date updatedDt) {
		
		this.name = name;
		this.sportId = sportId;
		this.updatedDt = updatedDt;
	}
	
	public static String[] getQualifiedColumns() {
		String[] qualifiedColumns = new String[TABLE_COLUMNS.length];
		for (int i = 0; i < TABLE_COLUMNS.length; i++) {
			qualifiedColumns[i] = addPrefix(TABLE_COLUMNS[i]);
		}
		
		return qualifiedColumns;
	}
	
	public static String addPrefix(String column) {
		return TABLE_NAME + "." + column;
	}
	
	public String getName() {
		return name;
	}
	
	public Date getUpdatedDt() {
		return updatedDt;
	}

	public Long getSportId() {
		return sportId;
	}
}
