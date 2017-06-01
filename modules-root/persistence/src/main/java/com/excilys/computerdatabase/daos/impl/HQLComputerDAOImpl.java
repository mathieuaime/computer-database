package com.excilys.computerdatabase.daos.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;

import org.hibernate.Hibernate;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import com.excilys.computerdatabase.config.hibernate.HibernateConfig;
import com.excilys.computerdatabase.daos.interfaces.ComputerDAO;
import com.excilys.computerdatabase.exceptions.CompanyNotFoundException;
import com.excilys.computerdatabase.exceptions.ComputerNotFoundException;
import com.excilys.computerdatabase.models.Company;
import com.excilys.computerdatabase.models.Computer;

@Repository
public class HQLComputerDAOImpl implements ComputerDAO {

    private static final String QUERY_DELETE_FROM_COMPANY       = "DELETE FROM Computer WHERE company_id = :id";

    private static final String QUERY_DELETE_COMPUTERS          = "DELETE FROM Computer WHERE id IN (:list)";

    private static final String QUERY_FIND_COMPUTER             = "SELECT c FROM Computer c ";

    private static final String QUERY_FIND_COMPUTER_BY_NAME     = QUERY_FIND_COMPUTER + "WHERE c.name LIKE :name";

    private static final String QUERY_FIND_COMPANY              = "SELECT c.company from Computer c where c.id = :id";

    private static final String QUERY_COUNT_COMPUTERS           = "SELECT COUNT(c) FROM Computer c";

    private static final String QUERY_COUNT_COMPUTERS_SEARCH    = QUERY_COUNT_COMPUTERS + " WHERE c.name LIKE :search OR c.company.name LIKE :search";

    private static long countTotal = -1;

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(HQLComputerDAOImpl.class);

    @Override
    public List<Computer> findAll() {
        LOGGER.info("findAll()");
        return findAll(0, 0, null, "name", "ASC");
    }

    @Override
    public List<Computer> findAll(int offset, int length, String search, String column, String order) {
        LOGGER.info("findAll(offset : " + offset + ", length : " + length + ", search : " + search + ", column : "
                + column + ", order : " + order + ")");

        column = column == null || column.equals("") ? "name" : column;
        order = order == null || order.equals("") ? "ASC" : order;
        length = length == 0 ? (int) count(null) : length;

        String query1 = QUERY_FIND_COMPUTER;
        boolean isSearch = search != null && !search.equals("");

        //TODO forcer hibernate à utiliser une requete sql custom pour le tri par companie
        /*if (column.startsWith("company")) {
            query1 = QUERY_FIND_COMPUTER_ORDER_BY_COMPANY;

            if (isSearch) {
                query1 += " AND c.name LIKE :search OR c.company.name LIKE :search";
            }

        } else {
            if (isSearch) {
                query1 += " WHERE c.name LIKE :search OR c.company.name LIKE :search";
            }
        }*/

        if (isSearch) {
            query1 += " WHERE c.name LIKE :search OR c.company.name LIKE :search";
        }

        query1 += " ORDER BY c." + column + " " + order;

        try (Session session = HibernateConfig.getSessionFactory().openSession();) {
            Query<Computer> q1 = session.createQuery(query1, Computer.class);

            if (isSearch) {
                q1.setParameter("search", search + "%");
            }
            q1.setMaxResults(length);
            q1.setFirstResult(offset);

            return q1.list();
        }
    }

    @Override
    public Computer getById(long id) throws ComputerNotFoundException, CompanyNotFoundException {
        LOGGER.info("getById(id : " + id + ")");
        try (Session session = HibernateConfig.getSessionFactory().openSession();) {
            Computer computer = session.load(Computer.class, id);
            Hibernate.initialize(computer);
            return computer;
        } catch (ObjectNotFoundException e) {
            throw new ComputerNotFoundException("Computer Not Found");
        } catch (ConstraintViolationException e) {
            throw new CompanyNotFoundException("Company not Found");
        }
    }

