package com.example.joinexample.data;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.joinexample.domain.Team;
import com.example.joinexample.domain.Sport;

public class DBManager extends SQLiteOpenHelper {

	private static final String TAG = "DBManager";
	
	private static final String DB_FILE_NAME = "joinexample.db";
	private static final int DB_VERSION = 1;
	
	public DBManager(Context context) {
		super(context, DB_FILE_NAME, null, DB_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {

    	Log.i(TAG, "Creating sport table");
    	db.execSQL(createTableSQL(Sport.TABLE_NAME, Sport.TABLE_COLUMNS));
    	Log.i(TAG, "Creating team database table");
        db.execSQL(createTableSQL(Team.TABLE_NAME, Team.TABLE_COLUMNS));

        loadInitialData(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO not worrying about an upgrade for this example
	}

	
	String createTableSQL(String tableName, String[] columns) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("create table ");
		sb.append(tableName);
		sb.append(" (" );
		sb.append(columns[0]);
		sb.append(" integer primary key autoincrement ");
		
		for (int i=1; i<columns.length; i++) {
			sb.append(", ");
			sb.append(columns[i]);
		}
		
		sb.append(" ) ");
		
		String sql = sb.toString();
		Log.i(TAG, sql);
		return sql;
	}
	
	public long addSport(SQLiteDatabase db, Sport sport) {
		ContentValues newTeamValues = new ContentValues();
		
		newTeamValues.put(Sport.KEY_NAME, sport.getName());
		newTeamValues.put(Sport.KEY_PERIOD_TYPE, sport.getPeriodType());
		newTeamValues.put(Sport.KEY_UPDATED_DT, sport.getUpdatedDt().getTime());
		
		return db.insert(Sport.TABLE_NAME, null, newTeamValues);
	}
	
	public long addTeam(SQLiteDatabase db, Team team) {
		ContentValues newTeamValues = new ContentValues();
		
		newTeamValues.put(Team.KEY_NAME, team.getName());
		newTeamValues.put(Team.KEY_SPORT_ID, team.getSportId());
		
		return db.insert(Team.TABLE_NAME, null, newTeamValues);
	}
	
    private void loadInitialData(SQLiteDatabase db) {
		Sport soccer = new Sport("Soccer", "Half", new Date());
		Sport basketball = new Sport("Basketball", "Half",new Date());
		Sport baseball = new Sport("Baseball", "Inning",new Date());
		
		long basketballId = addSport(db, basketball);
		long soccerId = addSport(db, soccer);
		long baseballId = addSport(db, baseball);
		
		addTeam(db, new Team("Dragons", basketballId, new Date()));
		addTeam(db, new Team("Rockets", basketballId,  new Date()));
		addTeam(db, new Team("Saber-Toothed Tigers", soccerId, new Date()));
		addTeam(db, new Team("The Muppets", baseballId, new Date()));
		
		// Add a team with no sport, just to demonstrate the left join
		addTeam(db, new Team("Mystery No Sport", null, new Date()));
    }
}
