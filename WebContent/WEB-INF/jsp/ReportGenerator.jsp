<%@ page language="java" contentType="text/html; charset=UTF-8"
 pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	<script src="http://code.jquery.com/jquery-latest.min.js"></script>
	<script src="script.js"></script>
	<link rel="stylesheet" href="WebContent/WEB-INF/jsp/style.css">
</head>
<body>
<script>
income=[];
date=[];
cinemas=[];
films=[];
session=[];
attendance=[];
fields=[{
	value:"cinema"
},
{
	value:"film"
},
{
	value:"date"
},
{
	value:"time"
},
{
	value:"attendance"
},
{
	value:"income"
}
];
$(document).ready(function(){
//============CINEMAS===============
$("#add_cinema").click(function (){
	cinemas.push({
	value:$("#cinema_name").val()
	});
	$("#cinema_list").html(print(cinemas));
});
//============FILMS==================
$("#add_film").click(function (){
	films.push({
		value:$("#film_name").val()
	});
	$("#film_list").html(print(films));
});
//============INCOME==================
$("#add_income").click(function () {
if ($("#r1").is(":checked")) {
		income.push({
	value:$("#income_value").val()
		});
	} else{
		income.push({
	from:$("#min_income").val(),
	to:$("#max_income").val()
});
	}
$("#income_list").html(print(income));
});
//============DATE==================
$("#add_date").click(function () {
if ($("#radio_data_single").is(":checked")) {
		date.push({
	value:$("#date_value").val()
		});
	} else{
		date.push({
	from:$("#min_date").val(),
	to:$("#max_date").val()
});
	}
$("#date_list").html(print(date));
});
//==============SESSION===================
	$("#add_session").click(function () {
if ($("#single_session").is(":checked")) {
		session.push({
	value:$("#session_value").val()
		});
	} else{
		session.push({
	from:$("#min_session").val(),
	to:$("#max_session").val()
});
	}
$("#session_list").html(print(session));
});
//==============Attendance===================
	$("#add_attendance").click(function () {
if ($("#single_attendance").is(":checked")) {
		attendance.push({
	value:$("#attendance_value").val()
		});
	} else{
		attendance.push({
	from:$("#min_attendance").val(),
	to:$("#max_attendance").val()
});
	}
$("#attendance_list").html(print(attendance));
});
//============GENERATE====================
$("#generate").click(function(){
$.ajax({
		type:"GET",
		url: "generate", 
		data: "parameters="+printJSON(),
		success: function(result){
			$("#result").html("<a href=\""+result+"\">Download</a>");
		}});
	});
		});
	

function printJSON () {
var map={
income: income,
date: date,
attendance: attendance,
time: session,
film_id: films,
cinema_id: cinemas,
fields: fields
}
return JSON.stringify(map);
}

function print (list) {
var result;
result="<ul>";
for (var i = list.length - 1; i >= 0; i--) {
	var value=list[i].value;
	var from=list[i].from;
	var to=list[i].to;
	if (value==null) {
		result+="<li>from  "+from+"   to "+to+"</li>";
	} else{
		result+="<li>"+value+"</li>";	
	};
	
};
result+="</ul>";
return result;
}
</script>
<style>
		body{
			background-color: #f5f5f5;
		}

		p{
			color: red;
			font-size: 14pt;
		}

		form{
			width: 700px;
			margin: auto;
		}

		#parameter_list{
			float: right;
		}
		div{
			margin: 20px;
		}

		#result{
			margin:20px;
		}

		input[type=text],input[type=date]{
			border:1px solid blue;
			height: 130%;
			border-radius: 4px;
			background-color: #5CCCCC;
		}
			input[type=text]:focus,input[type=date]:focus{
			background-color: #fff;
		}

		input[type=button]{
			width:70px;
			border-radius: 14px;
		}

		input[type=button]:hover{
			cursor: pointer;
		}
		.line{
			margin: 7px;
		}
		.line-indent{
			margin:7px 40px;	
		}

