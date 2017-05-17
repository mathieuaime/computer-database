<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="utils"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
<head>
<title><spring:message code="label.title" /></title>
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
		<div class="container">
			<utils:link home="true" href="dashboard" classe="navbar-brand" />
		</div>
	</header>

	<section id="main">
		<div class="container">
			<div class="row">
				<div class="col-xs-8 col-xs-offset-2 box">
					<h1>
						<spring:message code="label.addComputer.title" />
					</h1>
					<form action="addComputer" method="POST">
						<fieldset>
							<div class="form-group">
								<label for="computerName"><spring:message
										code="label.computer.name" /></label> <input type="text"
									class="form-control" id="computerName" name="name" required>
							</div>
							<div class="form-group">
								<label for="introduced"><spring:message
										code="label.computer.introduced" /></label> <input type="date"
									class="form-control" id="introduced"
									placeholder="Introduced date" name="introduced">
							</div>
							<div class="form-group">
								<label for="discontinued"><spring:message
										code="label.computer.discontinued" /></label> <input type="date"
									class="form-control" id="discontinued" name="discontinued"
									min="<%=request.getParameter("introduced")%>">
							</div>
							<div class="form-group">
								<label for="companyId"><spring:message
										code="label.computer.company" /></label> <select class="form-control"
									id="companyId" name="companyId">
									<c:forEach items="${companies}" var="company">
										<option value="${company.id}">${company.name}</option>
									</c:forEach>
								</select>
							</div>
						</fieldset>
						<div class="actions pull-right">
							<spring:message code="label.add" var="addMessage" />
							<spring:message code="label.cancel" var="cancelMessage" />
							<input type="submit" value="${addMessage}"
								class="btn btn-primary">
							<spring:message code="label.or" />
							<utils:link href="dashboard" classe="btn btn-default"
								text="${cancelMessage}" />
						</div>
					</form>
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
	<script>
		window.onload = function() {
			document.getElementById('introduced').onchange = function() {
				document.getElementById("discontinued").setAttribute("min",
						this.value);
			}
		};
	</script>
</body>
</html>