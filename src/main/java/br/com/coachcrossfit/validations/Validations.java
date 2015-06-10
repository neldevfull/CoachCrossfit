package br.com.coachcrossfit.validations; 

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import br.com.coachcrossfit.utilities.Util;

public class Validations {

	private Pattern pattern;
	private Matcher matcher;
	private Util util = new Util();
	
	/**
	 * Validação de Data
	 */
	public void validateDate(FacesContext fc, UIComponent ui, Object obj) throws ValidatorException{
		// Casting para String
		Date date = (Date) obj;
		String dateStr = util.parseDateToString(date);
		// Composição
		this.pattern = Pattern.compile("[a-zA-Z]");
		this.matcher = this.pattern.matcher(dateStr);

		// Verificação
		if(!this.matcher.matches()){
			pattern = Pattern.compile("^\\d{2}/\\d{2}/\\d{4}");
			matcher = pattern.matcher(dateStr);
			
			if(!matcher.matches())
				throw new ValidatorException(new FacesMessage("Data inválida"));
		}
		else{
			throw new ValidatorException(new FacesMessage("Data inválido"));
		}			
	}

	/**
	 * Validação de Float
	 */
	public void validateFloat(FacesContext fc, UIComponent ui, Object obj) throws ValidatorException{		
		float numberFloat = (float) obj;
		if (!Float.isFinite(numberFloat))
			throw new ValidatorException(new FacesMessage("Valor decimal inválido"));
	}

	/**
	 * Validação de Nome
	 */
	public void validateName(FacesContext fc, UIComponent ui, Object obj) throws ValidatorException {
		// Casting para String
		String name = obj.toString();
		// Composição
		this.pattern = Pattern.compile("[a-zA-Z âÂãÃáÁàÀêÊéÉèÈíÍìÌôÔõÕóÓòÒúÚùÙûÛçÇ]{3,150}");
		this.matcher = this.pattern.matcher(name);
		// Verificação
		if (!this.matcher.matches())
			throw new ValidatorException(new FacesMessage("Nome inválido"));
	}
	
	/**
	 * Validação de texto
	 */
	public void validateTextArea(FacesContext fc, UIComponent ui, Object obj) throws ValidatorException{
		String textArea = obj.toString();
		// Verificação		
		this.pattern = Pattern.compile("[a-zA-Z0-9 \nâÂãÃáÁàÀêÊéÉèÈíÍìÌôÔõÕóÓòÒúÚùÙûÛçÇ$%.|\\s,-:.;]{3,999}"); 
		this.matcher = this.pattern.matcher(textArea);
		
		if(!matcher.matches())
			throw new ValidatorException(new FacesMessage("Texto inválido"));							
	}
	
	/**
	 * Validação de String
	 */
	public void validateString(FacesContext fc, UIComponent ui, Object obj) throws ValidatorException{
		String textArea = obj.toString();
		// Verificação		
		this.pattern = Pattern.compile("[a-zA-Z0-9 \nâÂãÃáÁàÀêÊéÉèÈíÍìÌôÔõÕóÓòÒúÚùÙûÛçÇ$ªº?!%(),+*-:.;]{3,150}"); 
		this.matcher = this.pattern.matcher(textArea);
		
		if(!matcher.matches())
			throw new ValidatorException(new FacesMessage("Texto inválido"));							
	}

	/**
	 * Validação de inteiro
	 */
	public void validateInt(FacesContext fc, UIComponent ui, Object obj) throws ValidatorException{		
		int integer = (int) obj;
		if (integer != 1 && integer != 2)
			throw new ValidatorException(new FacesMessage("Valor numérico inválido"));
	}

	/**
	 * Validação de senha
	 */
	public void validatePass(FacesContext fc, UIComponent ui, Object obj) throws ValidatorException{	
		String password = obj.toString();
		// Verfica tamanho
		if(password.length() >= 4 && password.length() <= 12){
			// Composição
			this.pattern = Pattern.compile("[!@#$%¨&*()'`{}^~/?;:<>,.]");
	        this.matcher = this.pattern.matcher(password);
	        // Verfica se existe caracteres especiais
	        if(this.matcher.matches())
	        	throw new ValidatorException(new FacesMessage("Senha incorreta"));	        	
		}
		else{
			throw new ValidatorException(new FacesMessage("Senha incorreta"));			
		}
	}

	/**
	 * Validação de E-mail
	 */
	public void validateEmail(FacesContext fc, UIComponent ui, Object obj) throws ValidatorException{
		String email = obj.toString();
		// Composição
		this.pattern = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$");
		this.matcher = this.pattern.matcher(email);

		// Verificação
		if (!this.matcher.matches())
			throw new ValidatorException(new FacesMessage("Email inválido"));
	}
}
