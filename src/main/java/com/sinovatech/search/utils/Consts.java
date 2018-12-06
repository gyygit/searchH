package com.sinovatech.search.utils;

public class Consts {

	/**
	 * 用户
	 */
	public static final String SESSION_USER = "loginUser";
	/**
	 * 用户
	 */
	public static final String SESSION_USER_INFO = "userInfo";
	/**
	 * 用户-web版
	 */
	public static final String SESSION_USER_WEB = "loginUserWeb";
	/**
	 * 用户信息-web版
	 */
	public static final String SESSION_USER_INFO_WEB = "userInfoWeb";
	/**
	 * cookies JUT
	 */
	public static final String JUT = "JUT";
	/**
	 * 登录前的地址
	 */
	public static final String JUMPURI = "jumpUri";
	/**
	 * 分页每页显示条数
	 */
	public static final int ROWS_PER_PAGE = 10;

	/**
	 * 第一次使用wlm
	 */
	public static final String NERVER_USE="0";
	/**
	 * 不是第一次使用wlm
	 */
	public static final String ALREADY_USE="1";
	
	/**
	 * 渠道标识
	 */
	public static final String LOGINCHANNELWAP = "01";
	public static final String LOGINCHANNELWEB = "02";
	/**
	 * 收藏相关常量
	 * 
	 * @author Aron
	 * 
	 */
	public static final class WlmAdCollection {
		// 全部类型
		public static final String ALLTYPE = "allType";
		// 卡包
		public static final String CARDPACKAGE = "0";

		// 上架时间
		public static final String UPTIME = "upTime";

		// 广告类型
		public static final String CARD = "110000"; // 单号卡
		public static final String CONTRACT = "120000";// 合约机
		public static final String BARE = "170000";// 裸机
		public static final String ACCESSORY = "150000";// 配件
		public static final String PACKAGE = "140000";// 流量包
		public static final String INTERNETCARD = "130000";// 上网卡
		public static final String WIDE_BRAND = "160000";// 宽带

		// 返回成功结果码
		public static final String RETURN_OK = "0000";
		// 返回失败结果码
		public static final String RETURN_FAIL = "1111";
		// 返回异常编码
		public static final String SYS_ERROR = "9999";

	}

	public static final class WlmAccount {
		// 账户状态:0 正常,1冻结
		public static final String INCOME_STATE_OK = "1";
		public static final String INCOME_STATE_FROZEN = "2";

		public static final String ACCOUNT_TIME_LAST_ONE = "1M";
		public static final String ACCOUNT_TIME_LAST_TRI = "3M";
		public static final String ACCOUNT_TIME_LAST_SIX = "6M";

	}

	public static final class WlmOrder {
		// 00不可预算 01待预算 02已预算 03已结算 04 预算回退
		public static final String ORDER_STATE_CHECKOUT = "03";
		public static final String ORDER_STATE_BUDGET = "02";
		public static final String ORDER_STATE_INVALID = "04";

		public static final String ORDER_TIME_LAST_ONE = "1M";
		public static final String ORDER_TIME_LAST_TRI = "3M";
		public static final String ORDER_TIME_LAST_SIX = "6M";

	}

	public static final class addJingyan {
		public static final String GET_AD = "role1";// 每日领取广告
		public static final String INVENT_DAY_ADD = "role3";// 每日邀请-点击邀请好友按钮
		public static final String INVENT_REG_ADD = "role4";// 有邀请的好友注册
		public static final String LOGIN_ADD = "role5";// 每日登录
		public static final String ORDER_SUCCESS = "role2";//领取广告并成功下单

		public static final String INVENT_NAME = "INVFRIEND";
		public static final String REG_NAME = "INVITE";
		public static final String LOGIN_NAME = "SIGNIN";
		public static final String GET_AD_CODE = "ADVERT";

	}

	public static final class sendMsg {
		public static final String INDEXCODE = "wlm_template_types";
		public static final String CODE = "234600";
	}

}
