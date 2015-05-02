package br.com.coachcrossfit.models;

import java.util.Date;

public class Student{
	
	private int idStudent;
	private int idUser;
	private int idCoach;
	private String nameStudent;
	private float weightStudent;
	private Date dateBirthStudent;
	private float heightStudent;
	private String imageStudent;
	private int genderStudent;		
	private User user = new User(); 
		
	public Date getDateBirthStudent() {
		return dateBirthStudent;
	}

	public void setDateBirthStudent(Date dateBirthStudent) {
		this.dateBirthStudent = dateBirthStudent;
	}
		
	public int getIdStudent() {
		return idStudent;
	}
	
	public void setIdStudent(int idStudent) {
		this.idStudent = idStudent;
	}
	
	public int getIdUser() {
		return idUser;
	}
	
	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}
	
	public int getIdCoach() {
		return idCoach;
	}
	
	public void setIdCoach(int idCoach) {
		this.idCoach = idCoach;
	}
	
	public String getNameStudent() {
		return nameStudent;
	}
	
	public void setNameStudent(String nameStudent) {
		this.nameStudent = nameStudent;
	}
	
	public float getWeightStudent() {
		return weightStudent;
	}
	
	public void setWeightStudent(float weightStudent) {
		this.weightStudent = weightStudent;
	}
	
	public float getHeightStudent() {
		return heightStudent;
	}
	
	public void setHeightStudent(float heightStudent) {
		this.heightStudent = heightStudent;
	}
	
	public String getImageStudent() {
		return imageStudent;
	}
	
	public void setImageStudent(String imageStudent) {
		this.imageStudent = imageStudent;
	}
	
	public int getGenderStudent() {
		return genderStudent;
	}

	public void setGenderStudent(int gender) {
		this.genderStudent = gender;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User userbean) {
		this.user = userbean;
	}
		
}
