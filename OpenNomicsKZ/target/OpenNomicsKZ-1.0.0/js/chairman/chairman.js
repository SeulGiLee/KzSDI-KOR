$(document).ready(function() {
	selectChairmanList();
});



function selectChairmanList(no){
		var pageNum = 1;
		if(no!=null){
			pageNum=no;
		}
		
		var url=CONTEXT+"/receipt/selectChairmanList.ajax";
		var params ={
			pageNum : pageNum	
		};
		sendJsonRequest(url, params, chaListCallback);
}

var chaList = null;
function chaListCallback(result) {
		chaList = result.list;
		var pageCounts = result.count;
		closePop();
		$('#tableBody > tr').remove();
		$('#pageCountView > a').remove();
		for ( var obj in chaList) {
			$('#tableBody').append(
					"<tr>" +
					"<td>" + chaList[obj].rid + "</td>" +
					"<td>" + chaList[obj].uid + "</td>" +
					"<td>" + chaList[obj].utype + "</td>" +
					"<td>" + chaList[obj].jname + "</td>" +
					"<td>" + chaList[obj].jcontent + "</td>"+ 
					"<td>" + foo(chaList[obj].cdate) + "</td>"+ 
					"<td>" + "<input type='button' class='bigButton' name='btnQA"+obj+"' id ='btnQA"+obj+"'value='확인' onclick='detailInfo("+""+obj+""+")';>" + "</td>"+				
					"</tr>");
			
		}
		for (var i = 1; i <= pageCounts; i++) {
			$('#pageCountView').append(
					"<a href='javascript:selectChairmanList(" + i + ")'>" + "<font size='3px' style='padding-right:10px'>" + i + "</font>" + "</a>");
		}
}

function foo(timestamp){
	var date = new Date(timestamp);
	var year = date.getFullYear();
	var month = date.getMonth()+1;
	var day = date.getDay();
	var hour = date.getHours();
	var min = date.getMinutes();
	var sec = date.getSeconds();


	var retVal =   year + "-" + (month < 10 ? "0" + month : month) + "-" 
	                        + (day < 10 ? "0" + day : day) + " " 
	                        + (hour < 10 ? "0" + hour : hour) + ":"
	                        + (min < 10 ? "0" + min : min) + ":" 
	                        + (sec < 10 ? "0" + sec : sec);

	return retVal;
}

function detailInfo(obj){
	var params = {
			rid : chaList[obj].rid,
			tname : chaList[obj].tname
		};
		getToUrl(CONTEXT + "/chairman/detailInfoView.do", params, "_self");
}










