<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<meta charset="UTF-8">
<title>一括登録｜シアトルライブラリ｜シアトルコンサルティング株式会社</title>
<link href="<c:url value="/resources/css/reset.css" />" rel="stylesheet" type="text/css">
<link href="https://fonts.googleapis.com/css?family=Noto+Sans+JP" rel="stylesheet">
<link href="<c:url value="/resources/css/default.css" />" rel="stylesheet" type="text/css">
<link href="https://use.fontawesome.com/releases/v5.6.1/css/all.css" rel="stylesheet">
<link href="<c:url value="/resources/css/home.css" />" rel="stylesheet" type="text/css">
<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script src="resources/js/thumbnail.js"></script>
<script src="resources/js/addBtn.js"></script>
</head>
<body class="wrapper">
    <header>
        <div class="left">
            <img class="mark" src="resources/img/logo.png" />       
            <div class="logo">Seattle Library</div>
        </div>
        <div class="right">
            <ul>
                <li><a href="<%=request.getContextPath()%>/home" class="menu">Home</a></li>
                <li><a href="<%=request.getContextPath()%>/">ログアウト</a></li>
            </ul>
        </div>
    </header>
    
    <main>
    <h1>書籍リクエスト</h1>
        <div class="request">
            <div class="requestlist">
                <h2>現在届いているリクエスト一覧！</h2>
                <c:if test="${!empty noRequest}">
                    <div class="lending">${noRequest}</div>
                </c:if>
                
                <c:forEach  var="requestbookInfo" items="${requestbookList}">
                    <div class="requestedBooks">
                        <ul>
                            <li class="requestedbooks">${requestbookInfo.requestTitle}</li>
                             <li class="requestedcounts">${requestbookInfo.counts}回のリクエスト</li>
                        </ul>
                    </div>
                </c:forEach>
                
                
            </div>
            <div class="request_announce">
                <p>読みたい本がなかった…という方</p>
                <P>これが読みたい！という本がある方</p>
            </div>
            <div class=request_form>
                <label for="request">読みたい本をリクエストしてみよう！</label>
                <div class="cp_iptxt">
                    <form class="ef" method="post" action="request">
                        <input type="text" class="iuput" name="requestTitle" id="requestTitle" autocomplete="off">
                        <button type="submit" class="btn_requestBook">リクエスト送信</button>
                    </form>
                </div>
                <c:if test="${!empty requestComplete}">
                    <div class="error">${requestComplete}</div>
                </c:if>
                <c:if test="${!empty requestError}">
                    <div class="error">${requestError}</div>
                </c:if>
            </div>
        </div>
    </main>
</body>
</html>
