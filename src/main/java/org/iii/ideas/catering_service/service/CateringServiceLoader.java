package org.iii.ideas.catering_service.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.iii.ideas.catering_service.rest.CacheMethod;
import org.iii.ideas.catering_service.rest.WebRestApiImpl;
import org.iii.ideas.catering_service.util.CateringServiceCode;
import org.iii.ideas.catering_service.util.CateringServiceUtil;
import org.iii.ideas.catering_service.util.SystemConfiguration;


@WebServlet(name="HelloServlet", urlPatterns={"/load.info"},loadOnStartup=1)
public class CateringServiceLoader  extends HttpServlet{
	private Logger log = Logger.getLogger(this.getClass());
	private static int cacheEnable = CateringServiceCode.CACHE_TEST;
	private static MemcachedClient  cacheClient=null;
	private static Long seq=(long) 0;
	private static Long lastSeqTime=(long) 0;
	private static String machineId="00";
	/*
	 * Sequence  long range: 
	 * 9223372036854775807 ~ -9223372036854
	 * 140436954001000001
	 * yyyymmddhhmiss
	 * 1404298344019992
	 * 
	 */
	synchronized public static Long getSeqNo() throws InterruptedException{
		Long currentTime = System.currentTimeMillis() / 1000 ;
		CateringServiceLoader.seq = ++CateringServiceLoader.seq;//保留六位數的
		//確保同一秒內被取號沒超過 1,000,000 次
		if(CateringServiceLoader.seq==100000){
			//如果同一秒內被取號超過1秒,就sleep 1 秒再送
			if(currentTime==CateringServiceLoader.lastSeqTime){
				Thread.sleep(1000);
				currentTime = System.currentTimeMillis() / 1000 ;
			}
			CateringServiceLoader.seq=(long) 0;
		}
		String seqStr = String.valueOf( currentTime ) + CateringServiceLoader.machineId  +   StringUtils.leftPad( CateringServiceLoader.seq.toString() , 5 , "0");
		CateringServiceLoader.lastSeqTime = currentTime;
		return Long.parseLong(seqStr);
	}
	
	public CateringServiceLoader() throws Exception{
		String msg = "";
		//如果有enable memcached 功能的function 就採用memcached
		if(CateringServiceLoader.getCacheEnable()  == CateringServiceCode.CACHE_TEST ){
			synchronized(this) {
				if(CateringServiceUtil.isEmpty(SystemConfiguration.getConfig("cache_server"))){
					CateringServiceLoader.setCacheEnable(CateringServiceCode.CACHE_DISABLE);
					log.info("------disable cache server------!");
					System.out.println("------disable cache server------!");
				}else{
					//建立memcached 的value
					try{
						this.syncInitMemcached();
						CateringServiceLoader.setCacheEnable(CateringServiceCode.CACHE_ENABLE);
						log.info("------enable cache server------");
						System.out.println("------enable cache server------");
					}catch(Exception e){
						CateringServiceLoader.setCacheEnable(CateringServiceCode.CACHE_DISABLE);
						log.info("------disable cache server------");
						System.out.println("------disable cache server------");
						log.info(e.getStackTrace());
					}	
				}
				//load cache qpi list
				WebRestApiImpl.cacheMethodList =   new HashMap<String, CacheMethod>(){{
			        put("customerQueryCounties", new CacheMethod(CateringServiceCode.CACHE_ONLY_WITHOUT_AUTH, 60));
			        put("customerQueryMonthBySchool", new CacheMethod(CateringServiceCode.CACHE_ONLY_WITHOUT_AUTH, 60));
			        put("customerQueryArea", new CacheMethod(CateringServiceCode.CACHE_ONLY_WITHOUT_AUTH, 60));
			        put("customerQueryCateringBySchoolAndDate", new CacheMethod(CateringServiceCode.CACHE_ONLY_WITHOUT_AUTH, 60));
			        put("customerQueryMenuDetailInfo", new CacheMethod(CateringServiceCode.CACHE_ONLY_WITHOUT_AUTH, 60));
			    }};
				
			}
		}
		//------load 本機 seq ------
		
		synchronized(this) {
			System.out.println("開始初始化UUID");
			if(CateringServiceUtil.isEmpty(SystemConfiguration.getConfig("machine_id"))){
				msg="確認是否有設定machine_id 於 context.xml中  格式00~99 兩位數";
				System.out.println(msg);
				log.info(msg);
				throw new Exception(msg);
			}else{
				this.machineId = SystemConfiguration.getConfig("machine_id");
				if(this.machineId.length()<1){
					System.out.println(msg);
					throw new Exception(msg);
				}else if(!StringUtils.isNumeric(this.machineId)){
					System.out.println(msg);
					throw new Exception(msg);
				}
				else{
					System.out.println("抓出: " + this.machineId);
				}
				Thread.sleep(1000);//sleep 1 秒,確保資料不會重覆
				System.out.println("loading machine_id:"+this.machineId);
				//for(int i =0;i<1000002;i++){
				System.out.println("get Seqence:"+CateringServiceLoader.getSeqNo());//測試資料
				//}
			}
		}
		
	}
	
	public void syncInitMemcached() throws IOException, Exception{
		log.info("------initial memcached------");
		cacheClient = new MemcachedClient(
				new BinaryConnectionFactory(),
		        AddrUtil.getAddresses(SystemConfiguration.getConfig("cache_server")));
		cacheClient.get("test cache!!");
	}
	
	public static MemcachedClient getCacheClient() throws IOException, Exception{
		/*
		MemcachedClient cacheClient = new MemcachedClient(
				new BinaryConnectionFactory(),
		        AddrUtil.getAddresses(SystemConfiguration.getConfig("cache_server")));
		        */
		return cacheClient;
	}
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
                        throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>System Information</title>");
        out.println("</head>");
        out.println("<body>");
        if(CateringServiceLoader.getCacheEnable()==CateringServiceCode.CACHE_DISABLE){
        	out.println("<h1> Cache Disable </h1>");
        }else{
        	out.println("<h1> Cache Enable </h1>");
        }
        out.println("</body>");
        out.println("</html>");
        out.close();
    }

	public static int getCacheEnable() {
		return cacheEnable;
	}

	public static void setCacheEnable(int cacheEnable) {
		CateringServiceLoader.cacheEnable = cacheEnable;
	}
}