    @Override
    public List<Computer> getByName(String name) {
        LOGGER.info("getByName(name : " + name + ")");
        try (Session session = HibernateConfig.getSessionFactory().openSession();) {
            Query<Computer> query = session.createQuery(QUERY_FIND_COMPUTER_BY_NAME, Computer.class);
            query.setParameter("name", name);
            return query.list();
        }
    }

    @Override
    public Computer save(Computer computer) throws CompanyNotFoundException {
        LOGGER.info("add(computer : " + computer + ")");
        try (Session session = HibernateConfig.getSessionFactory().openSession();) {
            long id = (Long) session.save(computer);
            computer.setId(id);
            ++countTotal;
            return computer;
        } catch (ConstraintViolationException e) {
            throw new CompanyNotFoundException("Computer Not Found");
        }
    }

    @Override
    public Computer update(Computer computer) throws ComputerNotFoundException, CompanyNotFoundException {
        LOGGER.info("update(computer : " + computer + ")");
        try (Session session = HibernateConfig.getSessionFactory().openSession();) {
            session.beginTransaction();
            session.update(computer);
            session.getTransaction().commit();
            return computer;
        } catch (OptimisticLockException e) {
            throw new ComputerNotFoundException("Computer Not Found");
        } catch (ConstraintViolationException e) {
            throw new CompanyNotFoundException("Computer Not Found");
        }
    }

    @Override
    public void delete(long id) {
        LOGGER.info("delete(id : " + id + ")");
        delete(new ArrayList<Long>(Arrays.asList(id)));
    }

    @Override
    public void deleteFromCompany(long companyId) throws CompanyNotFoundException {

        try (Session session = HibernateConfig.getSessionFactory().openSession();) {
            Query<?> q = session.createQuery(QUERY_DELETE_FROM_COMPANY);
            q.setParameter("id", companyId);

            session.beginTransaction();
            countTotal -= q.executeUpdate();
            session.getTransaction().commit();
        } catch (ConstraintViolationException e) {
            throw new CompanyNotFoundException("Company Not Found");
        }
    }

    @Override
    public int count(String search) {
        LOGGER.info("count(search : " + search + ")");
        long count = 0;
        String query = QUERY_COUNT_COMPUTERS;
        boolean searchForTotalCount = search == null || search.equals("");

        if (searchForTotalCount && countTotal > 0) {
            count = countTotal;
        } else {
            if (!searchForTotalCount) {
                LOGGER.debug("count with search");
                query = QUERY_COUNT_COMPUTERS_SEARCH;
            }
            try (Session session = HibernateConfig.getSessionFactory().openSession();) {
                Query<?> q1 = session.createQuery(query);
                if (!searchForTotalCount) {
                    q1.setParameter("search", search + "%");
                }
                count = (Long) q1.uniqueResult();
            }
            if (searchForTotalCount) {
                countTotal = count;
            }
        }
        return (int) count;
    }

    @Override
    public Company getCompany(long id) throws CompanyNotFoundException, ComputerNotFoundException {
        LOGGER.info("getCompany(id : " + id + ")");
        try (Session session = HibernateConfig.getSessionFactory().openSession();) {
            Query<Company> query = session.createQuery(QUERY_FIND_COMPANY, Company.class);
            query.setParameter("id", id);
            Company company = query.getSingleResult();
            return company;
        } catch (NoResultException e) {
            throw new ComputerNotFoundException("Computer Not Found");
        }
    }

    @Override
    public void delete(List<Long> listId) {
        LOGGER.info("delete(listId : " + listId + ")");
        try (Session session = HibernateConfig.getSessionFactory().openSession();) {
            Query<?> q = session.createQuery(QUERY_DELETE_COMPUTERS);
            q.setParameterList("list", listId);

            session.beginTransaction();
            countTotal -= q.executeUpdate();
            session.getTransaction().commit();
        }
    }
}
