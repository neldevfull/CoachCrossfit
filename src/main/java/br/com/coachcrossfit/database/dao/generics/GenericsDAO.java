package br.com.coachcrossfit.database.dao.generics;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.mysql.jdbc.Statement;

public class GenericsDAO {
	
	/**
	 * Atributo responsável pela conexão e declaração SQL
	 */
	protected Connection connection = null;
	protected PreparedStatement statement = null;
	protected ResultSet resultSet = null;
	protected String sql = "";
	
	public GenericsDAO(Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * Join Binary or Ternary
	 */
	public ResultSet join(List<String> fields, List<String> fieldsConditions, List<Object> valuesConditions, 
			 List<String> joinsConditions, List<String> joinsConditionsC, String join, 
			 String tableA, String tableB, String tableC, String conditions) throws SQLException{
		String sql = "";
		String joinConditions = "";
		String joinConditionsC = "";
		String fieldConditions = "";
		String condition = "";
		String conditionC = "";
		
		for(String field : fields){
			sql += sql == "" ? "SELECT " + field : ", " + field;
		}
		
		// Faz JOIN duplo
		for(String fieldJoin :  joinsConditions){
			condition  = tableA + "." + fieldJoin + " = " + tableB + "." + fieldJoin;
			joinConditions += joinConditions == "" ? " FROM " + tableA + " " +join + " " + tableB + " ON " + condition : " AND " +  condition;
		}
		
		// Faz JOIN triplo 
		if(!tableC.equals("")){
			for (String fieldJoinC : joinsConditionsC) {
				conditionC = tableB + "." + fieldJoinC + " = " + tableC + "." + fieldJoinC;
				joinConditionsC += joinConditionsC == "" ? join + tableC + " ON " +conditionC : " AND " + conditionC;
			}			
			joinConditions += joinConditionsC;
		}

		// Atribui condições
		if(fieldsConditions.size() > 0){
			for(String field : fieldsConditions){
				fieldConditions += fieldConditions == "" ? " WHERE " + field + " = ?" : " AND " + field + " = ?";
			}
		}
		
		sql += joinConditions + fieldConditions + conditions;
		
		this.statement = this.connection.prepareStatement(sql);
		loadStatement(valuesConditions, this.statement);
		
		try{			
			ResultSet result = this.statement.executeQuery(); 			
			return result;
		}
		catch(Exception e){
			throw new SQLException("Falha ao selecionar registros no banco de dados");
		}
						
	}
	 
	/**
	 * Faz a inserção de dados de maneira genêrica
	 */
	public void insert(Field[] fields, List<Object> values, String table) throws SQLException{
		// Monta a declaração
		String sql = "INSERT INTO " + table + " (";
		String parameter = "";
		int count = 0;
		
		for (Field field : fields) {
			sql += count == 0 ? field.getName() : "," + field.getName();
			parameter += count == 0 ? "?" : ",?";			 
			count++;
		}
		
		try(PreparedStatement statement = this.connection.prepareStatement(sql+") VALUES ("+parameter+")")){								
			loadStatement(values, statement);			
			statement.executeUpdate();
		}
		catch(Exception e){
			throw new SQLException("Falha ao inserir no banco de dados");
		}
		
	}
	
	/**
	 * Faz a inserção de dados de maneira genêrica
	 */
	public void insert(List<String> fields, List<Object> values, String table) throws SQLException{
		// Monta a declaração
		String sql = "INSERT INTO " + table + " (";
		String parameter = "";
		int count = 0;
		
		for (String field : fields) {
			sql += count == 0 ? field : "," + field;
			parameter += count == 0 ? "?" : ",?";			 
			count++;
		}
		
		try(PreparedStatement statement = this.connection.prepareStatement(sql+") VALUES ("+parameter+")")){								
			loadStatement(values, statement);			
			statement.executeUpdate();
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			throw new SQLException("Falha ao inserir no banco de dados");
		}
		
	}
	
	/**
	 * Faz a inserção de dados e retorna id
	 */
	public int insertReturnId(Field[] fields, List<Object> values, String table) throws SQLException{
		int idUser = 0;
		// Monta a declaração
		String sql = "INSERT INTO " + table + " (";
		String parameter = "";
		int count = 0;
		
		for (Field field : fields) {
			sql += count == 0 ? field.getName() : "," + field.getName();
			parameter += count == 0 ? "?" : ",?";			 
			count++;
		}
		
		try(PreparedStatement statement = this.connection.prepareStatement(sql+") VALUES ("+parameter+")", Statement.RETURN_GENERATED_KEYS)){								
			loadStatement(values, statement);			
			statement.executeUpdate();
			
			ResultSet result = statement.getGeneratedKeys();
			while(result.next()){
				idUser = result.getInt(1);
			}
			return idUser;   
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			throw new SQLException("Falha ao inserir no banco de dados");
		}
		
	}
	
	/**
	 * Faz a inserção de dados e retorna id
	 */
	public int insertReturnId(List<String> fields, List<Object> values, String table) throws SQLException{
		int idUser = 0;
		// Monta a declaração
		String sql = "INSERT INTO " + table + " (";
		String parameter = "";
		int count = 0;
		
		for (String field : fields) {
			sql += count == 0 ? field : "," + field;
			parameter += count == 0 ? "?" : ",?";			 
			count++;
		}
		
		try(PreparedStatement statement = this.connection.prepareStatement(sql+") VALUES ("+parameter+")", Statement.RETURN_GENERATED_KEYS)){								
			loadStatement(values, statement);			
			statement.executeUpdate();
			
			ResultSet result = statement.getGeneratedKeys();
			while(result.next()){
				idUser = result.getInt(1);
			}
			return idUser;   
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			throw new SQLException("Falha ao inserir no banco de dados");
		}
		
	}
	
	public void delete(List<String> fields, List<Object> values, String table) throws SQLException{
		String sql = "DELETE FROM " + table + " WHERE ";		
		if(fields.size() == 1){
			sql += fields.get(0) + " = ?";
		}
		else{
			int count = 0;
			for (String field : fields) {
				sql += count == 0 ? field + " = ?" : " AND " + field + " = ?";
				count++;
			}
		}
				
		try(PreparedStatement statement = this.connection.prepareStatement(sql)){
			loadStatement(values, statement);
			statement.executeUpdate();
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			throw new SQLException("Falha ao deletar registro(s) no(s) banco de dados");
		}
		
	}
	
	public ResultSet select(Field[] fields, String table, String conditions) throws SQLException{			
		String sql = "SELECT ";
		String fieldsSelect = "";
		
		for (Field field : fields) {
			fieldsSelect += fieldsSelect == "" ? field.getName() : ","+field.getName();
		}
		
		sql += conditions != "" ? fieldsSelect + " FROM " + table + conditions :
			fieldsSelect + " FROM " + table;
		this.statement = this.connection.prepareStatement(sql);
		
		try{			
			ResultSet result = statement.executeQuery(); 			
			return result;
		}
		catch(Exception e){
			throw new SQLException("Falha ao selecionar registros no banco de dados");
		}	
		
	}
	
	public ResultSet select(List<String> fields, String table, String conditions) throws SQLException{			
		String sql = "SELECT ";
		String fieldsSelect = "";
		
		for (String field : fields) {
			fieldsSelect += fieldsSelect == "" ? field : ","+field;
		}
		
		sql += conditions != "" ? fieldsSelect + " FROM " + table + conditions :
			fieldsSelect + " FROM " + table;
		this.statement = this.connection.prepareStatement(sql);
		
		try{			
			ResultSet result = statement.executeQuery(); 			
			return result;
		}
		catch(Exception e){
			throw new SQLException("Falha ao selecionar registros no banco de dados");
		}	
		
	}
	
	public ResultSet select(Field[] fields, Field[] fieldsConditions, List<Object> valuesConditions, String table,
						    String conditions) throws SQLException{			
		String sql = "";		
		String fieldConditions = "";
		
		for (Field field : fields) {
			sql += sql == "" ? "SELECT " + field.getName() : ","+field.getName();
		}
				
		for (Field field : fieldsConditions) {
			fieldConditions += fieldConditions == "" ? " FROM " + table + " WHERE " + field.getName() + " = ?" : " AND " + field.getName() + " = ?";
		}
		
		sql += fieldConditions+" "+conditions;
		this.statement = this.connection.prepareStatement(sql);
		loadStatement(valuesConditions, statement);
		
		try{			
			ResultSet result = statement.executeQuery(); 			
			return result;
		}
		catch(Exception e){
			throw new SQLException("Falha ao selecionar registros no banco de dados");
		}	
		
	}
	
	public ResultSet select(List<String> fields, List<String> fieldsConditions, List<Object> valuesConditions, String table,
			String conditions) throws SQLException{			
		String sql = "";		
		String fieldConditions = "";
		
		for (String field : fields) {
			sql += sql == "" ? "SELECT " + field : ","+field;
		}
		
		for (String field : fieldsConditions) {
			fieldConditions += fieldConditions == "" ? " FROM " + table + " WHERE " + field + " = ?" : " AND " + field + " = ?";
		}
		
		sql += fieldConditions+" "+conditions;
		this.statement = this.connection.prepareStatement(sql);
		loadStatement(valuesConditions, statement);
		
		try{			
			ResultSet result = statement.executeQuery(); 			
			return result;
		}
		catch(Exception e){
			throw new SQLException("Falha ao selecionar registros no banco de dados");
		}	
		
	}
	  
	public void update(List<String> fields, List<Object> values, String table) throws SQLException{
		String sql = "UPDATE " + table + " SET ";
		int count = 1;
		
		for (String field : fields) {
			if(count != fields.size()){
				sql += count == 1 ? " " + field + " = ? " : ", " + field + " = ? ";
			}
			else{
				sql += " WHERE " + field + " = ?";
			}
			
			count++;
		}	
		
		try(PreparedStatement statement = this.connection.prepareStatement(sql)){
			loadStatement(values, statement);
			statement.executeUpdate();
		}
		catch(Exception e){
			throw new SQLException("Falha ao alterar registro(s) no(s) banco de dados");
		}
				
	}
	
	protected PreparedStatement loadStatement(List<Object> values, PreparedStatement statement) throws SQLException{
		int count = 1;
		for (Object value : values) {			
			if(value instanceof Integer){
				int inteiro = (int) value;
				statement.setInt(count, inteiro);
			}
			if(value instanceof Double){
				double decimal = (double) value;
				statement.setDouble(count, decimal);
			}
			if(value instanceof Float){
				float decimalFloat = (float) value;
				statement.setFloat(count, decimalFloat); 
			}
			if(value instanceof String){
				String texto = (String) value;
				statement.setString(count, texto);
			}
			if(value instanceof Date){
				Date date = (Date) value;
				java.sql.Date sqlDate = new java.sql.Date(date.getTime());
				statement.setDate(count, sqlDate);
			}
			count++; 
		}
		return statement;
	}
	
	/**
	 * Fecha a declaração ao banco de dados	  
	 */
	public void closeStatement() throws SQLException{
		try{
			if (this.statement != null)
				this.statement.close();			
		}
		catch(Exception e){
			throw new SQLException("Falha durane a operação com o banco de dados");
		}
	}
	
	/**
	 * Fecha o ResultSet	 
	 */
	protected void closeResultSet() throws SQLException{
		try{
			if(this.resultSet != null)
				this.resultSet.close();
		}
		catch(Exception e){
			throw new SQLException("Falha durane a operação com o banco de dados");
		}
	}
	
	public Connection getConnection() {
		return connection;
	}

}
