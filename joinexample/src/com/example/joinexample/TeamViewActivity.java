package com.example.joinexample;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class TeamViewActivity extends FragmentActivity  {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// If the screen is now in landscape mode, we can show the
			// dialog in-line with the list so we don't need this activity.
			finish();
			return;
		}

		if (savedInstanceState == null) {
			// During initial setup, plug in the details fragment.

			TeamViewFragment details = new TeamViewFragment();
		
			Bundle extras = getIntent().getExtras();
			details.setArguments(extras);
			
			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, details).commit();
		}
	}


}
