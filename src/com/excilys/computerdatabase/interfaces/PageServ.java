package com.excilys.computerdatabase.interfaces;

import com.excilys.computerdatabase.models.Page;

public interface PageServ<T> {
	public Page<T> getPage();
	public Page<T> getPage(int pageNumero, int length);
}