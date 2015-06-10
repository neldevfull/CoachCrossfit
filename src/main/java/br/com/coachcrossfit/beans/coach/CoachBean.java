package br.com.coachcrossfit.beans.coach;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import br.com.coachcrossfit.beans.user.UserBean;
import br.com.coachcrossfit.database.connections.ConnectionFactory;
import br.com.coachcrossfit.database.dao.generics.GenericsDAO;
import br.com.coachcrossfit.models.Coach;
import br.com.coachcrossfit.models.User;
import br.com.coachcrossfit.utilities.GetFields;

@ManagedBean
@ViewScoped
public class CoachBean extends UserBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Coach coach;
	private String nameUser;
	private Class<Coach> coaClas;
	private String table;
	private List<String> fields;
	private List<String> fieldsConditions;
	private List<Object> valuesConditions;
	private String conditions;
	
	public CoachBean() {		
		this.nameUser 		  = "";		
		this.coaClas 		  = Coach.class;
		this.table   		  = "tb_coach";	
		this.fields 		  = new ArrayList<String>();
		this.fieldsConditions = new ArrayList<String>();
		this.valuesConditions = new ArrayList<Object>();
		this.conditions 	  = "";
		this.coach 			  = this.loadCoach();		
	}
	
	/**
	 * Busca informação do coach
	 */
	private Coach resultSetCoach(ResultSet result) throws SQLException{
		Coach coach = new Coach();
		while(result.next()){
			coach.setIdCoach(result.getInt("idCoach"));		
			coach.setIdUser(result.getInt("idUser"));
			coach.setCrefCoach(result.getString("crefCoach"));				
		}
		return coach;
	}
	
	/**
	 * Busca coach de acordo com usuário
	 */
	public Coach selectCoach(GenericsDAO generics, User user) throws NoSuchFieldException, SecurityException, SQLException{							
		this.loadFields(coaClas);
		
		this.fieldsConditions.clear();
		this.fieldsConditions.add("idUser");
				
		if(this.valuesConditions.size() > 0)
			this.valuesConditions.clear();
		
		this.valuesConditions.add(user.getIdUser());		
		
		ResultSet result = generics.select(this.fields, this.fieldsConditions, this.valuesConditions, this.table, this.conditions);
		Coach coach = this.resultSetCoach(result);
		
		// Se achar o coach e não tiver usuário setado
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
	
	/**
	 * Loads fields according to annotation GetFields
	 */
	private void loadFields(Class<?> classGenerics){		
		this.fields.clear();
		this.fields.add("idCoach");
		for(Field field : classGenerics.getDeclaredFields()){
			if(field.isAnnotationPresent(GetFields.class))
				this.fields.add(field.getName());
		}
	}
	
	public Coach loadCoach(){
		HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		User user = (User) session.getAttribute("userLogged");
		Coach coach = new Coach();
		
		try(Connection connection = new ConnectionFactory().getConnection()){									
			// Cria GenericsDAO e Update Usuário
			GenericsDAO generics = new GenericsDAO(connection);
			
			coach = this.selectCoach(generics, user);
			
		}
		catch(Exception e){
			FacesContext.getCurrentInstance().addMessage("messages",  new FacesMessage(e.getMessage().toString()));
		}
		
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
