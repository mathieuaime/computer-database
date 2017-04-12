package com.excilys.computerdatabase.interfaces;

import com.excilys.computerdatabase.models.Page;

public interface IPage<T> {
	public Page<T> get();
	public Page<T> get(int pageNumero, int length);
}