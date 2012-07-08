package com.example.joinexample;

import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.joinexample.domain.Sport;
import com.example.joinexample.domain.Team;

public class TeamViewFragment extends Fragment {

	public static TeamViewFragment newInstance(Bundle state) {
		TeamViewFragment f = new TeamViewFragment();
		f.setArguments(state);
		return f;
	}
	 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedState) {
    	
        if (container == null) {
            return null;
        }
        
        View viewer = inflater.inflate(R.layout.team_view, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
        	
        	String sportName = bundle.getString(Sport.addAliasPrefix(Sport.KEY_NAME));
        	LinearLayout viewTeamSportItem = (LinearLayout)viewer.findViewById(R.id.viewTeamSportItem);
        	if (sportName == null) {
        		viewTeamSportItem.setVisibility(View.INVISIBLE);
        	} else {
        		viewTeamSportItem.setVisibility(View.VISIBLE);
		        TextView sportTextView = (TextView)viewer.findViewById(R.id.viewTeamSport);
		        sportTextView.setText(sportName);
        	}
        	
        	LinearLayout viewTeamNameItem = (LinearLayout)viewer.findViewById(R.id.viewTeamNameItem);
        	String teamName = bundle.getString(Team.KEY_NAME);
        	if (teamName == null) {
        		
            	viewTeamNameItem.setVisibility(View.INVISIBLE);
        	} else {
		        TextView nameTextView = (TextView)viewer.findViewById(R.id.viewTeamName);
		        nameTextView.setText(bundle.getString(Team.KEY_NAME));
        	}
        	
        	String periodType = bundle.getString(Sport.addAliasPrefix(Sport.KEY_PERIOD_TYPE));
        	LinearLayout periodTypeItem = (LinearLayout)viewer.findViewById(R.id.viewTeamPeriodTypeItem);
        	if (periodType == null) {  		
        		periodTypeItem.setVisibility(View.INVISIBLE);
        	} else {
        		periodTypeItem.setVisibility(View.VISIBLE);
            	TextView viewTeamPeriodType = (TextView)viewer.findViewById(R.id.viewTeamPeriodType);
            	viewTeamPeriodType.setText(periodType);
        	}
        } 

      return viewer; 
    }
   
    
    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }
    
    public long getShownId() {
    	return getArguments().getLong(BaseColumns._ID, 0);
    }
}
