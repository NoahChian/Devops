package org.iii.ideas.catering_service.rest.api;

import java.text.ParseException;

import javax.naming.NamingException;

import org.iii.ideas.catering_service.util.CateringServiceCode;

public class SystemMaintainSwitch extends AbstractApiInterface<SystemMaintainSwitchRequest, SystemMaintainSwitchResponse> {

	@Override
	public void process() throws NamingException, ParseException {
		// TODO Auto-generated method stub
		if(CateringServiceCode.System_States == 1){ //服務轉維護
			CateringServiceCode.System_States = 0;
			this.responseObj.setResStatus(1);
			this.responseObj.setMsg("系統進入維護狀態。");
		}else if(CateringServiceCode.System_States == 0){ //維護轉服務
			CateringServiceCode.System_States = 1;
			this.responseObj.setResStatus(1);
			this.responseObj.setMsg("系統恢復服務狀態。");
		}
	}

}
