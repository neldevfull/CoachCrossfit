package br.com.coachcrossfit.beans.coach;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import br.com.coachcrossfit.beans.user.UserBean;
import br.com.coachcrossfit.database.GenericsDAO;
import br.com.coachcrossfit.models.Coach;
import br.com.coachcrossfit.models.User;
import br.com.coachcrossfit.reflections.Reflections;

@ManagedBean
@ViewScoped
public class CoachBean extends UserBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Coach coach;
	private String nameUser;
	private Reflections<Coach> coaRef;
	private Class<Coach> coaClas;
	private String table;
	private Field field;
	private Field[] fields;	
	private List<String> compareClass;
	private List<Object> valuesConditions;
	
	public CoachBean() {
		this.coach = this.loadCoach() != null ? this.loadCoach() : new Coach();
		this.nameUser = "";
		this.coaRef  = new Reflections<Coach>();
		this.coaClas = Coach.class;
		this.table   = "tb_coach";
		this.compareClass = new ArrayList<String>();
		this.valuesConditions = new ArrayList<Object>();
	}
	
	/**
	 * Busca informação do coach
	 */
	public Coach resultSetCoach(ResultSet result) throws SQLException{
		Coach coach = new Coach();
		while(result.next()){
			coach.setIdCoach(result.getInt("idCoach"));			
			coach.setCrefCoach(result.getString("crefCoach"));				
		}
		return coach;
	}
	
	/**
	 * Busca coach de acordo usuário
	 */
	public Coach selectCoach(GenericsDAO generics, User user) throws NoSuchFieldException, SecurityException, SQLException{					
		this.fields = this.coaClas.getDeclaredFields();				
		Field[] fieldsConditions = { super.userClas.getDeclaredField("idUser") };
		
		if(this.valuesConditions.size() > 0)
			this.valuesConditions.clear();
		
		this.valuesConditions.add(user.getIdUser());
		
		ResultSet result = generics.select(this.fields, fieldsConditions, this.valuesConditions, this.table);
		Coach coach = this.resultSetCoach(result);
		
		if(coach.getIdCoach() > 0){
			coach.setIdUser(user.getIdUser());
			coach.setNameUser(user.getNameUser());
			coach.setEmailUser(user.getEmailUser());
			coach.setPassUser(user.getPassUser());
			coach.setTypeUser(user.getTypeUser());
			coach.setStatusUser(user.getStatusUser());
			coach.setGenderUser(user.getGenderUser()); 
		}
				
		result.close();
		generics.closeStatement();
		
		return coach;
	}
	
	private Coach loadCoach(){
		HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		Coach coach = (Coach)session.getAttribute("coachLogged");
		return coach; 
	}
	
	public Coach getCoach() {
		return coach;
	}

	public void setCoach(Coach coach) {
		this.coach = coach;
	}		
	
	public void setNameUser(String nameCoach) {
		this.nameUser = nameCoach; 
	} 

	public String getNameUser(){	
		String[] name = this.coach.getNameUser().split(" ");
		this.nameUser= name[0]; 
		return this.nameUser;
	}
	
}
