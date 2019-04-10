package ITSecurity.BigInteger;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ShiftLeftTests {
	private static String testFilePath = "src/test/java/ITSecurity/BigInteger/test_files/Shift-Left-Tests.txt";

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
			Object[] arr = { test.title, test.size, test.map.get("a"), test.map.get("b"), test.map.get("c"),
					test.map.get("d"), test.map.get("e"), test.map.get("f"), test.map.get("g"), test.map.get("h"), };
			objects.add(arr);
		}
		return objects;
	}

	public ShiftLeftTests(String title, int size, String a, String b, String c, String d, String e, String f, String g,
			String h) {
		this.title = title;
		this.size = size;
		this.values = new String[] { a, b, c, d, e, f, g, h };
	}

	private void testShift(int count) {
		BigInt a = new BigInt(true, 0, this.size / Cell.CELL_BASE);
		a.fromHexString(this.values[0]);
		a.shiftLeft(count);
		assertEquals(this.values[count], a.toHexString(this.size / 4));
	}

	@Test
	public void shiftBy1() {
		this.testShift(1);
	}

	@Test
	public void shiftBy2() {
		this.testShift(2);
	}

	@Test
	public void shiftBy3() {
		this.testShift(3);
	}

	@Test
	public void shiftBy4() {
		this.testShift(4);
	}

	@Test
	public void shiftBy5() {
		this.testShift(5);
	}

	@Test
	public void shiftBy6() {
		this.testShift(6);
	}

	@Test
	public void shiftBy7() {
		this.testShift(7);
	}
}
