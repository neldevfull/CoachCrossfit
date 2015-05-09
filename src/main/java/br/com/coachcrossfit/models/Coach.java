package br.com.coachcrossfit.models;

public class Coach {

	private int idCoach;	
	private int idUser;
	private String crefCoach;
	private String nameCoach;	
	private String imageCoach;
	private User user;
	
	public int getIdCoach() {
		return idCoach;
	}
	
	public void setIdCoach(int idCoach) {
		this.idCoach = idCoach;
	}
	
	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}
	
	public String getCrefCoach() {
		return crefCoach;
	}
	
	public void setCrefCoach(String crefCoach) {
		this.crefCoach = crefCoach;
	}
	
	public String getNameCoach() {
		return nameCoach;
	}
	
	public void setNameCoach(String nameCoach) {
		this.nameCoach = nameCoach;
	}		
	
	public String getImageCoach() {
		return imageCoach;
	}

	public void setImageCoach(String imageCoach) {
		this.imageCoach = imageCoach;
	}

	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
				
}
