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

				<!-- Controller에서 mode="write" or "reply"로 구분해서 들어올 수도 있음 -->
				<c:choose>
					<c:when test="${mode == 'reply'}">
						<form class="board-form" method="post"
							action="${pageContext.request.contextPath}/board/reply">
					</c:when>
					<c:otherwise>
						<form class="board-form" method="post"
							action="${pageContext.request.contextPath}/board/write">
					</c:otherwise>
				</c:choose>

				<!-- 글쓰기 or 답글 쓰기 -->
				<table class="tbl-ex">
					<tr>
						<c:choose>
							<c:when test="${mode == 'reply'}">
								<th colspan="2">답글 쓰기</th>
							</c:when>
							<c:otherwise>
								<th colspan="2">글쓰기</th>
							</c:otherwise>
						</c:choose>
					</tr>
					<tr>
						<td class="label">제목</td>
						<td><input type="text" name="title" value="" /></td>
					</tr>
					<tr>
						<td class="label">내용</td>
						<td><textarea id="content" name="content"></textarea></td>
					</tr>
				</table>

				<!-- 작성자: 로그인된 사용자 이름을 hidden 필드로 전송 -->
				<input type="hidden" name="author"
					value="${sessionScope.authUser.name}" />

				<!-- [답글 모드] 일 경우 group_no, order_no, depth hidden 필드 -->
				<c:if test="${mode == 'reply'}">
					<input type="hidden" name="group_no" value="${board.group_no}" />
					<input type="hidden" name="order_no" value="${board.order_no}" />
					<input type="hidden" name="depth" value="${board.depth}" />
				</c:if>

				<div class="bottom">
					<a href="${pageContext.request.contextPath}/board">취소</a>

					<c:choose>
						<c:when test="${mode == 'reply'}">
							<input type="submit" value="답글 등록" />
						</c:when>
						<c:otherwise>
							<input type="submit" value="등록" />
						</c:otherwise>
					</c:choose>
				</div>
				</form>

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
