package br.com.coachcrossfit.utilities;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;
import br.com.coachcrossfit.models.User;

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
        
        User user = (User)session.getAttribute("userLogged");
        
        if (user == null && loginPage == false) {
            NavigationHandler nh = fc.getApplication().getNavigationHandler();
            nh.handleNavigation(fc, null, "Pag404");            
        }
                     
    }
    
}
