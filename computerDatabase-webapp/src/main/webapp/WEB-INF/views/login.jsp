<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="utils"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<title><spring:message code="label.title" /> - <spring:message
		code="label.login.login" /></title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<link href="resources/css/bootstrap.min.css" rel="stylesheet"
	media="screen">
<link href="resources/css/font-awesome.css" rel="stylesheet"
	media="screen">
<link href="resources/css/main.css" rel="stylesheet" media="screen">
<link href="resources/css/login.css" rel="stylesheet" media="screen">
</head>

<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<utils:navbar />
	</header>

	<section id="main">
		<div class="container">
			<div class="form">

				<ul class="tab-group">
					<li class="tab active"><a href="#login"> <spring:message
								code="label.login.login" />
					</a></li>
					<li class="tab"><a href="#signup"> <spring:message
								code="label.login.register" />
					</a></li>
				</ul>

				<div class="tab-content">
					<div id="login">
						<h1><spring:message code="label.login.login.title" /></h1>

						<form action="login" method="post">
							<c:if test="${param.error != null}">
								<div class="alert alert-danger">
									<p>
										<spring:message code="label.login.login.invalid" />
									</p>
								</div>
							</c:if>
							<c:if test="${param.logout != null}">
								<div class="alert alert-success">
									<p>
										<spring:message code="label.login.logoutSucessful" />
									</p>
								</div>
							</c:if>
							<div class="field-wrap">
								<label> <spring:message
										code="label.login.login.username" /><span class="req">*</span>
								</label> <input type="text" name="username" id="username" required autocomplete="off" />
							</div>

							<div class="field-wrap">
								<label> <spring:message
										code="label.login.login.password" /><span class="req">*</span>
								</label> <input type="password" name="password" id="password" required
									autocomplete="off" />
							</div>

							<input type="hidden" name="${_csrf.parameterName}"
								value="${_csrf.token}" />

							<button class="button button-block" id="submit">
								<spring:message code="label.login.login"/>
							</button>

						</form>

					</div>

					<div id="signup">
						<h1><spring:message code="label.login.register.title" /></h1>

						<form:form action="signup" method="POST" commandName="signupForm"
							modelAttribute="User">
							<c:if test="${param.error != null}">
								<div class="alert alert-danger">
									<p>
										<spring:message code="label.login.register.invalid" />
									</p>
								</div>
							</c:if>
							<fieldset>
								<div class="field-wrap">
									<form:label path="username">
										<spring:message code="label.login.register.username" />
										<span class="req">*</span>
									</form:label>
									<form:input type="text" path="username" autocomplete="off" />
									<form:errors path="username" cssClass="error" />
								</div>

								<div class="field-wrap">
									<form:label path="password">
										<spring:message code="label.login.register.password" />
										<span class="req">*</span>
									</form:label>
									<form:input type="password" path="password" autocomplete="off" />
									<form:errors path="password" cssClass="error" />
								</div>
								<button type="submit" class="button button-block">
									<spring:message code="label.login.register" />
								</button>
							</fieldset>
						</form:form>

					</div>

				</div>
				<!-- tab-content -->

			</div>
			<!-- /form -->


		</div>
	</section>

	<script src="resources/js/jquery.min.js"></script>
	<script src="resources/js/bootstrap.min.js"></script>
	<script src="resources/js/login.js"></script>

</body>
</html>