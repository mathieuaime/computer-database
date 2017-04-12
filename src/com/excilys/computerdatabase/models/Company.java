package com.excilys.computerdatabase.models;

import java.io.Serializable;


public class Company implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID 	= -8416954786511932741L;
	
	public static final String TABLE_NAME 		= "company";
	
	public static final String FIELD_ID 		= "id";	
	public static final String FIELD_NAME 		= "name";
	
	private int id;
	
	private String name;
	
	public Company() {
		this(0,null);
	}

	public Company(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Company other = (Company) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}	
	
	@Override
	public String toString() {
		return id + "/" + name;
	}

	//TODO toString
}
