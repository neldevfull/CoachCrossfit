package br.com.coachcrossfit.models;

public class User {	
	private int idUser;	
	private String emailUser;
	private String passUser;
	private int typeUser;
	private int statusUser;	
	
	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUserBean) {
		this.idUser = idUserBean;
	}
		
	public String getEmailUser() {
		return emailUser;
	}
	
	public void setEmailUser(String emailUser) {
		this.emailUser = emailUser;
	}
	
	public String getPassUser() {
		return passUser;
	}
	
	public void setPassUser(String passUser) {
		this.passUser = passUser;
	}
	
	public int getTypeUser() {
		return typeUser;
	}
	
	public void setTypeUser(int typeUser) {
		this.typeUser = typeUser;
	}
	
	public int getStatusUser() {
		return statusUser;
	}
	
	public void setStatusUser(int statusUser) {
		this.statusUser = statusUser;
	}	
	
}
