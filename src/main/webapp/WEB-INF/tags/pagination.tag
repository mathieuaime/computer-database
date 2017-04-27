<%@ taglib tagdir="/WEB-INF/tags" prefix="customTag"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ tag language="java" pageEncoding="ISO-8859-1"%>

<%@ attribute name="page" required="true" rtexprvalue="true"%>
<%@ attribute name="nbPage" required="true" type="java.lang.Integer"%>
<%@ attribute name="pageSize" required="true" type="java.lang.Integer"%>
<%@ attribute name="search" required="false" type="java.lang.String"%>
<%@ attribute name="column" required="false" type="java.lang.String"%>
<%@ attribute name="order" required="false" type="java.lang.String"%>

<ul class="pagination">
	<c:if test="${page > 1}">
		<customTag:link href="dashboard" page="${page}" pageSize="${pageSize}"
			search="${search}" column="${column}" order="${order}" previous="true"
			li="default"></customTag:link>
	</c:if>

	<c:forEach var="i" begin="${page > 5 ? page - 5 : 1}"
		end="${page + 5 <= nbPage ? page + 5 : nbPage}">

		<c:choose>
			<c:when test="${page == i}">
				<customTag:link href="dashboard" page="${i}" pageSize="${pageSize}"
					search="${search}" column="${column}" order="${order}" text="${i}"
					li="active" />
			</c:when>
			<c:otherwise>
				<customTag:link href="dashboard" page="${i}" pageSize="${pageSize}"
					search="${search}" column="${column}" order="${order}" text="${i}"
					li="default" />
			</c:otherwise>
		</c:choose>


	</c:forEach>

	<c:if test="${page < nbPage}">
		<customTag:link href="dashboard" page="${page}" pageSize="${pageSize}"
			search="${search}" column="${column}" order="${order}" next="true"
			li="default" />
	</c:if>
</ul>

<div class="btn-group btn-group-sm pull-right" role="group">

	<c:forEach items="${pageSizes}" var="i">
		<c:choose>
			<c:when test="${pageSize == i}">
				<customTag:link href="dashboard" pageSize="${i}" text="${i}"
					search="${search}" column="${column}" order="${order}" button="primary" />
			</c:when>
			<c:otherwise>
				<customTag:link href="dashboard" pageSize="${i}" text="${i}"
					search="${search}" column="${column}" order="${order}" button="default" />
			</c:otherwise>
		</c:choose>
	</c:forEach>
</div>