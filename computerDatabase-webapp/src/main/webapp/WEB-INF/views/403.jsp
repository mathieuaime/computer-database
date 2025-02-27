<%@ taglib tagdir="/WEB-INF/tags" prefix="utils"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
<head>
	<title><spring:message code="label.title" /></title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<!-- Bootstrap -->
	<link href="resources/css/bootstrap.min.css" rel="stylesheet" media="screen">
	<link href="resources/css/font-awesome.css" rel="stylesheet" media="screen">
	<link href="resources/css/main.css" rel="stylesheet" media="screen">
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<utils:navbar user="${user}"></utils:navbar>
	</header>

	<section id="main">
		<div class="container">
			<div class="alert alert-danger">
				Error 403: Access denied!
				<br/>
					Dear <strong>${user}</strong>, You are not authorized to perform this action.
				<!-- stacktrace -->
			</div>
		</div>
	</section>

	<script src="resources/js/jquery.min.js"></script>
	<script src="resources/js/bootstrap.min.js"></script>
	<script src="resources/js/dashboard.js"></script>

</body>
</html>