package br.com.coachcrossfit.beans.student;  

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import br.com.coachcrossfit.beans.coach.CoachBean;
import br.com.coachcrossfit.beans.cycle.CycleBean;
import br.com.coachcrossfit.beans.user.UserBean;
import br.com.coachcrossfit.database.connections.ConnectionFactory;
import br.com.coachcrossfit.database.dao.generics.GenericsDAO;
import br.com.coachcrossfit.models.Student;
import br.com.coachcrossfit.reflections.Reflections;
import br.com.coachcrossfit.validations.Validations;
  
@ManagedBean
@SessionScoped
public class StudentBean extends UserBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private Student student;
	private CoachBean coachBean;
	private Validations validations;
	private Reflections<Student> stuRef;	
	private Class<Student> stuClas;
	private String table;
	private String join;	
	private String conditions;
	private List<String> fieldsList;
	private List<String> joinsConditions;
	private List<String> joinsConditionsC;
	private List<String> fieldsConditions;
	private Field[] fields;
	private List<Object> values;	
	private List<Object> valuesConditions;
	
	public StudentBean() {
		this.student     	  = new Student();
		this.coachBean   	  = new CoachBean();
		this.validations 	  = new Validations();
		this.stuRef 	 	  = new Reflections<Student>();
		this.stuClas 	 	  = Student.class;
		this.table 		 	  = "tb_student";
		this.join             = " INNER JOIN ";		
		this.conditions       = "";
		this.fieldsList       = new ArrayList<String>();
		this.joinsConditions  = new ArrayList<String>();
		this.joinsConditionsC = new ArrayList<String>();
		this.fieldsConditions = new ArrayList<String>();
		this.values           = new ArrayList<Object>();		
		this.valuesConditions = new ArrayList<Object>();			
	}
	
	/**
	 * Método definição 
	 */
	public void studentMain(){
		if(this.student.getIdStudent() > 0)
			this.studentUp();
		else			
			this.studentReg();
	}
	
	/**
	 * Altera aluno
	 */
	public void studentUp(){
		try(Connection connection = new ConnectionFactory().getConnection()){			
			// AutoCommit para que haja atomicidade dos dados
			connection.setAutoCommit(false);
			
			// Cria GenericsDAO e Update Usuário
			GenericsDAO generics = new GenericsDAO(connection);
			
			// Limpa listas
			this.fieldsList.clear(); 
			this.values.clear();
			
			// Carrega campos do usuário
			this.fieldsList.add("nameUser");
			this.fieldsList.add("emailUser");
			this.fieldsList.add("genderUser");
			this.fieldsList.add("statusUser");
			this.fieldsList.add("idUser");
			
			// Carrega valores do usuário
			this.values.add(this.student.getNameUser());
			this.values.add(this.student.getEmailUser());
			this.values.add(this.student.getGenderUser());
			this.values.add(this.student.getStatusUser());
			this.values.add(this.student.getIdUser());
			
			// Faz atualização do usuário
			generics.update(this.fieldsList, this.values, super.table);
			
			// Commit para que haja atomicidade dos dados
			connection.commit();
			
			// Mensagem Sucesso!
			FacesContext.getCurrentInstance().addMessage("messages",  new FacesMessage("Sucesso ao atualizar"));

		}
		catch(Exception e){
			FacesContext.getCurrentInstance().addMessage("messages",  new FacesMessage(e.getMessage().toString()));
		}
	}
	
	/**
	 * Registra aluno
	 */
	private void studentReg() {								
		try(Connection connection = new ConnectionFactory().getConnection()){
			// Atribui valores padrão
			this.assignValuesDefault();
			
			// Gera a chave que será utilizada como senha e tipo aluno 2
			this.student.setPassUser(super.generateKey(this.student.getDateBirthStudent()));	
			
			// Aluno é do tipo dois
			this.student.setTypeUser(2);
			
			// AutoCommit para que haja atomicidade dos dados
			connection.setAutoCommit(false);
			
			// Cria GenericsDAO e Insert Usuário
			GenericsDAO generics = new GenericsDAO(connection);
			
			// Inseri usuário
			int idUser =  this.insertUser(generics);
			
			// Inseri aluno
			this.insertStudent(generics, idUser);								
			
			// Commit para que haja atomicidade dos dados
			connection.commit();
			
			// Mensagem Sucesso!
			FacesContext.getCurrentInstance().addMessage("messages",  new FacesMessage("Cadastrado com Sucesso!"));
			
			// Inicializa aluno
			this.student = new Student();
		}
		catch(Exception e){
			FacesContext.getCurrentInstance().addMessage("messages",  new FacesMessage(e.getMessage().toString()));			
		}			
	}

	private List<Student> resultSetStudents(List<Student> students, ResultSet result) throws SQLException{
		while(result.next()){
			Student student = new Student();	
			student.setIdUser(result.getInt("idUser"));
			student.setNameUser(result.getString("nameUser"));
			student.setEmailUser(result.getString("emailUser"));
			student.setPassUser(result.getString("passUser"));
			student.setStatusUser(result.getInt("statusUser"));
			student.setTypeUser(result.getInt("typeUser"));
			student.setImageUser(result.getString("imageUser"));
			student.setGenderUser(result.getInt("genderUser"));
			student.setIdStudent(result.getInt("idStudent"));
			student.setIdUser(result.getInt("idUser"));
			student.setIdCoach(result.getInt("idCoach"));			
			student.setWeightStudent(result.getFloat("weightStudent"));
			student.setDateBirthStudent(result.getDate("dateBirthStudent"));
			student.setHeightStudent(result.getFloat("heightStudent"));						
			students.add(student);
		}
		return students;
	}
	
	/**
	 * Carrega o objeto para alteração 	  
	 */
	public String loadStudent(Student student, String url){
		this.student = student; 
		return url;  
	} 
	
	/**
	 * Carrega o objeto para criação 	  
	 */
	public String loadStudent(String url){
		this.student = new Student();
		return url;
	}
	
	public String loadCycle(String url) throws IOException{		
		CycleBean.setNrStudentORGroup(this.student.getIdStudent());
		// Type Student for Cycle
		CycleBean.setTypeCycle(1);
		return url; 
	}
	 	
	/**
	 * Atribui valor aos campos nulos
	 */
	private void assignValuesDefault(){
		if(this.student.getDateBirthStudent() == null)
			this.student.setDateBirthStudent(new Date());
		if(this.student.getImageUser() == null)
			this.student.setImageUser("");		
	}

	/**
	 * Faz a inserção de alunos
	 */
	private void insertStudent(GenericsDAO generics, int idUser) throws SQLException {								
		this.fields = this.stuRef.getAllLessId(this.stuClas);
						
		// Limpa valores caso haja
		if(this.values.size() > 0)
			this.values.clear();
		
		// Atribui a lista os valores de usuário
		this.values.add(idUser);
		this.values.add(this.coachBean.getCoach().getIdCoach());		
		this.values.add(student.getWeightStudent());
		this.values.add(student.getDateBirthStudent());
		this.values.add(student.getHeightStudent());				
		
		// Insert Aluno
		generics.insert(this.fields, this.values, this.table);
	}
	
	/**
	 * inseri usuário
	 */
	private int insertUser(GenericsDAO generics) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException {		
		int idUser = 0;
		// Busca os campos de UserBean menos o id da Classe					
		this.fields = super.userRef.getAllLessId(super.userClas);
		
		// Atribui a lista os valores de usuário
		if(super.values.size() > 0)
			super.values.clear();
		
		super.values = new ArrayList<Object>();
		super.values.add(this.student.getNameUser());
		super.values.add(this.student.getEmailUser()); 
		// Faz o hash da senha		
		super.values.add(super.util.hashPass(this.student.getPassUser()));		
		super.values.add(this.student.getTypeUser());
		super.values.add(this.student.getStatusUser());
		super.values.add(this.student.getImageUser());
		super.values.add(this.student.getGenderUser());
																
		idUser = generics.insertReturnId(this.fields, super.values, super.table);										
		return idUser;
	}
	
	/**
	 * Busca uma lista de estudantes
	 */
	public List<Student> studentsList(List<Student> students, GenericsDAO generics) throws NoSuchFieldException, SecurityException, SQLException{
		// Todos os campos selecionados
		if(this.fieldsList.size() > 0)
			this.fieldsList.clear();		
		this.fieldsList.add("*");
		// Carrega condição
		if(this.fieldsConditions.size() > 0)
			this.fieldsConditions.clear();
		this.fieldsConditions.add(this.table+".idCoach");
		// Carrega valores de condição
		if(this.valuesConditions.size() > 0)
			this.valuesConditions.clear();
		this.valuesConditions.add(this.coachBean.loadCoach().getIdCoach());
		// Condição de junção
		if(this.joinsConditions.size() > 0)
			this.joinsConditions.clear();
		// Atribui a condição de junção ao usuário
		this.joinsConditions.add("idUser");
		this.conditions = " ORDER BY statusUser ASC, idStudent DESC ";
		
		ResultSet result = generics.join(this.fieldsList, this.fieldsConditions, this.valuesConditions,
									     this.joinsConditions, this.joinsConditionsC, this.join, super.table, this.table, "",
									     this.conditions);		

		students = this.resultSetStudents(students, result);		
		this.fullClose(result, generics);
		
		return students;
	}
	
	// Close ResultSet e GenericsDAO
	private void fullClose(ResultSet result, GenericsDAO generics) throws SQLException{
		result.close();
		generics.closeStatement();
	}
	
	public Student getStudent() {
		return student;
	}
	
	public void setStudent(Student student) {
		this.student = student;
	}

	public Validations getValidations() {
		return validations;
	}

	public void setValidations(Validations validations) {
		this.validations = validations;
	}
			
}
 