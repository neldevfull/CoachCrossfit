package br.com.coachcrossfit.beans;  

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
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.com.coachcrossfit.database.ConnectionFactory;
import br.com.coachcrossfit.database.GenericsDAO;
import br.com.coachcrossfit.models.Student;
import br.com.coachcrossfit.models.User;
import br.com.coachcrossfit.reflections.Reflections;
import br.com.coachcrossfit.validations.Validations;;

@ManagedBean
@ViewScoped
public class StudentBean extends UserBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private Student student = new Student();
	private Validations validations = new Validations();	
	private Reflections<User> ref;
	private Class<User> userRef;
	private String table;
	private Field field;
	private Field[] fields;
	private List<Object> values;
	private List<String> compareClass;		
	
	// Cadastra usuário e aluno
	public void studentCad() {								
		try(Connection connection = new ConnectionFactory().getConnection()){
			// Atribui valores padrão
			this.assignValuesDefault();
			
			// Gera a chave que será utilizada como senha e tipo aluno 2
			student.getUser().setPassUser(new UserBean().generateKey(student.getDateBirthStudent(), connection));	
			
			// Aluno é do tipo dois
			student.getUser().setTypeUser(2);
			
			// AutoCommit para que haja atomicidade dos dados
			connection.setAutoCommit(false);
			
			// Cria GenericsDAO e Insert Usuário
			GenericsDAO generics = new GenericsDAO(connection);
			
			// Inseri usuário
			insertUser(generics);
			
			// Busca idUser
			int idUser = selectIdUser(generics);
			
			// Inseri aluno
			insertStudent(generics, idUser);								
			
			// Commit para que haja atomicidade dos dados
			connection.commit();
			
			FacesContext.getCurrentInstance().addMessage("messages",  new FacesMessage("Cadastrado com Sucesso!"));
		}
		catch(Exception e){
			FacesContext.getCurrentInstance().addMessage("messages",  new FacesMessage(e.getMessage().toString()));			
		}			
	}
	
	// Busca registro de determinado aluno
	public Student resultSetStudent(Student student, ResultSet result) throws SQLException{
		 while(result.next()){
			student.getUser().setIdUser(result.getInt("idUser"));
	    	student.getUser().setEmailUser(result.getString("emailUser")); 
	    	student.getUser().setTypeUser(result.getInt("typeUser"));		    	
	    	student.getUser().setStatusUser(result.getInt("statusUser"));	 
	    	student.setNameStudent(result.getString("nameStudent"));
	    	student.setWeightStudent(result.getFloat("weightStudent"));
	    	student.setDateBirthStudent(result.getDate("dateBirthStudent"));
	    	student.setHeightStudent(result.getFloat("heightStudent"));
	    	student.setImageStudent(result.getString("imageStudent"));
	    	student.setGenderStudent(result.getInt("genderStudent"));
		  }
		  return student;
	}
	
	private void assignValuesDefault(){
		if(this.student.getDateBirthStudent() == null)
			this.student.setDateBirthStudent(new Date());
	}

	private void insertStudent(GenericsDAO generics, int idUser) throws SQLException {
		// Cadastra Aluno
		this.table = "tb_student";
		
		// Busca os campos de StudentBean menos o id da Classe
		Reflections<Student> refStu = new Reflections<Student>();
		Class<Student> stuRef = Student.class;
		// Utiliza compareClass para não busca atributo de associação
		this.compareClass = new ArrayList<String>();
		this.compareClass.add("user");				
		this.fields = refStu.getAllLessId(stuRef, compareClass);
						
		// Atribui a lista os valores de usuário
		this.values.clear();
		this.values.add(idUser);
		this.values.add(7/*idCoach*/);
		this.values.add(student.getNameStudent());
		this.values.add(student.getWeightStudent());
		this.values.add(student.getDateBirthStudent());
		this.values.add(student.getHeightStudent());
		this.values.add("imgDefault"/*student.getImageStudent()*/);	
		this.values.add(student.getGenderStudent());
		
		// Insert Aluno
		generics.insert(this.fields, this.values, this.table);
	}

	private int selectIdUser(GenericsDAO generics) throws NoSuchFieldException, SQLException {
		// Busca o idUser do aluno
		this.field = this.userRef.getDeclaredField("idUser");
		Field[] fieldsSel = { this.field };
		
		this.field = this.userRef.getDeclaredField("emailUser");
		Field[] fieldsConditions = { this.field };
		
		List<Object> valuesConditions = new ArrayList<Object>();
		valuesConditions.add(student.getUser().getEmailUser());
		
		ResultSet result = generics.select(fieldsSel, fieldsConditions, valuesConditions, this.table);
		int idUser = 0;
		
		while(result.next()){
			idUser = result.getInt("idUser");
		}
		
		result.close();
		generics.closeStatement();
		return idUser;
	}

	private void insertUser(GenericsDAO generics) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException {
		// Cadastra Usuario
		this.table = "tb_user";
		
		// Busca os campos de UserBean menos o id da Classe
		this.ref = new Reflections<User>();
		this.userRef = User.class;		
		this.fields = ref.getAllLessId(userRef);
		
		// Atribui a lista os valores de usuário
		this.values = new ArrayList<Object>();
		this.values.add(student.getUser().getEmailUser());
		// Faz o hash da senha
		String pass = super.util.hashPass(student.getUser().getPassUser());		
		this.values.add(pass);
		this.values.add(student.getUser().getTypeUser());
		this.values.add(student.getUser().getStatusUser());																							
																
		generics.insert(this.fields, this.values, this.table);
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
