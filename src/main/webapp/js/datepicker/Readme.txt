jQuery DatePicker 日期選擇(民國年)

還不錯用,只是有點小小問題不符合需求。

需求：顯示民國年(簡單又複雜的需求)

1. 年份的下拉式選單改成民國xx年
找到產生年份下拉式選單的程式碼然後修改, 透過FireBug來觀察下拉式選單的內容, 
發現二個事件
onclick對應jQuery.datepicker._clickMonthYear
onchange對應jQuery.datepicker._selectMonthYear

透過_clickMonthYear或_selectMonthYear關鍵字搜尋可以找到 _generateMonthYearHeader

_generateMonthYearHeader內容就是產生年月下拉式選單的程式碼

下拉式選單的option的value保持西元年, 只有顯示改成民國xx年
所以新增一個方法(formatYear), 透過方法進行轉換。

	var formatYear=function(v){return '民國'+(v-1911)+'年';};
	
修改程式碼：

	for (; year <= endYear; year++) {
		html += '<option value="' + year + '"' +
			(year == drawYear ? ' selected="selected"' : '') +
			'>' + formatYear(year) + '</option>';
	}
	
2. 選擇日期(20091215)後, 文字框內顯示(981215)
透過FireBug來觀察日期的內容, 可以發現
onclick對應到jQuery.datepicker._selectDay

最後會找到formatDate方法是關鍵點
_selectDay -> _selectDate -> _formatDate -> formatDate

formatDate方法有三個參數, 回傳是字串形態
	format：格式化設定。
	date：選擇的日期, 是Date的形態。
	settings：日期顯示的設定值, {shortYearCutoff, dayNamesShort, dayNames, monthNamesShort, monthNames}

所以改寫formatDate方法內容如下：
formatDate: function (format, date, settings) {
	var d = date.getDate();
	var m = date.getMonth()+1;
	var y = date.getFullYear();			
	var fm = function(v){			
		return (v<10 ? '0' : '')+v;
	};
	return (y-1911) +''+ fm(m) +''+ fm(d);
}

3. 發現bug, 在第二次選擇日期時, 不會自動跳到文字框內的日期, 還是顯示今天的年月。
因為拿到的版本是點選小圖示顯示日曆, 所以從img開始找
img -> _connectDatepicker -> _showDatepicker -> _setDateFromField -> parseDate

parseDate方法有三個參數, 回傳是Date形態
	format：格式化設定。
	value：目前的日期, 是String的形態。
	settings：日期顯示的設定值, {shortYearCutoff, dayNamesShort, dayNames, monthNamesShort, monthNames}
	
所以改寫parseDate方法內容如下：
parseDate: function (format, value, settings) {
    var v = new String(value);
    var Y,M,D;
    if(v.length==7){/*1001215*/
        Y = v.substring(0,3)-0+1911;
        M = v.substring(3,5)-0-1;
        D = v.substring(5,7)-0;
        return (new Date(Y,M,D));
    }else if(v.length==6){/*981215*/
        Y = v.substring(0,2)-0+1911;
        M = v.substring(2,4)-0-1;
        D = v.substring(4,6)-0;
        return (new Date(Y,M,D));
    }
    return (new Date());
};

4.透過jQuery的extend來修改原有的方法內容, 而不是直接修改主檔內容, 下次主檔更新時就不用再改一次了。
但是_generateMonthYearHeader方法內容還是要改, 因為主檔本身就沒有提供這樣子的方法。

(function($) {	
	$.extend($.datepicker, {
	    formatDate: function (format, date, settings) {
				var d = date.getDate();
				var m = date.getMonth()+1;
				var y = date.getFullYear();			
				var fm = function(v){			
				    return (v<10 ? '0' : '')+v;
				};			
				return (y-1911) +''+ fm(m) +''+ fm(d);
	    },
	    parseDate: function (format, value, settings) {
	        var v = new String(value);
	        var Y,M,D;
	        if(v.length==7){/*1001215*/
	            Y = v.substring(0,3)-0+1911;
	            M = v.substring(3,5)-0-1;
	            D = v.substring(5,7)-0;
	            return (new Date(Y,M,D));
	        }else if(v.length==6){/*981215*/
	            Y = v.substring(0,2)-0+1911;
	            M = v.substring(2,4)-0-1;
	            D = v.substring(4,6)-0;
	            return (new Date(Y,M,D));
	        }
	        return (new Date());
	    },
	    formatYear:function(v){
	    	return '民國'+(v-1911)+'年';
			}
	});	
	
})(jQuery);


_generateMonthYearHeader方法修改如下：
var formatYear = $.datepicker.formatYear ? $.datepicker.formatYear : function(v){return v;};

for (; year <= endYear; year++) {
	html += '<option value="' + year + '"' +
		(year == drawYear ? ' selected="selected"' : '') +
		'>' + formatYear(year) + '</option>';
}

直接殺進程式碼改東西, 還滿有趣的, 文件寫出來的東西不一定是自己要的
改完後, 再去看文件才發現是有提到我要的東西, 哈哈~