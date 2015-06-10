package br.com.coachcrossfit.models;

import java.util.Date;

import br.com.coachcrossfit.utilities.GetFields;

public class Training {

	@GetFields
	private int nrTraining;
	@GetFields
	private Date dateDay;
	@GetFields
	private int idCycle;
	@GetFields
	private int nrWeek;		
	@GetFields
	private String exerciseTraining;
	@GetFields
	private String actionTraining;
	
	public int getNrTraining() {
		return nrTraining;
	}
	
	public void setNrTraining(int nrTraining) {
		this.nrTraining = nrTraining;
	}
	
	public Date getDateDay() {
		return dateDay;
	}
	
	public void setDateDay(Date dateTraining) {
		this.dateDay = dateTraining;
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
	
	public String getExerciseTraining() {
		return exerciseTraining;
	}
	
	public void setExerciseTraining(String exerciseTraining) {
		this.exerciseTraining = exerciseTraining;
	}
	
	public String getActionTraining() {
		return actionTraining;
	}
	
	public void setActionTraining(String actionTraining) {
		this.actionTraining = actionTraining;
	}
			
}
