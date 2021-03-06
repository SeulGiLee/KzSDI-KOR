$(document).ready(function() {
	$('.layClose').click(function() {
		$('.layerPop').xHidePopup();
	});
});

function closePop() {
	$('.layerPop').xHidePopup();
}

function fixUrlPath(url) {
	if (url.indexOf(CONTEXT) < 0) {
		url = CONTEXT + url;
	}
	if (url.indexOf("?") >= 0) {
		url += "&t=" + (new Date()).getTime();
	} else {
		url += "?t=" + (new Date()).getTime();
	}
	return url;
}


function postToUrl(path, params, target) {
    var form = document.createElement("form");
//    form.setAttribute("method", "get");
    form.setAttribute("method", "post");
    form.setAttribute("action", path);
    for(var key in params) {
        var hiddenField = document.createElement("input");
        hiddenField.setAttribute("type", "hidden");
        hiddenField.setAttribute("name", key);
        hiddenField.setAttribute("value", params[key]);
        form.appendChild(hiddenField);
    }
    form.target = target;
    document.body.appendChild(form);
    form.submit();
}


function getToUrl(path, params, target) {
	var form = document.createElement("form");
	form.setAttribute("method", "get");
	form.setAttribute("action", path);
	for (var key in params) {
		var value = params[key];
		if (value instanceof Array) {
			for (var idx in value) {
				var hiddenField = document.createElement("input");
				hiddenField.setAttribute("type", "hidden");
				hiddenField.setAttribute("name", key);
				hiddenField.setAttribute("value", value[idx]);
				form.appendChild(hiddenField);
			}
		} else {
			var hiddenField = document.createElement("input");
			hiddenField.setAttribute("type", "hidden");
			hiddenField.setAttribute("name", key);
			hiddenField.setAttribute("value", value);
			form.appendChild(hiddenField);
		}
	}
	form.target = target;
	document.body.appendChild(form);
	form.submit();
}

function sendJsonRequest(url, params, doneCallback){
	var deferredObj = 
		$.ajax({
		url : fixUrlPath(url),
//		type : "POST",
		type : "GET",
		contentType : "application/json; charset=UTF-8",
		dataType : "json",
		cache : false,
		data : params,
		traditional: true
	});
	deferredObj.done(function(data, textStatus, jqXHR) {
		processDone(data, textStatus, jqXHR, doneCallback);
	});
	deferredObj.fail(function(jqXHR, textStatus, errorThrown) {
		processFail(jqXHR, textStatus, errorThrown);
	});
	return deferredObj;
}

function sendZipcodeRequest(url, params, doneCallback) {
	var deferredObj = 
		$.ajax({
		url : url,
//		type : "POST",
		type : "GET",
		dataType : "json",
		cache : false,
		data : params,
		traditional: true
		
	});
	deferredObj.done(function(data, textStatus, jqXHR) {
		processDone(data, textStatus, jqXHR, doneCallback);
	});
	deferredObj.fail(function(jqXHR, textStatus, errorThrown) {
		processFail(jqXHR, textStatus, errorThrown);
	});
	return deferredObj;
}

function sendObjectRequest(url, params, doneCallback) {
	var deferredObj = 
		$.ajax({
		url : fixUrlPath(url),
		type : "POST",
//		type : "GET",
		dataType : "json",
		contentType : "application/json; charset=UTF-8",
		cache : false,
		data : JSON.stringify(params),
		traditional: true
	});
	deferredObj.done(function(data, textStatus, jqXHR) {
		processDone(data, textStatus, jqXHR, doneCallback);
	});
	deferredObj.fail(function(jqXHR, textStatus, errorThrown) {
		processFail(jqXHR, textStatus, errorThrown);
	});
	return deferredObj;
}



function processDone(data, textStatus, jqXHR, doneCallback) {
	if (typeof(data) !== 'undefined' && typeof(data.errorCode) !== 'undefined') {
		alertPopup("Inform",data.errorDesc);
	} else if (typeof(doneCallback) !== 'undefined') {
		doneCallback(data);
	}
}

