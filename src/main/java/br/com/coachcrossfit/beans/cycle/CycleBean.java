package br.com.coachcrossfit.beans.cycle; 

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import br.com.coachcrossfit.beans.coach.CoachBean;
import br.com.coachcrossfit.database.connections.ConnectionFactory;
import br.com.coachcrossfit.database.dao.cycle.CycleDAO;
import br.com.coachcrossfit.database.dao.exercise.ExerciseDAO;
import br.com.coachcrossfit.models.Cycle;
import br.com.coachcrossfit.models.Day;
import br.com.coachcrossfit.models.Exercise;
import br.com.coachcrossfit.models.Training;
import br.com.coachcrossfit.models.Week;
import br.com.coachcrossfit.utilities.Util;
import br.com.coachcrossfit.validations.Validations;

@ManagedBean 
@ApplicationScoped
public class CycleBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private int idCoach;
	private Cycle cycle;
	private Week week;
	private Day day;
	private Training training;
	private Map<Integer, Training> trainingUp;
	private List<Exercise> exercises;
	private int nrTraining;
	private String exerciseTraining;
	private String actionTraining;
	private Date dateDay;
	private CycleDAO cycleDAO;
	private static int nrStudentORGroup;	
	private static int typeCycle;	
	private String trainingClicked;
	private boolean isUpdate;	
	private String labelDate;
	private String periodCycle;
	private String paymentCycle;
	private Util util;
	private Validations validations;	
		
	public CycleBean() {
		this.idCoach           = new CoachBean().loadCoach().getIdCoach();
		this.cycle 			   = new Cycle();	
		this.week              = new Week();		
		this.exercises         = new ArrayList<Exercise>();
		this.paymentCycle 	   = "";
		this.util              = new Util();
		this.validations 	   = new Validations();
	}
	
	/**
	 * Load Cycle for insert
	 */
	public String loadCycle(String url){
		this.cycle = new Cycle(); 
		this.periodCycle  = "";
		this.paymentCycle = ""; 					
		return url;
	}
	
	/**
	 * Load Cycle for update
	 */
	public String loadCycle(Cycle cycle, String url){
		this.cycle = cycle;
		this.paymentCycle = Float.toString(this.cycle.getPaymentCycle()); 				
		return url;
	}
	
	/**
	 * Load Weeks
	 */
	public String loadWeeks(Cycle cycle, String url){		
		this.cycle = cycle;
		try(Connection connection = new ConnectionFactory().getConnection()){
			// Connection for GenericsDAO
			this.cycleDAO = new CycleDAO(connection);	
			this.cycle.setWeeks(this.cycleDAO.seekWeeks(this.cycle, this.cycle.getWeeks()));			
		}		
		catch(Exception e){											
			System.out.println(e.getMessage());
		}		
		return url;
	}
	
	/**
	 * Register Cycle 
	 */
	public String cycleCad(String url) throws IOException{
		try(Connection connection = new ConnectionFactory().getConnection()){				
			this.cycleDAO = new CycleDAO(connection);
			// AutoCommit so there atomicity of data
			this.cycleDAO.getConnection().setAutoCommit(false);
			
			this.cycle.setIdCoach(this.idCoach);			
			this.cycle.setNrStudentORGroup(nrStudentORGroup);
			
			// Calcula fim do Ciclo
			this.cycle.setDateEndCycle(
					this.util.calculateWeek(this.cycle.getDateIniCycle(),
							Long.parseLong(this.periodCycle)));
			
			// TypeCycle for Student or Group
			this.cycle.setTypeCycle(typeCycle);
			
			// Converte pagamento para float
			this.cycle.setPaymentCycle(Float.parseFloat(this.paymentCycle));
			
			// Cadastra Ciclo
			this.week.setIdCycle(this.cycleDAO.cycleCad(this.cycle));
									
			// Add weeks
			this.createWeek(this.week.getIdCycle());
			
			// Create weeks
			this.cycleDAO.weekCad(this.cycle);
			
			// AutoCommit so there atomicity of data
			this.cycleDAO.getConnection().commit();
									
		}		
		catch(Exception e){			
			FacesContext.getCurrentInstance().addMessage("messages",  new FacesMessage("Falha ao cadastrar Ciclo"));				
		}
		
		return url;
	} 
	
	/**
	 * Update Cycle
	 */
	public void cycleUp(){
		try(Connection connection = new ConnectionFactory().getConnection()){
			// Connection for GenericsDAO
			this.cycleDAO = new CycleDAO(connection);	
			
			this.cycle.setPaymentCycle(Float.parseFloat(this.paymentCycle));
			
			this.cycleDAO.cycleUp(cycle);
			
			FacesContext.getCurrentInstance().addMessage("messages",  new FacesMessage("Alterado com Sucesso!"));
		}		
		catch(Exception e){						
			FacesContext.getCurrentInstance().addMessage("messages",  new FacesMessage("Falha ao alterar Ciclo"));		
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Insert or Update Day and Trainings
	 */
	public void trainingCad(){ 
		try(Connection connection = new ConnectionFactory().getConnection()){
			// Connection for GenericsDAO
			this.cycleDAO = new CycleDAO(connection);	
			// AutoCommit so there atomicity of data
			this.cycleDAO.getConnection().setAutoCommit(true);
			// Check that already exist for insert or update
			if (!(this.isUpdate)) {			
				this.cycleDAO.trainingCad(this.day);
				FacesContext.getCurrentInstance().addMessage("messages",  new FacesMessage("Cadastrado com Sucesso!"));
			}
			else {
				this.cycleDAO.trainingUp(this.trainingUp);
				FacesContext.getCurrentInstance().addMessage("messages",  new FacesMessage("Alterado com Sucesso!"));
			}																								
		}		
		catch(Exception e){						
			FacesContext.getCurrentInstance().addMessage("messages",  new FacesMessage("Falha ao cadastrar Treinamento"));		
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Add Training and Days list
	 */
	public void trainingAdd(){
		// Creates new training and assigns values
		this.training = new Training();
		this.training.setNrTraining(++nrTraining);		
		this.training.setIdCycle(this.week.getIdCycle());
		this.training.setNrWeek(this.week.getNrWeek());
		this.training.setDateDay(this.dateDay);
		this.training.setExerciseTraining(this.exerciseTraining);
		this.training.setActionTraining(this.actionTraining);
		// Add Training in Day and Day in Week
		this.day.getTrainings().add(this.training);
		// Add Day in Week if action is three
		if(!this.week.getDays().contains(this.day)) 
			this.week.getDays().add(this.day);	
		// Add Training in TrainingUp if different null
		if(this.isUpdate)
			this.trainingUp.put(1, this.training);
		// Initializes attributes
		this.exerciseTraining = "";
		this.actionTraining   = "";
	}
	
	/**
	 * Remove list of training
	 */
	public void removeTraining(Training training){
		// Add in TrainingUp
		if (this.isUpdate)	
			this.trainingUp.put(0, training);							
		this.day.getTrainings().remove(training);
	}
	
	/**
	 * Create Week for register
	 */
	private void createWeek(int idCycle){		
		int count = Integer.parseInt(this.periodCycle);
		Date date = this.cycle.getDateIniCycle();
		for(int i = 1; i <= count; i++){
			this.week = new Week();
			this.week.setNrWeek(i);
			this.week.setIdCycle(idCycle);
			this.week.setNameWeek("Semana "+i);
			if(i == 1)
				this.week.setDateIniWeek(date);
			else
				this.week.setDateIniWeek(date = this.util.sumDays(date, 1));				
			this.week.setDateEndWeek(date = this.util.sumDays(date, 6));
			this.cycle.getWeeks().add(this.week);			
		}		
	}
	
	/**
	 * Set date ini and end
	 */
	public String loadCalendar(Week week, String url){			
		// When insert
		if(this.cycle.getWeeks().contains(week))
			this.week = week;
		// When update
		if(this.cycle.getWeeks().get(week.getNrWeek() - 1).getDays().size() == 0)
			this.seekTrainings(week);
		
		return url;
	}
	
	/**
	 * Event after clicking calendar
	 */
	public void onDateSelect(SelectEvent event) throws ParseException, IOException {                
		// Convert data(event) for Calendar type
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String dateStr = format.format(event.getObject());        
        this.dateDay = format.parse(dateStr);                
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.dateDay);
                  
        // Handling to labelDate
        String numberDay   = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        String numberMonth = Integer.toString(calendar.get(Calendar.MONTH));
                
        if(numberDay.length() == 1)
        	numberDay = "0"+numberDay;
        if(numberMonth.length() == 1)
        	numberMonth = "0"+numberMonth;
        
        this.labelDate = numberDay+"/"+numberMonth;
        
        // Check exist, if not, create new Day
        if(!(this.checkDateDay())){
        	this.day = new Day();
        	this.loadTraining(calendar);
        }        
        	        
        FacesContext.getCurrentInstance().getExternalContext().redirect("Training.xhtml");
    }
	
	/**
	 * Check that day exist, it does not create new
	 */
	private boolean checkDateDay(){		
		this.isUpdate = false;
		for (Day day : this.week.getDays()){
			if(this.dateDay.equals(day.getDateDay())){
				this.day   		= day;									
				this.nrTraining = this.day.getTrainings().get(this.day.getTrainings().size() - 1).getNrTraining(); 	 			
				this.trainingUp	= new HashMap<Integer, Training>();
				this.isUpdate = true;
			}
		}		
		return this.isUpdate;
	}
		
	/**
	 * Assigns weekday name
	 */
	private void loadTraining(Calendar calendar){			
		this.nrTraining = 0;
		this.day.setDateDay(this.dateDay);
		this.day.setIdCycle(this.week.getIdCycle());
		this.day.setNrWeek(this.week.getNrWeek());
		
		if(Calendar.SUNDAY == calendar.get(Calendar.DAY_OF_WEEK)){
			this.day.setNameDay("Domingo");
		}
		else if(Calendar.MONDAY == calendar.get(Calendar.DAY_OF_WEEK)){
			this.day.setNameDay("Segunda");
		}
		else if(Calendar.TUESDAY == calendar.get(Calendar.DAY_OF_WEEK)){
			this.day.setNameDay("Terça");
		}
		else if(Calendar.WEDNESDAY == calendar.get(Calendar.DAY_OF_WEEK)){
			this.day.setNameDay("Quarta");
		}
		else if(Calendar.THURSDAY == calendar.get(Calendar.DAY_OF_WEEK)){
			this.day.setNameDay("Quinta");
		}
		else if(Calendar.FRIDAY == calendar.get(Calendar.DAY_OF_WEEK)){
			this.day.setNameDay("Sexta");
		}
		else if(Calendar.SATURDAY ==  calendar.get(Calendar.DAY_OF_WEEK)){
			this.day.setNameDay("Sábado");
		}
		
	}
	
	/**
	 * View Week
	 */
	public String viewWeek(Week week, String url){		
		if(this.cycle.getWeeks().get(week.getNrWeek() - 1).getDays().size() == 0){
			this.seekTrainings(week);			
		}	
		this.week = week;
		return url;
	}

	private void seekTrainings(Week week) {
		try(Connection connection = new ConnectionFactory().getConnection()){
			// Connection for GenericsDAO
			this.cycleDAO = new CycleDAO(connection);	
			
			this.cycleDAO.seekTrainings(week);																		
		}		
		catch(Exception e){								
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Suggest Exercises
	 */
	public List<String> suggestExercises(String suggest){		
		List<String> exercisesTraining = new ArrayList<String>();
		
		if(this.exercises.size() == 0){
			this.searchExerciseList();
		}
		
		for (Exercise exercise : this.exercises) {
			if(exercise.getNameExercise().toLowerCase().startsWith(suggest.toLowerCase()))
				exercisesTraining.add(exercise.getNameExercise());
		}
		return exercisesTraining;
	}
	
	/**
	 * Search Exercise list
	 */
	private void searchExerciseList(){
		try(Connection connection = new ConnectionFactory().getConnection()){
			this.exercises.clear();
			this.exercises = new ExerciseDAO(connection).listExercises(this.exercises, this.idCoach);			
		}		
		catch(Exception e){			
			// IMPLEMENTAR LOG
			System.out.println(e.getMessage());			
		}		
	}	

	public Cycle getCycle() {
		return cycle;
	}
	
	public Week getWeek() {
		return week;
	}
	
	public Day getDay() {
		return day;
	}
	
	public void setExerciseTraining(String exerciseTraining) {
		this.exerciseTraining = exerciseTraining;
	}
	
	public String getExerciseTraining() {
		return exerciseTraining;
	}
			
	public String getActionTraining() {
		return actionTraining;
	}
	
	public void setActionTraining(String actionTraining) {
		this.actionTraining = actionTraining;
	}
	
	public static void setNrStudentORGroup(int nrStudentORGroup) {
		CycleBean.nrStudentORGroup = nrStudentORGroup;		
	}
	
	public static int getNrStudentORGroup() {
		return nrStudentORGroup;
	}
	
	public static void setTypeCycle(int typeCycle) {
		CycleBean.typeCycle = typeCycle;
	}
	
	public void clicked(Training training) {
		this.trainingClicked = training.getExerciseTraining() + " - " +
				training.getActionTraining();
	}
	
	public String getTrainingClicked() {
		return trainingClicked;
	}
	
	public String getPeriodCycle() {
		return periodCycle;
	}
	
	public String getLabelDate() {
		return "Dia: "+labelDate;
	}
	
	public void setPeriodCycle(String periodCycle) {
		this.periodCycle = periodCycle;
	}
		
	public String getPaymentCycle() {
		return paymentCycle;
	}
	
	public void setPaymentCycle(String paymentCycle) {
		this.paymentCycle = paymentCycle;
	}

	public Validations getValidations() {
		return validations;
	}
				
}
  