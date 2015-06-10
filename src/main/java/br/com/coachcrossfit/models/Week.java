package br.com.coachcrossfit.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.coachcrossfit.utilities.GetFields;

public class Week {

	@GetFields
	private int nrWeek;
	@GetFields
	private int idCycle;
	@GetFields
	private String nameWeek;
	@GetFields
	private Date dateIniWeek;
	@GetFields
	private Date dateEndWeek;
	private List<Day> days = new ArrayList<Day>();
	
	public int getNrWeek() {
		return nrWeek;
	}
	
	public void setNrWeek(int nrWeek) {
		this.nrWeek = nrWeek;
	}
	
	public int getIdCycle() {
		return idCycle;
	}
	
	public void setIdCycle(int idCycle) {
		this.idCycle = idCycle;
	}
	
	public String getNameWeek() {
		return nameWeek;
	}
	
	public void setNameWeek(String nameWeek) {
		this.nameWeek = nameWeek;
	}
	
	public Date getDateIniWeek() {
		return dateIniWeek;
	}
	
	public void setDateIniWeek(Date dateIniWeek) {
		this.dateIniWeek = dateIniWeek;
	}
	public Date getDateEndWeek() {
	
		return dateEndWeek;
	}
	
	public void setDateEndWeek(Date dateEndWeek) {
		this.dateEndWeek = dateEndWeek;
	}

	public List<Day> getDays() {
		return days;
	}

	public void setDays(List<Day> days) {
		this.days = days;
	}
	
}
