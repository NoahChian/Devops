jQuery DatePicker ������(����~)

�٤�����,�u�O���I�p�p���D���ŦX�ݨD�C

�ݨD�G��ܥ���~(²��S�������ݨD)

1. �~�����U�Ԧ����令����xx�~
��첣�ͦ~���U�Ԧ���檺�{���X�M��ק�, �z�LFireBug���[��U�Ԧ���檺���e, 
�o�{�G�Өƥ�
onclick����jQuery.datepicker._clickMonthYear
onchange����jQuery.datepicker._selectMonthYear

�z�L_clickMonthYear��_selectMonthYear����r�j�M�i�H��� _generateMonthYearHeader

_generateMonthYearHeader���e�N�O���ͦ~��U�Ԧ���檺�{���X

�U�Ԧ���檺option��value�O���褸�~, �u����ܧ令����xx�~
�ҥH�s�W�@�Ӥ�k(formatYear), �z�L��k�i���ഫ�C

	var formatYear=function(v){return '����'+(v-1911)+'�~';};
	
�ק�{���X�G

	for (; year <= endYear; year++) {
		html += '<option value="' + year + '"' +
			(year == drawYear ? ' selected="selected"' : '') +
			'>' + formatYear(year) + '</option>';
	}
	
2. ��ܤ��(20091215)��, ��r�ؤ����(981215)
�z�LFireBug���[���������e, �i�H�o�{
onclick������jQuery.datepicker._selectDay

�̫�|���formatDate��k�O�����I
_selectDay -> _selectDate -> _formatDate -> formatDate

formatDate��k���T�ӰѼ�, �^�ǬO�r��κA
	format�G�榡�Ƴ]�w�C
	date�G��ܪ����, �ODate���κA�C
	settings�G�����ܪ��]�w��, {shortYearCutoff, dayNamesShort, dayNames, monthNamesShort, monthNames}

�ҥH��gformatDate��k���e�p�U�G
formatDate: function (format, date, settings) {
	var d = date.getDate();
	var m = date.getMonth()+1;
	var y = date.getFullYear();			
	var fm = function(v){			
		return (v<10 ? '0' : '')+v;
	};
	return (y-1911) +''+ fm(m) +''+ fm(d);
}

3. �o�{bug, �b�ĤG����ܤ����, ���|�۰ʸ����r�ؤ������, �٬O��ܤ��Ѫ��~��C
�]�����쪺�����O�I��p�ϥ���ܤ��, �ҥH�qimg�}�l��
img -> _connectDatepicker -> _showDatepicker -> _setDateFromField -> parseDate

parseDate��k���T�ӰѼ�, �^�ǬODate�κA
	format�G�榡�Ƴ]�w�C
	value�G�ثe�����, �OString���κA�C
	settings�G�����ܪ��]�w��, {shortYearCutoff, dayNamesShort, dayNames, monthNamesShort, monthNames}
	
�ҥH��gparseDate��k���e�p�U�G
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

4.�z�LjQuery��extend�ӭק�즳����k���e, �Ӥ��O�����ק�D�ɤ��e, �U���D�ɧ�s�ɴN���ΦA��@���F�C
���O_generateMonthYearHeader��k���e�٬O�n��, �]���D�ɥ����N�S�����ѳo�ˤl����k�C

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
	    	return '����'+(v-1911)+'�~';
			}
	});	
	
})(jQuery);


_generateMonthYearHeader��k�ק�p�U�G
var formatYear = $.datepicker.formatYear ? $.datepicker.formatYear : function(v){return v;};

for (; year <= endYear; year++) {
	html += '<option value="' + year + '"' +
		(year == drawYear ? ' selected="selected"' : '') +
		'>' + formatYear(year) + '</option>';
}

�������i�{���X��F��, �ٺ����쪺, ���g�X�Ӫ��F�褣�@�w�O�ۤv�n��
�粒��, �A�h�ݤ��~�o�{�O������ڭn���F��, ����~