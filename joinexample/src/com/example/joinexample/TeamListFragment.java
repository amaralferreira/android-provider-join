package com.example.joinexample;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.joinexample.data.TeamProvider;
import com.example.joinexample.domain.Sport;
import com.example.joinexample.domain.Team;

public class TeamListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

	private static final int TEAM_LIST_LOADER = 0x01;

	private SimpleCursorAdapter adapter;

	boolean mDualPane;
	int mCurIndex = 0;
	long mCurItemId = 0;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setAdapter();

		View detailsFrame = getActivity().findViewById(R.id.team_details);
		mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

		if (savedInstanceState != null) {

			mCurItemId = savedInstanceState.getLong("curId", 0);
			mCurIndex = savedInstanceState.getInt("curIndex", 0);
		}

		if (mDualPane) {
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			showDetails(mCurIndex, mCurItemId);
		} else if (mCurItemId != 0) {
			showDetails(mCurIndex, mCurItemId);
		}
	}

	void showDetails(int index, long id) {
		mCurIndex = index;
		mCurItemId = id;

		if (mDualPane) {
			getListView().setItemChecked(index, true);

			TeamViewFragment details = (TeamViewFragment) getFragmentManager().findFragmentById(R.id.team_details);

			if (details == null || details.getShownId() != id) {

				details = TeamViewFragment.newInstance(getDetailsBundle(id));

				FragmentTransaction ft = getFragmentManager().beginTransaction();
				ft.replace(R.id.team_details, details);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.commit();
			}

		} else {
			
			Intent intent = new Intent();
			intent.setClass(getActivity(), TeamViewActivity.class);

			intent.putExtras(getDetailsBundle(id));
			startActivity(intent);
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		showDetails(position, id);
	}

	private Bundle getDetailsBundle(long teamId) {
		
		String projection[] = createdCombinedProjection();
		Cursor cursor = getActivity().getContentResolver().query(
				Uri.withAppendedPath(TeamProvider.CONTENT_URI, String.valueOf(teamId)), projection, null, null, null);

		Bundle selectedTeamDetails = new Bundle();
		if (cursor.moveToFirst()) {
			selectedTeamDetails.putLong(BaseColumns._ID, cursor.getLong(cursor.getColumnIndex(BaseColumns._ID)));
			selectedTeamDetails.putString(Team.KEY_NAME, cursor.getString(cursor.getColumnIndex(Team.KEY_NAME)));
			selectedTeamDetails.putLong(Team.KEY_SPORT_ID, cursor.getLong(cursor.getColumnIndex(Team.KEY_SPORT_ID)));
			selectedTeamDetails.putLong(Team.KEY_UPDATED_DT, cursor.getLong(cursor.getColumnIndex(Team.KEY_UPDATED_DT)));
			
			String aliasedSportName = Sport.addAliasPrefix(Sport.KEY_NAME);
			String aliasedSportPeriodType = Sport.addAliasPrefix(Sport.KEY_PERIOD_TYPE);
			String alisedSportUpdatedDt = Sport.addAliasPrefix(Sport.KEY_UPDATED_DT);
			selectedTeamDetails.putString(aliasedSportName, cursor.getString(cursor.getColumnIndex(aliasedSportName)));
			selectedTeamDetails.putString(aliasedSportPeriodType, cursor.getString(cursor.getColumnIndex(aliasedSportPeriodType)));
			selectedTeamDetails.putLong(alisedSportUpdatedDt, cursor.getLong(cursor.getColumnIndex(alisedSportUpdatedDt)));
		}
		cursor.close();

		return selectedTeamDetails;
	}

	private void setAdapter() {
		String[] uiBindFrom = {  Team.KEY_NAME };
		int[] uiBindTo = { R.id.name };

		getLoaderManager().initLoader(TEAM_LIST_LOADER, null, this);

		adapter = new SimpleCursorAdapter(getActivity().getApplicationContext(), R.layout.list_item, null, uiBindFrom,
				uiBindTo, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

		ViewBinder viewBinder = new ViewBinder() {

			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				int viewId = view.getId();
				switch (viewId) {
				case R.id.name:
					TextView nameView = (TextView) view;
					nameView.setText(cursor.getString(columnIndex));

					break;
				}

				return false;
			}
		};

		adapter.setViewBinder(viewBinder);
		setListAdapter(adapter);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("curIndex", mCurIndex);
		outState.putLong("curId", mCurItemId);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		
		String projection[] = createdCombinedProjection();
		CursorLoader cursorLoader = new CursorLoader(getActivity(), TeamProvider.CONTENT_URI, projection, null, null,
				null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		adapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.swapCursor(null);
	}
	
	private String[] createdCombinedProjection() {
		String teamProjection[] = Team.getQualifiedColumns();
		String sportProjection[] = Sport.getQualifiedColumns();
		int teamLength = teamProjection.length;
		int sportLength = sportProjection.length;
		
		String projection[] = new String[teamLength + sportLength];
		for (int i = 0; i < teamLength; i++) {
			projection[i] = teamProjection[i];
		}

		for (int i = 0; i < sportLength; i++) {
			projection[teamLength + i] = sportProjection[i];
		}
		
		return projection;
	}
}
