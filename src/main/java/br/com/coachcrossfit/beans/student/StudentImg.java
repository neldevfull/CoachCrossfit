package br.com.coachcrossfit.beans.student;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.event.CaptureEvent;
import org.primefaces.model.ByteArrayContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@SessionScoped
public class StudentImg implements Serializable {

	private static final long serialVersionUID = 1L;
	private StreamedContent photoContent;
	private byte[] photoBytes;
	
	public void capturePhoto(CaptureEvent event){
		photoBytes 	 = event.getData();
		photoContent = new ByteArrayContent(photoBytes); 
	}

	public StreamedContent getPhotoContent() {
		return photoContent;
	}

	public byte[] getPhotoBytes() {
		return photoBytes;
	}
	
	public boolean isPhotoCapture(){
		return getPhotoContent() != null;
	}
		
}
