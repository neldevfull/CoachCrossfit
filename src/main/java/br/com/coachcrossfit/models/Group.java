package br.com.coachcrossfit.models;

public class Group {
	
	private int idGroup;
	private int idCoach;
	private String nameGroup;
	private int statusGroup;
	
	public int getIdGroup() {
		return idGroup;
	}
	
	public void setIdGroup(int idGroup) {
		this.idGroup = idGroup;
	}
	
	public int getIdCoach() {
		return idCoach;
	}
	
	public void setIdCoach(int idCoach) {
		this.idCoach = idCoach;
	}
	
	public String getNameGroup() {
		return nameGroup;
	}
	
	public void setNameGroup(String nameGroup) {
		this.nameGroup = nameGroup;
	}
	
	public int getStatusGroup() {
		return statusGroup;
	}
	
	public void setStatusGroup(int statusGroup) {
		this.statusGroup = statusGroup;
	}
		
}
