package br.com.coachcrossfit.beans; 

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import br.com.coachcrossfit.database.ConnectionFactory;
import br.com.coachcrossfit.database.GenericsDAO;
import br.com.coachcrossfit.models.Student;

@ManagedBean
@SessionScoped
public class LoginBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Student student = new Student();
	private StudentBean studentBean = new StudentBean();		
  
	public String login(){
		try(Connection connection = new ConnectionFactory().getConnection()){
			// Variáveis condicionais
			boolean success;
			int statusUser = -1;
			
			// Cria GenericsDAO para select de usuário
		    GenericsDAO generics = new GenericsDAO(connection);
		    
			// Seleciona estudante
			success = selectStudent(generics);
		    
			// Atribui valor a variavel condiconal caso exista usuário
			if(success == true)			    
			    statusUser = this.student.getUser().getStatusUser(); 		    		    
		    
		    if(statusUser == 1){
		    	HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		    	session.setAttribute("usuarioLogado", this.student);
		    	return "Home?faces-redirect=true";
		    }
		    else if(statusUser == 0){
		    	FacesContext.getCurrentInstance().addMessage("messages",  new FacesMessage("Aluno inativo"));
		    }
		    else{
		    	FacesContext.getCurrentInstance().addMessage("messages",  new FacesMessage("E-mail ou senha incorretos"));
		    }
		    		    		    
		}
		catch(Exception e){
			FacesContext.getCurrentInstance().addMessage("messages",  new FacesMessage("Erro de sistema: "+e.getMessage().toString()));			
		}
		
		return "Login"; 
	}

	private boolean selectStudent(GenericsDAO generics) throws NoSuchFieldException, SQLException, NoSuchAlgorithmException, UnsupportedEncodingException {
		boolean success = false;
		ResultSet result;
		List<Object> valuesConditions = new ArrayList<Object>();
		List<String> fields = new ArrayList<String>();
		List<String> fieldsConditions = new ArrayList<String>();
		List<String> joinsConditions = new ArrayList<String>();	
		String join = "INNER JOIN", tableA = "tb_user", tableB = "tb_student";
		
		fields.add("*");
		fieldsConditions.add("emailUser");
		fieldsConditions.add("passUser");
		
		valuesConditions.add(this.student.getUser().getEmailUser());
		
		// Gera hash da senha
		String pass = studentBean.util.hashPass(this.student.getUser().getPassUser());
		valuesConditions.add(pass);
		
		joinsConditions.add("idUser");
		
		result = generics.joinDuble(fields, fieldsConditions, valuesConditions, joinsConditions, join, tableA, tableB);
		
		if(result.last()){
			// Chama método para carregar aluno		
			this.studentBean.resultSetStudent(this.student, result);
			success = true;
		}
				
		result.close();
		generics.closeStatement();
		
		return success;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}
		
}
