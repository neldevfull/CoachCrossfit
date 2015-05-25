package br.com.coachcrossfit.models;

public class GroupStudent {

	private Student student;
	private boolean statusGroupStudent;
	
	public Student getStudent() {
		return student;
	}
	
	public void setStudent(Student student) {
		this.student = student;
	}
	
	public boolean isStatusGroupStudent() {
		return statusGroupStudent;
	}
	
	public void setStatusGroupStudent(boolean statusGroupStudent) {
		this.statusGroupStudent = statusGroupStudent;
	}
				
}
