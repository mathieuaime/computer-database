<%@ taglib tagdir="/WEB-INF/tags" prefix="customTag"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<%@ tag language="java" pageEncoding="ISO-8859-1"%>

<%@ attribute name="user" required="false" rtexprvalue="true"%>

<div class="navbar-collapse collapse">
	<ul class="nav navbar-nav">
		<customTag:link home="true" href="dashboard" classe="navbar-brand" />
	</ul>

	<ul class="nav navbar-nav navbar-right">
		<li class="dropdown"><a href="#" class="dropdown-toggle"
			data-toggle="dropdown">Language <b class="caret"></b></a>
			<ul class="dropdown-menu">
				<li><a href="dashboard?language=fr">FR</a></li>
				<li><a href="dashboard?language=en">EN</a></li>
			</ul></li>
		<c:if test="${user != null}">
			<li class="dropdown"><a href="#" class="dropdown-toggle"
				data-toggle="dropdown">${user} <b class="caret"></b></a>
				<ul class="dropdown-menu">
					<li><a href="logout"><spring:message code="label.logout" /></a></li>
				</ul></li>

		</c:if>
	</ul>
</div>