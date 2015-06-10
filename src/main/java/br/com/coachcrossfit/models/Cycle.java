package br.com.coachcrossfit.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.coachcrossfit.utilities.GetFields;

public class Cycle {
	
	private int idCycle;
	@GetFields
	private int idCoach;
	@GetFields
	private int nrStudentORGroup;
	@GetFields
	private String nameCycle;
	@GetFields
	private Date dateIniCycle;
	@GetFields
	private Date dateEndCycle;
	@GetFields
	private int typeCycle;
	@GetFields
	private float paymentCycle;
	@GetFields
	private String desCycle;
	private List<Week> weeks = new ArrayList<Week>();
	
	public int getIdCycle() {
		return idCycle;
	}
	
	public void setIdCycle(int idCycle) {
		this.idCycle = idCycle;
	}
	
	public int getIdCoach() {
		return idCoach;
	}
	
	public void setIdCoach(int idCoach) {
		this.idCoach = idCoach;
	}
	
	public int getNrStudentORGroup() {
		return nrStudentORGroup;
	}
	
	public void setNrStudentORGroup(int nrStudentORGroup) {
		this.nrStudentORGroup = nrStudentORGroup;
	}
	
	public String getNameCycle() {
		return nameCycle;
	}
	
	public void setNameCycle(String nameCycle) {
		this.nameCycle = nameCycle;
	}
	
	public Date getDateIniCycle() {
		return dateIniCycle;
	}
	
	public void setDateIniCycle(Date dateIniCycle) {
		this.dateIniCycle = dateIniCycle;
	}
	
	public Date getDateEndCycle() {
		return dateEndCycle;
	}
	
	public void setDateEndCycle(Date dateEndCycle) {
		this.dateEndCycle = dateEndCycle;
	}
	
	public int getTypeCycle() {
		return typeCycle;
	}
	
	public void setTypeCycle(int typeCycle) {
		this.typeCycle = typeCycle;
	}
	
	public float getPaymentCycle() {
		return paymentCycle;
	}
	
	public void setPaymentCycle(float paymentCycle) {
		this.paymentCycle = paymentCycle;
	}
	
	public String getDesCycle() {
		return desCycle;
	}
	
	public void setDesCycle(String desCycle) {
		this.desCycle = desCycle;
	}

	public List<Week> getWeeks() {
		return weeks;
	}

	public void setWeeks(List<Week> weeks) {
		this.weeks = weeks;
	}
				
}
