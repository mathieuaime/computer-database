package com.excilys.computerdatabase.dtos;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CompanyDTO {

    @Min(0)
    private long id;

    @NotNull
    private String name;

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

    @Override
    public String toString() {
        return id + "/" + name;
    }
}
