package br.com.coachcrossfit.beans.group; 

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import br.com.coachcrossfit.beans.coach.CoachBean;
import br.com.coachcrossfit.beans.cycle.CycleBean;
import br.com.coachcrossfit.database.connections.ConnectionFactory;
import br.com.coachcrossfit.database.dao.generics.GenericsDAO;
import br.com.coachcrossfit.database.dao.student.StudentDAO;
import br.com.coachcrossfit.models.Coach;
import br.com.coachcrossfit.models.Group;
import br.com.coachcrossfit.models.GroupStudent;
import br.com.coachcrossfit.models.Student;
import br.com.coachcrossfit.reflections.Reflections;
import br.com.coachcrossfit.validations.Validations;

@ManagedBean
@ApplicationScoped 
public class GroupBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Group group;
	private List<GroupStudent> groupStudents;
	private List<Student> studentsNoGroup;	
	private Map<Long, Boolean> checkedStudentNo;	
	private String message;
	private boolean renderStudent;
	private boolean selectStudent;
	private Coach coach;
	private Validations validations;
	private Class<Group> gruClas;
	private Reflections<Group> gruRef;
	private String table;
	private String join;	
	private String conditions;
	private List<String> fieldsList;
	private List<String> joinsConditions;
	private List<String> fieldsConditions;
	private Field[] fields;	
	private List<Object> valuesConditions;
	private List<String> compareClass;
	private List<String> joinsConditionsC;
	private List<Object> values;
	
	public GroupBean() {
		this.group 			  = new Group();		
		this.studentsNoGroup  = new ArrayList<Student>();
		this.checkedStudentNo = new HashMap<Long, Boolean>();				
		this.message 		  = "";
		this.renderStudent 	  = false;
		this.selectStudent    = true;
		this.coach            = new CoachBean().loadCoach();
		this.validations 	  = new Validations();
		this.gruClas 		  = Group.class;
		this.gruRef           = new Reflections<Group>();
		this.table 			  = "tb_group";	
		this.join             = " INNER JOIN ";		
		this.conditions       = "";
		this.fieldsList       = new ArrayList<String>();
		this.joinsConditions  = new ArrayList<String>();
		this.joinsConditionsC = new ArrayList<String>();
		this.fieldsConditions = new ArrayList<String>();
		this.valuesConditions = new ArrayList<Object>();	
		this.compareClass     = new ArrayList<String>();
		this.values			  = new ArrayList<Object>();
		this.iniGroupStudents();
	}	
	
	/**
	 * Método definição
	 */
	public void groupMain(){
		List<Student> checkedStu = checkedStudents();
		
		// Cadastra ou altera
		if(this.group.getIdGroup() > 0){
			this.groupUp(checkedStu);
		}
		else{
			this.groupCad(checkedStu);
		}
	}

	/**
	 * Retorna os alunos(sem grupo) selecionados	
	 */
	private List<Student> checkedStudents() {
		List<Student> checkedStu = new ArrayList<Student>();
		
		// Adiciona alunos sem grupo
		for (Student student : this.studentsNoGroup) {
			if(this.checkedStudentNo.get(student.getIdStudent())){
				checkedStu.add(student);				
			}
		}
		return checkedStu;
	}
	
	/**
	 * Carrega grupo para ser alterado
	 */
	public String loadGroup(Group group, String url){				
		this.group = group;
		this.iniGroupStudents();
		
		this.loadGroupStudents();				
		this.studentsNoGroup();
		
		return url;
	}
	
	/**
	 * Inicializa alunos do grupo
	 */
	private void iniGroupStudents(){
		this.groupStudents = new ArrayList<GroupStudent>();
	}

	/**
	 * Carrega alunos do grupo
	 */
	private void loadGroupStudents() {
		for(Student student : this.group.getStudents()){
			GroupStudent groupStudent = new GroupStudent();
			groupStudent.setStudent(student);
			groupStudent.setStatusGroupStudent(true);
			this.groupStudents.add(groupStudent);
		}
	}
	
	/**
	 * Redireciona para cadastro
	 */
	public String loadGroup(String url){
		this.group = new Group();
		this.iniGroupStudents();
		this.studentsNoGroup();
		
		return url;
	}
	
	/**
	 * Busca alunos que não possuem grupo
	 */
	private void studentsNoGroup(){
		try(Connection connection = new ConnectionFactory().getConnection()){
			GenericsDAO generics = new GenericsDAO(connection);			
			
			this.studentsNoGroup.clear();
			this.values.clear();		
			
			this.values.add(1);
			this.values.add(1);
			
			this.studentsNoGroup = new StudentDAO(connection).studentsNoGroup(this.studentsNoGroup, this.values, generics);
		}catch(Exception e){	
			// DEVERÁ SER CRIADA A PÁGINA DE ERRO 
			// DEVERÁ SER IMPLEMENTADO NO LOG DA APLICAÇÃO
			System.out.println(e.getMessage().toString());			
		}
	}
	
	/**
	 * Cadastra grupo
	 */
	public void groupCad(List<Student> students){		
		try(Connection connection = new ConnectionFactory().getConnection()){
			// AutoCommit para que haja atomicidade dos dados
			connection.setAutoCommit(false);
			
			// Cria GenericsDAO e inseri grupo
			GenericsDAO generics = new GenericsDAO(connection);
			
			// Inseri grupo e participantes
			int idGroup = this.insertGroup(generics);			
			this.insertParticipant(generics, students, idGroup);
			this.ajaxInsertStudentGroup(students);
			
			// Commit para que haja atomicidade dos dados
			connection.commit();
			
			// Busca dados do grupo após cadastro
			this.selectGroup(generics, idGroup);
			
			// Mensagem Sucesso!
			FacesContext.getCurrentInstance().addMessage("messages",  new FacesMessage("Cadastrado com Sucesso!"));
		}
		catch(Exception e){
			/**
			 * IMPLEMENTAR LOG
			 */
			FacesContext.getCurrentInstance().addMessage("messages",  new FacesMessage(e.getMessage().toString()));			
		}
	}
	
	/**
	 * Cadastra grupo
	 */
	public void groupUp(List<Student> students){
		try(Connection connection = new ConnectionFactory().getConnection()){
			// AutoCommit para que haja atomicidade dos dados
			connection.setAutoCommit(false);
			
			// Cria GenericsDAO e inseri grupo
			GenericsDAO generics = new GenericsDAO(connection);
			
			// Inseri aluno(s) selecionados caso haja
			if(students.size() > 0){
				this.insertParticipant(generics, students, this.group.getIdGroup());
				
				this.ajaxInsertStudentGroup(students);						
				students.clear();
			}
			
			// Carrega alunos que serão excluidos do grupo			
			students = this.loadStudents(this.groupStudents);
						
			// Deleta alunos do grupo
			if(students.size() > 0){
				this.deleteParticipant(generics, students, this.group.getIdGroup());	
				this.ajaxDeleteStudentGroup(students);
			}
			
			// Commit para que haja atomicidade dos dados
			connection.commit();
									
			FacesContext.getCurrentInstance().addMessage("messages",  new FacesMessage("Grupo alterado com Sucesso!"));	
			
		}
		catch(Exception e){
			FacesContext.getCurrentInstance().addMessage("messages",  new FacesMessage(e.getMessage().toString()));			
		}
	}
	
	/**
	 * Busca dados do grupo
	 */
	private void selectGroup(GenericsDAO generics, int idGroup) throws NoSuchFieldException, SecurityException, SQLException{
		// Busca todos os campos
		this.compareClass.clear();
		this.compareClass.add("students");
		this.fields = gruRef.getAllFields(this.gruClas, this.compareClass);
		
		// Atribui o campo idGroup para condição
		Field field = gruClas.getDeclaredField("idGroup");
		Field[] fieldsConditions = { field };
		
		// Adiciona o valor idGroup
		this.valuesConditions.clear();
		this.valuesConditions.add(idGroup);
								
		this.conditions = "";
		
		ResultSet result = generics.select(this.fields, fieldsConditions, this.valuesConditions, this.table, this.conditions);
		this.resulSetGroup(generics, result);
		
		this.fullClose(generics, result);
	}
	
	/**
	 * Busca alunos do grupo	 
	 */
	private void resulSetGroup(GenericsDAO generics, ResultSet result) throws SQLException{
		while(result.next()){
			this.group.setIdGroup(result.getInt("idGroup"));
			this.group.setIdCoach(result.getInt("idCoach"));
			this.group.setNameGroup(result.getString("nameGroup"));
			this.group.setStatusGroup(result.getInt("statusGroup"));
			this.group.setStudents(this.selectListStudent(this.group.getIdGroup(), generics));
		}		
	}
	
	/**
	 * Deleta aluno(s) do grupo para que seja atualizado na tela
	 */
	private void ajaxDeleteStudentGroup(List<Student> students){
		for(Student student : students){						
			for(GroupStudent groupStudent : this.groupStudents){
				if(student.equals(groupStudent.getStudent())){
					this.groupStudents.remove(groupStudent);
					break;
				}
			}
			this.studentsNoGroup.add(student);
			this.checkedStudentNo.put((long) student.getIdStudent(), false);
		}
	}
	
	/**
	 * Inseri aluno ao grupo para que seja atualizado na tela
	 */
	private void ajaxInsertStudentGroup(List<Student> students){
		for(Student student : students){
			GroupStudent groupStudent = new GroupStudent();
			
			groupStudent.setStudent(student);
			groupStudent.setStatusGroupStudent(true);
			
			this.groupStudents.add(groupStudent);
			this.studentsNoGroup.remove(student);
		}
	}
	
	/**
	 * Carrega alunos deletados do grupo
	 */
	private List<Student> loadStudents(List<GroupStudent> groupStudents){
		List<Student> students = new ArrayList<Student>();
		
		for(GroupStudent groupStudent : groupStudents){
			if(groupStudent.isStatusGroupStudent() == false)
				students.add(groupStudent.getStudent());
		}
		return students;
	}
	
	/**
	 * Deleta alunos do grupo	 
	 */
	private void deleteParticipant(GenericsDAO generics, List<Student> students, int idGroup) throws SQLException{				
		this.fieldsList.clear();
		this.fieldsList.add("idStudent");
		this.fieldsList.add("idGroup");
		
		for(Student student : students){
			this.values.clear();			
			this.values.add(student.getIdStudent());
			this.values.add(idGroup);
			generics.delete(this.fieldsList, this.values, "tb_participant");
		}
	}
	
	/**
	 * Inseri participantes	 	 
	 */
	private void insertParticipant(GenericsDAO generics, List<Student> students, int idGroup) throws SQLException, NoSuchFieldException, SecurityException{		
		// Atribui campos
		Field fieldA = this.gruClas.getDeclaredField("idGroup");
		Field fieldB = Student.class.getDeclaredField("idStudent");		
		Field[] fields = {fieldA, fieldB};
								
		// Inserção por aluno
		for (Student student : students) {
			this.values.clear();
			this.values.add(idGroup);
			this.values.add(student.getIdStudent());
			generics.insert(fields, this.values, "tb_participant");
		}
	}
	
	public String updateStatus(Group group, int value){
		try(Connection connection = new ConnectionFactory().getConnection()){			
			// Conexão no banco
			GenericsDAO generics = new GenericsDAO(connection);
			
			// Carrega campos
			this.fieldsList.clear();
			this.fieldsList.add("statusGroup");
			this.fieldsList.add("idGroup");
			
			// Carrega valores
			this.values.clear();
			this.values.add(value);
			this.values.add(group.getIdGroup());
			
			// Altera status do Grupo
			generics.update(this.fieldsList, this.values, this.table);							
		}		
		catch(Exception e){			
			FacesContext.getCurrentInstance().addMessage("messages",  new FacesMessage(e.getMessage().toString()));			
		}
		return "/manager/groups/Groups.xhtml?faces-redirect=true";
	}
	
	/**
	 * Inseri grupo	 
	 */
	private int insertGroup(GenericsDAO generics) throws SQLException{
		int idGroup = 0;
		// Limpa e adiciona campo
		this.compareClass.clear();
		this.compareClass.add("students");
		
		// Busca todos os campos menos o de composição
		this.fields = this.gruRef.getAllLessId(this.gruClas, this.compareClass);
		
		// Adiciona valores
		this.values.clear();
		this.values.add(this.coach.getIdCoach());
		this.values.add(this.group.getNameGroup());
		/*Status Ativo*/
		this.values.add(1);
		
		idGroup = generics.insertReturnId(this.fields, this.values, this.table);
		return idGroup;
	}
		
	/**
	 * Retorna os Grupos do Coach	 
	 */
	public List<Group> listGroups(List<Group> groups, GenericsDAO generics) throws SQLException, NoSuchFieldException, SecurityException {		
		// Limpa e adiciona campo
		this.compareClass.clear();
		this.compareClass.add("students");
		// Busca todos os campos menos o de composição
		this.fields = this.gruRef.getAllFields(this.gruClas, this.compareClass);
		Field[] fieldsConditions = { this.gruClas.getDeclaredField("idCoach") };
		
		this.valuesConditions.clear();
		this.valuesConditions.add(this.coach.getIdCoach());		
		
		ResultSet result = generics.select(this.fields, fieldsConditions, this.valuesConditions, this.table,
										   "ORDER BY statusGroup ASC, idGroup DESC");
		
		groups = this.resultSetGroups(groups, result, generics);
		
		this.fullClose(generics, result);
		
		return groups;
	}

	private List<Group> resultSetGroups(List<Group> groups, ResultSet result, GenericsDAO generics) throws SQLException {
		while(result.next()){
			Group group = new Group();
			group.setIdGroup(result.getInt("idGroup"));
			group.setIdCoach(result.getInt("idCoach"));
			group.setNameGroup(result.getString("nameGroup"));
			group.setStatusGroup(result.getInt("statusGroup"));
			group.setStudents(this.selectListStudent(group.getIdGroup(), generics));
			groups.add(group);
		}
		return groups;
	}
	
	public List<Student> selectListStudent(int idGroup, GenericsDAO generics) throws SQLException{
		List<Student> students = new ArrayList<Student>();		
		// Todos os campos selecionados
		if(this.fieldsList.size() > 0)
			this.fieldsList.clear();
		// Adiciona os campos requeridos
		this.fieldsList.add("tb_student.idStudent");
		this.fieldsList.add("tb_student.idUser");
		this.fieldsList.add("nameUser");
		this.fieldsList.add("emailUser");
		this.fieldsList.add("statusUser");						
		
		// Atribui campo da condição
		if(this.fieldsConditions.size() > 0)
			this.fieldsConditions.clear();
		this.fieldsConditions.add("idGroup");
		
		// Atribui valor da condição
		if(this.valuesConditions.size() > 0)
			this.valuesConditions.clear();
		this.valuesConditions.add(idGroup);
		
		// Condição de junção
		if(this.joinsConditions.size() > 0)
			this.joinsConditions.clear();
		this.joinsConditions.add("idStudent");
		
		// condição da terceira tabela
		if(this.joinsConditionsC.size() > 0)
			this.joinsConditionsC.clear();
		this.joinsConditionsC.add("idUser");
		
		this.conditions = " ORDER BY idGroup DESC ";
		
		ResultSet result = generics.join(this.fieldsList, this.fieldsConditions, this.valuesConditions,
									     this.joinsConditions, this.joinsConditionsC, this.join, "tb_participant", "tb_student", "tb_user",
									     this.conditions);	
		
		while(result.next()){
			Student student = new Student();
			student.setIdStudent(result.getInt("idStudent"));
			student.setIdUser(result.getInt("idUser"));
			student.setNameUser(result.getString("nameUser"));
			student.setEmailUser(result.getString("emailUser"));
			student.setStatusUser(result.getInt("statusUser"));
			students.add(student);
		}
		
		this.fullClose(generics, result);
		
		return students;
	}
	
	/**
	 * Load Group for Cycle
	 */
	public String loadCycle(String url) throws IOException{		
		CycleBean.setNrStudentORGroup(this.group.getIdGroup());
		// Type Group for Cycle
		CycleBean.setTypeCycle(2);
		return url; 
	}
	
	private void fullClose(GenericsDAO generics, ResultSet result) throws SQLException{
		result.close();
		generics.closeStatement();
	}
	
	public Group getGroup() {
		return group;
	}
	
	public Validations getValidations() {
		return validations;
	}
	
	public void setValidations(Validations validations) {
		this.validations = validations;
	}
	
	public List<Student> getStudentsNoGroup() {
		return studentsNoGroup;
	}
	
	public Map<Long, Boolean> getCheckedStudentNo() {
		return checkedStudentNo; 
	}
	
	public boolean isSelectStudent() {
		return selectStudent;
	}
	
	public List<GroupStudent> getGroupStudents() {
		return groupStudents;
	}

	public boolean isRenderStudent() {
		if(this.groupStudents != null){
			if(this.groupStudents.size() == 0)			
				return this.renderStudent = true;		
			else
				return this.renderStudent = false;
		}
		else{
			return false;
		}
	}
	
	public String getMessage() {
		if(this.renderStudent == true)
			this.message = "Não há alunos vinculados";
		return message;
	}
	
}
