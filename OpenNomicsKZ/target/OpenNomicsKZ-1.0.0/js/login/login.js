$(document).ready(function() {
	
	$('#rdiInspector').attr("checked", true);
	radioChange();
	radioChangeMain();
	$('#radioset1').click(function() {
		radioChange();
	});
	$('#radioset2').click(function() {
		radioChangeMain();
	});
	
	new FormDeco.radioSet("radioset1");
	
	$('input[name=radio]').change(function() {
		var chk = $(this).val();
		if( chk == "inspector" ) {
			$('#rdiInspector').attr("class","on");
		}
	});
	
	
});

function radioChange() {
	var mailTp = $('input[name="radio"]:checked').val();
	
	if( mailTp == "inspector" ) {
		$('#memberType').text("검수수행자");
		$('#id').attr("placeholder", "아이디");
		$('#pw').attr("placeholder", "비밀번호");
	} else{
		$('#memberType').text("부의장");
		$('#id').attr("placeholder", "아이디");
		$('#pw').attr("placeholder", "비밀번호");
	} 
}
function radioChangeMain() {
	var mailTp = $('input[name="radioMain"]:checked').val();
	
	if( mailTp == "inspector" ) {
		$('#id').attr("placeholder", "아이디");
		$('#pw').attr("placeholder", "비밀번호");
	}  else {
		$('#id').attr("placeholder", "아이디");
		$('#pw').attr("placeholder", "비밀번호");
	}
}
var loginType = "";
function login(type) {
	var id=$('#id').val();
	var pw=$('#pw').val();
	
	
	if( type == "L" ) {
		loginType = $('input[name=radio]:checked').val(); 
	} 
	
	
	var params = {
			id : id,
			pw : pw,
			loginType : loginType
		};
	
	
	if( loginType == null ) {
		alert("error");
		return false;
	}
	else{
		var url=CONTEXT+"/user/login.ajax";
		sendJsonRequest(url, params, loginCallback);
	}
}


function loginCallback(result) {
	var user = result.user;
	var isFlag = result.flag;
	
	//정상시
	if(isFlag=='itrue'){
		$('#iloginCompletePopup').xShowPopup();
	}
	else if(isFlag=='ctrue'){
		$('#cloginCompletePopup').xShowPopup();
	}
	else if(isFlag=='false'){
		alertPopup("INFORM","로그인정보가 일치하지 않습니다!");
	}
}





