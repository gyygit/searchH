package com.sinovatech.search.luceneindex.task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sinovatech.search.entity.CreateIndexProcessLogDTO;
import com.sinovatech.search.luceneindex.AppContext;
import com.sinovatech.search.luceneindex.db.dao.CreateIndexProcessLogDAO;
import com.sinovatech.search.utils.SpringContextHolder;

public class CindexLogTread implements Runnable{

	 private CreateIndexProcessLogDAO createIndexProcessLogDAOL;
	 
	 private static final Logger log = LoggerFactory.getLogger(CindexLogTread.class);
	@Override
	public void run() {
		CreateIndexProcessLogDTO createIndexProcessLogDTO = null;
		createIndexProcessLogDAOL = SpringContextHolder.getBean("createIndexProcessLogDAOL");
			while (true) {
				try {
				createIndexProcessLogDTO = AppContext.cindexlogBlockingQueue.take();
				if(createIndexProcessLogDTO.getFlag().equals("2")){
					
					log.info("写入日志-CindexLogTread-write-lucene-log["+createIndexProcessLogDTO.toString()+"]");

				}
				createIndexProcessLogDAOL.add(createIndexProcessLogDTO);
				Thread.sleep(100);
				log.info("写入日志-CindexLogTread-write-lucene-log["+createIndexProcessLogDTO.toString()+"]");
				jisuan(createIndexProcessLogDTO);
				
				
				Date today = new Date();  
				Calendar c = Calendar.getInstance();  
				c.setTime(today);  
				c.add(Calendar.DAY_OF_MONTH, -30);// 删除30天前的日志
				Date tomorrow = c.getTime(); 
				createIndexProcessLogDAOL.del("cdate:[2000-01-01 TO "+datestring(tomorrow)+"]");
                }catch (Exception e) {
					e.printStackTrace();
					log.error(e.getMessage(), e);
				}
			}
		
	}
 
	public static String datestring(Date date){
        SimpleDateFormat sdf=new SimpleDateFormat();
        
        sdf.applyPattern("yyyy-MM-dd");
        return sdf.format(date);
    }
	private void jisuan(CreateIndexProcessLogDTO createIndexProcessLogDTO)
	{
		Map<String, Map<String, CreateIndexProcessLogDTO>>  map =AppContext.getCindexProcessMap();
		if(map!=null)
		{
			Map<String, CreateIndexProcessLogDTO> itmap =map.get(createIndexProcessLogDTO.getKu()+createIndexProcessLogDTO.getAppcode());
			if(itmap!=null)
			{
				CreateIndexProcessLogDTO createIndexProcessLogDTO11 = itmap.get(createIndexProcessLogDTO.getCommandcode());
				if(createIndexProcessLogDTO11!=null)
				{
					try
					{
						//if(Integer.valueOf(createIndexProcessLogDTO11.getCpage())<Integer.valueOf(createIndexProcessLogDTO.getCpage())){
							
							createIndexProcessLogDTO11.setCdate(createIndexProcessLogDTO.getCdate());
							createIndexProcessLogDTO11.setCpage(createIndexProcessLogDTO.getCpage());
							createIndexProcessLogDTO11.setTotalpage(createIndexProcessLogDTO.getTotalpage());
							createIndexProcessLogDTO11.setDobegintime(createIndexProcessLogDTO.getDobegintime());
							createIndexProcessLogDTO11.setDoendtime(createIndexProcessLogDTO.getDoendtime());
							createIndexProcessLogDTO11.setCdate(createIndexProcessLogDTO.getCdate());
							createIndexProcessLogDTO11.setErrinfo(createIndexProcessLogDTO.getErrinfo());
							createIndexProcessLogDTO11.setFlag(createIndexProcessLogDTO.getFlag());
							log.info("计算创建进度 -CindexLogTread-jisuan-lucene-log:appcode["+createIndexProcessLogDTO11.getAppcode()+"]"
									+"commandcode:["+createIndexProcessLogDTO11.getCommandcode()+"]"
									+"c/t["+createIndexProcessLogDTO11.getCpage()+"/"+createIndexProcessLogDTO11.getTotalpage()+"] " +
											"haoshi:["+(createIndexProcessLogDTO11.getDoendtime()-createIndexProcessLogDTO11.getDobegintime())/1000+"秒");
						//}
					}catch(Exception e)
					{
						e.printStackTrace();
						log.error("计算创建进度 -CindexLogTread-jisuan-lucene-erroelog",e);
					}
				}
			}
		}
		
		
	}
}
