package br.com.coachcrossfit.models;

import br.com.coachcrossfit.utilities.GetFields;

public class Coach extends User {

	private int idCoach;
	@GetFields
	private int idUser;
	@GetFields
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
