package br.com.coachcrossfit.utilities;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean
@ApplicationScoped
public class PagesBean {

	private String page;
	
	public void loadPage(String page){
		setPage(page);
	}
	
	public String getPage() {
		return page;
	}
	
	public void setPage(String page) {
		this.page = page;
	}
}
