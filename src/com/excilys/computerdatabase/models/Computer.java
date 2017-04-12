package com.excilys.computerdatabase.models;

import java.io.Serializable;
import java.util.Date;

import com.excilys.computerdatabase.config.Config;

import java.text.SimpleDateFormat;

public class Computer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID 		= 8297703855786572960L;
	
	public static final String TABLE_NAME 			= "computer";
	
	public static final String FIELD_ID 			= "id";
	public static final String FIELD_NAME 			= "name";
	public static final String FIELD_INTRODUCED 	= "introduced";
	public static final String FIELD_DISCONTINUED	= "discontinued";
	public static final String FIELD_COMPANY_ID 	= "company_id";
	
	private int id;     
	
	private String name;
	
	private Date introduced;
	
	private Date discontinued;

	private int company_id;
	
	public Computer() {
		this(0,null,null,null,0);
	}
	
	public Computer(int id, String name, Date introduced, Date discontinued, int company_id) {
		this.id = id;
		this.name = name;
		this.introduced = introduced;
		this.discontinued = discontinued;
		this.company_id = company_id;
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

	public Date getIntroduced() {
		return introduced;
	}

	public void setIntroduced(Date introduced) {
		this.introduced = introduced;
	}

	public Date getDiscontinued() {
		return discontinued;
	}

	public void setDiscontinued(Date discontinued) {
		this.discontinued = discontinued;
	}

	public int getCompany_id() {
		return company_id;
	}

	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + company_id;
		result = prime * result
				+ ((discontinued == null) ? 0 : discontinued.hashCode());
		result = prime * result + id;
		result = prime * result
				+ ((introduced == null) ? 0 : introduced.hashCode());
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
		Computer other = (Computer) obj;
		if (company_id != other.company_id)
			return false;
		if (discontinued == null) {
			if (other.discontinued != null)
				return false;
		} else if (!discontinued.equals(other.discontinued))
			return false;
		if (id != other.id)
			return false;
		if (introduced == null) {
			if (other.introduced != null)
				return false;
		} else if (!introduced.equals(other.introduced))
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
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Config.DATE_FORMAT);
		return id + "/" 
				+ name + "/" 
				+ (introduced != null ? simpleDateFormat.format(introduced) : "") + "/"
				+ (discontinued != null ? simpleDateFormat.format(discontinued) : "") + "/" 
				+ company_id;
	}
}
