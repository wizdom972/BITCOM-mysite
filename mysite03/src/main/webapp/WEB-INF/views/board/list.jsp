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
	<c:set var="pageSize" value="${map.pageSize}" />
	<c:set var="currentPage" value="${map.currentPage}" />
	<c:set var="totalCount" value="${map.totalCount}" />
	<c:set var="totalPages" value="${map.totalPageCount}" />
	<c:set var="startPage" value="${map.startPage}" />
	<c:set var="endPage" value="${map.endPage}" />

	<c:set var="maxNumber" value="${pageSize * currentPage}" />
	<c:if test="${maxNumber >= totalCount}">
		<c:set var="maxNumber" value="${totalCount}" />
	</c:if>


	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp" />
		<div id="content">
			<div id="board">

				<!-- 검색 폼 -->
				<form id="search_form"
					action="${pageContext.request.contextPath}/board" method="get">
					<input type="text" id="keyword" name="kwd" value="${keyword}" /> <input
						type="submit" value="찾기" />
				</form>

				<!-- 게시글 목록 -->
				<table class="tbl-ex">
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>글쓴이</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>&nbsp;</th>
					</tr>

					<c:forEach var="board" items="${map.list}" varStatus="status">
						<tr>
							<!-- 게시글 번호: 가장 최신 글이 맨 위, 역순으로 매기기 -->
							<td>${totalCount - (currentPage - 1) * pageSize - status.index}</td>

							<!-- 제목(답글 depth만큼 들여쓰기) -->
							<td style="text-align:left; padding-left:${board.depth * 20}px">
								<!-- 답글 아이콘 --> <c:if test="${board.depth > 0}">
									<img
										src="${pageContext.request.contextPath}/assets/images/reply.png" />
								</c:if> <!-- 제목 클릭 -> 게시글 보기 --> <a
								href="${pageContext.request.contextPath}/board/view?no=${board.no}">
									${board.title} </a>
							</td>

							<td>${board.author}</td>
							<td>${board.hits}</td>
							<td>${board.reg_date}</td>
							<td>
								<!-- 삭제 링크 --> <a
								href="${pageContext.request.contextPath}/board/delete?no=${board.no}"
								class="del"> 삭제 </a>
							</td>
						</tr>
					</c:forEach>
				</table>

				<!-- 페이지네이션 -->
				<div class="pager">
					<ul>
						<!-- 이전 페이지 -->
						<c:if test="${currentPage > 1}">
							<li><a
								href="${pageContext.request.contextPath}/board?p=${currentPage - 1}&kwd=${keyword}">
									◀ </a></li>
						</c:if>

						<!-- 페이지 번호 반복 -->
						<c:forEach begin="${startPage}" end="${endPage}" var="i">
							<c:choose>
								<c:when test="${i == currentPage}">
									<li class="selected">${i}</li>
								</c:when>
								<c:otherwise>
									<li><a
										href="${pageContext.request.contextPath}/board?p=${i}&kwd=${keyword}">
											${i} </a></li>
								</c:otherwise>
							</c:choose>
						</c:forEach>

						<!-- 다음 페이지 -->
						<c:if test="${currentPage < totalPages}">
							<li><a
								href="${pageContext.request.contextPath}/board?p=${currentPage + 1}&kwd=${keyword}">
									▶ </a></li>
						</c:if>
					</ul>
				</div>
				<!-- 페이지네이션 끝 -->

				<!-- 글쓰기 버튼 -->
				<div class="bottom">
					<a href="${pageContext.request.contextPath}/board/write"
						id="new-book"> 글쓰기 </a>
				</div>

			</div>
			<!-- #board -->
		</div>
		<!-- #content -->
		<c:import url="/WEB-INF/views/includes/navigation.jsp" />
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>
	<!-- #container -->
</body>
</html>
