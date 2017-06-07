<%@ taglib tagdir="/WEB-INF/tags" prefix="customTag"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<%@ tag language="java" pageEncoding="ISO-8859-1"%>

<%@ attribute name="isAdmin" required="true" type="java.lang.Boolean"%>
<%@ attribute name="user" required="false" rtexprvalue="true"%>

<div class="sidebar-nav">
	<div class="navbar navbar-default" role="navigation">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target=".sidebar-navbar-collapse">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<span class="visible-xs navbar-brand">Menu</span>
		</div>
		<div class="navbar-collapse collapse sidebar-navbar-collapse">
			<ul class="nav navbar-nav">
				<li class="active"><a href="dashboard"><spring:message
							code="label.dashboard.title" /></a></li>
				<c:if test="${isAdmin}">
					<li><a href="addComputer" id="addComputer"><spring:message
								code="label.addComputer.title" /></a></li>
				</c:if>
			</ul>
		</div>
		<!--/.nav-collapse -->
	</div>
</div>