function processFail(jqXHR, textStatus, errorThrown) {
	if (typeof (console) !== 'undefined' && typeof (console.log) !== 'undefined') {
		console.log(textStatus + " - " + jqXHR.status + " (" + errorThrown + ")");
	}
	if (jqXHR.status == 500) {
		alert("내부 시스템 에러.");
	} else if (jqXHR.status == 404) {
		alert("경로가 잘못 되었습니다.");
	} else if (jqXHR.status == 408) {
		alert("잠시 후 다시 시도해 주세요.");
	}
	if (jqXHR.getResponseHeader("SESSION_EXPIRED") != null) {
		alert("세션이 만료되어 메인페이지로 이동합니다.");
		window.location.href = "/main.do";
	}
}

// 천단위 구분하기
function commify(n) {
	var reg = /(^[+-]?\d+)(\d{3})/; // 정규식
	n += ''; // 숫자를 문자열로 변환

	while (reg.test(n))
		n = n.replace(reg, '$1' + ',' + '$2');

	return n;
}



/*
 * 예)
 * 
var validCase1 = {
		id          : {type : "text", name : "아이디를", required : true},
		pw          : {type : "text", name : "비밀번호를", required : true},
		rePw        : {type : "text", name : "비밀번호 확인을", required : true}
}
*/

//공백체크
function validation(validCase) {
	var errorCnt = 0;
	$.each(validCase, function(key, val) {
		if(val.required) {
			
			var str = "";
			
			if(val.type == "text") {
				str = $.trim( $('#'+key).val() );
			} else if( val.type == "radio"){
				str = $('input[name='+key+']:checked').val();
			}
			if(!str) {
				if(val.type == "text") {
					alertPopup("INFORM","Please enter your "+val.name);
				} else if( val.type == "radio"){
					alertPopup("INFORM","Please choose your "+val.name);
				}
				errorCnt++;
				return false;
			}
		}
	});
	
	if (errorCnt == 0) {
		return true;
	} else {
		return false;
	}
}


/**
 * layer popup open 처리
 */
$.fn.xShowPopup = function() {
	$(this).each(function() {
		$("#mask").show();
		var tempm = $(this).height()/2 ;
		$(this).css('margin-top', '-'+tempm+'px');
		$(this).show();
	});
};
/**
 * layer popup close 처리
 */
$.fn.xHidePopup = function(e) {
	if (typeof(e) !== "undefined"){
		e.preventDefault();
	}
	$(this).each(function() {
		$("#mask").hide();
		$(this).hide();
	});
};
function NoDoubleClick(func){
	var _self = this;
	_self._callfunc = func;
	_self.called = false;
	
	_self.runFunction = function(e){
		if(_self.called){
			if (typeof(e) !== "undefined"){
				e.preventDefault();
			}
			return;
		}
		_self.called = true;
		_self._callfunc(e);
		setTimeout(function(){_self.called = false;}, 500);
	};
	
};

$.fn.noDupClick = function(func){
	$.each($(this), function(index, o){
		var _noDuble = new NoDoubleClick(func);
		$(o).click(_noDuble.runFunction);
	});
};

function showMsgPopup() {
	$('#msgPopup').xShowPopup();
}
function showAfterMsgPopup() {
	closePop();
	$('#msgPopup').xShowPopup();
}

function closeMsgPopup() {
	$('#msgPopup').xHidePopup();
}

//공동사용 알럿
function alertPopup(main, sub) {
	$('#warningPopup').xShowPopup();
	$('#warningMain').text(main);
	$('#warningSub').html(sub);
}

/**
 * showProgress 처리
 */
$.fn.showProgress = function() {
	$(this).each(function() {
		$("#mask").show();
		$(this).show();
	});
};

/**
 *  hideProgress 처리
 */
$.fn.hideProgress = function(e) {
	if (typeof(e) !== "undefined"){
		e.preventDefault();
	}
	$(this).each(function() {
		$("#mask").hide();
		$(this).hide();
	});
};

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

