package com.excilys.computerdatabase.interfaces;

import com.excilys.computerdatabase.dtos.Page;

public interface PageServ<T> {
	public Page<T> getPage();

	public Page<T> getPage(int pageNumero, int length);
}