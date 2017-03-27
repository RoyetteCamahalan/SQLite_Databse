package com.magbanua.mytasks;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class Tasks extends Activity implements OnClickListener, OnTimeSetListener, OnDateSetListener{
	
	private EditText mTaskNameET = null;
	private EditText mTaskDescriptionET = null;
	private EditText mTaskDateDeadlineET = null;
	private EditText mTaskTimeDeadlineET = null;
	private Spinner mPrioritySpinner = null;
	private CheckBox mStatusCB = null;
	
	private TimePickerDialog mTimePicker = null;
	
	private int extraTaskID = -1;
	private String extraTaskName = "";
	private String extraTaskDesc = "";
	private String extraTaskDateDeadline = "";
	private String extraTaskTimeDeadline = "";
	private int extraTaskPriority = -1;
	private int extraTaskStatus = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tasks);
		
		//get all extras
		Intent intent = getIntent();
		extraTaskID = intent.getIntExtra("task_id", -1);
		extraTaskName = intent.getStringExtra("task_name");
		extraTaskDesc = intent.getStringExtra("task_desc");
		extraTaskDateDeadline = intent.getStringExtra("task_date_deadline");
		extraTaskTimeDeadline = intent.getStringExtra("task_time_deadline");
		extraTaskPriority = intent.getIntExtra("task_priority", 1);
		extraTaskStatus = intent.getIntExtra("task_status", 0);
		
		mTaskNameET = (EditText) findViewById(R.id.task_name);
		mTaskDescriptionET = (EditText) findViewById(R.id.task_description);
		mTaskDateDeadlineET = (EditText) findViewById(R.id.task_date_deadline);
		mTaskTimeDeadlineET = (EditText) findViewById(R.id.task_time_deadline);
		mPrioritySpinner = (Spinner) findViewById(R.id.task_priority);
		mStatusCB = (CheckBox) findViewById(R.id.task_status);
		
		
		
		mTaskDateDeadlineET.setOnClickListener(this);
		mTaskTimeDeadlineET.setOnClickListener(this);
		mStatusCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				if(isChecked){
					mStatusCB.setText("done");
				} else {
					mStatusCB.setText("pending");
				}
				
			}
		});
		
		//get the dataset of priority from string resource
		String priorities [] = getResources().getStringArray(R.array.priority_dataset);
		//load the dataset to adapter for the spinner
		ArrayAdapter<String> adapter = new ArrayAdapter<String>( this, 
																 android.R.layout.simple_list_item_1, 
																 priorities);
		mPrioritySpinner.setAdapter(adapter);
		
		if(extraTaskID>0){
			
			//fill in all the extra data to the ui elements
			mTaskNameET.setText(extraTaskName);
			mTaskDescriptionET.setText(extraTaskDesc);
			mTaskDateDeadlineET.setText(extraTaskDateDeadline);
			mTaskTimeDeadlineET.setText(extraTaskTimeDeadline);
			mPrioritySpinner.setSelection((extraTaskPriority-1),true);
			
			if(extraTaskStatus==1){
				mStatusCB.setChecked(true);
			} else {
				mStatusCB.setChecked(false);
			}
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.tasks, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
		
			case R.id.action_save:
				
				String taskName = mTaskNameET.getText().toString();
				String taskDescription = mTaskDescriptionET.getText().toString();
				String taskDateDeadline = mTaskDateDeadlineET.getText().toString();
				String taskTimeDeadline = mTaskTimeDeadlineET.getText().toString();
				int priority = mPrioritySpinner.getSelectedItemPosition() + 1;
				int status = 0;
				
				if(mStatusCB.isChecked()){
					status = 1;
				} 
				
				//insert task
				DBHelper dbHelper = new DBHelper(this);
				
				if(extraTaskID>0){
					
					Task task = new Task();
					task.setId(extraTaskID);
					task.setName(taskName);
					task.setDescription(taskDescription);
					task.setDateDeadline(taskDateDeadline);
					task.setTimeDeadline(taskTimeDeadline);
					task.setPriority(priority);
					task.setStatus(status);
					
					if(dbHelper.updateTask(task)){
			
						Toast.makeText(this, "Task is updated successfully!", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(this, "Failed to update task!", Toast.LENGTH_SHORT).show();
					}
					
					finish();
					
					return true;
				}
				
				if(dbHelper.insertTask(taskName, 
									   taskDescription, 
									   taskDateDeadline, 
									   taskTimeDeadline, 
									   priority, 
									   status)){
					
					Toast.makeText(this, "Task is saved successfully!", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, "Failed to save task!", Toast.LENGTH_SHORT).show();
				}
				
				finish();
				
				break;
			
			case R.id.action_cancel:
				
				finish();
				break;
			
			default:
		
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onClick(View v) {
		
		Calendar cal = Calendar.getInstance();
		
		switch(v.getId()){
			
			case R.id.task_date_deadline:
				
				DatePickerDialog datePicker = new DatePickerDialog(this, this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
				datePicker.show();
				
				break;
				
			case R.id.task_time_deadline:
				
				TimePickerDialog timePicker = new TimePickerDialog(this, this, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false);
				timePicker.show();
				
				break;
			
			default:
		
		}
		
	}

	@Override
	public void onTimeSet(TimePicker timePicker, int hourOfday, int minute) {
		
		String strTime = "";
		
		if(hourOfday>12){
			strTime = strTime + String.format("%02d", (hourOfday-12)) + ":" + String.format("%02d", minute)+" PM";
		} else if(hourOfday==12 && minute>0){
			strTime = strTime + String.format("%02d", hourOfday) + ":" + String.format("%02d", minute)+" PM";
		} else if(hourOfday==0){
			strTime = strTime + String.format("%02d", (hourOfday+12))+ ":" + String.format("%02d", minute)+" AM";
		} else {
			strTime = strTime + String.format("%02d", hourOfday)+ ":" + String.format("%02d", minute)+" AM";
		}
		
		mTaskTimeDeadlineET.setText(strTime);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		
		mTaskDateDeadlineET.setText(year+"-"+String.format("%02d", (monthOfYear+1))+"-"+String.format("%02d", dayOfMonth));
	}
	
}
