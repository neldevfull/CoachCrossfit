package br.com.coachcrossfit.database.connections;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {
			
//	public Connection getConnection(){
//		try{
//			Class.forName("com.mysql.jdbc.Driver");
//			String url = "jdbc:mysql://aa13u4zzezlj84e.cbvshgri1s60.sa-east-1.rds.amazonaws.com:3306/ebdb?user=coachcrossfit&password=aL69esaBataD30#";
//			return DriverManager.getConnection(url);	 		
//		}
//		catch(Exception e){
//			throw new RuntimeException("Falha na conexão com o banco de dados \n "
//					+ "O Sistema apresenta a seguinte informação: "+e.getMessage());
//		}		
//	}
	
	public Connection getConnection(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost/coachcrossfit_v002";
			return DriverManager.getConnection(url, "root", "123456");			
		}
		catch(Exception e){
			throw new RuntimeException("Falha na conexão com o banco de dados \n "
					+ "O Sistema apresenta a seguinte informação: "+e.getMessage());
		}		
	}
 			
} 