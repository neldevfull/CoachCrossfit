package br.com.coachcrossfit.utilities;

import java.util.ArrayList;
import java.util.List;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import br.com.coachcrossfit.models.User;

public class LoginPhaseListener_BKP implements PhaseListener{

	private static final long serialVersionUID = 1L;
	private List<String> urls = new ArrayList<String>();
	private FacesContext fc;

	/**
	 * Carrega a primeira fase - RESTORE VIEW
	 */
	public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

    public void beforePhase(PhaseEvent event) {    	    	
    }
    
    private String handleURL(String page){
    	String[] pages = page.split("/");
    	page = pages[2];
    	page = page.replaceAll(".xhtml", "");
    	return page;
    }
    
    private void loadURLs(){
    	this.urls.add("MANAGER");
    	this.urls.add("HOME");    	
    }

    /**
     * Responsável pela autorização do usuário no sistema
     */
    public void afterPhase(PhaseEvent event) {
    	this.fc = event.getFacesContext();
    	FacesContext facesContext = FacesContext.getCurrentInstance(); 
    	HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest(); 
    	
    	HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        User user = (User) session.getAttribute("userLogged");
    	
    	String page = request .getRequestURI(); 
    	page = this.handleURL(page);
    	
    	if(urls.size() == 0){
    		this.loadURLs();
    	}
    	
    	for (String url : urls) {
    		if(page.toUpperCase().equals(url)){
    			if(url.toUpperCase().equals("MANAGER") && user.getTypeUser() == 2){
    				NavigationHandler nh = this.fc.getApplication().getNavigationHandler();
    				nh.handleNavigation(this.fc, null, "Pag404");
    			}
    			if(url.toUpperCase().equals("HOME") && user.getTypeUser() == 1){
    				NavigationHandler nh = this.fc.getApplication().getNavigationHandler();
    				nh.handleNavigation(this.fc, null, "Pag404");
    			}
    		}
		}       
    }

}
