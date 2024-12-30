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
				<!-- 
				   1) action="/board" -> "/board/modify" 로 변경 
				   2) "a=modify" 파라미터는 사용하지 않으므로 제거 
				-->
				<form class="board-form" method="post"
					action="${pageContext.request.contextPath}/board/modify">
					
					<!-- 
					    Controller의 modify(BoardVo boardVo)와 매핑될 필드 
					    => @RequestParam("no") 자동 바인딩 or boardVo.setNo()
					-->
					<input type="hidden" name="no" value="${vo.no}">

					<table class="tbl-ex">
						<tr>
							<th colspan="2">글수정</th>
						</tr>
						<tr>
							<td class="label">제목</td>
							<td>
								<!-- vo.title과 매핑 -->
								<input type="text" name="title" value="${vo.title}">
							</td>
						</tr>
						<tr>
							<td class="label">내용</td>
							<td>
								<!-- vo.content와 매핑 -->
								<textarea id="content" name="content">${vo.content}</textarea>
							</td>
						</tr>
					</table>
					
					<div class="bottom">
						<!-- 취소하면 목록으로 돌아가기 -->
						<a href="${pageContext.request.contextPath}/board">취소</a>
						<!-- 폼 전송(수정 처리) -->
						<input type="submit" value="수정">
					</div>
				</form>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp" />
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>
</body>
</html>
