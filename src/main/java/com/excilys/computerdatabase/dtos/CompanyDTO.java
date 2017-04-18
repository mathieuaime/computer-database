package com.excilys.computerdatabase.dtos;

import java.util.ArrayList;
import java.util.List;

public class CompanyDTO {
    private long id;
    private String name;
    private List<Long> computersList;
    
    public CompanyDTO() {
        computersList = new ArrayList<Long>();
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
    public List<Long> getComputersList() {
        return computersList;
    }
    public void setComputersList(List<Long> computersList) {
        this.computersList = computersList;
    }

    @Override
    public String toString() {
        return id + "/" + name;
    }
}
