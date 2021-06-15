<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:import url="../layout/app.jsp">
    <c:param name="content">

        <c:choose>      <!-- 条件分岐開始 -->

            <!-- タスク内容が空欄でない場合（ちゃんとデータがある時） -->
            <c:when test="${task != null}">

                <h2>id : ${task.id} のタスク詳細ページ</h2>

                <p>タスク内容：<c:out value="${task.content}" /></p>
                <p>作成日時：<fmt:formatDate value="${task.created_at}" pattern="yyyy-MM-dd HH:mm:ss" /></p>
                <p>更新日時：<fmt:formatDate value="${task.updated_at}" pattern="yyyy-MM-dd HH:mm:ss" /></p>

                <p><a href="${pageContext.request.contextPath}/index">一覧に戻る</a></p>
                <p><a href="${pageContext.request.contextPath}/edit?id=${task.id}">このタスクを編集する</a></p>

            </c:when>

            <!-- タスク内容が空欄の場合の表示 -->
            <c:otherwise>
                <h2>お探しのデータは見つかりませんでした。</h2>
            </c:otherwise>

        </c:choose>     <!-- 条件分岐終了 -->
    </c:param>
</c:import>