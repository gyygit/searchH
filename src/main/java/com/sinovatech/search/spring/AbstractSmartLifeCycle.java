package com.sinovatech.search.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;

/**
 * 抽象生命周期类，凡继承本类的Spring Bean可在容器启动时自动加载并初始化<br>
 * 可重写getPhase方法来标识加载顺序，值越小越优先加载，默认值为0<br>
 * 
 * @author 陈杰
 * 
 */
public abstract class AbstractSmartLifeCycle implements SmartLifecycle {
	private static Logger logger = LoggerFactory
			.getLogger(AbstractSmartLifeCycle.class);
	private boolean isRunning = false;

	public boolean isAutoStartup() {
		return true;
	}

	public void stop(Runnable callback) {
		callback.run();
		isRunning = false;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void start() {
		isRunning = true;
		logger.info(getClass() + "==>已初始化!!");
	}

	public void stop() {
		isRunning = false;
	}

	public int getPhase() {
		return 0;
	}

}
