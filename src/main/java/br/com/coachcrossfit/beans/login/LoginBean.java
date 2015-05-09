package br.com.coachcrossfit.beans.login; 

import java.io.Serializable;
import java.sql.Connection;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import br.com.coachcrossfit.beans.coach.CoachBean;
import br.com.coachcrossfit.beans.student.StudentBean;
import br.com.coachcrossfit.beans.user.UserBean;
import br.com.coachcrossfit.database.ConnectionFactory;
import br.com.coachcrossfit.database.GenericsDAO;
import br.com.coachcrossfit.models.Coach;
import br.com.coachcrossfit.models.Student;
import br.com.coachcrossfit.models.User;
import br.com.coachcrossfit.validations.Validations;

@ManagedBean
@ViewScoped
public class LoginBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private User user = new User();
	private UserBean userBean = new UserBean();	
	private Validations validations = new Validations();
  
	public String login(){
		try(Connection connection = new ConnectionFactory().getConnection()){			
			// Cria GenericsDAO para select de usuário
		    GenericsDAO generics = new GenericsDAO(connection);
		    
		    // Seleciona usuário
		    this.user = this.userBean.selectLoginUSer(this.user, generics);		    
		    		    		    
		    if(this.user.getIdUser() > 0){
		    	// Cria sessão
		    	HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);		    			    	
		    	
		    	// Verifica tipo de usuário
		    	if(user.getTypeUser() == 1){
		    		// Cria coach e configura sessão
		    		CoachBean coachBean = new CoachBean();
		    		Coach coach = coachBean.selectCoach(generics, this.user);		    		
		    		
		    		session.setAttribute("coachLogged", coach);
		    		return "Manager?faces-redirect=true";		    				    	
		    	}
		    	else{
		    		// Cria aluno e configura sessão
		    		StudentBean studentBean = new StudentBean();
		    		Student student = studentBean.selectStudent(generics, this.user);
		    		
		    		session.setAttribute("studentLogged", student);
		    		return "Home?faces-redirect=true";
		    	}
		    }
		    else{
		    	FacesContext.getCurrentInstance().addMessage("messages",  new FacesMessage("E-mail ou senha incorretos"));
		    	return "Login";
		    }		    		    		    
		}
		catch(Exception e){
			FacesContext.getCurrentInstance().addMessage("messages",  new FacesMessage("Erro de sistema: "+e.getMessage().toString()));	
			return "Login";
		}				 
	}
			
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Validations getValidations() {
		return validations;
	}

	public void setValidations(Validations validations) {
		this.validations = validations;
	}
		
}