</style>
<form action="" method="post">
	<fieldset>
	<legend>include following fields in report</legend>
		<!-- ================FIELDS SELECTOR====================== -->
		<div id="fields">
			<div class="line"><input type="checkbox" name="fields" id="film_field" checked="true"><label for="film_field">fields</label></div>
			<div class="line"><input type="checkbox" name="fields" id="cinema_field" checked="true"><label for="cinema_field">fields</label></div>
			<div class="line"><input type="checkbox" name="fields" id="date_field" checked="true"><label for="date_field">fields</label></div>
			<div class="line"><input type="checkbox" name="fields" id="time_field" checked="true"><label for="time_field">fields</label></div>
			<div class="line"><input type="checkbox" name="fields" id="attendance_field" checked="true"><label for="attendance_field">attendance</label></div>
			<div class="line"><input type="checkbox" name="fields" id="income_field" checked="true"><label for="income_field">income</label></div>
			<input type="button" value="add" name="add" id="add_fields" class="add">
		</div>
		</fieldset>
		<!-- ================CINEMAS SELECTOR====================== -->
		<fieldset>
		<legend>setting cinemas list</legend>
		<div id="cinema">
			<input type="text" id="cinema_name">
			<input type="button" value="add" name="add" id="add_cinema" class="add">
		<div id="cinema_list"></div>
		</div>
		</fieldset>
		<!-- ================FILMS SELECTOR====================== -->
		<fieldset>
		<legend>setting films list</legend>
		<div id="films">
			<input type="text" id="film_name">
			<input type="button" value="add" name="add" id="add_film" class="add">
			<div id="film_list"></div>
		</div>
		</fieldset>
		<!-- ================INCOME PARAMETERS====================== -->
		<fieldset>
		<legend>setting income constraints</legend>
		<div id="income">
			<div class="line">
				<input type="radio" name="income_group" value="1" id="r1" checked="true">
				<label for="r1">set single income value</label>	
			</div>
			<div class="line-indent">
				<label for="min_income">income value: </label>
				<input type="text" id="income_value">
			</div>
			<div class="line">
				<input type="radio" name="income_group" value="2" id="r2">
				<label for="r2">set income range</label>
			</div>
			<div class="line-indent">
				<label for="min_income">from: </label><input type="text" name="min_income" id="min_income">
				<label for="max_income">to: </label><input type="text" name="max_income" id="max_income">
			</div>
			<input type="button" value="add" name="add" id="add_income" class="add">
		<div id="income_list"></div>
		</div>
		</fieldset>
		<!-- ================ATTENDANCE PARAMETERS=================== -->
		<fieldset>
		<legend>setting attendance constraints</legend>
		<div id="attendance">
			<div class="line">
				<input type="radio" name="attendance" value="1" id="single_attendance" checked="true">
			<label for="single_attendance">set single attendance value</label>
			</div>
			<div class="line-indent">
				<label for="min_attendance">attendance value </label>
				<input type="text" id="attendance_value">
			</div>
			<div class="line">
				<input type="radio" name="attendance" value="2" id="multiple_attendance">
				<label for="multiple_attendance">set attendance range: </label>	
			</div>
			<div class="line-indent">
				<label for="min_attendance">from: </label><input type="text" name="min_attendance" id="min_attendance">
				<label for="max_attendance">to: </label><input type="text" name="max_attendance" id="max_attendance">
			</div>
			<input type="button" class="line" value="add" name="add" id="add_attendance" class="add">
		<div id="attendance_list"></div>
		</div>
		</fieldset>
		<!-- ====================DATE PARAMETERS===================== -->
		<fieldset>
		<legend>setting date constraints</legend>
		<div id="date">
			<div class="line">
				<input type="radio" id="radio_data_single" name="radio" checked="true">
				<label for="radio_data_single">single date value</label><br>
			</div>
			<div class="line-indent">
				<label for="">date value </label>
				<input type="date" id="date_value">
			</div>
			<div class="line">
				<input type="radio" id="radio_data_multiple" name="radio">
				<label for="radio_data_multiple">date range: </label>
			</div>
			<div class="line-indent">
				<label for="min_date">from: </label><input type="date" class="date" id="min_date" name="min_date">
				<label for="max_date">to: </label><input type="date" class="date" id="max_date" name="max_date">
			</div>
			<div class="line"><input type="button" value="add" name="add" id="add_date" class="add"></div>
		<div id="date_list"></div>
		</div>
		</fieldset>
		<!-- ===================SESSION PARAMETERS=================== -->
		<fieldset>
		<legend>setting session time constraints</legend>
		<div id="session">
			<div class="line">
				<input type="radio" name="session" value="1" id="single_session" checked="true">
				<label for="single_session">set single session value</label>
			</div>
			<div class="line-indent">
				<label for="min_session">session value </label>
				<input type="text" id="session_value">
			</div>
			<div class="line">
				<input type="radio" name="session" value="2" id="multiple_session">
				<label for="multiple_session">set session range: </label>			
			</div>
			<div class="line-indent">
				<label for="min_session">from: </label><input type="text" name="min_session" id="min_session">
				<label for="max_session">to: </label><input type="text" name="max_session" id="max_session">
			</div>
			<div class="line">
				<input type="button" value="add" name="add" id="add_session" class="add">
			</div>
		<div id="session_list"></div>
		</div>
		</fieldset>
		<input type="button" value="Generate" id="generate">
	</form>
	<div id="result"></div>
</body>
</html>