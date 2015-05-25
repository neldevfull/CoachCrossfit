package br.com.coachcrossfit.database.dao.students;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import br.com.coachcrossfit.database.dao.generics.GenericsDAO;
import br.com.coachcrossfit.models.Student;

public class StudentDAO extends GenericsDAO {
	
	public StudentDAO(Connection connection) {
		super(connection);		
	}

	/**
	 * Atributo responsável pela conexão e declaração SQL
	 */		
	public List<Student> studentsNoGroup(List<Student> students, List<Object> values, GenericsDAO generics) throws SQLException{
		super.sql = "SELECT tb_student.idStudent, tb_student.idCoach, tb_user.idUser, tb_user.nameUser, tb_user.emailUser "+
					"FROM tb_student  LEFT JOIN  tb_participant "+
					"ON tb_student.idStudent = tb_participant.idStudent "+
					"LEFT JOIN tb_user ON tb_student.idUser = tb_user.idUser "+
					"WHERE tb_student.idCoach = ? "+
					"AND tb_user.statusUser = ? "+
					"AND tb_participant.idGroup IS NULL "+
					"ORDER BY idStudent DESC;";
		
		super.statement = super.connection.prepareStatement(super.sql);	
		super.loadStatement(values, super.statement);
		
		try{
			// Executa a query e retorna resultados
			super.resultSet = super.statement.executeQuery();
			students = this.resultStudentNoGroup(students, super.resultSet);
			// Fecha ResultSet e Statement
			super.closeResultSet();
			super.closeStatement();
			
			return students;
		}
		catch(Exception e){
			throw new SQLException("Falha ao selecionar registros no banco de dados");
		}				
	}
	
	private List<Student> resultStudentNoGroup(List<Student> students, ResultSet result) throws SQLException{
		while(result.next()){
			Student student = new Student();
			student.setIdStudent(result.getInt("idStudent"));
			student.setIdCoach(result.getInt("idCoach"));
			student.setIdUser(result.getInt("idUser"));
			student.setNameUser(result.getString("nameUser"));
			student.setEmailUser(result.getString("emailUser"));
			students.add(student);
		}
		return students;
	}
}
