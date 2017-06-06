<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="utils"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<title><spring:message code="label.title" /> - <spring:message
		code="label.dashboard.title" /></title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<!-- Bootstrap -->
<link href="resources/css/bootstrap.css" rel="stylesheet"
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
			<utils:sidebar isAdmin="${isAdmin}"/>
		</div>
		<div class="col-sm-10">
			<section id="main">
				<div class="container">

					<c:if test="${info != null}">
						<div class="row">
							<div class="col-xs-8 col-xs-offset-2 box">
								<br />
								<div class="alert alert-danger">
									<strong>Info </strong>${info}
								</div>
							</div>
						</div>
					</c:if>

					<h1 id="homeTitle">${computerCount}Computer${computerCount > 1 ? 's' : ''}</h1>

					<div id="actions" class="form-horizontal">
						<div class="pull-left">
							<form id="searchForm" action="" method="GET" class="form-inline">
								<spring:message code="label.dashboard.search"
									var="searchMessage" />
								<spring:message code="label.dashboard.filter"
									var="filterMessage" />

								<input type="hidden" id="pageSize" name="pageSize"
									value="${pageSize}" /> <input type="search" id="searchbox"
									name="search" class="form-control"
									placeholder="${searchMessage}" value="${search}" /> <input
									type="submit" id="searchsubmit" value="${filterMessage}"
									class="btn btn-primary" />
							</form>
						</div>
						<c:if test="${isAdmin}">
							<div class="pull-right">
								<a class="btn btn-default" id="editComputer" href="#"
									onclick="$.fn.toggleEditMode();"><spring:message
										code="label.edit" /></a>
							</div>
						</c:if>
					</div>
				</div>

				<c:if test="${isAdmin}">
					<form id="deleteForm" action="dashboard" method="POST">
						<input type="hidden" name="selection" value="" /> <input
							type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" />
					</form>
				</c:if>

				<div class="container" style="margin-top: 10px;">
					<table class="table table-striped table-bordered">
						<thead>
							<tr>
								<!-- Variable declarations for passing labels as parameters -->

								<c:if test="${isAdmin}">
									<th class="editMode" style="width: 60px; height: 22px;"><input
										type="checkbox" id="selectall" /> <span
										style="vertical-align: top;"> - <a href="#"
											id="deleteSelected" onclick="$.fn.deleteSelected();"> <i
												class="fa fa-trash-o fa-lg"></i>
										</a>
									</span></th>
								</c:if>

								<!-- Table header for Computer Name -->
								<spring:message code="label.computer.name"
									var="computerNameMessage" />
								<th><utils:link href="dashboard" pageSize="${pageSize}"
										search="${search}" column="name"
										order="${column != null && column == 'name' && order != 'DESC' ? 'DESC' : 'ASC'}"
										text="${computerNameMessage}" /> <c:if
										test="${column == 'name' && order == 'ASC'}">
										<span class="glyphicon glyphicon-chevron-up"></span>
									</c:if> <c:if test="${column == 'name' && order == 'DESC'}">
										<span class="glyphicon glyphicon-chevron-down"></span>
									</c:if></th>

								<!-- Table header for Introduced Date -->
								<spring:message code="label.computer.introduced"
									var="computerIntroducedMessage" />
								<th><utils:link href="dashboard" pageSize="${pageSize}"
										search="${search}" column="introduced"
										order="${column != null && column == 'introduced' && order != 'DESC' ? 'DESC' : 'ASC'}"
										text="${computerIntroducedMessage}" /> <c:if
										test="${column == 'introduced' && order == 'ASC'}">
										<span class="glyphicon glyphicon-chevron-up"></span>
									</c:if> <c:if test="${column == 'introduced' && order == 'DESC'}">
										<span class="glyphicon glyphicon-chevron-down"></span>
									</c:if></th>

								<!-- Table header for Discontinued Date -->
								<spring:message code="label.computer.discontinued"
									var="computerDiscontinuedMessage" />
								<th><utils:link href="dashboard" pageSize="${pageSize}"
										search="${search}" column="discontinued"
										order="${column != null && column == 'discontinued' && order != 'DESC' ? 'DESC' : 'ASC'}"
										text="${computerDiscontinuedMessage}" /> <c:if
										test="${column == 'discontinued' && order == 'ASC'}">
										<span class="glyphicon glyphicon-chevron-up"></span>
									</c:if> <c:if test="${column == 'discontinued' && order == 'DESC'}">
										<span class="glyphicon glyphicon-chevron-down"></span>
									</c:if></th>

								<!-- Table header for Company -->
								<spring:message code="label.computer.company"
									var="computerCompanyMessage" />
								<th><utils:link href="dashboard" pageSize="${pageSize}"
										search="${search}" column="company.name"
										order="${column != null && column == 'company.name' && order != 'DESC' ? 'DESC' : 'ASC'}"
										text="${computerCompanyMessage}" /> <c:if
										test="${column == 'company.name' && order == 'ASC'}">
										<span class="glyphicon glyphicon-chevron-up"></span>
									</c:if> <c:if test="${column == 'company.name' && order == 'DESC'}">
										<span class="glyphicon glyphicon-chevron-down"></span>
									</c:if></th>

							</tr>
						</thead>
						<!-- Browse attribute computers -->
						<tbody id="results">
							<c:forEach items="${computerPage.objects}" var="computer">
								<utils:computer computer="${computer}" isAdmin="${isAdmin}"></utils:computer>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</section>
		</div>
	</div>

	<footer class="navbar-fixed-bottom">
		<div class="container text-center">
			<utils:pagination nbPage="${nbPage}" pageSize="${pageSize}"
				search="${search}" page="${page}" />
		</div>

	</footer>
	<script src="resources/js/jquery.min.js"></script>
	<script src="resources/js/bootstrap.min.js"></script>
	<script src="resources/js/dashboard.js"></script>

</body>
</html>