package com.excilys.computerdatabase.interfaces;

import java.util.List;

import com.excilys.computerdatabase.models.Computer;

public interface ComputerServ {
	public List<Computer> get();

	public Computer get(int id);

	public boolean add(Computer computer);

	public boolean update(Computer computer);

	public boolean delete(int id);
}
