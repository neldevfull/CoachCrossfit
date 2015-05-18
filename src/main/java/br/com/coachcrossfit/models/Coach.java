package br.com.coachcrossfit.models;

public class Coach extends User {

	private int idCoach;
	// Utilizado apenas na reflection	
	@SuppressWarnings("unused")
	private int idUser;
	private String crefCoach;		
	
	public int getIdCoach() {
		return idCoach;
	}
	
	public void setIdCoach(int idCoach) {
		this.idCoach = idCoach;
	}
		
	public String getCrefCoach() {
		return crefCoach;
	}
	
	public void setCrefCoach(String crefCoach) {
		this.crefCoach = crefCoach;
	}
				
}
