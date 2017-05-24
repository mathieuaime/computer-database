<%@ taglib tagdir="/WEB-INF/tags" prefix="customTag"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ tag language="java" pageEncoding="ISO-8859-1"%>

<%@ attribute name="user" required="false" rtexprvalue="true"%>

<div class="navbar-collapse collapse">
	<ul class="nav navbar-nav">
		<customTag:link home="true" href="dashboard" classe="navbar-brand" />
	</ul>
	<c:if test="${user != null}">
		<ul class="nav navbar-nav navbar-right">
			<li class="dropdown"><a href="#" class="dropdown-toggle"
				data-toggle="dropdown">${user} <b class="caret"></b></a>
				<ul class="dropdown-menu">
					<li><a href="logout">Logout</a></li>
				</ul></li>
		</ul>
	</c:if>
</div>