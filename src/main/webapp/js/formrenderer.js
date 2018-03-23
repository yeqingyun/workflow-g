
/**
 * form对应的string/date/long/enum/boolean类型表单组件生成器
 * fp_的意思是form paremeter
 */
var formFieldCreator = {
  'string': function(prop) {
	  var result = "<td width='90'>" + prop.name + "：</td>";
	  if (prop.writable === true) {
			result += "<td><input type='text' id='" + prop.id + "' name='fp_" + prop.id + "' class='easyui-validatebox' value='' data-options='required:"+ prop.required +"' style='width:200px;'/>";
	  } else {
			result += "<td><input type='text' id='" + prop.id + "' class='easyui-validatebox' value='" + prop.value + "' disabled='disabled' style='width:200px;'/>";
	  }
	  return result;
  },
  'date': function(prop) {
	  var result = "<td width='90'>" + prop.name + "：</td>";
	  if (prop.writable === true) {
			result += "<td><input type='text' id='" + prop.id + "' name='fp_" + prop.id + "' class='easyui-datebox' value='' data-options='required:"+ prop.required +"' style='width:205px;'/>";
	  } else {
			result += "<td><input type='text' id='" + prop.id + "' class='easyui-datebox' value='" + prop.value + "' disabled='disabled' style='width:205px;'/>";
	  }
	  return result;
  },
  'enum': function(prop) {
	  var result = "<td width='90'>" + prop.name + "：</td>";
	  if (prop.writable === true) {
		  result += "<td><select id='" + prop.id + "' name='fp_" + prop.id + "' class='easyui-combobox' data-options='width:205,required:"+ prop.required +"'>";
		  $.each(prop.enumValues, function(i, d) {
			  result += "<option value='" + d.id + "'>" + d.name + "</option>";
		  });
		  result += "</select>";
	  } else {
		  result += "<td><select id='" + prop.id + "' class='easyui-combobox' disabled='disabled' data-options='width:200'>";
		  $.each(prop.enumValues, function(i, d) {
			  if (d.id == prop.value) {
				  result += "<option value='" + d.id + "' selected='true'>" + d.name + "</option>";
			  } else {
				  result += "<option value='" + d.id + "'>" + d.name + "</option>";
			  }
		  });
		  result += "</select>";
	  }
	  return result;
  },
  'users': function(prop) {
	var result = "<td width='90'>" + prop.name + "：</td><td><select id='userTree' class='easyui-combotree' data-options=\" url:\'flowService/orgTree.json\',method:\'get\',required:true,onlyLeafCheck:true,lines:true,panelWidth:300,panelHeight:280,onClick:treeClick\" style='width:205px;' name='fp_" + prop.id + "'></select>";
	return result;
  },
  'text': function(prop) {
		var result = "<td width='90'>" + prop.name + "：</td><td><textarea  id='"+ prop.id +"' class='easyui-validatebox'  style='width:200px;height:100px;' name='fp_" + prop.id + "'></textarea>";
		return result;
  },
  'curUserNm': function(prop) {
	  var result = "<td width='90'>" + prop.name + "：</td>";
	  result += "<td><input type='text' id='" + prop.id + "' name='fp_" + prop.id + "' class='easyui-validatebox' value='" + prop.value + "' readOnly='true' style='width:200px;'/>";
	  return result;
  },
  'curUserId': function(prop) {
	  var result = "<td width='90'></td>";
	  result += "<td><input type='hidden' id='" + prop.id + "' name='fp_" + prop.id + "' value='" + prop.value + "' style='width:200px;'/>";
	  return result;
  },
  'comboTree': function(prop) {
		var result = "<td width='90'>" + prop.name + "：</td><td><input id='"+ prop.id+"' type='text' class='easyui-combotree' data-options=\" url:\'"+ prop.datePattern +"\',method:\'get\',required:true,onlyLeafCheck:true,lines:true,panelWidth:300,panelHeight:280\" style='width:205px;' name='fp_" + prop.id + "'></input>";
		return result;
  },
  'html': function(prop) {
		var result = "<td width='90'>" + prop.name + "：</td><td><textarea  id='"+ prop.id +"' class='easyui-validatebox'  style='width:200px;height:100px;' name='fp_" + prop.id + "'></textarea>";
		return result;
  }
};

function treeClick(node){
	if ($('#userTree').tree('isLeaf', node.target)){
		return true;
	} else {
		$("#userTree").combotree('clear');
		//提示
		$.messager.alert('提示信息','请选择组织结构人员!','warning');
	}
}

//生成单个Field
function createFieldHtml(prop, className) {
  return formFieldCreator[prop.type](prop, className);
}

//生成附件Div
function createAttachmentHtml(prop){
	var result = "<tr id=attchTr_"+ prop.id +">";
	result += "<td width='90'>附件：</td>";
	result += "<td>" + prop.description + "&nbsp;<a href='javascript:void(0)' onclick='delAttch("+ prop.id +");'>删除</a>";
	result += "&nbsp;<a href='javascript:void(0)' onclick='downAttch("+ prop.id +");'>下载</a>"
	result += "</tr>";
	return result;
}

var rowCount = 1;
//添加附件的Div
function addAttachmentDiv(btnTrId){
	 var newRow='<tr id="option'+rowCount+'"><td width="90">附件：</td><td><input type="file" name="myfiles" style="width:150px"><a href="javascript:void(0)" onclick=delRow('+rowCount+')>删除</a></td></tr>';  
	 $('#'+ btnTrId).before(newRow);
	 rowCount++;
}

//删除行  
function delRow(rowCount){  
    $("#option"+rowCount).remove();  
    rowCount--;  
}  

//提交表单
function submitForm(postUrl){
	$("#dynamic-form").attr("action",postUrl);
	$("#dynamic-form").submit();
}

//删除附件
function delAttch(attachmentId){
	 $.post("myflow/task/delAttachment.json",{attachmentId:attachmentId},
			function(data){
		 		if (data.success){
		 			$('#attchTr_' + attachmentId).remove();
		 		}
			 },
	 "json");
}
//下载附件
function downAttch(attachmentId){
	 var form = $("<form>");
     form.attr('style', 'display:none');
     form.attr('method', 'post');
     form.attr('action', 'downLoadServlet');

     var attachId = $('<input>');
     attachId.attr('type', 'hidden');
     attachId.attr('name', 'attachmentId');
     attachId.attr('value', attachmentId);
     
     $('body').append(form);
     form.append(attachId);
     
     form.submit();
     form.remove();
}
