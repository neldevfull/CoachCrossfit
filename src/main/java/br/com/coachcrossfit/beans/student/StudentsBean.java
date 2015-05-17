package br.com.coachcrossfit.beans.student;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.com.coachcrossfit.database.ConnectionFactory;
import br.com.coachcrossfit.database.GenericsDAO;
import br.com.coachcrossfit.models.Student;

@ManagedBean
@ViewScoped
public class StudentsBean {

	private List<Student> students  = new ArrayList<Student>();
	private StudentBean studentBean = new StudentBean();
	
	public StudentsBean() {
		try(Connection connection = new ConnectionFactory().getConnection()){
			GenericsDAO generics = new GenericsDAO(connection);
			this.students = this.studentBean.studentsList(students, generics);
		}		
		catch(Exception e){
			System.out.println(e.getMessage());
			FacesContext.getCurrentInstance().addMessage("messages",  new FacesMessage(e.getMessage().toString()));			
		}
	}
	
	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}
			
}
