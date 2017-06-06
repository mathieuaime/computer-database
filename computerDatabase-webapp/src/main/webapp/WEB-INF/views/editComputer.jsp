<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="utils"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<title><spring:message code="label.title" /> - <spring:message
		code="label.editComputer.title" /></title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<link href="resources/css/bootstrap.min.css" rel="stylesheet"
	media="screen">
<link href="resources/css/font-awesome.css" rel="stylesheet"
	media="screen">
<link href="resources/css/main.css" rel="stylesheet" media="screen">
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<utils:navbar user="${user}"></utils:navbar>
	</header>
	<div class="row">
		<div class="col-sm-2">
			<utils:sidebar isAdmin="${isAdmin}" />
		</div>
		<div class="col-sm-10">
			<section id="main">
				<div class="container">
					<div class="row">
						<div class="col-xs-8 col-xs-offset-2 box">
							<div class="label label-default pull-right">id:
								${computerDTO.id}</div>
							<h1>
								<spring:message code="label.editComputer.title" />
							</h1>
							<form:form action="editComputer" method="POST"
								modelAttribute="computerDTO">
								<fieldset>
									<form:hidden path="id" />
									<div class="form-group">
										<form:label path="name">
											<spring:message code="label.computer.name" />
										</form:label>
										<form:input class="form-control" path="name" />
									</div>
									<div class="form-group">
										<form:label path="introduced">
											<spring:message code="label.computer.introduced" />
										</form:label>
										<form:input type="date" class="form-control" path="introduced" />
									</div>
									<div class="form-group">
										<form:label path="discontinued">
											<spring:message code="label.computer.discontinued" />
										</form:label>
										<form:input type="date" class="form-control"
											path="discontinued" />
									</div>
									<div class="form-group">
										<form:label path="company">
											<spring:message code="label.computer.company" />
										</form:label>
										<form:select class="form-control" path="company.id">
											<form:options items="${companies}" itemValue="id"
												itemLabel="name" />
										</form:select>
									</div>
								</fieldset>
								<div class="actions pull-right">
									<spring:message code="label.edit" var="editMessage" />
									<spring:message code="label.cancel" var="cancelMessage" />
									<input type="submit" value="${editMessage}"
										class="btn btn-primary">
									<spring:message code="label.or" />
									<utils:link href="dashboard" classe="btn btn-default"
										text="${cancelMessage}" />
								</div>
							</form:form>
						</div>
					</div>
					<c:if test="${error != null}">
						<div class="row">
							<div class="col-xs-8 col-xs-offset-2 box">
								<br />
								<div class="alert alert-danger">
									<strong><spring:message code="label.error" /> </strong>${error}
								</div>
							</div>
						</div>
					</c:if>
				</div>
			</section>
		</div>
	</div>
	<script src="resources/js/jquery.min.js"></script>
	<script src="resources/js/bootstrap.min.js"></script>
	<script>
		window.onload = function() {
			document.getElementById("discontinued").setAttribute("min",
					document.getElementById('introduced').value);
			document.getElementById('introduced').onchange = function() {
				document.getElementById("discontinued").setAttribute("min",
						this.value);
			}
		};
	</script>
</body>
</html>