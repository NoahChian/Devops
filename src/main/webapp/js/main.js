$(function () {
  // Bootstrap 3 Datepicker v4
  // github: https://github.com/Eonasdan/bootstrap-datetimepicker
  $('.datetimepicker').datetimepicker({
    // debug: true,
    dayViewHeaderFormat: 'YYYY MMMM',
    format: 'YYYY/MM/DD',
    locale: 'zh-tw',
    useCurrent: true,
    allowInputToggle: true,
    showTodayButton: true
  });
  
  //hide jqueryui's datepicker
  $(".datetimepicker").click(function(){
		$("#ui-datepicker-div").css("display", "none");	
  });
});