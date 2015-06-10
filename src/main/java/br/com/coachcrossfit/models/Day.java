package br.com.coachcrossfit.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.coachcrossfit.utilities.GetFields;

public class Day {

	@GetFields
	private Date dateDay;
	@GetFields
	private int idCycle;
	@GetFields
	private int nrWeek;
	@GetFields
	private String nameDay;
	List<Training> trainings = new ArrayList<Training>();
	
	public Date getDateDay() {
		return dateDay;
	}
	
	public void setDateDay(Date dateDay) {
		this.dateDay = dateDay;
	}
	
	public int getIdCycle() {
		return idCycle;
	}
	
	public void setIdCycle(int idCycle) {
		this.idCycle = idCycle;
	}
	
	public int getNrWeek() {
		return nrWeek;
	}
	
	public void setNrWeek(int nrWeek) {
		this.nrWeek = nrWeek;
	}
	
	public String getNameDay() {
		return nameDay;
	}
	
	public void setNameDay(String nameTraining) {
		this.nameDay = nameTraining;
	}

	public List<Training> getTrainings() {
		return trainings;
	}

	public void setTrainings(List<Training> trainings) {
		this.trainings = trainings;
	}
				
}
