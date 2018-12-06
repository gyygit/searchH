package com.sinovatech.search.utils;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * @功能:多节点重复执行任务帮助类
 * @注意:此工具类依赖于ShardedJedisPool  redis
 * @author wuzhu
 */
public class NodeRepeatTaskUtil {

	// 任务名词 目的防止不同的定时任务出现重名
	private static Set<String> taskNameSet = new HashSet<String>();

	static {
		// 特价机票低价日历ftp文件解析任务
		taskNameSet.add("SPECIAL_CALENDAR_TICKET_TASK");

		// 异常退款信息更新到订单中心任务
		taskNameSet.add("REFUND_EXCEPTION_TICKET_TASK");
	}

	/**
	 * @功能:是否继续执行任务 本方法适用定时周期>=2s的定时任务
	 * @param taskName
	 *            任务名称
	 * @param executionCycle
	 *            任务执行周期(秒) 注意:执行周期为任务真实的周期如果乱填写会影响任务的正常执行
	 * @return
	 */
	public static boolean isContinueExecute(String taskName, long executionCycle) {
		// 无效的参数或未注册的任务名称
		if (StringUtils.isEmpty(taskName) || executionCycle <= 0l
				|| !taskNameSet.contains(taskName)) {
			return false;
		}
		ShardedJedisPool jedisPool = null;
		ShardedJedis jedis = null;
		try {
			jedisPool = SpringContextHolder.getBean(ShardedJedisPool.class);
			jedis = jedisPool.getResource();
			// 任务的执行次数 incr函数每次执行默认+1 如果key不存在先创建key=0 然后再加1
			long executeCount = jedis.incr(taskName);
			// 判断是否设置了失效时间
			long ttl = jedis.ttl(taskName);
			// 未设置失效时间则设置失效时间
			if (ttl == -1l) {
				// 设置失效时间 失效默认为周期/2
				jedis.expire(taskName, (int) executionCycle / 2);
			}
			if (executeCount == 1l) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (jedisPool != null && jedis != null) {
				jedisPool.returnResource(jedis);
			}
		}
	}
}
