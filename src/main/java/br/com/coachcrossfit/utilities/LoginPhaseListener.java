package br.com.coachcrossfit.utilities;

import java.util.Enumeration;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;

import br.com.coachcrossfit.models.Coach;
import br.com.coachcrossfit.models.Student;

public class LoginPhaseListener implements PhaseListener{

	private static final long serialVersionUID = 1L;

	/**
	 * Carrega a primeira fase - RESTORE VIEW
	 */
	public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

    public void beforePhase(PhaseEvent event) {
    }

    /**
     * Responsável pela autorização do usuário no sistema
     */
    public void afterPhase(PhaseEvent event) {
    	FacesContext fc = event.getFacesContext();
        boolean loginPage = fc.getViewRoot().getViewId().lastIndexOf("Login") > -1 ? true : false;
        
        HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        
        Enumeration<?> attrNames =  session.getAttributeNames();
        
        while(attrNames.hasMoreElements()){
        	String name = (String) attrNames.nextElement();
        	
        	if(name.equals("studentLogged")){
        		 Student student = (Student)session.getAttribute("studentLogged");
        	        
    	        if (student == null && loginPage == false) {
    	            NavigationHandler nh = fc.getApplication().getNavigationHandler();
    	            nh.handleNavigation(fc, null, "Pag404");
    	            return;
    	        }
    	        return;
        	}
        	
        	if(name.equals("coachLogged")){
       		 	Coach coach = (Coach)session.getAttribute("coachLogged");
       	        
	   	        if (coach == null && loginPage == false) {
	   	            NavigationHandler nh = fc.getApplication().getNavigationHandler();
	   	            nh.handleNavigation(fc, null, "Pag404");
	   	            return;
	   	        }
	   	        return; 
	       	}
        	        	
        }
        
        if(loginPage == false){
        	NavigationHandler nh = fc.getApplication().getNavigationHandler();
	        nh.handleNavigation(fc, null, "Pag404");
        }
               
    }
    
}
