package com.magbanua.mytasks;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

public class MyTasks extends Activity {
	
	public static final String TAG = "MyTasks";
	
	private ListView myTasksListView = null;
	private TasksAdapter adapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_tasks);
		
		myTasksListView = (ListView)findViewById(R.id.my_tasks_listview);
		registerForContextMenu(myTasksListView);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		DBHelper dbHelper = new DBHelper(this);
		ArrayList<Task> tasks = dbHelper.getAllTasks();
		
		for(Task task:tasks){
			Log.d(TAG, task.getName());
		}
		
		//get all the preferences
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		boolean hideDone = sharedPref.getBoolean("hide_done", false);
		
		Log.d(TAG, "hideDone = "+hideDone);
		
		//we exclude done tasks
		if(hideDone){
			
			ArrayList<Task> notDone = new ArrayList<Task>();
			
			for(Task task:tasks){
				
				//add all tasks that not done to notDone arraylist
				if(task.getStatus()==0) {
					notDone.add(task);
				}
			}
			
			//clears the tasks arraylist so that it will contain the not done tasks
			tasks.clear();
			tasks.addAll(notDone);
		}
		
		//ArrayAdapter<Task> adapter = new ArrayAdapter<Task>(this, android.R.layout.simple_list_item_1, tasks);
		adapter = new TasksAdapter(this,tasks);
		myTasksListView.setAdapter(adapter);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	                                ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.task_context_menu, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    DBHelper dbHelper = new DBHelper(this);
	    //get the task base on the position
    	Task task = (Task) adapter.getItem(info.position);
	    
	    switch (item.getItemId()) {
	    
	        case R.id.context_action_edit:
	        	
	        	Toast.makeText(this, "edit! "+info.position, Toast.LENGTH_SHORT).show();
	        	
	        	//start the add task activity
				Intent tasks = new Intent(this,Tasks.class);
				//load extras from the task
				tasks.putExtra("task_id", task.getId());
				tasks.putExtra("task_name", task.getName());
				tasks.putExtra("task_desc", task.getDescription());
				tasks.putExtra("task_date_deadline", task.getDateDeadline());
				tasks.putExtra("task_time_deadline", task.getTimeDeadline());
				tasks.putExtra("task_priority", task.getPriority());
				tasks.putExtra("task_status", task.getStatus());
				
				startActivity(tasks);
	        	
	            return true;
	            
	        case R.id.context_action_delete:
	            
	        	//Toast.makeText(this, "delete!"+info.position, Toast.LENGTH_SHORT).show();
	        	dbHelper.deleteTask(task.getId());
	        	
	        	//delete the task from the adapter
	        	adapter.removeTask(info.position);
	        	//update the listview
	        	adapter.notifyDataSetChanged();
	        	
	            return true;
	            
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_tasks, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
			case R.id.action_add:
				
				//start the add task activity
				Intent tasks = new Intent(this,Tasks.class);
				startActivity(tasks);
				
				break;
			
			case R.id.action_settings:
				
				//start settings activity
				Intent settings = new Intent(this,SettingsActivity.class);
				startActivity(settings);
				
				break;
		}
		
		return true;
	}
}
