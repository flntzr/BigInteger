package ITSecurity.BigInteger;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class Mul10Tests {
	private static String testFilePath = "src/test/java/ITSecurity/BigInteger/test_files/Mul10-Tests.txt";

	private String title;
	private int size;
	private String[] values;

	@Parameterized.Parameters(name = "{0}")
	public static Collection<Object[]> data() {
		TestFileReader reader = new TestFileReader(testFilePath);
		List<ImportedTestCase> tests = reader.read();
		return convertTestMap(tests);
	}

	public static Collection<Object[]> convertTestMap(List<ImportedTestCase> tests) {
		List<Object[]> objects = new ArrayList<Object[]>();
		for (ImportedTestCase test : tests) {
			Object[] arr = { test.title, Integer.parseInt(test.map.get("s")), test.map.get("a"), test.map.get("b"),
					test.map.get("c"), test.map.get("d"), test.map.get("e"), test.map.get("f"), test.map.get("g"),
					test.map.get("h"), };
			objects.add(arr);
		}
		return objects;
	}

	public Mul10Tests(String title, int size, String a, String b, String c, String d, String e, String f, String g,
			String h) {
		this.title = title;
		this.size = size;
		this.values = new String[] { a, b, c, d, e, f, g, h };
	}

	private void testMul(int count) {
		BigInt a = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		a.fromHexString(this.values[0]);
		a.reduce();
		for (int i = 0; i < count; i++) {
			a.mul10();
		}
		assertEquals(this.values[count], a.toHexString(this.size / 4));
	}

	@Test
	public void mulBy10Exp1() {
		this.testMul(1);
	}

	@Test
	public void mulBy10Exp2() {
		this.testMul(2);
	}

	@Test
	public void mulBy10Exp3() {
		this.testMul(3);
	}

	@Test
	public void mulBy10Exp4() {
		this.testMul(4);
	}

	@Test
	public void mulBy10Exp5() {
		this.testMul(5);
	}

	@Test
	public void mulBy10Exp6() {
		this.testMul(6);
	}

	@Test
	public void mulBy10Exp7() {
		this.testMul(7);
	}

}
