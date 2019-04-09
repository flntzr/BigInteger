package ITSecurity.BigInteger;

import java.io.File;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ShiftLeftTests extends TestCase {

	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public ShiftLeftTests(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(ShiftLeftTests.class);
	}

//	@Test
	public void test() {
		System.out.println(new File(".").getAbsolutePath());
		TestFileReader reader = new TestFileReader("src/test/java/ITSecurity/BigInteger/test_files/Shift-Left-Tests.txt");
		List<ImportedTest> tests = reader.read();
	}
}
