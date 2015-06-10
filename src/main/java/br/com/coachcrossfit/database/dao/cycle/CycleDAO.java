package br.com.coachcrossfit.database.dao.cycle;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.coachcrossfit.database.dao.generics.GenericsDAO;
import br.com.coachcrossfit.models.Cycle;
import br.com.coachcrossfit.models.Day;
import br.com.coachcrossfit.models.Training;
import br.com.coachcrossfit.models.Week;
import br.com.coachcrossfit.utilities.GetFields;

public class CycleDAO extends GenericsDAO{ 
	
	private String table;
	//private String conditions;
	private Class<Cycle> cycClas;
	private Class<Week> weeClas;
	private Class<Day> dayClas;
	private Class<Training> traClas;
	private List<String> fields;
	//private List<String> fieldsConditions;	
	private List<Object> values;
	//private List<Object> valuesConditons;
	
	public CycleDAO(Connection connection) {
		super(connection);		
		// Initialization of attributes
		this.table 			  = "tb_cycle";
		//this.conditions 	  = "";
		this.cycClas 		  = Cycle.class;
		this.weeClas          = Week.class;
		this.dayClas          = Day.class;
		this.traClas		  = Training.class;
		this.fields 	 	  = new ArrayList<String>();
		//this.fieldsConditions = new ArrayList<String>();
		this.values 		  = new ArrayList<Object>();
		//this.valuesConditons  = new ArrayList<Object>();
	}
	
	/**
	 * Insert Cycle
	 */
	public int cycleCad(Cycle cycle) throws SQLException{
		// Initialization variables
		int idCycle = 0;
		// Busca campos anotados
		this.loadFields(this.cycClas);
		// Adiciona os valores
		this.values.clear();
		this.values.add(cycle.getIdCoach());
		this.values.add(cycle.getNrStudentORGroup());
		this.values.add(cycle.getNameCycle());
		this.values.add(cycle.getDateIniCycle());
		this.values.add(cycle.getDateEndCycle());
		this.values.add(cycle.getTypeCycle());
		this.values.add(cycle.getPaymentCycle());
		this.values.add(cycle.getDesCycle());
		
		// Insert Cycle
		idCycle = super.insertReturnId(this.fields, this.values, this.table);
				
		return idCycle;
	}
	
	/**
	 * Insert Week
	 */
	public void weekCad(Cycle cycle) throws SQLException{
		// Get Fields
		this.loadFields(this.weeClas);
		// Add values and insert
		for(Week week : cycle.getWeeks()){			
			this.values.clear();
			this.values.add(week.getNrWeek());
			this.values.add(week.getIdCycle());
			this.values.add(week.getNameWeek());
			this.values.add(week.getDateIniWeek());
			this.values.add(week.getDateEndWeek());
			this.insert(this.fields, this.values, "tb_week");
		}				
	}
	
	/**
	 * Insert Day and Training(s)
	 */
	public void trainingCad(Day day) throws SQLException{
		// Get Fields
		this.loadFields(this.dayClas);
		// Add values and insert
		this.values.clear();
		this.values.add(day.getDateDay());
		this.values.add(day.getIdCycle());
		this.values.add(day.getNrWeek());
		this.values.add(day.getNameDay());
		// Insert Day
		super.insert(this.fields, this.values, "tb_day");
		// Insert Trainings			
		this.trainingIns(day.getTrainings());
	}

	/**
	 * Insert Training(s)
	 */
	private void trainingIns(List<Training> trainings) throws SQLException {
		// Get Fields
		this.loadFields(this.traClas);
		
		for (Training training : trainings) {			
			// Add values and insert
			this.values.clear();			
			this.values.add(training.getNrTraining());
			this.values.add(training.getDateDay());
			this.values.add(training.getIdCycle());
			this.values.add(training.getNrWeek());
			this.values.add(training.getExerciseTraining());
			this.values.add(training.getActionTraining());
			
			super.insert(this.fields, this.values, "tb_training");
		}
	}
	
	/**
	 * Delete Training(s)
	 */
	private void trainingDel(List<Training> trainings) throws SQLException{ 
		this.fields.clear();
		this.fields.add("nrTraining");
		this.fields.add("dateDay");
		this.fields.add("idCycle");
		this.fields.add("nrWeek");
		
		for (Training training : trainings){
			this.values.clear();
			this.values.add(training.getNrTraining());
			this.values.add(training.getDateDay());
			this.values.add(training.getIdCycle());
			this.values.add(training.getNrWeek());
			
			super.delete(this.fields, this.values, "tb_training");
		}
	}
	
	/**
	 * Update Training(s)
	 */
	public void trainingUp(Map<Integer, Training> trainingUp) throws SQLException{
		List<Training> trainingIns = new ArrayList<Training>();
		List<Training> trainingDel = new ArrayList<Training>();
		for(Integer key : trainingUp.keySet()){
			if(key == 0)
				trainingDel.add(trainingUp.get(key));								
			else
				trainingIns.add(trainingUp.get(key));
		}
		
		if(trainingDel.size() > 0)
			this.trainingDel(trainingDel);
		
		if(trainingIns.size() > 0)
			this.trainingIns(trainingIns);
				
	}
	
	
	
	/**
	 * Loads fields according to annotation GetFields
	 */
	private void loadFields(Class<?> classGenerics){		
		this.fields.clear();				
		for(Field field : classGenerics.getDeclaredFields()){
			if(field.isAnnotationPresent(GetFields.class))
				this.fields.add(field.getName());
		}
	}
	
}
