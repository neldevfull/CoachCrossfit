package br.com.coachcrossfit.reflections;

import java.lang.reflect.Field;
import java.util.List;

public class Reflections<T> {
	
	/**
	 * Busca todos os atributos da classe menos o(s) associado(s)	
	 */
	public Field[] getAllFields(Class<T> object, List<String> compareClass){		
		int count = 0;
		boolean success;
		// Pega número total dos campos
		int attSize =  object.getDeclaredFields().length;
		
		// Pega todos os campos e cria o novo array fields
		Field[] fields = object.getDeclaredFields();
		Field[] fieldsNew = new Field[attSize - compareClass.size()];
		
		// Armazena todos os campos do Bean menos o id da Classe e o campo associado
		for (Field field : fields) {
			success = true;
			for (String fieldClass : compareClass) {
				if(field.getName().compareTo(fieldClass) == 0){
					success = false;
					break;
				}				
			}
			if(success == true){
				fieldsNew[count] = field;
				count++;
			}
		}
		
		return fieldsNew;
	}
	
	/**
	 * Busca todos os atributos da classe menos o id da Classe e o(s) associado(s)
	 */
	public Field[] getAllLessId(Class<T> object, List<String> compareClass){		
		int count = 0;
		boolean success;
		boolean pass = false;
		// Pega número total dos campos
		int attSize =  object.getDeclaredFields().length;
		
		// Pega todos os campos e cria o novo array fields
		Field[] fields = object.getDeclaredFields();
		Field[] fieldsNew = new Field[attSize - compareClass.size() - 1];
		
		// Armazena todos os campos do Bean menos o id da Classe e o campo associado
		for (Field field : fields) {
			success = true;
			for (String fieldClass : compareClass) {
				if(field.getName().compareTo(fieldClass) == 0){
					success = false;
					break;
				}				
			}
			if(success == true){
				if(! field.getName().substring(0, 2).equals("id")){
					fieldsNew[count] = field;				
					count++;
				}
				else{
					if(pass){
						fieldsNew[count] = field;
						count++;
					}
					pass = true;
				}
			}
		}
		
		return fieldsNew;
	}

	/**
	 * Busca todos os campos do Bean e retorna-os menos o id da Classe
	 */
	public Field[] getAllLessId(Class<T> object){		
		int count = 0;
		boolean passar = false;
		// Pega número total dos campos
		int attSize =  object.getDeclaredFields().length;
		
		// Pega todos os campos e cria o novo array fields
		Field[] fields = object.getDeclaredFields();
		Field[] fieldsNew = new Field[attSize - 1];
		
		// Armazena todos os campos do Bean menos o id da Classe
		for (Field field : fields) {			
			if(! field.getName().substring(0, 2).equals("id")){
				fieldsNew[count] = field;				
				count++;
			}
			else{
				if(passar){
					fieldsNew[count] = field;
					count++;
				}
				passar = true;
			}
		}
		
		return fieldsNew;
	}
}
 