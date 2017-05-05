<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="utils"%>
<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
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
					<div class="label label-default pull-right">id:
						${computer.id}</div>
					<h1>Edit Computer</h1>

					<form action="editComputer" method="POST">
						<input type="hidden" value="${computer.id}" id="id" name="id" />
						<fieldset>
							<div class="form-group">
								<label for="computerName">Computer name</label> <input
									type="text" class="form-control" id="computerName"
									placeholder="Computer name" value="${computer.name}"
									name="name">
							</div>
							<div class="form-group">
								<fmt:parseDate value="${computer.introduced}"
									pattern="${dateFormat}" var="introducedParsed" />
								<fmt:formatDate value="${introducedParsed}" pattern="yyyy-MM-dd"
									var="formatedIntroduced" />

								<label for="introduced">Introduced date</label> <input
									type="date" class="form-control" id="introduced"
									placeholder="Introduced date" value="${formatedIntroduced}"
									name="introduced">
							</div>
							<div class="form-group">
								<fmt:parseDate value="${computer.discontinued}"
									pattern="${dateFormat}" var="discontinuedParsed" />
								<fmt:formatDate value="${discontinuedParsed}" pattern="yyyy-MM-dd"
									var="formatedDiscontinued" />
									
								<label for="discontinued">Discontinued date</label> <input
									type="date" class="form-control" id="discontinued"
									placeholder="Discontinued date"
									value="${formatedDiscontinued}"
									name="discontinued">
							</div>
							<div class="form-group">
								<label for="companyId">Company</label> <select
									class="form-control" id="companyId" name="companyId">
									<c:forEach items="${companies}" var="company">
										<c:choose>
											<c:when test="${company.id == computer.company.id}">
												<option value="${company.id}" selected="selected">${company.name}</option>
											</c:when>
											<c:otherwise>
												<option value="${company.id}">${company.name}</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</select>
							</div>
						</fieldset>
						<div class="actions pull-right">
							<input type="submit" value="Edit" class="btn btn-primary">
							or
							<utils:link href="dashboard" classe="btn btn-default"
								text="Cancel" />
						</div>
					</form>
				</div>
			</div>
			<c:if test="${error != null}">
				<div class="row">
					<div class="col-xs-8 col-xs-offset-2 box">
						<br />
						<div class="alert alert-danger">
							<strong>Erreur </strong>${error}
						</div>
					</div>
				</div>
			</c:if>
		</div>
	</section>
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