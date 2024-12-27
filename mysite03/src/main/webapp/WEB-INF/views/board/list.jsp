<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>
<%@ taglib uri="jakarta.tags.functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.request.contextPath}/assets/css/board.css"
	rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp" />
		<div id="content">
			<div id="board">
				<form id="search_form"
					action="${pageContext.request.contextPath}/board" method="get">
					<input type="hidden" name="a" value="search"> <input
						type="text" id="kwd" name="kwd" value="${keyword}"> <input
						type="submit" value="찾기">
				</form>
				<table class="tbl-ex">
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>글쓴이</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>&nbsp;</th>
					</tr>
					<c:forEach var="board" items="${boardList}" varStatus="status">
						<tr>

							<td>${totalCount - ((currentPage - 1) * pageSize) - status.index}</td>
							<td style="text-align:left; padding-left:${board.depth * 20}px">
								<c:if test="${board.depth > 0}">
									<img
										src="${pageContext.request.contextPath}/assets/images/reply.png" />
								</c:if> <a
								href="${pageContext.request.contextPath}/board?a=view&no=${board.no}">${board.title}</a>
							</td>
							<td>${board.author}</td>
							<td>${board.hits}</td>
							<td>${board.reg_date}</td>
							<td><a
								href="${pageContext.request.contextPath}/board?a=delete&no=${board.no}"
								class="del">삭제</a></td>
						</tr>
					</c:forEach>
				</table>

				<!-- 페이지네이션 -->
				<div class="pager">
					<ul>
						<c:if test="${currentPage > 1}">
							<li><a
								href="${pageContext.request.contextPath}/board?page=${currentPage - 1}">◀</a></li>
						</c:if>

						<c:forEach begin="${startPage}" end="${endPage}" var="i">
							<c:choose>
								<c:when test="${i == currentPage}">
									<li class="selected">${i}</li>
								</c:when>
								<c:otherwise>
									<li><a
										href="${pageContext.request.contextPath}/board?page=${i}">${i}</a></li>
								</c:otherwise>
							</c:choose>
						</c:forEach>

						<c:if test="${currentPage < totalPages}">
							<li><a
								href="${pageContext.request.contextPath}/board?page=${currentPage + 1}">▶</a></li>
						</c:if>
					</ul>
				</div>
				<!-- 페이지네이션 -->

				<div class="bottom">
					<a href="${pageContext.request.contextPath}/board?a=writeForm"
						id="new-book">글쓰기</a>
				</div>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp" />
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>
</body>
</html>
