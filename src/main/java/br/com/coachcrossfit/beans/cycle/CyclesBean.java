package br.com.coachcrossfit.beans.cycle;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.com.coachcrossfit.beans.coach.CoachBean;
import br.com.coachcrossfit.database.connections.ConnectionFactory;
import br.com.coachcrossfit.database.dao.cycle.CycleDAO;
import br.com.coachcrossfit.models.Cycle;

@ManagedBean
@ViewScoped
public class CyclesBean {
	
	private List<Cycle> cycles;	
	private CycleDAO cycleDAO;
	
	public CyclesBean() {
		this.cycles = new ArrayList<Cycle>();
		
		try(Connection connection = new ConnectionFactory().getConnection()){			
			this.cycleDAO = new CycleDAO(connection);
			this.cycles   = cycleDAO.listCycles(cycles, new CoachBean().loadCoach().getIdCoach(),
					CycleBean.getNrStudentORGroup()); 
			
		}		
		catch(Exception e){			
			FacesContext.getCurrentInstance().addMessage("messages",  new FacesMessage(e.getMessage().toString()));			
		}
	}
	
	public List<Cycle> getCycles() {
		return cycles;
	}
	
}
