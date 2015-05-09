package br.com.coachcrossfit.beans.student;  

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
import br.com.coachcrossfit.beans.user.UserBean;
import br.com.coachcrossfit.database.ConnectionFactory;
import br.com.coachcrossfit.database.GenericsDAO;
import br.com.coachcrossfit.models.Student;
import br.com.coachcrossfit.models.User;
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
	private List<String> fieldsConditions;
	private Field field;
	private Field[] fields;
	private List<Object> values;
	private List<String> compareClass;	
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
		this.fieldsConditions = new ArrayList<String>();
		this.values           = new ArrayList<Object>();
		this.compareClass     = new ArrayList<String>();
		this.valuesConditions = new ArrayList<Object>();			
	}
	
	/**
	 * Método definição 
	 */
	public void studentMain(){
		if(this.student.getIdStudent() > 0)
			this.studentUp();
		else			
			this.studentRes();
	}
	
	/**
	 * Altera aluno
	 */
	public void studentUp(){
		try(Connection connection = new ConnectionFactory().getConnection()){
			// Limpa lista caso haja valores
			if(this.fieldsList.size() > 0)
				this.fieldsList.clear();
			
			// Carrega campos do aluno
			this.fieldsList.add("nameStudent");
			this.fieldsList.add("genderStudent");
			this.fieldsList.add("idStudent");
			
			// Limpa lista caso haja valores
			if(this.values.size() > 0)
				this.values.clear();
			
			// Carrega valores do aluno
			this.values.add(this.student.getNameStudent());
			this.values.add(this.student.getGenderStudent());
			this.values.add(this.student.getIdStudent());
			
			// AutoCommit para que haja atomicidade dos dados
			connection.setAutoCommit(false);
			
			// Cria GenericsDAO e Insert Usuário
			GenericsDAO generics = new GenericsDAO(connection);
			
			// Faz atualização do aluno
			generics.update(this.fieldsList, this.values, this.table);
			
			// Limpa listas
			this.fieldsList.clear(); 
			this.values.clear();
			
			// Carrega campos do usuário
			this.fieldsList.add("emailUser");
			this.fieldsList.add("statusUser");
			this.fieldsList.add("idUser");
			
			// Carrega valores do usuário
			this.values.add(this.student.getUser().getEmailUser());
			this.values.add(this.student.getUser().getStatusUser());
			this.values.add(this.student.getUser().getIdUser());
			
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
	private void studentRes() {								
		try(Connection connection = new ConnectionFactory().getConnection()){
			// Atribui valores padrão
			this.assignValuesDefault();
			
			// Gera a chave que será utilizada como senha e tipo aluno 2
			this.student.getUser().setPassUser(new UserBean().generateKey(this.student.getDateBirthStudent()));	
			
			// Aluno é do tipo dois
			this.student.getUser().setTypeUser(2);
			
			// AutoCommit para que haja atomicidade dos dados
			connection.setAutoCommit(false);
			
			// Cria GenericsDAO e Insert Usuário
			GenericsDAO generics = new GenericsDAO(connection);
			
			// Inseri usuário
			this.insertUser(generics);
			
			// Busca idUser
			int idUser = this.selectIdUser(generics);
			
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
	
	/**
	 * Busca registro de determinado aluno	 
	 */
	public Student resultSetStudent(Student student, ResultSet result) throws SQLException{
		 while(result.next()){			
			student.setIdUser(result.getInt("idUser"));
			student.setIdCoach(result.getInt("idCoach"));
			student.setNameStudent(result.getString("nameStudent"));
			student.setWeightStudent(result.getFloat("weightStudent"));
			student.setDateBirthStudent(result.getDate("dateBirthStudent"));
			student.setHeightStudent(result.getFloat("heightStudent"));
			student.setImageStudent(result.getString("imageStudent"));
			student.setGenderStudent(result.getInt("genderStudent"));
		 }		 
		 return student;
	}
	
	private List<Student> resultSetStudents(List<Student> students, ResultSet result) throws SQLException{
		while(result.next()){
			Student student = new Student();	
			student.getUser().setIdUser(result.getInt("idUser"));
			student.getUser().setEmailUser(result.getString("emailUser"));
			student.getUser().setPassUser(result.getString("passUser"));
			student.getUser().setStatusUser(result.getInt("statusUser"));
			student.getUser().setTypeUser(result.getInt("typeUser"));
			student.setIdStudent(result.getInt("idStudent"));
			student.setIdUser(result.getInt("idUser"));
			student.setIdCoach(result.getInt("idCoach"));
			student.setNameStudent(result.getString("nameStudent"));
			student.setWeightStudent(result.getFloat("weightStudent"));
			student.setDateBirthStudent(result.getDate("dateBirthStudent"));
			student.setHeightStudent(result.getFloat("heightStudent"));
			student.setImageStudent(result.getString("imageStudent"));
			student.setGenderStudent(result.getInt("genderStudent"));			
			students.add(student);
		}
		return students;
	}
	
	/**
	 * Carrega o objeto para alteração 	  
	 */
	public String loadStudent(Student student){
		this.student = student;
		return "/screen/students/StudentReg?faces-redirect=true";
	}
	
	/**
	 * Carrega o objeto para criação 	  
	 */
	public String loadStudent(){
		this.student = new Student();
		return "/screen/students/StudentReg?faces-redirect=true";
	}
	
	/**
	 * Atribui valor aos campos nulos
	 */
	private void assignValuesDefault(){
		if(this.student.getDateBirthStudent() == null)
			this.student.setDateBirthStudent(new Date());
	}

	/**
	 * Faz a inserção de alunos
	 */
	private void insertStudent(GenericsDAO generics, int idUser) throws SQLException {		
		// Utiliza compareClass para não busca atributo de associação
		if(this.compareClass.size() > 0)
			this.compareClass.clear();
		
		this.compareClass.add("user");				
		this.fields = this.stuRef.getAllLessId(this.stuClas, this.compareClass);
						
		// Limpa valores caso haja
		if(this.values.size() > 0)
			this.values.clear();
		
		// Atribui a lista os valores de usuário
		this.values.add(idUser);
		this.values.add(this.coachBean.getCoach().getIdCoach());
		this.values.add(student.getNameStudent());
		this.values.add(student.getWeightStudent());
		this.values.add(student.getDateBirthStudent());
		this.values.add(student.getHeightStudent());
		this.values.add("imgDefault"/*student.getImageStudent()*/);	
		this.values.add(student.getGenderStudent());
		
		// Insert Aluno
		generics.insert(this.fields, this.values, this.table);
	}
	
	/**
	 * Seleciona um aluno	 
	 */
	public Student selectStudent(GenericsDAO generics, User user) throws NoSuchFieldException, SecurityException, SQLException{				
		// Utiliza compareClass para não busca atributo de associação
		loadCompareClass(); 
		// Atribui valor ao campo
		this.field  = this.stuClas.getDeclaredField("idUser");
		Field[] fieldsConditions = { this.field };
		
		if(this.valuesConditions.size() > 0)
			this.valuesConditions.clear();
		
		this.valuesConditions.add(user.getIdUser());
		
		ResultSet result = generics.select(this.fields, fieldsConditions, this.valuesConditions, this.table);				
		this.resultSetStudent(this.student, result);
		
		// Se aluno OK, usuário é atribuido
		if(this.student.getIdStudent() > 0)
			this.student.setUser(user);
						
		result.close();
		generics.closeStatement();
						
		return this.student;
	}
	
	/**
	 * Utiliza compareClass para não busca atributo de associação
	 */
	private void loadCompareClass() {		
		this.compareClass = new ArrayList<String>();
		this.compareClass.add("user");	
		this.fields = this.stuRef.getAllLessId(this.stuClas, this.compareClass);
	}

	/**
	 * Seleciona aluno por idUser
	 */
	private int selectIdUser(GenericsDAO generics) throws NoSuchFieldException, SQLException {		
		// Busca o idUser do aluno
		this.field = super.userClas.getDeclaredField("idUser");
		Field[] fieldsSel = { this.field };
		
		this.field = super.userClas.getDeclaredField("emailUser");
		Field[] fieldsConditions = { this.field };
		
		if(super.valuesConditions.size() > 0)
			super.valuesConditions.clear();
				
		super.valuesConditions.add(this.student.getUser().getEmailUser());
		
		ResultSet result = generics.select(fieldsSel, fieldsConditions, super.valuesConditions, super.table);
		int idUser = 0;
		
		while(result.next()){
			idUser = result.getInt("idUser");
		}
		
		result.close();
		generics.closeStatement();
		return idUser;
	}

	/**
	 * inseri usuário
	 */
	private void insertUser(GenericsDAO generics) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException {		
		// Busca os campos de UserBean menos o id da Classe					
		this.fields = super.userRef.getAllLessId(super.userClas);
		
		// Atribui a lista os valores de usuário
		if(super.values.size() > 0)
			super.values.clear();
		
		super.values = new ArrayList<Object>();
		super.values.add(this.student.getUser().getEmailUser());
		// Faz o hash da senha		
		super.values.add(super.util.hashPass(this.student.getUser().getPassUser()));
		super.values.add(this.student.getUser().getTypeUser());
		super.values.add(this.student.getUser().getStatusUser());																							
																
		generics.insert(this.fields, super.values, super.table);
	}
	
	/**
	 * Busca uma lista de estudantes
	 */
	public List<Student> studentsList(List<Student> students, GenericsDAO generics) throws NoSuchFieldException, SecurityException, SQLException{
		// Todos os campos selecionados
		if(this.fieldsList.size() > 0)
			this.fieldsList.clear();		
		this.fieldsList.add("*");
		// Não haverá condição
		if(this.fieldsConditions.size() > 0)
			this.fieldsConditions.clear();
		// Limpa valores de condição
		if(this.valuesConditions.size() > 0)
			this.valuesConditions.clear();
		// Condição de junção
		if(this.joinsConditions.size() > 0)
			this.joinsConditions.clear();
		// Atribui a condição de junção ao usuário
		this.joinsConditions.add("idUser");
		this.conditions = " ORDER BY idStudent DESC ";
		
		ResultSet result = generics.joinDuble(this.fieldsList, this.fieldsConditions, this.valuesConditions,
											  this.joinsConditions, this.join, super.table, this.table,
											  this.conditions);		

		students = this.resultSetStudents(students, result);
		
		result.close();
		generics.closeStatement();
		
		return students;
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
