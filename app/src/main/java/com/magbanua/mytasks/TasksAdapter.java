package com.magbanua.mytasks;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class TasksAdapter extends BaseAdapter{
	
	public Context context;
	public ArrayList<Task> tasks;
	public LayoutInflater inflater;
	
	public TasksAdapter(Context context, ArrayList<Task> tasks){
		this.context = context;
		this.tasks = tasks;
		
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	//this will return the size of the dataset
	@Override
	public int getCount() {
		return tasks.size();
	}
	
	//returns the current item
	@Override
	public Object getItem(int position) {
		return tasks.get(position);
	}

	@Override
	public long getItemId(int position) {
		return tasks.get(position).getId();
	}
	
	public void removeTask(int position){
		tasks.remove(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = new ViewHolder();
		
		if(convertView==null){
			
			convertView = inflater.inflate(R.layout.task_item, parent, false);
			
			//assign the data to the layout
			holder.taskNameTV = (TextView) convertView.findViewById(R.id.task_name);
			holder.taskDescriptionTV = (TextView) convertView.findViewById(R.id.task_description);
			holder.taskDeadlineTV = (TextView) convertView.findViewById(R.id.task_deadline);
			holder.statusCB = (CheckBox) convertView.findViewById(R.id.status);
			
			convertView.setTag(holder);
			
		} else {
			
			holder = (ViewHolder) convertView.getTag();
			
		}
		
		//get the data base on position
		Task task = (Task) getItem(position);
		
		holder.taskNameTV.setText(task.getName());
		holder.taskDescriptionTV.setText(task.getDescription());
		holder.taskDeadlineTV.setText(task.getDateDeadline()+" "+task.getTimeDeadline());
		
		switch(task.getPriority()){
			
			case 1: //low priority - yellow
				holder.taskNameTV.setTextColor(Color.YELLOW);
				break;
				
			case 2: //normal priority - green
				holder.taskNameTV.setTextColor(Color.GREEN);
				break;
				
			case 3: //high priority - red
				holder.taskNameTV.setTextColor(Color.RED);
				break;
				
				default:
		}
		
		//set the status
		if(task.getStatus()==1){
			holder.statusCB.setChecked(true);
		} else {
			holder.statusCB.setChecked(false);
		}
		
		//set the task id as tag
		holder.statusCB.setTag(task);
		
		//implement listener for checkbox
		holder.statusCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				//get the id of the task store in the tag
				Task task = (Task) buttonView.getTag();
				DBHelper dbHelper = new DBHelper(context);
				
				if(isChecked){
					Toast.makeText(context, "done! "+task.getId(), Toast.LENGTH_SHORT).show();
					task.setStatus(1);
				} else {
					Toast.makeText(context, "pending! "+task.getId(), Toast.LENGTH_SHORT).show();
					task.setStatus(0);
				}
				
				//update the tasks
				dbHelper.updateTask(task);
			}
		});
		
		return convertView;
	}
	
	public static class ViewHolder {
		public TextView taskNameTV;
		public TextView taskDescriptionTV;
		public TextView taskDeadlineTV;
		public CheckBox statusCB;
	}
}
