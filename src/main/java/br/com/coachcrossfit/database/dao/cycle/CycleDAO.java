package br.com.coachcrossfit.database.dao.cycle;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
	private String conditions;
	private Class<Cycle> cycClas;
	private Class<Week> weeClas;
	private Class<Day> dayClas;
	private Class<Training> traClas;
	private List<String> fields;
	private List<String> fieldsConditions;
	private List<String> joinsConditions;
	private List<Object> values;
	private List<Object> valuesConditions;
	
	public CycleDAO(Connection connection) {
		super(connection);		
		// Initialization of attributes
		this.table 			  = "tb_cycle";
		this.conditions 	  = "";
		this.cycClas 		  = Cycle.class;
		this.weeClas          = Week.class;
		this.dayClas          = Day.class;
		this.traClas		  = Training.class;
		this.fields 	 	  = new ArrayList<String>();
		this.fieldsConditions = new ArrayList<String>();
		this.joinsConditions  = new ArrayList<String>();
		this.values 		  = new ArrayList<Object>();
		this.valuesConditions = new ArrayList<Object>();
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
	 * Update the Cycle
	 */
	public void cycleUp(Cycle cycle) throws SQLException{
		this.fields.clear();
		this.fields.add("nameCycle");
		this.fields.add("paymentCycle");
		this.fields.add("desCycle");
		this.fields.add("idCycle");
		
		this.values.clear();
		this.values.add(cycle.getNameCycle());
		this.values.add(cycle.getPaymentCycle());
		this.values.add(cycle.getDesCycle());
		this.values.add(cycle.getIdCycle());
		
		super.update(this.fields, this.values, this.table);
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
	 * Seek Weeks
	 */
	public List<Week> seekWeeks(Cycle cycle, List<Week> weeks) throws SQLException{
		// Get Fields
		this.loadFields(this.weeClas);
		// Get Fields Conditions
		this.fieldsConditions.clear();
		this.fieldsConditions.add("idCycle");
		// Add Values Conditions
		this.valuesConditions.clear();
		this.valuesConditions.add(cycle.getIdCycle());
		// Assigns Condition
		this.conditions = " ORDER BY nrWeek ASC";
		
		ResultSet result = super.select(this.fields, this.fieldsConditions, this.valuesConditions, "tb_week", this.conditions);
		this.resultSetWeeks(result, weeks);
		
		super.closeResultSet();
		super.closeStatement();
		
		return weeks;
	}
	
	/**
	 * ResultSet the Weeks
	 */
	private List<Week> resultSetWeeks(ResultSet result, List<Week> weeks) throws SQLException{
		while(result.next()){
			Week week = new Week();
			week.setNrWeek(result.getInt("nrWeek"));
			week.setIdCycle(result.getInt("idCycle"));
			week.setNameWeek(result.getString("nameWeek"));
			week.setDateIniWeek(result.getDate("dateIniWeek"));
			week.setDateEndWeek(result.getDate("dateEndWeek"));
			weeks.add(week);
		}
		return weeks;
	}
	
	/**
	 * Seek Trainings
	 */
	public void seekTrainings(Week week) throws SQLException{
		// Get Fields
		this.fields.clear();
		this.fields.add("*");
		
		this.fieldsConditions.clear();
		this.fieldsConditions.add("tb_day.nrWeek");
		this.fieldsConditions.add("tb_day.idCycle");
		
		this.valuesConditions.clear();
		this.valuesConditions.add(week.getNrWeek());
		this.valuesConditions.add(week.getIdCycle());
		
		this.joinsConditions.clear();
		this.joinsConditions.add("dateDay");
		this.joinsConditions.add("idCycle");
		this.joinsConditions.add("nrWeek");
		
		this.conditions = " ORDER BY tb_day.dateDay ASC ";
		
		ResultSet result = super.join(this.fields, this.fieldsConditions, this.valuesConditions, this.joinsConditions,
				new ArrayList<String>(), " JOIN ", "tb_day", "tb_training", "", this.conditions);
		
		this.resultSetWeek(week, result);
		
		super.closeResultSet();
		super.closeStatement();
	}
	
	/**
	 * ResultSet of the Week
	 */
	private Week resultSetWeek(Week week, ResultSet result) throws SQLException{		
		Date date = new Date();
		Day day   = new Day();
		while(result.next()){			
			date = result.getDate("dateDay");
			if(!date.equals(day.getDateDay())){
				day = new Day();
				day.setDateDay(date);
				day.setIdCycle(result.getInt("idCycle"));
				day.setNrWeek(result.getInt("nrWeek"));
				day.setNameDay(result.getString("nameDay"));
			}
									
			Training training = new Training();
			training.setNrTraining(result.getInt("nrTraining"));
			training.setIdCycle(result.getInt("idcycle"));
			training.setNrWeek(result.getInt("nrWeek"));
			training.setDateDay(result.getDate("dateDay"));
			training.setExerciseTraining(result.getString("exerciseTraining"));
			training.setActionTraining(result.getString("actionTraining"));
			
			day.getTrainings().add(training);
			
			if(!week.getDays().contains(day))
				week.getDays().add(day);
		}
		return week;
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
	
	public List<Cycle> listCycles(List<Cycle> cycles, int idCoach, int nrStudentOrGroup) throws SQLException{
		// Get fields
		this.loadFields(cycClas);
		this.fields.add("idCycle");
		
		this.fieldsConditions.clear();
		this.fieldsConditions.add("idCoach");
		this.fieldsConditions.add("nrStudentOrGroup");
		
		this.valuesConditions.clear();
		this.valuesConditions.add(idCoach);
		this.valuesConditions.add(nrStudentOrGroup);
		
		this.conditions = " ORDER BY idCycle DESC";
		
		ResultSet result = super.select(this.fields, this.fieldsConditions, this.valuesConditions, this.table, this.conditions);
		this.resultSetCycles(cycles, result);
		
		super.closeResultSet();
		super.closeStatement();
		
		return cycles;
	}
	
	private List<Cycle> resultSetCycles(List<Cycle> cycles, ResultSet result) throws SQLException{
		while(result.next()){
			Cycle cycle = new Cycle();
			cycle.setIdCycle(result.getInt("idCycle"));
			cycle.setIdCoach(result.getInt("idCoach"));
			cycle.setNrStudentORGroup(result.getInt("nrStudentORGroup"));
			cycle.setNameCycle(result.getString("nameCycle"));
			cycle.setDateIniCycle(result.getDate("dateIniCycle"));
			cycle.setDateEndCycle(result.getDate("dateEndCycle"));
			cycle.setTypeCycle(result.getInt("typeCycle"));
			cycle.setPaymentCycle(result.getFloat("paymentCycle"));
			cycle.setDesCycle(result.getString("desCycle"));
			cycles.add(cycle);
		}
		return cycles;
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
