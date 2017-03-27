package com.magbanua.mytasks;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class MyTasksPreferenceFragment extends PreferenceFragment{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.my_tasks_preference);

	}

}
