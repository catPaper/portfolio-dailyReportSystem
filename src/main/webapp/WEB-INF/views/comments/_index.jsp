<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.AttributeConst"%>
<%@ page import="constants.ForwardConst"%>

<c:set var="actCmt" value="${ForwardConst.ACT_CMT.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commEdit" value="${ForwardConst.CMD_EDIT.getValue()}" />
<c:set var="commDel" value="${ForwardConst.CMD_DESTROY.getValue()}" />
<c:set var="commCrt" value="${ForwardConst.CMD_CREATE.getValue()}" />



<c:choose>
    <c:when test="${nodelete_comment_count == null || nodelete_comment_count == 0}">
        <h2>コメントはまだありません</h2>
    </c:when>
    <c:otherwise>
        <h2>コメント一覧</h2>

        <c:forEach var="comment" items="${comments}" varStatus="status">
            <c:choose>
                <c:when test="${comment.deleteFlag == 1}">
                    <div>[メッセージは削除されました]</div>
                </c:when>
                <c:otherwise>
                    <div class = "commentFrame">
                        <p>
                        <c:out value="${comment.employee.name}" />&nbsp;&nbsp;
                        <fmt:parseDate value="${comment.createdAt}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="createDay" type="date" />
                        <fmt:formatDate value="${createDay}" pattern="yyyy-MM-dd HH:mm:ss"/>&nbsp;&nbsp;
                        <c:if test="${comment.createdAt != comment.updatedAt}">（編集済み）</c:if>
                        </p>
                        <pre><c:out value="${comment.content}" /></pre>
                        <c:if test="${sessionScope.login_employee.id == comment.employee.id}">
                            <p>
                                <a href="<c:url
                                    value='?action=${actCmt}&command=${commEdit}&c_id=${comment.id}' />">
                                    コメントを編集する </a>&nbsp;&nbsp;
                                <a href="#"
                                    onclick="confirmDestroy(${comment.id});">
                                    コメントを削除する</a>
                             </p>

                            <form name="del${comment.id}" method="POST"
                                action="<c:url value='?action=${actCmt}&command=${commDel}' />">
                                <input type="hidden" name="${AttributeConst.CMT_ID.getValue()}"
                                    value="${comment.id}" /> <input type="hidden"
                                    name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />
                            </form>
                            <script>
                                function confirmDestroy(i) {
                                    if (confirm("コメントを削除しますか？")) {
                                        document.forms["del"+i].submit();
                                    }
                                }
                            </script>
                        </c:if>
                    </div>
                </c:otherwise>
            </c:choose>
            <br>
        </c:forEach>

        <div id="pagination">
            （全 ${comment_count}件）<br>
            <c:forEach var="i" begin="1"
                end="${((comment_count -1) / maxRow) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page }">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a
                            href="<c:url value='?action=${actCmt}&command=${commIdx}&page=${i}' />"><c:out
                                value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>
<br>
<br>
<h3>
    <b>新しくコメントをする</b>
</h3>
<br>
<%-- コメントの新規登録フォーム --%>
<form method="POST"
    action="<c:url value='?action=${actCmt}&command=${commCrt}' />">
    <c:import url="/WEB-INF/views/comments/_form.jsp" />
</form>
