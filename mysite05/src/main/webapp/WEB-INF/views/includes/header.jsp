<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>
<%@ taglib uri="jakarta.tags.functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
window.addEventListener("load", function() {
	anchors = document.querySelectorAll('#languages a');
	anchors.forEach(function(el) {
		el.addEventListener("click", function(event) {
			event.preventDefault();
			document.cookie =
				"lang=" + this.getAttribute('data-lang') + ";" +
				"path=" + "${pageContext.request.contextPath }" + ";" +
				"max-age=" + (30*24*60*60);
			
			location.reload();
		})	
	});
});
</script>
		<div id="header">
			<h1>${site.title }</h1>
			<div id="languages">
				<c:choose>
					<c:when test="${lang == 'en' }">
						<a href="" data-lang="ko">KO</a><a href="" class="active" data-lang="en">EN</a>
					</c:when>
					<c:otherwise>
						<a href="" data-lang="ko" class="active">KO</a><a href="" data-lang="en">EN</a>
					</c:otherwise>
				</c:choose>
			</div>			
			<ul>				
				<sec:authorize access="!isAuthenticated()">
    				<li><a href="${pageContext.request.contextPath}/user/login">로그인</a><li>
   					<li><a href="${pageContext.request.contextPath}/user/join">회원가입</a><li>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
    				<sec:authentication property="principal" var="authUser"/>
    				<li><a href="${pageContext.request.contextPath}/user/update">회원정보수정</a><li>
    				<li><a href="${pageContext.request.contextPath}/user/logout">로그아웃</a><li>
    				<li><spring:message code="header.gnb.greeting"/> ${authUser.name }</li>
				</sec:authorize>
			</ul>
		</div>