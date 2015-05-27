package br.com.coachcrossfit.beans.exercises;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.com.coachcrossfit.beans.coach.CoachBean;
import br.com.coachcrossfit.database.connections.ConnectionFactory;
import br.com.coachcrossfit.database.dao.exercises.ExerciseDAO;
import br.com.coachcrossfit.models.Exercise;

@ManagedBean
@ViewScoped
public class ExercisesBean {

	private List<Exercise> exercises;
	private ExerciseDAO exerciseDAO;
	
	public ExercisesBean() {
		this.exercises = new LinkedList<Exercise>();
		
		try(Connection connection = new ConnectionFactory().getConnection()){			
			this.exerciseDAO = new ExerciseDAO(connection);
			this.exercises = this.exerciseDAO.listExercises(exercises, new CoachBean().loadCoach().getIdCoach());
		}		
		catch(Exception e){			
			FacesContext.getCurrentInstance().addMessage("messages",  new FacesMessage(e.getMessage().toString()));			
		}
	}
	
	public List<Exercise> getExercises() {
		return exercises;
	}
	
}
