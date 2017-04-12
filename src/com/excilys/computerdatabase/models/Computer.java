package com.excilys.computerdatabase.models;

import java.util.Date;
import java.text.SimpleDateFormat;

import com.excilys.computerdatabase.config.Config;
import com.excilys.computerdatabase.exceptions.IntroducedAfterDiscontinuedException;

public class Computer {

	public static final String TABLE_NAME = "computer";

	public static final String FIELD_ID = "id";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_INTRODUCED = "introduced";
	public static final String FIELD_DISCONTINUED = "discontinued";
	public static final String FIELD_COMPANY_ID = "company_id";

	private long id;

	private String name;

	private Date introduced;

	private Date discontinued;

	private Company company;

	private Computer(Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.introduced = builder.introduced;
		this.discontinued = builder.discontinued;
		this.company = builder.company;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public static class Builder {

		private long id;
		private String name;
		private Date introduced;
		private Date discontinued;
		private Company company;

		public Builder(long id, String name) {
			this.id = id;
			this.name = name;
		}

		public Builder introduced(Date introduced) {
			this.introduced = introduced;
			return this;
		}

		public Builder discontinued(Date discontinued) {
			this.discontinued = discontinued;
			return this;
		}

		public Builder company(Company company) {
			this.company = company;
			return this;
		}

		public Computer build() throws IntroducedAfterDiscontinuedException {
		    Computer computer = new Computer(this);
		    
		    if (introduced.after(discontinued)) {
		        throw new IntroducedAfterDiscontinuedException("Introduced date after Discontinued date");
		    }
		    
		    return computer;
		}

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((discontinued == null) ? 0 : discontinued.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((introduced == null) ? 0 : introduced.hashCode());
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
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
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
		return id + "/" + name + "/" + (introduced != null ? simpleDateFormat.format(introduced) : "") + "/"
				+ (discontinued != null ? simpleDateFormat.format(discontinued) : "") + "/" + company;
	}
}
