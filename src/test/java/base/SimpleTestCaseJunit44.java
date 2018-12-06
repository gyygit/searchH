package base;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class SimpleTestCaseJunit44 {

	@BeforeClass
	public static void beforeClass1() {
		System.out.println("@beforeClass1");
	}

	@Before
	public void before1() throws Exception {
		System.out.println("@before1");
	}

	@Test
	public void testAdd() {
		System.out.println(1);
	}

	@Test
	public void testSubstract() {
		System.out.println(2);
	}

	@Ignore("Multiply() Not yet implemented")
	@Test
	public void testMultiply() {
		System.out.println(3);
	}

	@Test
	public void testDivide() {
		System.out.println(4);
	}

	@Test(timeout = 1000)
	public void testSquareRoot() {
		System.out.println(5);
	}

	@Test
	public void divideByZero() {
		System.out.println(6);
	}

	@After
	public void after1() {
		System.out.println("@after1");
	}

	@AfterClass
	public static void afterClass1() {
		System.out.println("@afterClass1");
	}

}
