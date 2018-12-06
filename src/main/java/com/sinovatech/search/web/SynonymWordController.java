package com.sinovatech.search.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sinovatech.common.util.StringUtils;
import com.sinovatech.common.util.Validate;
import com.sinovatech.search.constants.RedisKeyConst;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.entity.SearchAppDTO;
import com.sinovatech.search.entity.SearchCommandDTO;
import com.sinovatech.search.entity.SynonymWordDTO;
import com.sinovatech.search.services.RecommendService;
import com.sinovatech.search.services.SearchAppService;
import com.sinovatech.search.services.SynonymWordService;
import com.sinovatech.search.services.UserService;
import com.sinovatech.search.utils.Consts;
import com.sinovatech.search.utils.JsonUtil;
import com.sinovatech.search.utils.web.BaseController;

/**
 * SynonymWord 同义词管理Controller类
 * @author ChenZhuo
 * @date 2016年12月6日 上午10:38:16
 */
@Controller
@RequestMapping("synonymWord")
public class SynonymWordController extends BaseController {

    @Autowired
    private SynonymWordService synonymWordService;
    @Autowired
    private SearchAppService searchAppService;
    @Autowired
    private RecommendService recommendService;
	@Autowired
	private UserService userService;

    private static final Log log = LogFactory.getLog(SynonymWordController.class);

    @RequestMapping("querySynonymWord")
    public String querySynonymWord(SynonymWordDTO synonymWordDTO, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
 
        LimitInfo limit = new LimitInfo();
        limit.setFirstEnter(true);
        limit.setRowDisplayed(Consts.ROWS_PER_PAGE);
        getRequestPageNo(request, limit);// 翻页代码
        //获取当前登录用户登录名
  		String usercode = (String)request.getSession().getAttribute(RedisKeyConst.Search.SESSION_USERCODE);
  		//获取当前登录用户关联的appcode
  		String appCode = (String)request.getSession().getAttribute(RedisKeyConst.Search.SESSION_APPCOCE); 
        List<SynonymWordDTO> list = synonymWordService.list(limit, synonymWordDTO,usercode,appCode);
        //获取登录用户信息
  		String username = (String)request.getSession().getAttribute(RedisKeyConst.Search.CALL_WS_USERNAME);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("limit", limit);
        map.put("list", list);
        map.put("username", username);
        request.setAttribute("json", JsonUtil.getJSONString(map));
        return "response";
    }

    @RequestMapping("beforeAddSynonymWord")
    public String beforeAddSynonymWord(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	//获取当前登录用户登录名
		String usercode = (String)request.getSession().getAttribute(RedisKeyConst.Search.SESSION_USERCODE);
		//获取当前登录用户关联的appcode
		String appCode = (String)request.getSession().getAttribute(RedisKeyConst.Search.SESSION_APPCOCE);
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<SearchAppDTO> appList = new ArrayList<SearchAppDTO>();
		if(StringUtils.isNotBlank(usercode) && !"null".equals(usercode)){
			if("admin".equals(usercode)){
				appList = searchAppService.getAllDatas();
			}else{
				appList = userService.getSearchAppInfoByCurrentUser(appCode);
			}
		}
//    	List<SearchAppDTO> appList = searchAppService.getAllDatas();
        map.put("appList", appList);
        map.put("m", new SynonymWordDTO());
        request.setAttribute("json", JsonUtil.getJSONString(map));
        return "response";
    }

    @RequestMapping("addSynonymWord")
    public String addSynonymWord(SynonymWordDTO synonymWordDTO, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
    	String commandCode = request.getParameter("commandCode");
    	synonymWordDTO.setCommandCode(commandCode);
        String synonymArray = request.getParameter("synonymArray");
        synonymWordDTO.setSynonymArray(synonymArray);
        String username = (String)request.getSession().getAttribute(RedisKeyConst.Search.CALL_WS_USERNAME);
        synonymWordDTO.setOperator(username);
        String flag = this.synonymWordService.save(synonymWordDTO);
        log.info("Save synonymWord is ok ");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("flag", flag);
        request.setAttribute("json", JsonUtil.getJSONString(map));
        return "response";
    }

    @RequestMapping("beforeEditSynonymWord")
    public String beforeEditSynonymWord(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String id = request.getParameter("id");
        SynonymWordDTO m = null;
        if (id == null || "".equals(id) || "undefined".equals(id)) {
            m = new SynonymWordDTO();
        } else {
            m = synonymWordService.get(id);
        }
        //获取当前登录用户登录名
		String usercode = (String)request.getSession().getAttribute(RedisKeyConst.Search.SESSION_USERCODE);
		//获取当前登录用户关联的appcode
		String appCode = (String)request.getSession().getAttribute(RedisKeyConst.Search.SESSION_APPCOCE);
		
		List<SearchAppDTO> appList = new ArrayList<SearchAppDTO>();
		if(StringUtils.isNotBlank(usercode) && !"null".equals(usercode)){
			if("admin".equals(usercode)){
				appList = searchAppService.getAllDatas();
			}else{
				appList = userService.getSearchAppInfoByCurrentUser(appCode);
			}
		}
//        List<SearchAppDTO> appList = searchAppService.getAllDatas();
        List<SearchCommandDTO> commandList = recommendService.getListCommendByAppCode(m.getAppCode());
        map.put("m", m);
		map.put("appList",appList);
		map.put("commandList",commandList);
		String commandCode = m.getCommandCode();
		map.put("command", commandCode);
		
//		String[] command = null;
//		if(StringUtils.isNotBlank(commandCode)){
//			command = commandCode.split(",");
//			map.put("commands", command);
//		}
        request.setAttribute("json", JsonUtil.getJSONString(map));
        return "response";
    }

    @RequestMapping("editSynonymWord")
    public String editSynonymWord(SynonymWordDTO synonymWordDTO, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
    	String commandCode = request.getParameter("commandCode");
    	synonymWordDTO.setCommandCode(commandCode);
    	Map<String, Object> map = new HashMap<String, Object>();
        String username = (String)request.getSession().getAttribute(RedisKeyConst.Search.CALL_WS_USERNAME);
        synonymWordDTO.setOperator(username);
        String flag = this.synonymWordService.update(synonymWordDTO);
        log.info("Update synonymWord is ok ");
        map.put("flag", flag);
        request.setAttribute("json", JsonUtil.getJSONString(map));
        return "response";
    }
    
    @RequestMapping("deleteSynonymWord")
    public String deleteSynonymWord(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String ids = request.getParameter("ids");
        // 验证方法， 如果为null或者为空则直接返回异常
        Map<String, Object> map = new HashMap<String, Object>();
        Validate.notBlank(ids, "common", "errorparameter");
        try {
            this.synonymWordService.deleteTX(ids);
            log.info("Delete synonymWord is ok ");
            map.put("flag", "OK");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Delete synonymWord is error ");
            map.put("flag", "false");
        }
        request.setAttribute("json", JsonUtil.getJSONString(map));
        return "response";
    }

}
