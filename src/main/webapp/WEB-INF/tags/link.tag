<%@ tag language="java" pageEncoding="ISO-8859-1"%>

<%@ attribute name="href" required="true" rtexprvalue="true"%>
<%@ attribute name="page" required="false" type="java.lang.Integer"%>
<%@ attribute name="length" required="false" type="java.lang.Integer"%>
<%@ attribute name="text" required="false" type="java.lang.String"%>
<%@ attribute name="previous" required="false" type="java.lang.Boolean"%>
<%@ attribute name="next" required="false" type="java.lang.Boolean"%>
<%@ attribute name="classe" required="false" type="java.lang.String"%>
<%@ attribute name="button" required="false" type="java.lang.String"%>
<%@ attribute name="li" required="false" type="java.lang.String"%>

<%
    boolean previousA = (previous != null ? previous : false);
    boolean nextA = (next != null ? next : false);

    String output = "";
    String pageAdresse = "";
    String lengthAdresse = "";
    String adresse = "";
    String textA = (text != null ? text : "");
    
    if (button != null) {
        text = "<button type=\"button\" class=\"btn btn-" + button + "\">" + text + "</button>";
    }

    if (previousA || nextA) {
        pageAdresse = (page != null ? String.valueOf(page + (previousA ? -1 : 1)) : String.valueOf(page));
        
        lengthAdresse = (length != null ? String.valueOf(length) : String.valueOf(length));
        
        adresse = href + "?page=" + pageAdresse + "&length=" + lengthAdresse;
        
        output = "<a href=\"" + adresse + "\" aria-label=\"Next\"> <span aria-hidden=\"true\">&" + (previousA ? 'l' : 'r') + "aquo;</span></a>";
    } else {
        pageAdresse = (page != null ? String.valueOf(page) : "1");
        lengthAdresse = (length != null ? String.valueOf(length) : "10");

        adresse = href + "?page=" + pageAdresse + "&length=" + lengthAdresse;

        output = "<a class=\"" + classe + "\" href=\"" + adresse + "\">" + textA + "</a>";
    }
    
    if(li != null) {
        output = "<li" + (!li.equals("default") ? " class=" + li + " " : "") + ">" + output + "</li>";
    }

    out.println(output);
%>