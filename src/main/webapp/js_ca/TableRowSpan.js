jQuery.fn.rowspan = function(){
	var i=0;
	var pText='';
	var sObj;	//預計進行RowSpan物件
	var rcnt=0;	//計算rowspan的數字
	var tlen=this.length;
	return this.each(function(){
		i=i+1;
		rcnt=rcnt+1;
		//與前項不同
		if(pText!=$(this).text())
		{
			if(i!=1)
			{
				if((rcnt-1) > 1){
					//不是剛開始，進行rowspan
					sObj.attr('rowspan',rcnt-1);
					sObj.css('vertical-align','middle');
				}
				rcnt=1;
			}
			//設定要rowspan的物件
			sObj=$(this);
			pText=$(this).text();
		}
		else
		{
			$(this).hide();
		}
			
		if(i==tlen)
		{
			if((rcnt-1) > 1){
				sObj.attr('rowspan',rcnt-1);
				sObj.css('vertical-align','middle');
			}
		}
	});
}
