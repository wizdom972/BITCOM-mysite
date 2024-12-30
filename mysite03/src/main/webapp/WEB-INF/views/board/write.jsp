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
                     Controller에서 model.addAttribute("mode", "write") or ("reply")
                     model.addAttribute("board", boardVo) (for reply)
                -->

                <!-- 글쓰기 / 답글 쓰기 분기 -->
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
                            <td>
                                <input type="text" name="title" value="" />
                            </td>
                        </tr>
                        <tr>
                            <td class="label">내용</td>
                            <td>
                                <textarea id="content" name="content"></textarea>
                            </td>
                        </tr>
                    </table>

                    <!-- 
                        "답글"일 경우에만 group_no, order_no, depth를 hidden으로 넘김 
                        Controller에서 model.addAttribute("board", replyVo)
                     -->
                    <c:if test="${mode == 'reply'}">
                        <input type="hidden" name="group_no" value="${board.group_no}" />
                        <input type="hidden" name="order_no" value="${board.order_no}" />
                        <input type="hidden" name="depth" value="${board.depth}" />
                    </c:if>

                    <!-- 작성자(author)는 따로 입력받지 않음 
                         => Controller에서 session user name을 setAuthor(...) 
                    -->

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
            </div><!-- #board -->
        </div><!-- #content -->
        <c:import url="/WEB-INF/views/includes/navigation.jsp" />
        <c:import url="/WEB-INF/views/includes/footer.jsp" />
    </div><!-- #container -->
</body>
</html>
