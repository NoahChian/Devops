package org.iii.ideas.catering_service.ws.schemav2;

import javax.xml.bind.JAXBElement;

import org.iii.ideas.catering_service.ws.schema.ObjectFactory;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;


public class CateringServiceEndPointv2 {

	public ObjectFactory objectFactory=null;
	protected  static final String TARGET_NAMESPACE = "http://twfoodtrace.org.tw/FcloudDataschemas";
}
