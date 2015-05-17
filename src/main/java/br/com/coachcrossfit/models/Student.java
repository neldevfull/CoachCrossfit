package br.com.coachcrossfit.models;
 
import java.util.Date;

public class Student extends User{
	
	private int idStudent;	
	@SuppressWarnings("unused")
	// Utilizado apenas na reflection
	private int idUser;
	private int idCoach;	
	private float weightStudent;
	private Date dateBirthStudent;
	private float heightStudent;		
	 		
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
	
	public int getIdCoach() {
		return idCoach;
	}
	
	public void setIdCoach(int idCoach) {
		this.idCoach = idCoach;
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
		
}
