<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/common.jsp"%>
<script src="${ctx}/js/ol3/ol.js"></script>
<link rel="stylesheet" href="${ctx}/js/ol3/ol.css">
<script src="${ctx}/js/chairman/detailInfo.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Detail Information</title>
<style type="text/css">
.tbDetail{border-collapse:collapse; width:100%; text-align: center;}
.tbDetail thead th{padding: 10px 10px 10px 10px; background: #eee; border: 1px solid #ddd; font-size: 16px; font-family: 'NanumGothic';}
.tbDetail tbody td{padding: 10px 10px 10px 10px; border:1px solid #eee; font-size: 13px;}
.tbDetail .alignL {text-align: center;}
#bfLayer,#afLayer {
	width: 600px;
	height: 400px;
}
</style>
</head>
<body>
<div id="header"><jsp:include page="/WEB-INF/jsp/common/header.jsp" /></div>
<input type="hidden" id="rid" value="${rid}" />
<input type="hidden" id="tname" value="${tname}" />
<div class="defaultWrap">
	<div class="cntNaviWarp">
	<div class="cntSection">
    	<div class="cntWarp">
			<h3>부의장 > 승인요청정보</h3>
		</div>
		<table class="tbDetail" style="border: 1px solid #eee;">
				<thead>								
					<tr>
						<th scope="col" align="center" colspan="7">상세정보</th>
					</tr>
				</thead>
				<tbody id="mapBody">
					<tr>
						<th style="padding: 10px 10px 10px 10px;"scope="col" align="center" colspan="4">검수전</th>
						<th style="padding: 10px 10px 10px 10px;"scope="col" align="center" colspan="3">검수후</th>
					</tr>
					<tr>
						<td scope="col" colspan="4"><div id="bfLayer"></div></td>
						<td scope="col" colspan="3"><div id="afLayer"></div></td>
					</tr>
				</tbody>
				<thead>								
					<tr>
						<th scope="col" align="center" colspan="7">오류 리포트</th>
					</tr>
				</thead>
				<colgroup>
							<col width="10%" />
							<col width="12%" />
							<col width="13%" />
							<col width="13%" />
							<col width="15%" />
							<col width="8%" />
							<col width="25%" />
				</colgroup>
				<thead>
					<tr>
						<th>Type</th>
						<th>Number of items</br>in lot</th>
						<th>Number of</br>non-conforming items</th>
						<th>Ratio of</br>non- conforming</th>
						<th>Accuracy Proportion</br>(defined as 1-ratio)</th>
						<th>Weights</th>
						<th>Weighted value</br>(accuracy proportion * weight)</th>
					</tr>
				</thead>
				<tbody id="errTableBody">
				</tbody>
				<thead>								
					<tr>
						<th scope="col" align="center" colspan="7">신청정보</th>
					</tr>
				</thead>
				<tbody id="infoBody">
				</tbody>
			</table>
			<div align="center" style="margin-top: 6px;">
			<input type="button" class="bigButton" value="승인"style="width:100px;margin-right: 7px;"
				onclick="showChairmanApproval()" />
			<input type="button" class="bigButton" value="거절"style="width:100px;margin-right: 7px;"
				onclick="showChairmanDisApproval()" /> 
		</div>
	</div>
	</div>
</div>
<script type="text/javascript">
setSeparateMap("bfLayer","afLayer");
</script>
</body>
</html>