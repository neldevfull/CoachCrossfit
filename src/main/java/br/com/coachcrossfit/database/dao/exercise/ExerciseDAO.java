package br.com.coachcrossfit.database.dao.exercise;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.coachcrossfit.database.dao.generics.GenericsDAO;
import br.com.coachcrossfit.models.Exercise;
import br.com.coachcrossfit.reflections.Reflections;

public class ExerciseDAO extends GenericsDAO{
	
	private String table;
	private String conditions;
	private Class<Exercise> exeClas;
	private Reflections<Exercise> exerRef;
	private Field[] fields;
	private List<String> fieldsList;
	private List<Object> valuesConditions;
	private List<Object> values;
	
	public ExerciseDAO(Connection connection) {
		super(connection);
		// Inicialização dos atributos
		this.table      	  = "tb_exercise";
		this.conditions 	  = "";
		this.exeClas 		  = Exercise.class;
		this.exerRef          = new Reflections<Exercise>();
		this.fieldsList 	  = new ArrayList<String>();
		this.valuesConditions = new ArrayList<Object>();
		this.values  		  = new ArrayList<Object>();				
	}
	
	/**
	 * Update Exercise
	 */
	public void exerciseUp(Exercise exercise) throws SQLException{
		// Adiciona os campos
		this.fieldsList.clear();
		this.fieldsList.add("nameExercise");		
		this.fieldsList.add("idExercise");
		
		// Adiciona os valores
		this.values.clear();
		this.values.add(exercise.getNameExercise());		
		this.values.add(exercise.getIdExercise());
		
		// Altera Exercício 
		super.update(this.fieldsList, this.values, this.table);
	}
	
	/**
	 * Insert Exercise
	 */
	public void exerciseCad(Exercise exercise) throws SQLException{
		// Busca todos os campos
		this.fields = exerRef.getAllLessId(this.exeClas);
		
		// Adiciona valores
		this.values.clear();
		this.values.add(exercise.getIdCoach());
		this.values.add(exercise.getNameExercise());		
		
		// Inseri Exercício
		super.insert(this.fields, this.values, this.table);
	}
	
	/**
	 * Busca todos os exercícios
	 */
	public List<Exercise> listExercises(List<Exercise> exercises, int idCoach) throws SQLException, NoSuchFieldException, SecurityException{
		// Busca todos os campos
		this.fields = exeClas.getDeclaredFields();
		// Campo(s) de condição
		Field field = exeClas.getDeclaredField("idCoach");
		Field[] fieldsConditions = { field };
		// Valore(s) de condição
		this.valuesConditions.clear();
		this.valuesConditions.add(idCoach);
		// Condições
		this.conditions = " ORDER BY idExercise DESC";
		// Busca valores na table
		ResultSet result = super.select(this.fields, fieldsConditions, this.valuesConditions, this.table, this.conditions);
		exercises = this.resultSetExercises(exercises, result);
		
		super.closeResultSet();
		super.closeStatement();
		
		return exercises;
	}
	
	/**
	 * Lista todos os exercícios
	 */
	private List<Exercise> resultSetExercises(List<Exercise> exercises, ResultSet result) throws SQLException{
		while(result.next()){
			Exercise exercise = new Exercise();
			exercise.setIdExercise(result.getInt("idExercise"));
			exercise.setIdCoach(result.getInt("idCoach"));		
			exercise.setNameExercise(result.getString("nameExercise"));			
			exercises.add(exercise);
		}		
		return exercises;
	}
	
	/**
	 * Deleta Exercício
	 */
	public void deleteExercise(Exercise exercise) throws SQLException{
		// Adiciona campo
		this.fieldsList.clear();
		this.fieldsList.add("idExercise");
		// Adiciona valor
		this.values.clear();
		this.values.add(exercise.getIdExercise());
		// Deleta Exercício
		super.delete(this.fieldsList, this.values, this.table);
	}
	
}
