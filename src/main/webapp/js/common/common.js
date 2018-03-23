/**
 * 重写EasyUI日期控件个日期格式
 */
$.fn.datebox.defaults.formatter = function(date){
	var y = date.getFullYear();
	var m = date.getMonth()+1;
	var d = date.getDate();
	return y+'-'+(m<10?'0'+m:m)+'-'+(d<10?'0'+d:d);
}

//扩展easyui表单的验证
$.extend($.fn.validatebox.defaults.rules, {
    //验证数子
    number: {
    	validator: function (value, param) {
            if ((param != undefined) && (parseInt(value) < param[0] || parseInt(value) > param[1])) {
                $.fn.validatebox.defaults.rules.number.message = '输入的数值必须在' + param[0] + '至' + param[1] + '之间';
                return false;
            } else {
                if (!/^[0-9]*$/.test(value)) {
                    $.fn.validatebox.defaults.rules.number.message = '请输入数字.';
                    return false;
                } else {
                    return true;
                }
            }
        }, message: ''
    },
    mone:{
       //验证整数或小数
       validator: function (value, param) {
           	return (/^(([1-9]\d*)|\d)(\.\d{1,2})?$/).test(value);
       },
       message:'请输入整数或小数'
    },
    //移动手机号码验证
    mobile: {//value值为文本框中的值
        validator: function (value) {
            var reg = /^1[3|4|5|8|9]\d{9}$/;
            return reg.test(value);
        },
        message: '输入手机号码格式不准确.'
    },
    lessEq: {                
    	validator: function(value, param){
            var d1 = $.fn.datebox.defaults.parser(param[0]);
            var d2 = $.fn.datebox.defaults.parser(value);
            return d2<=d1;
        },
        message: '当前时间必须小于或等于 {0}！'
   },
   greaterEq: {                
   		validator: function(value, param){
           var d1 = $.fn.datebox.defaults.parser(param[0]);
           var d2 = $.fn.datebox.defaults.parser(value);
           return d2>=d1;
       },
       message: '当前时间必须大于或等于 {0}！'
  },
  percentage: {                
 		validator: function(value){
         return /\d%/.test(value);
     },
     message: '请输入百分比！'
  }
});

/**
 * 下载附件
 */
function downLoadTemplate(value){
	var form = $("<form>");
   	form.attr('style', 'display:none');
   	form.attr('method', 'post');
    form.attr('action', 'sysprocess/task/download.html');

    var key = $('<input>');
   	key.attr('type', 'hidden');
   	key.attr('name', 'key');
    key.attr('value', value);

   	$('body').append(form);
    form.append(key);
        
    form.submit();
    form.remove();
}
/**
 * textarea文字显示不全
 */
var autoTextarea = function (elem, extra, maxHeight) {
    extra = extra || 0;
    var isFirefox = !!document.getBoxObjectFor || 'mozInnerScreenX' in window,
    isOpera = !!window.opera && !!window.opera.toString().indexOf('Opera'),
            addEvent = function (type, callback) {
                    elem.addEventListener ?
                            elem.addEventListener(type, callback, false) :
                            elem.attachEvent('on' + type, callback);
            },
            getStyle = elem.currentStyle ? function (name) {
                    var val = elem.currentStyle[name];

                    if (name === 'height' && val.search(/px/i) !== 1) {
                            var rect = elem.getBoundingClientRect();
                            return rect.bottom - rect.top -
                                    parseFloat(getStyle('paddingTop')) -
                                    parseFloat(getStyle('paddingBottom')) + 'px';        
                    };

                    return val;
            } : function (name) {
                            return getComputedStyle(elem, null)[name];
            },
            minHeight = parseFloat(getStyle('height'))<0?0:parseFloat(getStyle('height'));

    elem.style.resize = 'none';

    var change = function () {
            var scrollTop, height,
                    padding = 0,
                    style = elem.style;

            if (elem._length === elem.value.length) return;
            elem._length = elem.value.length;

            if (!isFirefox && !isOpera) {
                    padding = parseInt(getStyle('paddingTop')) + parseInt(getStyle('paddingBottom'));
            };
            scrollTop = document.body.scrollTop || document.documentElement.scrollTop;
            style.height = minHeight + 'px';
            if (elem.scrollHeight > minHeight) {
                    if (maxHeight && elem.scrollHeight > maxHeight) {
                            height = maxHeight - padding;
                            style.overflowY = 'auto';
                    } else {
                            height = elem.scrollHeight - padding;
                            style.overflowY = 'hidden';
                    };
                    style.height = height + extra + 'px';
                    scrollTop += parseInt(style.height) - elem.currHeight;
                    document.body.scrollTop = scrollTop;
                    document.documentElement.scrollTop = scrollTop;
                    elem.currHeight = parseInt(style.height);
            };
    };

    addEvent('propertychange', change);
    addEvent('input', change);
    addEvent('focus', change);
    change();
};
