package com.excilys.computerdatabase.models;

public class Company {
    private long id;
    private String name;

    /**
     * Company default constructor.
     */
    public Company() {
        super();
    }

    /**
     * Company private constructor.
     * @param name name
     */
    private Company(String name) {
        this.name = name;
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

    public static class Builder {
        private Company company;

        /**
         * Builder constructor.
         * @param name name
         */
        public Builder(String name) {
            this.company = new Company(name);
        }

        /**
         * Add an id.
         * @param id id
         * @return Builder
         */
        public Builder id(long id) {
            company.id = id;
            return this;
        }

        /**
         * Build the company.
         * @return Company
         */
        public Company build() {
            return company;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Company other = (Company) obj;
        if (id != other.id) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return id + "/" + name;
    }
}
