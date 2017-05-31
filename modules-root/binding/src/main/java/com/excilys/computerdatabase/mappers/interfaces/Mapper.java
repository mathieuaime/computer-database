package com.excilys.computerdatabase.mappers.interfaces;

import java.sql.ResultSet;
import java.util.List;

import com.excilys.computerdatabase.exceptions.NotFoundException;

public interface Mapper<BEAN, DTO> {
    /**
     * Create a bean from a ResultSet
     * @param rset the ResultSet
     * @return BEAN
     */
    BEAN bean(ResultSet rset);

    /**
     * Create a list of beans from a ResultSet.
     * @param rset the ResultSet
     * @return List BEAN
     */
    List<BEAN> beans(ResultSet rset);

    /**
     * Create a computer from a DTO.
     * @param dto the dto
     * @return BEAN
     * @throws NotFoundException BEAN Not Found
     */
    BEAN bean(DTO dto) throws NotFoundException;

    /**
     * Create a DTO from a bean.
     * @param bean the bean
     * @return DTO
     */
    DTO dto(BEAN bean);
}
