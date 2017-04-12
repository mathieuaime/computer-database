package com.excilys.computerdatabase.models;

import java.util.ArrayList;
import java.util.List;

public class Page<T> {
	private List<T> objects;
	
	private int pageNumero;
	
	public Page() {
		this(new ArrayList<T>(), 0);
	}
	
	public Page(List<T> objects, int pageNumero) {
		this.objects = objects;
		this.pageNumero = pageNumero;
	}
	
	public List<T> getObjects() {
		return objects;
	}
	
	public void setObjects(List<T> objects) {
		this.objects = objects;
	}
	
	public int getPageNumero() {
		return pageNumero;
	}
	
	public void setPageNumero(int pageNumero) {
		this.pageNumero = pageNumero;
	}
	
	public int getObjectNumber() {
		return objects.size();
	}
}
