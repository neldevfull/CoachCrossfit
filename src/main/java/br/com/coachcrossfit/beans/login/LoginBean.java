package br.com.coachcrossfit.beans.login; 

import java.io.Serializable;
import java.sql.Connection;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import br.com.coachcrossfit.beans.user.UserBean;
import br.com.coachcrossfit.database.connections.ConnectionFactory;
import br.com.coachcrossfit.database.dao.generics.GenericsDAO;
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
		    	// Armazena sessão
		    	session.setAttribute("userLogged", this.user); 		    	
		    	// Verifica tipo de usuário e redireciona
		    	if(this.user.getTypeUser() == 1)		    		
		    		return "ManagerRedirect?faces-redirect=true";		    				    			    	
		    	else		    		
		    		return "Home?faces-redirect=true";		    	
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
