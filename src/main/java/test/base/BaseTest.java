package test.base;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sinovatech.common.util.MD5;

/**
 * @author liuzhenquan@sinovatech.com
 * @since 2012-06-04
 */
public class BaseTest extends TestCase {
	
	public static String[] configLocations = { "file:D:/gitwork/sc/sc/searchH/src/main/webapp/WEB-INF/classes/spring/applicationContext-spring.xml" };
	public static ApplicationContext context = new ClassPathXmlApplicationContext(
			configLocations);

	public void testContext() {
		assertNotNull("ApplicationContext is null!", context);
	}

	/**
	 * test
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		MD5 md5 = new MD5();
		System.out.println(md5.getMD5ofStr("buyer01"));
	}

}
