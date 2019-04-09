package ITSecurity.BigInteger;

import java.io.File;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ShiftLeftTests extends TestCase {

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(ShiftLeftTests.class);
	}

	public void test() {
		TestFileReader reader = new TestFileReader("src/test/java/ITSecurity/BigInteger/test_files/Shift-Left-Tests.txt");
		List<ImportedTest> tests = reader.read();
	}
}
