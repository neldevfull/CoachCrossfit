package br.com.coachcrossfit.beans.exercises;

import java.io.Serializable;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import br.com.coachcrossfit.beans.coach.CoachBean;
import br.com.coachcrossfit.database.connections.ConnectionFactory;
import br.com.coachcrossfit.database.dao.exercises.ExerciseDAO;
import br.com.coachcrossfit.models.Exercise;
import br.com.coachcrossfit.validations.Validations;

@ManagedBean
@SessionScoped
public class ExerciseBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private Exercise exercise;
	private List<Exercise> exercises;
	private ExerciseDAO exerciseDAO;
	private Validations validation;
	
	public ExerciseBean() {
		this.exercise 	= new Exercise();
		this.exercises  = new LinkedList<Exercise>();		
		this.validation = new Validations();
	}
	
	/**
	 * Método principal
	 */
	public void exerciseMain(){
		if(this.exercise.getIdExercise() > 0)
			this.exerciseUp();
		else
			this.exerciseCad();
	}
	
	/**
	 * Cadastra Exercício
	 */
	public void exerciseCad(){
		try(Connection connection = new ConnectionFactory().getConnection()){
			this.exerciseDAO = new ExerciseDAO(connection);
			
			this.exercise.setIdCoach(new CoachBean().loadCoach().getIdCoach());
			this.exerciseDAO.exerciseCad(this.exercise);
			
			FacesContext.getCurrentInstance().addMessage("messages",  new FacesMessage("Cadastrado com Sucesso!"));
			
			this.exercise = new Exercise();
			
		}		
		catch(Exception e){			
			FacesContext.getCurrentInstance().addMessage("messages",  new FacesMessage("Falha ao cadastrar exercício"));			
		}
	}
	
	/**
	 * Altera Exercício
	 */
	public void exerciseUp(){
		try(Connection connection = new ConnectionFactory().getConnection()){
			this.exerciseDAO = new ExerciseDAO(connection); 			
			this.exerciseDAO.exerciseUp(this.exercise);
			
			FacesContext.getCurrentInstance().addMessage("messages",  new FacesMessage("Alterado com Sucesso!")); 						
		}		
		catch(Exception e){			
			FacesContext.getCurrentInstance().addMessage("messages",  new FacesMessage("Falha ao cadastrar exercício"));			
		}
	}
	
	/**
	 * Carrega tela de cadastro
	 */
	public String loadExercise(String url){	
		this.exercise = new Exercise();
		return url;
	}
	
	/**
	 * Carrega tela de alteração
	 */
	public String loadExercise(Exercise exercise, String url){
		this.exercise = exercise;
		return url; 
	}
	
	public String deleteExercise(Exercise exercise){
		try(Connection connection = new ConnectionFactory().getConnection()){
			this.exerciseDAO = new ExerciseDAO(connection);
			this.exerciseDAO.deleteExercise(exercise);
		}		
		catch(Exception e){			
			FacesContext.getCurrentInstance().addMessage("messages",  new FacesMessage(e.getMessage().toString()));			
		}
		return "/manager/exercises/Exercises.xhtml?faces-redirect=true";
	}

	public Exercise getExercise() {
		return exercise;
	}
	
	public List<Exercise> getExercises() {
		return exercises;
	}

	public Validations getValidation() {
		return validation;
	}
		
}
