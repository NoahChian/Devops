package org.iii.ideas.catering_service.filter;

import static org.apache.commons.lang.exception.ExceptionUtils.getStackTrace;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.iii.ideas.catering_service.common.Common;

public class FilterSet implements Filter
{
	private Logger log = Logger.getLogger(this.getClass());
	private FilterConfig config;
	Common common = new Common();
	
	@Override
	public void init(FilterConfig fConfig) throws ServletException 
	{
		// TODO Auto-generated method stub
		this.config = fConfig;
	}
	
	@Override
	public void destroy() 
	{
		// TODO Auto-generated method stub
		this.config = null;
	}

	private void logContent(ServletRequest req){
		try{
			
			MultiReadHttpServletRequest request = new MultiReadHttpServletRequest((HttpServletRequest)req);
			
//			Scanner scanner = new Scanner(request.getInputStream());
//			
//			StringBuilder builder = new StringBuilder();
//			
//			while (scanner.hasNextLine()) {
//				builder.append(scanner.nextLine());
//		    }
//			scanner.close();
			StringBuilder builder = new StringBuilder();
			String aux = "";
			BufferedReader reader=request.getReader();
			while ((aux = reader.readLine()) != null) {
			    builder.append(aux);
			}
			
			reader.close();

			log.info(builder.toString());
		}catch(Exception e){			
			log.error(getStackTrace(e));
		}
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException 
	{
		/*
		//add hearder to all response header to solve cross domain issus
		HttpServletResponse res = (HttpServletResponse) response;
		res.setHeader("Access-Control-Allow-Origin", "*");
		res.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		
		//check xss，去除所有非法字元
        HttpServletRequest hreq = (HttpServletRequest)request;
        Map map = hreq.getParameterMap();
        Iterator itr = map.keySet().iterator();
        while( itr.hasNext() )
        {
            String key = itr.next().toString();
            String [] values = hreq.getParameterValues(key);
            if( values != null )
            {
                for( int i = 0; i < values.length; i++ )
                {
                    values[i] = cleanXSS(values[i]);
                }
            }
            hreq.setAttribute(key, values);
        }
		
		//check login session，擋掉未登入想直接進入網址的client
		HttpServletRequest requ = (HttpServletRequest)request;
		HttpSession session = requ.getSession();
		//String aaa=requ.getPathInfo();
		//String bbb=requ.getServletPath();		
		if(requ.getServletPath().indexOf("rest")>-1){
			request=new MultiReadHttpServletRequest((HttpServletRequest)request);
			logContent(request);
		}
		
		if( false
//			!requ.getPathInfo().endsWith("login/") &&
//			!requ.getPathInfo().startsWith("/setting/")
			
				) 		   
		{
			if(session.getAttribute("user_account") == null)
			{
				response.setContentType("text/html;charset=UTF-8"); 
				PrintWriter out = response.getWriter();	
				String loginError;
				
				if(requ.getServletPath().endsWith("productManager.do"))
					loginError = "<script>alert('您還沒有登錄');window.location.href='http://www.twfoodtrace.org.tw/index.php';</script>";
				else				
					loginError = "<script>alert('您還沒有登錄');window.location.href='/bill/web/main/login/';</script>";	
				
				common.setWritePrinter(out, loginError);
			}				
			else
				chain.doFilter(request, response);			
		}
		else
			chain.doFilter(request, response);		
			*/
		
		//add hearder to all response header to solve cross domain issus
		HttpServletResponse res = (HttpServletResponse) response;
		res.setHeader("Access-Control-Allow-Origin", "*");
		res.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		res.setHeader("X-Frame-Options","DENY");  //20150922 shine add		
		
		//check xss，去除所有非法字元
        HttpServletRequest hreq = (HttpServletRequest)request;
        Map map = hreq.getParameterMap();
        Iterator itr = map.keySet().iterator();
        while( itr.hasNext() )
        {
            String key = itr.next().toString();
            String [] values = hreq.getParameterValues(key);
            if( values != null )
            {
                for( int i = 0; i < values.length; i++ )
                {
                    values[i] = cleanXSS(values[i]);
                }
            }
            hreq.setAttribute(key, values); 
        }
       	
		//check login session，擋掉未登入想直接進入網址的client
		HttpServletRequest requ = (HttpServletRequest)request;
		HttpSession session = requ.getSession();
		
		
		if(requ.getPathInfo() != null)
		{		
			String[] uri = requ.getPathInfo().split("/"); //Ric 20140424 add 台中專頁  20140430 台北專頁 KC
 			if(!uri[1].equals("main")
 					&&!uri[1].equals("resetpwd")
 					&&!uri[1].equals("taipei") 
					&&!uri[1].equals("Taichung") 
					&&!uri[1].equals("mcontents") 
					&&!uri[1].equals("news")
					&&!uri[1].equals("custom")
					&&!uri[1].equals("privacy")
					&&!uri[1].equals("systemMaintainSwitch")
					&& session.getAttribute("account") == null)
			{
				response.setContentType("text/html;charset=UTF-8"); 
				PrintWriter out = response.getWriter();	
				String msg = "<script>alert('您還沒有登錄');window.location.href='http://" + request.getServerName() + ":" + request.getLocalPort() + "'</script>";
				
				common.printMsg(out, msg);
			}
			else
				chain.doFilter(request, response);	
		}
		else
			chain.doFilter(request, response);
		
	}

	private String cleanXSS(String value) {
        value = value.replaceAll("<", "& lt;").replaceAll(">", "& gt;");
        value = value.replaceAll("\\(", "& #40;").replaceAll("\\)", "&#41;");
        value = value.replaceAll("'", "& #39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        value = value.replaceAll("script", "");
        return value;
	}	
}
