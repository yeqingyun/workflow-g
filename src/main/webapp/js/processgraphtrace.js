/**
 * form对应的string/date/long/enum/boolean类型表单组件生成器
 * fp_的意思是form paremeter
 */
var hisFormFieldCreator = {
  'string': function(prop) {
	  var result = "<td width='90'>" + prop.name + "：</td>";
	  result += "<td><input type='text' id='" + prop.id + "' class='easyui-validatebox' value='" + prop.value + "' disabled='disabled' style='width:200px;'/>";
	  return result;
  },
  'date': function(prop) {
	  var result = "<td width='90'>" + prop.name + "：</td>";
	  result += "<td><input type='text' id='" + prop.id + "' class='easyui-datebox' value='" + prop.value + "' disabled='disabled' style='width:205px;'/>";
	  return result;
  },
  'enum': function(prop) {
	  var result = "<td width='90'>" + prop.name + "：</td>";
	  result += "<td><select id='" + prop.id + "' class='easyui-combobox' disabled='disabled' data-options='width:205'>";
	  $.each(prop.enumValues, function(i, d) {
		  if (d.id == prop.value) {
			  result += "<option value='" + d.id + "' selected='true'>" + d.name + "</option>";
		  } else {
			  result += "<option value='" + d.id + "'>" + d.name + "</option>";
		  }
	  });
	  result += "</select>";
	  return result;
  },
  'users': function(prop) {
	  var result = null;
	  $.ajax({
		  url: 'flowService/showUserName.json',
		  type: 'POST',
		  async:false,
		  data:{account:prop.value},
		  dataType: 'json',
		  success: function(data){
			  	result = "<td width='90'>" + prop.name + "：</td><td><input id='"+ prop.id +"' class='easyui-validatebox' value='" + data.msg + "' style='width:200px;' disabled='disabled' name='fp_" + prop.id + "'></input>";
		  }
	  });
	  return result;
  },
  'text': function(prop) {
	  var result = "<td width='90'>" + prop.name + "：</td>";
	  result += "<td><textarea id='" + prop.id + "' class='easyui-validatebox' disabled='disabled' style='width:200px;hieght:100px'>"+ prop.value +"</textarea >";
	  return result;
  },
  'curUserNm': function(prop) {
	  var result = "<td width='90'>" + prop.name + "：</td>";
	  result += "<td><input type='text' id='" + prop.id + "' class='easyui-validatebox' value='" + prop.value + "' disabled='disabled' style='width:200px;'/>";
	  return result;
  },
  'curUserId': function(prop) {
	  var result = "<td width='90'></td>";
	  result += "<td><input type='hidden' id='" + prop.id + "' name='fp_" + prop.id + "' value='" + prop.value + "' style='width:200px;'/>";
	  return result;
  },
  'comboTree': function(prop) {
		var result = "<td width='90'>" + prop.name + "：</td><td><input id='"+ prop.id +"' type='text' disabled='disabled' value='"+ prop.value +"' class='easyui-combotree' data-options=\" url:\'"+ prop.datePattern +"\',method:\'get\',required:true,onlyLeafCheck:true,lines:true,panelWidth:300,panelHeight:280,onClick:treeClick\" style='width:205px;' name='fp_" + prop.id + "'></input>";
		return result;
  },
  'html': function(prop) {
		var result = "<td width='90'>" + prop.name + "：</td><td>"+ prop.value +"";
		return result;
  }
};


//生成单个Field
function createHisFieldHtml(prop, className) {
  return hisFormFieldCreator[prop.type](prop, className);
}

//生成附件Div
function createHisAttachmentHtml(prop){
	var result = "<tr id=attchTr_"+ prop.id +">";
	result += "<td width='90'>附件：</td>";
	result += "<td>" + prop.description + "&nbsp;<a href='javascript:void(0)' onclick='delAttch("+ prop.id +");'>删除</a>";
	result += "&nbsp;<a href='javascript:void(0)' onclick='downAttch("+ prop.id +");'>下载</a>"
	result += "</tr>";
	return result;
}