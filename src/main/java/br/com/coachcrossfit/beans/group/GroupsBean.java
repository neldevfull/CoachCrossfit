package br.com.coachcrossfit.beans.group; 

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.com.coachcrossfit.database.connections.ConnectionFactory;
import br.com.coachcrossfit.database.dao.generics.GenericsDAO;
import br.com.coachcrossfit.models.Group;

@ManagedBean
@ViewScoped
public class GroupsBean {
	
	private List<Group> groups = new ArrayList<Group>();
	private GroupBean groupBean = new GroupBean(); 
	
	public GroupsBean() {			
		try(Connection connection = new ConnectionFactory().getConnection()){			
			GenericsDAO generics = new GenericsDAO(connection);
			this.groups   = this.groupBean.listGroups(this.groups, generics);
		}		
		catch(Exception e){			
			FacesContext.getCurrentInstance().addMessage("messages",  new FacesMessage(e.getMessage().toString()));			
		}
	}

	public List<Group> getGroups() {
		return groups;
	}
		
} 
