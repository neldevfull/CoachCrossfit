package br.com.coachcrossfit.models;

public class User {	
	private int idUser;	
	private String nameUser;
	private String emailUser;
	private String passUser;
	private int typeUser;
	private int statusUser;	
	private String imageUser;
	private int genderUser;
	
	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUserBean) {
		this.idUser = idUserBean;
	}		
		
	public String getNameUser() {
		return nameUser;
	}

	public void setNameUser(String nameUser) {
		this.nameUser = nameUser;
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

	public String getImageUser() {
		return imageUser;
	}

	public void setImageUser(String imageUser) {
		this.imageUser = imageUser;
	}

	public int getGenderUser() {
		return genderUser;
	}

	public void setGenderUser(int genderUser) {
		this.genderUser = genderUser;
	}			
	
}
