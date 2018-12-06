package com.sinovatech.search.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sinovatech.common.util.Validate;
import com.sinovatech.search.ectable.limit.ExLimitUtil;
import com.sinovatech.search.ectable.limit.ILimitUtil;
import com.sinovatech.search.ectable.limit.LimitInfo;
import com.sinovatech.search.orm.Page;
import com.sinovatech.search.utils.web.BaseController;
import com.sinovatech.search.entity.SearchMessageDTO;
import com.sinovatech.search.services.SearchMessageService;

/**
 * SearchMessage推送信息表 Controller类
 * 
 * 创建: 2014-12-18 14:06:04<br />
 * @author  作者liuzhenquan
 */
@Controller
@RequestMapping("searchMessage")
public class SearchMessageController extends BaseController {

@Autowired
private SearchMessageService searchMessageService;

/**
 *  <p>
 *  <b>业务处理描述</b>
 *  <ul>
 *  <li>查询前初始化</li>
 *  <li>目的：初始化方法</li>
 *  </ul>
 *  </p>
 * 
 *  @param searchMessageDTO:值对象
 *  @param request:request
 *  @param response:response
 *  @param response:HttpServletResponse
 *  @return ActionForward
 *  @exception Exception
*/
@RequestMapping(value = "/beforeQuerySearchMessage")
public String beforeQuerySearchMessage(SearchMessageDTO searchMessageDTO ,HttpServletRequest request,HttpServletResponse response)throws Exception
{ 
 //TODO 自定义实现
 return null;
}

/**
 *  <p>
 *  <b>业务处理描述</b>
 *  <ul>
 *  <li>可见性原因：需要被其他应用调用</li>
 *  <li>目的：初始化方法</li>
 *  <li>适用的前提条件</li>
 *  <li>后置条件：</li>
 *  <li>例外处理：无 </li>
 *  <li>已知问题：</li>
 *  <li>调用的例子：</li>
 *  </ul>
 *  </p>
 * 
 *  @param mapping:ActionMapping
 *  @param form:ActionForm
 *  @param request:HttpServletRequest
 *  @param response:HttpServletResponse
 *  @return ActionForward
 *  @exception Exception
*/
@RequestMapping(value = "/querySearchMessage")
public String  querySearchMessage(SearchMessageDTO searchMessageDTO ,HttpServletRequest request,HttpServletResponse response)throws Exception
{
 // 列表控件的TableId值
 String tableId = "SearchMessageExList";
 //获取分页信息
 ILimitUtil limitUtil = new ExLimitUtil();
 LimitInfo limit = limitUtil.getLimitInfo(request, tableId, 10);
 getRequestPageNo(request,limit);//翻页代码
 //如果不进行分页， 只做条件查询时，limit使用如下方式获取即可:
 // LimitInfo limit = new LimitInfo();
 //自定义条件
 //limit.addFilterProperty(HqlProperty.getEq("属性名称", 值【必须与属性类型匹配】));
 //状态保留
 //limit = this.limitStateStore(request, limit, mapping);
 //查询
 List list = searchMessageService.list(limit, "");
 //设置分页信息
 limitUtil.setLimitInfo(request, limit);
 //保存数据
 request.setAttribute("list", list);
 request.setAttribute("limit", limit);
 return "searchMessage/SearchMessageList";
}

/**
 *  <p>
 *  <b>业务处理描述</b>
 *  <ul>
 *  <li>可见性原因：需要被其他应用调用</li>
 *  <li>目的：初始化方法</li>
 *  <li>适用的前提条件</li>
 *  <li>后置条件：</li>
 *  <li>例外处理：无 </li>
 *  <li>已知问题：</li>
 *  <li>调用的例子：</li>
 *  </ul>
 *  </p>
 * 
 *  @param mapping:beforeAddSearchMessage
 *  @param request:
 *  HttpServletRequest
 *  @return ActionForward
 *  @exception Exception
*/
@RequestMapping(value = "/beforeAddSearchMessage")
public String beforeAddSearchMessage(SearchMessageDTO searchMessageDTO ,HttpServletRequest request,HttpServletResponse response)throws Exception
{
  return "searchMessage/SearchMessageAdd";
}

/**
 *  <p>
 *  <b>业务处理描述</b>
 *  <ul>
 *  <li>可见性原因：需要被其他应用调用</li>
 *  <li>目的：初始化方法</li>
 *  <li>适用的前提条件</li>
 *  <li>后置条件：</li>
 *  <li>例外处理：无 </li>
 *  <li>已知问题：</li>
 *  <li>调用的例子：</li>
 *  </ul>
 *  </p>
 * 
 *  @param mapping:ActionMapping
 *  @param request:
 *  HttpServletRequest
 *  @return ActionForward
 *  @exception Exception
*/
@RequestMapping(value = "/addSearchMessage")
public String  addSearchMessage(SearchMessageDTO searchMessageDTO ,HttpServletRequest request,HttpServletResponse response)throws Exception
{
  this.searchMessageService.saveTX(searchMessageDTO);
    return "searchMessage/SearchMessageList";
}

/**
 *  <p>
 *  <b>业务处理描述</b>
 *  <ul>
 *  <li>可见性原因：需要被其他应用调用</li>
 *  <li>目的：初始化方法</li>
 *  <li>适用的前提条件</li>
 *  <li>后置条件：</li>
 *  <li>例外处理：无 </li>
 *  <li>已知问题：</li>
 *  <li>调用的例子：</li>
 *  </ul>
 *  </p>
 * 
 *  @param mapping:ActionMapping
 *  @param request:
 *   HttpServletRequest
 *  @return ActionForward
 *  @exception Exception
*/
@RequestMapping(value = "/beforeEditSearchMessage")
public String  beforeEditSearchMessage(SearchMessageDTO searchMessageDTO ,HttpServletRequest request,HttpServletResponse response)throws Exception
{
  String id = request.getParameter("id");
  // 验证方法， 如果为null或者为空则直接返回异常
  Validate.notBlank(id, "common", "errorparameter");
  SearchMessageDTO m = this.searchMessageService.get(id);
  request.setAttribute("m", m);
  return "searchMessage/SearchMessageEdit";
}

/**
 *  <p>
 *  <b>业务处理描述</b>
 *  <ul>
 *  <li>可见性原因：需要被其他应用调用</li>
 *  <li>目的：初始化方法</li>
 *  <li>适用的前提条件</li>
 *  <li>后置条件：</li>
 *  <li>例外处理：无 </li>
 *  <li>已知问题：</li>
 *  <li>调用的例子：</li>
 *  </ul>
 *  </p>
 * 
 *  @param mapping:ActionMapping
 *  @param request:
 *  HttpServletRequest
 *  @return ActionForward
 *  @exception Exception
*/
@RequestMapping(value = "/editSearchMessage")
public String  editSearchMessage(SearchMessageDTO searchMessageDTO ,HttpServletRequest request,HttpServletResponse response)throws Exception
{
 this.searchMessageService.updateTX(searchMessageDTO);
 return "searchMessage/SearchMessageList";
}

/**
 *  <p>
 *  <b>业务处理描述</b>
 *  <ul>
 *  <li>可见性原因：需要被其他应用调用</li>
 *  <li>目的：初始化方法</li>
 *  <li>适用的前提条件</li>
 *  <li>后置条件：</li>
 *  <li>例外处理：无 </li>
 *  <li>已知问题：</li>
 *  <li>调用的例子：</li>
 *  </ul>
 *  </p>
 * 
 *  @param mapping:ActionMapping
 *  @param request:
 *  HttpServletRequest
 *  @return ActionForward
 *  @exception Exception
*/
@RequestMapping(value = "/detailSearchMessage")
public String detailSearchMessage(SearchMessageDTO searchMessageDTO ,HttpServletRequest request,HttpServletResponse response)throws Exception
{
 String id = request.getParameter("id");
 // 验证方法， 如果为null或者为空则直接返回异常
 Validate.notBlank(id, "common", "errorparameter");
 request.setAttribute("m",  this.searchMessageService.get(id));
 return "searchMessage/SearchMessageView";
}


/**
 *  <p>
 *  <b>业务处理描述</b>
 *  <ul>
 *  <li>可见性原因：需要被其他应用调用</li>
 *  <li>目的：初始化方法</li>
 *  <li>适用的前提条件</li>
 *  <li>后置条件：</li>
 *  <li>例外处理：无 </li>
 *  <li>已知问题：</li>
 *  <li>调用的例子：</li>
 *  </ul>
 *  </p>
 * 
 *  @param mapping:ActionMapping
 *  @param request:
 *  HttpServletRequest
 *  @return ActionForward
 *  @exception Exception
*/
@RequestMapping(value = "/deleteSearchMessage")
public String deleteSearchMessage(SearchMessageDTO searchMessageDTO ,HttpServletRequest request,HttpServletResponse response)throws Exception
 {
  String ids =  request.getParameter("ids");
  //验证方法， 如果为null或者为空则直接返回异常
  Validate.notBlank(ids, "common", "errorparameter");
  this.searchMessageService.deleteTX(ids);
  return "searchMessage/SearchMessageList";
}
 

}
