package com.excilys.computerdatabase.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerdatabase.models.Page;

public class PageValidator implements ConstraintValidator<VerificationPage, Page<?>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PageValidator.class);

    @Override
    public void initialize(VerificationPage arg0) {
    }

    @Override
    public boolean isValid(Page<?> page, ConstraintValidatorContext context) {
        LOGGER.info("-------->isValide(page, validatorContext) args: " + page);
        if (attributVide(page)) {
            return false;
        }
        return verificationPageSize(page.getPageSize()) && verificationSearch(page.getSearch())
                && verificationColumn(page.getColumn()) && verificationOrder(page.getOrder());
    }

    /**
     * Indique si les attribut en string son vide.
     * @param page : page de la page.
     * @return boolean : true ou false
     */
    public static boolean attributVide(Page<?> page) {
        return ((page.getOrder() == null)|| (page.getColumn() == null));
    }

    /**
     * Vérifie la valeur de nombreElement demandé.
     * @param nombreElement : nombre d'élément demandé
     * @return boolean : true or false
     */
    public static boolean verificationPageSize(int nombreElement) {
        LOGGER.info("-------->verificationPageSize(nombreElement) args: " + nombreElement);
        switch (nombreElement) {
        case 10:
        case 50:
        case 100:
            return true;
        default:
            return false;
        }
    }

    /**
     * Verifie la valeur de typeSearch.
     * @param type : type de la recherche
     * @return boolean : true or false
     */
    public static boolean verificationColumn(String column) {
        LOGGER.info("-------->verificationTypeSearch(typeSearch) args: " + column);
        return ((column.equals("name")) || (column.equals("company.name"))
                || (column.equals("discontinued")) || (column.equals("introduced")));
    }

    /**
     * Verification de la valeur de name et typeSearch.
     * @param name : nom entrer en recherche
     * @return boolean : true or false
     */
    public static boolean verificationSearch(String name) {
        LOGGER.info("-------->verificationSearch(name,typeSearch) args: " + name);
        if (name == null) {
            return true;
        }
        return name.equals(name.replaceAll("[^(\\d\\s\\w\\.\\-)]|_", " "));

    }

    /**
     * Verifie la valeur de order.
     * @param order : si on a besoin de order
     * @return boolean : true or false
     */
    public static boolean verificationOrder(String order) {
        return ((order.equals("ASC")) || (order.equals("DESC")));
    }
}
