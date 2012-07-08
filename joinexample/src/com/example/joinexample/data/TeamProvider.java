package com.example.joinexample.data;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.joinexample.domain.Sport;
import com.example.joinexample.domain.Team;

public class TeamProvider extends ContentProvider {

	private DBManager mDB;
	private static final String AUTHORITY = "com.example.joinexample.data.TeamProvider";

	public static final int ALL = 100;
	public static final int TEAM_ID = 110;

	private static final String TEAM_BASE_PATH = "team";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TEAM_BASE_PATH);

	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, TEAM_BASE_PATH, ALL);
		sURIMatcher.addURI(AUTHORITY, TEAM_BASE_PATH + "/#", TEAM_ID);
	}

	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/team";
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/team";
	private static final HashMap<String, String> mColumnMap = buildColumnMap();

	@Override
	public boolean onCreate() {
		mDB = new DBManager(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		String table = Team.TABLE_NAME;

		StringBuilder sb = new StringBuilder();
		sb.append(Team.TABLE_NAME);
		sb.append(" LEFT OUTER JOIN ");
		sb.append(Sport.TABLE_NAME);
		sb.append(" ON (");
		sb.append(Team.addPrefix(Team.KEY_SPORT_ID));
		sb.append(" = ");
		sb.append(Sport.addPrefix(BaseColumns._ID));
		sb.append(")");
		table = sb.toString();

		queryBuilder.setTables(table);
		queryBuilder.setProjectionMap(mColumnMap);
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case TEAM_ID:
			String id = BaseColumns._ID;
			id = Team.addPrefix(id);
			queryBuilder.appendWhere(id + "=" + uri.getLastPathSegment());
			break;
		case ALL:
			break;
		default:
			throw new IllegalArgumentException("Unknown URI");
		}

		Cursor cursor = queryBuilder.query(mDB.getReadableDatabase(), projection, selection, selectionArgs, null, null,
				sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	/**
	 * Because the tables we're joining have columns of the same name, we have to map column names to aliases.
	 * The team table is the primary table here, so the alias is just the column name. For the sport table,
	 * the alias is calculated by adding the table name plus "_", then the column name. 
	 * 
	 * @return
	 */
	private static HashMap<String, String> buildColumnMap() {
		HashMap<String, String> map = new HashMap<String, String>();
		String teamProjection[] = Team.TABLE_COLUMNS;
		for (String col : teamProjection) {

			String qualifiedCol = Team.addPrefix(col);
			map.put(qualifiedCol, qualifiedCol + " as " + col);
		}

		String sportProjection[] = Sport.TABLE_COLUMNS;
		for (String col : sportProjection) {

			String qualifiedCol = Sport.addPrefix(col);
			String alias = qualifiedCol.replace(".", Sport.ALIAS_JOINER);
			map.put(qualifiedCol, qualifiedCol + " AS " + alias);
		}

		return map;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// not implemented in this example
		
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case ALL:
			return CONTENT_TYPE;
		case TEAM_ID:
			return CONTENT_ITEM_TYPE;
		default:
			return null;
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// not implemented in this example
		
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		// not implemented in this example
		
		return 0;
	}
}
