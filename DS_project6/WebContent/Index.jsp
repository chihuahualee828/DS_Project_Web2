<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Index</title>
<style type="text/css">
body{
	font-family: "Arial", sans-serif
}
.dropzone{
	width:230px;
	height:200px;
	border:2px dashed #ccc;
	color:#ccc;
	line-height:100px;
	text-align:center;
	float: left;
	margin: 0% 29%;
	padding-top: 5em;
	padding-bottom: 5em;
	position: relative;
	vertical-align: middle;
	width: 40%;
	height: 35%;
}
.dropzone-padding{
	width:230px;
	height:100px;
	
	float: left;
	margin: 0% 29%;
	padding-top: 3em;
	
	position: relative;
	vertical-align: middle;
	width: 40%;
	height: 35%;
}

.dropzone.dragover{
	border-color:#000;
	color:#000;
}
.box-submit { 
	float: left;
	margin: 0% 30%;
	max-height: 25em;
	padding-top: 3em;
	position: relative;
	vertical-align: middle;
	width: 55%;
}


.title-box{ 
	float: left;
	margin: 0% 33%;
	max-height: 20em;
	padding-top: 12em;
	position: relative;
	vertical-align: middle;
	width: 55%;
}

.top-left{ 
	position: absolute;
  	top: 8px;
  	left: 8px;
}
.top-right {
  position: absolute;
  top: 8px;
  right: 8px;
}
.down-left{ 
	position: absolute;
  	bottom: 8px;
  	left: 8px;
}
.down-right{ 
	position: absolute;
  bottom: 8px;
  right: 8px;
}


</style>
</head>
	<body bgcolor="#00CCFF">
		<div class="title-box">
		<font size="20" face="微軟正黑體" color="rgb(120,120,120)">油圖搜索器
		</font>
		</div>
		<div class="box-submit">
		
		<form action="${requestUri}" method='get' >
				<input type= "file" name="file" />
				<input type="submit" value="search" onclick="javascript:theFile.submit();"/>
				<%
				//String urlString=request.getParameter("file");
				//System.out.print(urlString);
				//out.print(urlString);
				%>
		</form>
		</div>
		<div class="dropzone-padding" id="uploads"></div>
		<div class="dropzone" id="dropzone"> Drop files here</div>
		<script>
			
			(function(){
				var dropzone=document.getElementById('dropzone');
				var fileName='null';
				var entry='null';
				dropzone.ondrop=function(e){
					e.preventDefault();
					this.className='dropzone';
					fileName=e.dataTransfer.files[0].name;
					
					alert(fileName);
					
					
				};
				
				
				
				dropzone.ondragover=function(){
					this.className='dropzone dragover'
					return false;
				};
				
				dropzone.ondragleave=function(){
					this.className='dropzone';
					return false;
				};
				return dropzone.ondrop;
			}());
			
		
		</script>
		<div class="top-right">
		<img src="https://media1.tenor.com/images/1d63993e46f6715bcd9cc3d8c032bd4c/tenor.gif?itemid=15562709" width="130" height="130"/>
		</div>
		<div class="top-left">
		<img src="https://truth.bahamut.com.tw/s01/201612/becee5486be85600cfdddde31b6d3b7b.GIF" width="130" height="130"/>
		</div>
		<div class="down-right">
		<img src="https://i.imgur.com/cWHeWzj.gif" width="130" height="130"/>
		</div>
		<div class="down-left">
		<img src="https://media1.tenor.com/images/e65569919a3f3afb15b062ee16cedf98/tenor.gif?itemid=7499037" width="130" height="130"/>
		</div>
		
		
	
	</body>
</html>