<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<style>
/* http://meyerweb.com/eric/tools/css/reset/ 
   v2.0 | 20110126
   License: none (public domain)
*/
html, body, div, span, applet, object, iframe, h1, h2, h3, h4, h5, h6, p,
	blockquote, pre, a, abbr, acronym, address, big, cite, code, del, dfn,
	em, img, ins, kbd, q, s, samp, small, strike, strong, sub, sup, tt, var,
	b, u, i, center, dl, dt, dd, ol, ul, li, fieldset, form, label, legend,
	table, caption, tbody, tfoot, thead, tr, th, td, article, aside, canvas,
	details, embed, figure, figcaption, footer, header, hgroup, menu, nav,
	output, ruby, section, summary, time, mark, audio, video {
	margin: 0;
	padding: 0;
	border: 0;
	font-size: 100%;
	font: inherit;
	vertical-align: baseline;
}
/* HTML5 display-role reset for older browsers */
article, aside, details, figcaption, figure, footer, header, hgroup,
	menu, nav, section {
	display: block;
}

body {
	line-height: 1;
}

ol, ul {
	list-style: none;
}

blockquote, q {
	quotes: none;
}

blockquote:before, blockquote:after, q:before, q:after {
	content: '';
	content: none;
}

table {
	border-collapse: collapse;
	border-spacing: 0;
}
/* ================================== */


#content {
	float: left;
}

#layer {
	width: 250px;
	float: left;
}

#map {
	width: 1585px;
	height: 898px;
	float: left;
	/* 	border: 1px solid red; */
}

#wrap {
	height: 100%;
	width: 100%;
	overflow: hidden;
}

.rptOpen {
	bottom: 0px;
	left: 800px;
}

</style>
		<!-- header// -->
		<div class="headerWrap">
			<a href="${ctx }/main.do"><img src="${ctx }/img/logo.png" alt="OpenNomics" style="margin-top: 25px;" /></a>
			<ul class="gnbNavi">
			<br/ >
			<li><strong><a href="javascript:loadApprovalMap()">승인된 지도</a></strong></li>
			<c:choose>	
				<c:when test="${user eq null}">
				 		<li><strong><a href="${ctx }/main.do">로그인</a></strong></li>
				</c:when>
			</c:choose>
			<c:choose>
			<c:when test="${user.auth eq 1}">
				 		<li><strong><a href="javascript:ListComfirmPopup()">목록</a></strong></li>
			</c:when>
			<c:when test="${user.auth eq 2}">
				 		<li><strong><a href="javascript:ListComfirmPopup()">목록</a></strong></li>
			</c:when>
			</c:choose>
			<c:choose>	
				<c:when test="${user ne null}">
				 		<li><strong><a href="javascript:logoutComfirmPopup()">로그아웃</a></strong></li>
				</c:when>
			</c:choose>
			</ul>
		</div>
	<div style="width: 100%; border: 1px solid #263C42;"></div>
<div id="mask">dimmed</div> <!-- masking -->







