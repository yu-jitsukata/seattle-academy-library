<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<meta charset="UTF-8">
<title>書籍の詳細｜シアトルライブラリ｜シアトルコンサルティング株式会社</title>
<link href="<c:url value="/resources/css/reset.css" />" rel="stylesheet" type="text/css">
<link href="https://fonts.googleapis.com/css?family=Noto+Sans+JP" rel="stylesheet">
<link href="<c:url value="/resources/css/default.css" />" rel="stylesheet" type="text/css">
<link href="https://use.fontawesome.com/releases/v5.6.1/css/all.css" rel="stylesheet">
<link href="<c:url value="/resources/css/home.css" />" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="resources/css/lightbox.css">
<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script src="resources/js/lightbox.js" /></script>
<script src="resources/js/button.js" /></script>
</head>
<body class="wrapper">
    <header>
        <div class="left">
            <img class="mark" src="resources/img/logo.png" />
            <div class="logo">Seattle Library</div>
        </div>
        <div class="right">
            <ul>
                <li><a href="<%= request.getContextPath()%>/home" class="menu">Home</a></li>
                <li><a href="<%= request.getContextPath()%>/">ログアウト</a></li>
            </ul>
        </div>
    </header>
    <main>
        <h1>書籍の詳細</h1>
        <div class="content_body detail_book_content">
            <div class="content_left">
                <span>書籍の画像</span>
                <div class="book_thumnail">
                    <a href="${bookDetailsInfo.thumbnailUrl}" data-lightbox="image-1"> <c:if test="${bookDetailsInfo.thumbnailUrl == 'null'}">
                            <img class="book_noimg" src="resources/img/noImg.png">
                        </c:if> <c:if test="${bookDetailsInfo.thumbnailUrl != 'null'}">
                            <img class="book_noimg" src="${bookDetailsInfo.thumbnailUrl}">
                        </c:if> <input type="hidden" name="bookId" value="${bookDetailsInfo.bookId}">
                    </a>
                </div>
                <div>
                    <c:if test="${!empty lending}">
                        <div class="lending">${lending}</div>
                    </c:if>
                    <c:if test="${!empty Canlend}">
                        <div class="Canlend">${Canlend}</div>
                    </c:if>
                    <c:if test="${!empty returned}">
                        <div class="Canlend">${returned}</div>
                    </c:if>
                </div>
            </div>
            <div class="content_right">
                <div class="book_title">
                    <span>書籍名</span>
                    <p>${bookDetailsInfo.title}</p>
                </div>
                <div class="book_author">
                    <span>著者名</span>
                    <p>${bookDetailsInfo.author}</p>
                </div>
                <div class="book_publisher">
                    <span>出版社</span>
                    <p>${bookDetailsInfo.publisher}</p>
                </div>
                <div class="book_publish_date">
                    <span>出版日</span>
                    <p>${bookDetailsInfo.publishDate}</p>
                </div>
                <div class="book_title">
                    <span>ISBN</span>
                    <p>${bookDetailsInfo.isbn}</p>
                </div>
                <div class="book_title">
                    <span>説明文</span>
                    <p>${bookDetailsInfo.description}</p>
                </div>
            </div>
        </div>
        <div class="edtDelBookBtn_box">
            <form method="post" action="borrowBook">
                <button onclick="rent" type="submit" id="rent" value="${bookDetailsInfo.bookId}" name="bookId" class="btn_rentBook">借りる</button>
            </form>
            <form method="post" action="returnBook">
                <button onclick="return" type="submit" id="return" value="${bookDetailsInfo.bookId}" name="bookId" class="btn_returnBook">返す</button>
            </form>
            <form method="post" action="editBook">
                <button onclick="edit" type="submit" id="edit" value="${bookDetailsInfo.bookId}" name="bookId" class="btn_editBook">編集</button>
            </form>
            <form method="post" action="deleteBook">
                <button onclick="delete" type="submit" id="delete" value="${bookDetailsInfo.bookId}" name="bookId" class="btn_deleteBook" disabled="disabled">削除</button>
            </form>
           
        </div>
        <div>
        <c:if test="${!empty delete_error}">
                            <div class="error">${delete_error}</div>
                        </c:if>
        </div>
    </main>
</body>
</html>
