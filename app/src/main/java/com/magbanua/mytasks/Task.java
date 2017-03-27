package com.magbanua.mytasks;

public class Task {
	
	private int id = -1;
	private String name = "";
	private String description = "";
	private String dateDeadline = "";
	private String timeDeadline = "";
	private int priority = -1;
	private int status = -1;
	
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public String getDateDeadline() {
		return dateDeadline;
	}
	public String getTimeDeadline() {
		return timeDeadline;
	}
	public int getPriority() {
		return priority;
	}
	public int getStatus() {
		return status;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setDateDeadline(String dateDeadline) {
		this.dateDeadline = dateDeadline;
	}
	public void setTimeDeadline(String timeDeadline) {
		this.timeDeadline = timeDeadline;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
