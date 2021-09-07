<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="constants.AttributeConst"%>
<%@ page import="constants.ForwardConst"%>

<c:set var="actCmt" value="${ForwardCoonst.ACT_CMT.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commEdit" value="${ForwardConst.CMD_EDIT.getValue()}" />
<c:set var="commDel" value="${ForwardConst.CMD_DESTROY.getValue()}" />

<c:if test="${flush != null}">
    <div id="flush_success">
        <c:out value="${flush}" />
    </div>
</c:if>

<c:choose>
    <c:when test="${comments_count == null}">
        <h2>コメントはまだありません</h2>
    </c:when>
    <c:otherwise>
        <table>
            <c:forEach var="comment" items="${comments}" varStatus="status">
                <tr>
                    <c:choose>
                        <c:when test="${comment.deleteFlag == true}">
                            <td>[メッセージは削除されました]</td>
                        </c:when>
                        <c:otherwise>
                            <td><c:out value="${comment.employee.name}" /></td>
                            <td><c:out value="${comment.content}" /></td>
                            <c:if
                                test="${sessionScope.login_employee.id == comment.employee.id}">
                                <th><a
                                    href="<c:url
                                            value='?action=${actCmt}&commmand=${commEdit}&c_id=${comment.id}' />">コメントを編集する
                                </a></th>
                                <th><a href="#" onclick="confirmDestroy();">コメントを削除する</a>
                                    <form method="POST"
                                        action="<c:url value='?action=${actCmt}&command=${commDel}' />">
                                        <input type="hidden"
                                            name="${AttributeConst.CMT_ID.getValue()}"
                                            value="${comment.id}" /> <input type="hidden"
                                            name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />
                                    </form> <script>
                                        function confirmDestroy() {
                                            if (confirm("コメントを削除しますか？")) {
                                                document.forms[1].submit();
                                            }
                                        }
                                    </script></th>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
                </tr>
            </c:forEach>
        </table>

        <div id="pagination">
            （全 ${comments_count}件）<br>
            <c:forEach var="i" begin="1"
                end="${((comments_count -1) / maxRow) + 1}" step="1">
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
    action="<c:url value='?action=${actCmt}&command=${commNew}' />">
    <c:import url="/WEB-INF/views/comments/_form.jsp" />
</form>
