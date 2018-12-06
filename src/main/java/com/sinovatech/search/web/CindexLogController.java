package com.sinovatech.search.web;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sinovatech.common.util.Validate;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.entity.CreateIndexProcessLogDTO;
import com.sinovatech.search.luceneindex.AppContext;
import com.sinovatech.search.services.CindexLogService;
import com.sinovatech.search.utils.Consts;
import com.sinovatech.search.utils.JisuanDto;
import com.sinovatech.search.utils.JsonUtil;
import com.sinovatech.search.utils.web.BaseController;

/**
 *
 * @author liuzhenquan
 * @date 2018年5月3日 下午3:06:27
 */
@Controller
@RequestMapping("cindexlog")
public class CindexLogController extends BaseController {
    
    @Autowired
    private CindexLogService cindexLogService;
    
    private static final Log log = LogFactory.getLog(CindexLogController.class);
    
    @RequestMapping(value = "/queryCindexLog")
    public String queryCindexLog(CreateIndexProcessLogDTO clickDto, 
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        LimitInfo limit = new LimitInfo();
        limit.setFirstEnter(true);
        limit.setRowDisplayed(Consts.ROWS_PER_PAGE);
        
        getRequestPageNo(request, limit);
        
        List<CreateIndexProcessLogDTO> createIndexProcessLogDTO = cindexLogService.getCreateIndexProcessLogList(limit, clickDto);
       
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("limit", limit);
        map.put("list", createIndexProcessLogDTO);
        request.setAttribute("json", JsonUtil.getJSONString(map));
        return "response";
    }
    
//    private  static String  getDateByjisuan(String  date,int s ) {
//    	
//         SimpleDateFormat sdf=new SimpleDateFormat();
//        
//        sdf.applyPattern("yyyy-MM-dd hh:mm:ss");
//        Date d = sdf.parse(date);
//    	Calendar datec = Calendar.getInstance();
//    	datec.setTime(d);
//    	datec.add(Calendar.SECOND,s);
//    	return sdf.format(datec.getTime());
//    }
    @RequestMapping(value = "/jisuan")
    public String jisuan(CreateIndexProcessLogDTO clickDto, 
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String,JisuanDto > rmap =new HashMap<String,JisuanDto>();//如果是app则key放appcode 否则key存放commandcode
    	//appcode=监控app进度
    	//commandcode=监控commandcode进度
    	String flag =request.getParameter("flag");
    	String[]  appcode=null;
    	String ku="";
    	appcode=request.getParameter("appcode").split(",");
    	
    	for(String apc:appcode)
    	{
    		 
    		Map<String, CreateIndexProcessLogDTO>  itmapA = AppContext.getCindexProcessMap().get("A"+apc);
    		Map<String, CreateIndexProcessLogDTO>  itmapB = AppContext.getCindexProcessMap().get("B"+apc);
    		log.info("jindu-huoqu-appcode:["+apc+"]");	
    		if(itmapA==null || itmapB==null){
    			log.info("itmapA || itmapB is null break jindu-huoqu-appcode:["+apc+"]");	
    			continue;
    		}
    		if(itmapA.size()==0 || itmapB.size()==0){
    			log.info("itmapA || itmapB is size()==0 break jindu-huoqu-appcode:["+apc+"]");	
    			continue;
    		}
    		if("appcode".equals(flag))//app监控
    		{
    			if(itmapA.size()==1){//如果是只有一个业务 则用业务的创建速度来体现app创建速度
    				 Iterator  it =itmapA.keySet().iterator();
        	   	     while(it.hasNext())
        	   	     {
        	   	    	 try{
    	    	   	    	 CreateIndexProcessLogDTO  createIndexProcessLogDTO =itmapA.get(it.next());
    	    	   	    	 Integer[] rsti =new Integer[3];
    	    	   	    	 rsti[0]=Integer.parseInt(createIndexProcessLogDTO.getCpage());
    	    	   	    	 rsti[1]=Integer.parseInt(createIndexProcessLogDTO.getTotalpage());
    	    	   	    	 if("0".equals(createIndexProcessLogDTO.getTotalpage()))
    	    	   	    	 {
    	    	   	    		rsti[2]=0;
    	    	   	    	 }else{
    	    	   	    		rsti[2]= rsti[0]*100/rsti[1];
    	    	   	    	 }
    	    	   	    	JisuanDto jisuanDto =new JisuanDto();
    	    	   	    	 jisuanDto.setAppcode(apc);
    	    	   	    	 jisuanDto.setKu("A");
    	    	   	    	 jisuanDto.setCommandcode("-1");
    	    	   	    	 jisuanDto.setCpage(rsti[0]);
    	    	   	    	 jisuanDto.setTpage(rsti[1]);
    	    	   	    	 jisuanDto.setJindu(rsti[2]);
    	    	   	    	 jisuanDto.setOld(createIndexProcessLogDTO.getOld());
    	    	   	    	 jisuanDto.setHaoshi((createIndexProcessLogDTO.getDoendtime()-createIndexProcessLogDTO.getDobegintime())/1000);
    	    	   	    	 jisuanDto.setDate(createIndexProcessLogDTO.getCdate());
    	    	   	    	 jisuanDto.setFlag(createIndexProcessLogDTO.getFlag());
    	    	   	    	 jisuanDto.setErrorinfo(createIndexProcessLogDTO.getErrinfo());
    	    	   	    	 rmap.put("A"+apc,jisuanDto);
    	    	   	    	 log.info("只有一个业务的appcode计算结果["+apc+"]jisuanDto:Ku[A]"+"Commandcode["+jisuanDto.getCommandcode()+"]"+"Cpage["+jisuanDto.getCpage()+"]"+"Tpage["+jisuanDto.getTpage()+"]"+"Jindu["+jisuanDto.getJindu()+"]");	

        	   	    	 }catch(Exception e)
        	   	    	 {
        	   	    		 e.printStackTrace();
        	   	    		log.error("appcode计算错误",e);
        	   	    	 }
        	   	    	
        	   	     }
        	   	     
        	   	     //======================================
        	     Iterator  itb =itmapB.keySet().iterator();
     	   	     while(itb.hasNext())
     	   	     {
     	   	    	 try{
 	    	   	    	 CreateIndexProcessLogDTO  createIndexProcessLogDTO =itmapB.get(itb.next());
 	    	   	    	 Integer[] rsti =new Integer[3];
 	    	   	    	 rsti[0]=Integer.parseInt(createIndexProcessLogDTO.getCpage());
 	    	   	    	 rsti[1]=Integer.parseInt(createIndexProcessLogDTO.getTotalpage());
 	    	   	    	 if("0".equals(createIndexProcessLogDTO.getTotalpage()))
 	    	   	    	 {
 	    	   	    		rsti[2]=0;
 	    	   	    	 }else{
 	    	   	    		rsti[2]= rsti[0]*100/rsti[1];
 	    	   	    	 }
 	    	   	    	JisuanDto jisuanDto =new JisuanDto();
 	    	   	    	 jisuanDto.setAppcode(apc);
 	    	   	    	 jisuanDto.setKu("B");
 	    	   	    	 jisuanDto.setCommandcode("-1");
 	    	   	    	 jisuanDto.setCpage(rsti[0]);
 	    	   	    	 jisuanDto.setTpage(rsti[1]);
 	    	   	    	 jisuanDto.setJindu(rsti[2]);
 	    	   	    	 jisuanDto.setOld(createIndexProcessLogDTO.getOld());
 	    	   	    	 jisuanDto.setHaoshi((createIndexProcessLogDTO.getDoendtime()-createIndexProcessLogDTO.getDobegintime())/1000);
 	    	   	    	 jisuanDto.setDate(createIndexProcessLogDTO.getCdate());
 	    	   	    	 jisuanDto.setFlag(createIndexProcessLogDTO.getFlag());
	    	   	    	 jisuanDto.setErrorinfo(createIndexProcessLogDTO.getErrinfo());
 	    	   	    	 rmap.put("B"+apc,jisuanDto);
 	    	   	    	 log.info("只有一个业务的appcode计算结果["+apc+"]jisuanDto:Ku["+"B"+"]"+"Commandcode["+jisuanDto.getCommandcode()+"]"+"Cpage["+jisuanDto.getCpage()+"]"+"Tpage["+jisuanDto.getTpage()+"]"+"Jindu["+jisuanDto.getJindu()+"]");	

     	   	    	 }catch(Exception e)
     	   	    	 {
     	   	    		 e.printStackTrace();
     	   	    		log.error("appcode计算错误",e);
     	   	    	 }
     	   	    	
     	   	     }
        	   	     
    			}else{//如果多个则app创建速度为创建了第几个业务
       				 Iterator  it =itmapA.keySet().iterator();
       				 Integer[] rsti =new Integer[3];
       				 rsti[1]=itmapA.size();
       				 int count=0;
       				 double dd=0d;
       				 double ddb=0d;
       				 String cd1="";
       				 String cd2="";
       				 String flag1="1";
       				 String error="ok";
           	   	     while(it.hasNext())
           	   	     {
           	   	    	 try{
       	    	   	    	 	CreateIndexProcessLogDTO  createIndexProcessLogDTO =itmapA.get(it.next());
	       	    	   	    	 if(!createIndexProcessLogDTO.getCpage().equals("0")){
	       	    	   	    		 count++;
	       	    	   	    		 dd=(createIndexProcessLogDTO.getDoendtime()-createIndexProcessLogDTO.getDobegintime())/1000;
	       	    	   	    	     cd1=createIndexProcessLogDTO.getCdate();
	       	    	   	           	 flag1=createIndexProcessLogDTO.getFlag();
	       	    	   	           	 error = createIndexProcessLogDTO.getErrinfo();
	       	    	   	    	 }else{
	    	    	   	    		 cd1=createIndexProcessLogDTO.getCdate();
	    	    	   	    		 flag1=createIndexProcessLogDTO.getFlag();
	       	    	   	           	 error = createIndexProcessLogDTO.getErrinfo();
	    	    	   	    	 }
           	   	    	 }catch(Exception e)
           	   	    	 {
           	   	    		 e.printStackTrace();
           	   	    		log.error("appcode计算错误",e);
           	   	    	 }
           	   	    	
           	   	     }
           	   	 rsti[0]=count;
           		 rsti[2]= rsti[0]*100/rsti[1];
           	    
           		 JisuanDto jisuanDto =new JisuanDto();
	   	    	 jisuanDto.setAppcode(apc);
	   	    	 jisuanDto.setKu("A");
	   	    	 jisuanDto.setCommandcode("-1");
	   	    	 jisuanDto.setCpage(rsti[0]);
	   	    	 jisuanDto.setTpage(rsti[1]);
	   	    	 jisuanDto.setJindu(rsti[2]);
	   	    	 jisuanDto.setOld("");
	   	    	 jisuanDto.setHaoshi(dd);
	   	    	 jisuanDto.setDate(cd1);
	   	    	 jisuanDto.setFlag(flag1);
	   	    	 jisuanDto.setErrorinfo(error);
	   	    	 rmap.put("A"+apc,jisuanDto);
           		 
           	     log.info("有多个业务的appcode计算结果["+apc+"]jisuanDto:Ku["+ku+"]"+"Commandcode["+jisuanDto.getCommandcode()+"]"+"Cpage["+jisuanDto.getCpage()+"]"+"Tpage["+jisuanDto.getTpage()+"]"+"Jindu["+jisuanDto.getJindu()+"]");	

           	     //====================================
           	     Iterator  itb =itmapB.keySet().iterator();
				 Integer[] rstib =new Integer[3];
				 rstib[1]=itmapB.size();
				 int countb=0;
    	   	     while(itb.hasNext())
    	   	     {
    	   	    	 try{
	    	   	    	 	CreateIndexProcessLogDTO  createIndexProcessLogDTO =itmapB.get(itb.next());
    	    	   	    	 if(!createIndexProcessLogDTO.getCpage().equals("0")){
    	    	   	    		 countb++;
    	    	   	    		 ddb=(createIndexProcessLogDTO.getDoendtime()-createIndexProcessLogDTO.getDobegintime())/1000;
    	    	   	    		 cd2=createIndexProcessLogDTO.getCdate();
    	    	   	    		 
    	    	   	    		 flag1=createIndexProcessLogDTO.getFlag();
       	    	   	           	 error = createIndexProcessLogDTO.getErrinfo();
    	    	   	    	 }else{
    	    	   	    		 cd2=createIndexProcessLogDTO.getCdate();
    	    	   	    		 
    	    	   	    		 flag1=createIndexProcessLogDTO.getFlag();
       	    	   	           	 error = createIndexProcessLogDTO.getErrinfo();
    	    	   	    	 }
    	   	    	 }catch(Exception e)
    	   	    	 {
    	   	    		 e.printStackTrace();
    	   	    		log.error("appcode计算错误",e);
    	   	    	 }
    	   	    	
    	   	     }
		    	   	 rstib[0]=countb;
		    		 rstib[2]= rstib[0]*100/rstib[1];
		    	    
		    		 JisuanDto jisuanDtob =new JisuanDto();
			    	 jisuanDtob.setAppcode(apc);
			    	 jisuanDtob.setKu("B");
			    	 jisuanDtob.setCommandcode("-1");
			    	 jisuanDtob.setCpage(rstib[0]);
			    	 jisuanDtob.setTpage(rstib[1]);
			    	 jisuanDtob.setJindu(rstib[2]);
			    	 jisuanDtob.setOld("");
			    	 jisuanDtob.setHaoshi(ddb);
			    	 jisuanDtob.setDate(cd2);
			    	 jisuanDto.setFlag(flag1);
		   	    	 jisuanDto.setErrorinfo(error);
			    	 rmap.put("B"+apc,jisuanDtob);
		    	     log.info("有多个业务的appcode计算结果["+apc+"]jisuanDto:Ku["+"B"+"]"+"Commandcode["+jisuanDtob.getCommandcode()+"]"+"Cpage["+jisuanDtob.getCpage()+"]"+"Tpage["+jisuanDtob.getTpage()+"]"+"Jindu["+jisuanDtob.getJindu()+"]");	

    			}
    		}else if("commandcode".equals(flag)){//command 监控
    			 Iterator  it =itmapA.keySet().iterator();
    	   	     while(it.hasNext())
    	   	     {
    	   	    	 try{
	    	   	    	 CreateIndexProcessLogDTO  createIndexProcessLogDTO =itmapA.get(it.next());
	    	   	    	 Integer[] rsti =new Integer[3];
	    	   	    	 rsti[0]=Integer.parseInt(createIndexProcessLogDTO.getCpage());
	    	   	    	 rsti[1]=Integer.parseInt(createIndexProcessLogDTO.getTotalpage());
	    	   	    	 if("0".equals(createIndexProcessLogDTO.getTotalpage()))
	    	   	    	 {
	    	   	    		rsti[2]=0;
	    	   	    	 }else{
	    	   	    		rsti[2]= rsti[0]*100/rsti[1];
	    	   	    	 }
	    	   	    	 
	    	   	    	JisuanDto jisuanDto =new JisuanDto();
	    	   	    	 jisuanDto.setAppcode(apc);
	    	   	    	 jisuanDto.setKu("A");
	    	   	    	 jisuanDto.setCommandcode(createIndexProcessLogDTO.getCommandcode());
	    	   	    	 jisuanDto.setCpage(rsti[0]);
	    	   	    	 jisuanDto.setTpage(rsti[1]);
	    	   	    	 jisuanDto.setJindu(rsti[2]);
	    	   	    	 jisuanDto.setOld(createIndexProcessLogDTO.getOld());
	    	   	    	 jisuanDto.setDate(createIndexProcessLogDTO.getCdate());
	    	   	    	 jisuanDto.setHaoshi((createIndexProcessLogDTO.getDoendtime()-createIndexProcessLogDTO.getDobegintime())/1000);
	    	   	    	 jisuanDto.setFlag(createIndexProcessLogDTO.getFlag());
	    	   	    	 jisuanDto.setErrorinfo(createIndexProcessLogDTO.getErrinfo());
	    	   	    	 rmap.put("A"+jisuanDto.getCommandcode(),jisuanDto);
	    	   	      log.info("command计算结果["+apc+"]jisuanDto:Ku["+ku+"]"+"Commandcode["+jisuanDto.getCommandcode()+"]"+"Cpage["+jisuanDto.getCpage()+"]"+"Tpage["+jisuanDto.getTpage()+"]"+"Jindu["+jisuanDto.getJindu()+"]");	
    	   	    	 }catch(Exception e)
    	   	    	 {
    	   	    		 e.printStackTrace();
    	   	    		log.error("command计算错误",e);
    	   	    	 }
    	   	    	
    	   	     }
    	   	     
    	   	 Iterator  itb =itmapB.keySet().iterator();
 	   	     while(itb.hasNext())
 	   	     {
 	   	    	 try{
	    	   	    	 CreateIndexProcessLogDTO  createIndexProcessLogDTO =itmapB.get(itb.next());
	    	   	    	 Integer[] rsti =new Integer[3];
	    	   	    	 rsti[0]=Integer.parseInt(createIndexProcessLogDTO.getCpage());
	    	   	    	 rsti[1]=Integer.parseInt(createIndexProcessLogDTO.getTotalpage());
	    	   	    	 if("0".equals(createIndexProcessLogDTO.getTotalpage()))
	    	   	    	 {
	    	   	    		rsti[2]=0;
	    	   	    	 }else{
	    	   	    		rsti[2]= rsti[0]*100/rsti[1];
	    	   	    	 }
	    	   	    	 
	    	   	    	JisuanDto jisuanDto =new JisuanDto();
	    	   	    	 jisuanDto.setAppcode(apc);
	    	   	    	 jisuanDto.setKu("B");
	    	   	    	 jisuanDto.setCommandcode(createIndexProcessLogDTO.getCommandcode());
	    	   	    	 jisuanDto.setCpage(rsti[0]);
	    	   	    	 jisuanDto.setTpage(rsti[1]);
	    	   	    	 jisuanDto.setJindu(rsti[2]);
	    	   	    	 jisuanDto.setOld(createIndexProcessLogDTO.getOld());
	    	   	    	 jisuanDto.setDate(createIndexProcessLogDTO.getCdate());
	    	   	    	 jisuanDto.setHaoshi((createIndexProcessLogDTO.getDoendtime()-createIndexProcessLogDTO.getDobegintime())/1000);
	    	   	    	 jisuanDto.setFlag(createIndexProcessLogDTO.getFlag());
	    	   	    	 jisuanDto.setErrorinfo(createIndexProcessLogDTO.getErrinfo());
	    	   	    	 rmap.put("B"+jisuanDto.getCommandcode(),jisuanDto);
	    	   	      log.info("command计算结果["+apc+"]jisuanDto:Ku["+"B"+"]"+"Commandcode["+jisuanDto.getCommandcode()+"]"+"Cpage["+jisuanDto.getCpage()+"]"+"Tpage["+jisuanDto.getTpage()+"]"+"Jindu["+jisuanDto.getJindu()+"]");	
 	   	    	 }catch(Exception e)
 	   	    	 {
 	   	    		 e.printStackTrace();
 	   	    		log.error("command计算错误",e);
 	   	    	 }
 	   	    	
 	   	     }
    			
    		}
    		
	   	    
    	}
        request.setAttribute("json", JsonUtil.getJSONString(rmap));
        return "response";
    } 
    
    @RequestMapping("/deleteCindexLog")
    public String deleteClickLog(HttpServletRequest request,HttpServletResponse response)
    		throws Exception{
    	String ids = request.getParameter("ids");
    	Map<String, Object> map = new HashMap<String, Object>();
		Validate.notBlank(ids, "common", "errorparameter");
		try {
			this.cindexLogService.deleteTX(ids);
			log.info("Delete clickLog is ok ");
			map.put("flag", "OK");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Delete clickLog is error ");
			map.put("flag", "false");
		}
		request.setAttribute("json", JsonUtil.getJSONString(map));
		return "response";
    }
   
}
