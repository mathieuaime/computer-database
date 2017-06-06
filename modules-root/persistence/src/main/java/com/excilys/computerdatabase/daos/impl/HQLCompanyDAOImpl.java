package com.excilys.computerdatabase.daos.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import com.excilys.computerdatabase.config.hibernate.HibernateConfig;
import com.excilys.computerdatabase.daos.interfaces.CompanyDAO;
import com.excilys.computerdatabase.exceptions.CompanyNotFoundException;
import com.excilys.computerdatabase.exceptions.NotFoundException;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;
import com.excilys.computerdatabase.models.Page;

@Repository
public class HQLCompanyDAOImpl implements CompanyDAO {

    private static final String QUERY_FIND_COMPANY_BY_NAME  = "SELECT c FROM Company c WHERE c.name LIKE :name";

    private static final String QUERY_DELETE_COMPANY        = "DELETE FROM Company WHERE id = :id";

    private static final String QUERY_FIND_COMPANIES        = "SELECT c FROM Company c ORDER BY ";

    private static final String QUERY_FIND_COMPUTERS        = "SELECT c FROM Computer c WHERE c.company.id = :id";

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(HQLCompanyDAOImpl.class);

    @Override
    public Page<Company> findAll() {
        LOGGER.info("findAll()");
        //return findAll(0, -1, null, null, "name");
        return findAll(new Page<>());
    }

    @Override
    public Page<Company> findAll(Page<?> page) {
        LOGGER.info("findAll(page: " + page + ")");
        String query = QUERY_FIND_COMPANIES + page.getColumn() + " " + page.getOrder();
        
        Page<Company> res = new Page<>(page);

        try (Session session = HibernateConfig.getSessionFactory().openSession();) {
            Query<Company> q1 = session.createQuery(query, Company.class);

            if (page.getPageSize() != -1) {
                q1.setMaxResults(page.getPageSize());
                q1.setFirstResult(page.getOffset());
            }

            res.setObjects(q1.list());
        }
        
        return res;
    }

    @Override
    public Company getById(long id) throws CompanyNotFoundException {
        LOGGER.info("getById(id : " + id + ")");
        try (Session session = HibernateConfig.getSessionFactory().openSession();) {
            Company company = session.load(Company.class, id);
            Hibernate.initialize(company);
            return company;
        } catch (ObjectNotFoundException e) {
            throw new CompanyNotFoundException("Company Not Found");
        }
    }

    @Override
    public List<Company> getByName(String name) {
        LOGGER.info("getByName(name : " + name + ")");
        try (Session session = HibernateConfig.getSessionFactory().openSession();) {
            Query<Company> query = session.createQuery(QUERY_FIND_COMPANY_BY_NAME, Company.class);
            query.setParameter("name", name);
            return query.list();
        }
    }

    @Override
    public List<Computer> getComputers(long id) throws CompanyNotFoundException {
        LOGGER.info("getComputers(id : " + id + ")");
        try (Session session = HibernateConfig.getSessionFactory().openSession();) {
            Query<Computer> query = session.createQuery(QUERY_FIND_COMPUTERS, Computer.class);
            query.setParameter("id", id);
            return query.list();
        }
    }

    @Override
    public void delete(long id) {
        LOGGER.info("delete(id : " + id + ")");
        try (Session session = HibernateConfig.getSessionFactory().openSession();) {
            Query<?> q = session.createQuery(QUERY_DELETE_COMPANY);
            q.setParameter("id", id);
            session.beginTransaction();
            q.executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
    public Company save(Company computer) throws NotFoundException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Company update(Company computer) throws NotFoundException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(List<Long> listId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int count(String search) {
        throw new UnsupportedOperationException();
    }
}
