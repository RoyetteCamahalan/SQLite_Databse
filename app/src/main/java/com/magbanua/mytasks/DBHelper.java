package com.magbanua.mytasks;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper{
	
	public static final String TAG = "DBHelper";
	public static final String DB_NAME = "mytasks";
	public static final int VERSION = 2;
	
	public static final String TASK_TABLE_NAME = "tasks";
	public static final String COL_ID = "_id";
	public static final String COL_NAME = "name";
	public static final String COL_DESCRIPTION = "description";
	public static final String COL_DATE = "date";
	public static final String COL_TIME = "time";
	public static final String COL_PRIORITY = "priority";
	public static final String COL_STATUS = "status";
	
	public DBHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		//create tables here
		String sql = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s INTEGER, %s INTEGER)", 
				TASK_TABLE_NAME,
				COL_ID,
				COL_NAME,
				COL_DESCRIPTION,
				COL_DATE,
				COL_TIME,
				COL_PRIORITY,
				COL_STATUS);
		
		db.execSQL(sql);
		Log.d(TAG, "onCreate() "+sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		String sql = "DROP TABLE "+TASK_TABLE_NAME;
		db.execSQL(sql);
		//recreate tables
		onCreate(db);
		Log.d(TAG, "onUpgrade()");
	}
	
	public boolean insertTask(String taskName,
							  String taskDescription,
							  String taskDateDeadline,
							  String taskTimeDeadline,
							  int priority,
							  int status){
		boolean ret = false;
		
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(COL_NAME, taskName);
		values.put(COL_DESCRIPTION, taskDescription);
		values.put(COL_DATE, taskDateDeadline);
		values.put(COL_TIME, taskTimeDeadline);
		values.put(COL_PRIORITY, priority);
		values.put(COL_STATUS, status);
		
		long rowID = db.insert(TASK_TABLE_NAME, null, values);
		ret = rowID > 0;
		
		db.close();
		
		return ret;
	}
	
	public ArrayList<Task> getAllTasks(){
		
		ArrayList<Task> tasks = new ArrayList<Task>();
		SQLiteDatabase db = getWritableDatabase();
		
		//fetch all tasks in the database
		String sql = "SELECT * FROM "+TASK_TABLE_NAME;
		Cursor cur = db.rawQuery(sql, null);
		
		while(cur.moveToNext()){
			
			Task task = new Task();
			
			int id = cur.getInt(0); //this is for id
			String name = cur.getString(1); //task name
			String description = cur.getString(2); //description
			String dateDeadline = cur.getString(3); //date deadline
			String timeDeadline = cur.getString(4);
			int priority = cur.getInt(5);
			int status = cur.getInt(6);
			
			task.setId(id);
			task.setName(name);
			task.setDescription(description);
			task.setDateDeadline(dateDeadline);
			task.setTimeDeadline(timeDeadline);
			task.setPriority(priority);
			task.setStatus(status);
			
			tasks.add(task);
		}
		
		cur.close();
		db.close();
		
		return tasks;
	}
	
	public boolean updateTask(Task task){
		
		boolean ret = false;
		
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(COL_NAME, task.getName());
		values.put(COL_DESCRIPTION, task.getDescription());
		values.put(COL_DATE, task.getDateDeadline());
		values.put(COL_TIME, task.getTimeDeadline());
		values.put(COL_PRIORITY, task.getPriority());
		values.put(COL_STATUS, task.getStatus());
		
		String id = task.getId()+"";
		
		String whereArgs [] = new String[]{id};
		
		int rowsAffected = db.update(TASK_TABLE_NAME, values, COL_ID+"=?", whereArgs);
		
		ret = rowsAffected > 0;
		
		db.close();
		
		return ret;
	}
	
	public boolean deleteTask(int id){
		
		boolean ret = false;
		
		SQLiteDatabase db = getWritableDatabase();
		
		String whereArgs [] = new String[]{id+""};
		
		int rowsDeleted = db.delete(TASK_TABLE_NAME, COL_ID+"=?", whereArgs);
		ret = rowsDeleted > 0;
		
		db.close();
		
		return ret;
		
	}
}
