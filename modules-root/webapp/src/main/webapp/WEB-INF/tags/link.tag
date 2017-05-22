<%@tag import="com.excilys.computerdatabase.config.Config"%>
<%@ tag language="java" pageEncoding="ISO-8859-1"%>

<%@ attribute name="href" required="true" rtexprvalue="true"%>

<%@ attribute name="page" required="false" type="java.lang.Integer"%>
<%@ attribute name="pageSize" required="false" type="java.lang.Integer"%>

<%@ attribute name="search" required="false" type="java.lang.String"%>
<%@ attribute name="column" required="false" type="java.lang.String"%>
<%@ attribute name="order" required="false" type="java.lang.String"%>
<%@ attribute name="text" required="false" type="java.lang.String"%>

<%@ attribute name="previous" required="false" type="java.lang.Boolean"%>
<%@ attribute name="next" required="false" type="java.lang.Boolean"%>
<%@ attribute name="home" required="false" type="java.lang.Boolean"%>

<%@ attribute name="classe" required="false" type="java.lang.String"%>

<%@ attribute name="button" required="false" type="java.lang.String"%>
<%@ attribute name="li" required="false" type="java.lang.String"%>

<%
	int PAGE_DEFAULT = Integer.parseInt(Config.getProperties().getProperty("page_default"));
	int pageSize_DEFAULT = Integer.parseInt(Config.getProperties().getProperty("page_size_default"));

    previous 				= (previous	!= null ? previous 	: false);
    next 					= (next 	!= null ? next 		: false);

    page					= (page 	!= null ? page 		: PAGE_DEFAULT);
    pageSize				= (pageSize != null ? pageSize 	: pageSize_DEFAULT);

    String output 			= "";
    String pageAdresse 		= "?page=" + String.valueOf(page);
    String pageSizeAdresse 	= "&pageSize=" + String.valueOf(pageSize);

    String searchAdresse 	= (search	!= null && !search.equals("") 	? "&search=" 	+ search 	: "");
    String columnAdresse 	= (column 	!= null && !column.equals("") 	? "&column=" 	+ column 	: "");
    String orderAdresse 	= (order 	!= null && !order.equals("") 	? "&order=" 	+ order 	: "");
    
    text 					= (text != null ? text : (home != null && home ? " Application - Computer Database " : ""));

    String adresse 			= href + pageAdresse + pageSizeAdresse + searchAdresse + columnAdresse + orderAdresse;

    //Si on veut que le lien soit un bouton de type button
    if (button != null) {
        text = "<button type=\"button\" class=\"btn btn-" + button + "\">" + text + "</button>";
    }

    //Si on veut que le lien soit un lien précédent ou suivant
    if (previous || next) {

        //la page en string (page + ou - 1)
        pageAdresse = "?page=" + String.valueOf(page + (previous ? -1 : 1));

        //l'adresse avec les champs search, column et order facultatifs
        adresse = href + pageAdresse + pageSizeAdresse + searchAdresse + columnAdresse + orderAdresse;

        //la balise a avec les parametres correspondants (previous ou next)
        output = "<a href=\"" + adresse + "\" aria-label=\"" + (previous ? "Next" : "Previous")
                + "\"> <span aria-hidden=\"true\">&" + (previous ? 'l' : 'r') + "aquo;</span></a>";

    } else {

        //la balise a avec les parametres correspondants
        output = "<a class=\"" + classe + "\" href=\"" + adresse + "\">" + text + "</a>";
    }

    if (li != null) {
        output = "<li" + (!li.equals("default") ? " class=" + li + " " : "") + ">" + output + "</li>";
    }

    out.println(output);
%>