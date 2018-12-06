package com.sinovatech.search.utils;

import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * 拓展PropertyPlaceholderConfigurer功能<br>
 * 使其能直接把属性文件中的基本类型自动注入其他bean中 只支持String Boolean Integer
 */
public class AnnotationPropertyPlaceholderConfigurer extends
		PropertyPlaceholderConfigurer {
	protected final Log logger = LogFactory
			.getLog(AnnotationPropertyPlaceholderConfigurer.class);

	protected void processProperties(
			ConfigurableListableBeanFactory beanFactoryToProcess,
			Properties props) throws BeansException {
		super.processProperties(beanFactoryToProcess, props);
		logger.info(beanFactoryToProcess.getClass());
		if (beanFactoryToProcess instanceof BeanDefinitionRegistry) {
			BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactoryToProcess;
			Enumeration<Object> keys = props.keys();// 得到属性文件
			while (keys.hasMoreElements()) {// 从属性文件中得到键和值
				String key = (String) keys.nextElement();
				String value = props.getProperty(key).trim();
				RootBeanDefinition rbd = new RootBeanDefinition();
				rbd.setAbstract(false);
				rbd.setLazyInit(true);
				rbd.setAutowireCandidate(true);// 是否可以自动注入到其他bean
				ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
				rbd.setConstructorArgumentValues(constructorArgumentValues);// 设置构造参数
				if (logger.isDebugEnabled())
					logger.debug("register placehold key " + key + " " + value);
				if (StringUtils.isNotBlank(value) && value.length() < 11
						&& StringUtils.isNumeric(value)) {// 整型的支持
					Integer intValue = Integer.parseInt(value);
					constructorArgumentValues.addIndexedArgumentValue(0,
							intValue);// 把参数加入到构造参数列表
					rbd.setBeanClass(Integer.class);// 设置类型
				} else if (value.toLowerCase().equals("false")
						|| value.toLowerCase().equals("true")) {// 布尔型的支持
					Boolean boolValue = Boolean.parseBoolean(value);
					constructorArgumentValues.addIndexedArgumentValue(0,
							boolValue);// 把参数加入到构造参数列表
					rbd.setBeanClass(Boolean.class);// 设置类型
				} else {// 字符串型的支持
					constructorArgumentValues.addIndexedArgumentValue(0, value);
					rbd.setBeanClass(String.class);// 设置类型
				}
				registry.registerBeanDefinition(key, rbd);
			}
		}
	}

}
