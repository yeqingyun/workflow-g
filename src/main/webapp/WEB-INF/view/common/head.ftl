<!doctype html>
<html>
  <head>
	<base href="${base}"></base>
	<meta charset="utf-8"/>
	<title>${title!"开发指南"}</title>
	<link rel="shortcut icon" href="http://shop.gionee.com/favicon.ico" type="image/x-icon">
	<link rel="stylesheet" type="text/css" href="css/themes/default/easyui.css"/>
	<link rel="stylesheet" type="text/css" href="css/themes/icon.css"/>
	<link rel="stylesheet" type="text/css" href="css/themes/myicon.css"/>
	<link rel="stylesheet" type="text/css" href="css/themes/default/my97.css">
	<link rel="stylesheet" type="text/css" href="${base}js/tagsinput/jquery.tagsinput.css"/>
	
	<script type="text/javascript" src="js/jquery.min.js"></script>
	<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="js/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="js/formrenderer.js"></script>
	<script type="text/javascript" src="js/processgraphtrace.js"></script>
	
	<script type="text/javascript" src="js/my97/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="js/my97/jquery.my97.js"></script>
	
	<script type="text/javascript" src="${base}js/tagsinput/jquery.tagsinput.min.js"></script>
	
	<script type="text/javascript" src="${base}js/json/json2.js"></script>
	
	<!--file upload-->
	<link rel="stylesheet" type="text/css" href="css/fileupload/webuploader.css"/>
	<link rel="stylesheet" type="text/css" href="css/fileupload/fileupload.css"/>
	<#--<script src="js/fileupload/crypto-js.js"></script>-->
	<#--<script src="js/fileupload/enc-utf8.js"></script>-->
	<#--<script src="js/fileupload/enc-base64.js"></script>-->
	<#--<script type="text/javascript" src="js/fileupload/jquery.jsonp.js"></script>-->
	<script type="text/javascript" src="js/fileupload/webuploader.js"></script>
	<script type="text/javascript" src="js/fileupload/fileupload.js?rand=${rand}"></script>
	<script type="text/javascript" src="js/common/common.js"></script>
  </head>
  <body>
<style type="text/css">
	.formtable {
		font-size: 12px;
		background-color: #C0C0C0;
		width: 100%;
		border-collapse: collapse;
		border:1px solid #c0c0c0;
	}
	
	.formtable_td_key {
		height: 20px;
		padding: 7px;
		max-width: 120px;
		font-weight: bold;
		color: #1D1D1D;
		font-size: 12x;
		background-color: #F1F6FF;
		width: 12%;
		border-collapse: collapse;
		border:1px solid #c0c0c0;
	}
	
	.formtable_td_value {
		background-color: #FAFCFF;
		font-size: 12x;
		padding: 7px;
		border-collapse: collapse;
		border:1px solid #c0c0c0;
	}
	
	.formtable_remark {
		width: 95% !important;
		height: 60px;
		font-size: 12x;
		border: 1px solid #C0C0C0;
	}
	
	.span_wordwrap {
		white-space: pre-wrap;
		word-wrap: break-word;
		word-break: break-all;
	}
	
	input.hidden-input {
		padding: 0px;
		margin: 0px;
		width: 0px;
		height: 0px;
		border-width: 0px;
		display: block;
		float: left;
	}
	
	input.none-input {
		background-color: #FAFCFF;
		border: none;
	}
	
	textarea {
		color: black;
		width: 95% !important;
	}
	textarea.none-input {
		background-color: #FAFCFF;
		border: none;
		font-family: inherit
	}
	
	textarea.likedisabled {
		background-color: #F0F0F0;
		border: 1px solid #c0c0c0;
		font-family: inherit;
		font-size: 12px;
		width: 95% !important;
		max-width: 95%;
		resize: none;
		overflow-y: hidden;
		min-height: 60px;
	}
	
	textarea[disabled=disabled] {
		background-color: #F0F0F0;
		border: 1px solid #c0c0c0;
		font-family: inherit;
		font-size: 12px;
		width: 95% !important;
		max-width: 95%;
		resize: none;
		overflow-y: hidden;
		min-height: 60px;
	}
	input[type='checkbox']{
		-moz-appearance: none;
		width: 13px;
		height: 13px;
		vertical-align: middle;
		-webkit-appearance: none;
		background-image: url('${base}image/checkbox_unchecked.png');
		background-position: center;
	    background-repeat: no-repeat;
	    background-size: 100%;
      	border-radius:2px; 
	}
	input[type="checkbox"]:checked {
	  background-image: url('${base}image/checkbox_checked.png');
	}
	input[type='checkbox'][disabled]{
	  background-color: #F0F0F0
	}
	input[type='radio']{
		-moz-appearance: none;
		-webkit-appearance: none;
		width: 16px;
		height: 16px;
		vertical-align: middle;
		background-image: url('${base}image/radio_unchecked.png');
		background-position: center;
	    background-repeat: no-repeat;
	    background-size: 100%;
	    border-radius:8px; 
	}
	input[type="radio"]:checked {
	  background-image: url('${base}image/radio_checked.png');
	}
	input[type='radio'][disabled]{
	  background-color: #F0F0F0
	}
</style>
