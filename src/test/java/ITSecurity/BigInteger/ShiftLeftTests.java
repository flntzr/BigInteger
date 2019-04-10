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

	@Test
	public void testBitShifts() {
		BigInt a = new BigInt(true, 0, this.size / Cell.CELL_BASE);
		a.fromHexString(this.values[0]);
		String[] shiftedHexResults = new String[7];
		for (int i = 0; i < 7; i++) {
			BigInt shifted = new BigInt(a, this.size / Cell.CELL_BASE);
			shifted.shiftLeft(i + 1);
			String hexResult = shifted.toHexString(this.size / 4);
			shiftedHexResults[i] = hexResult;
		}
		assertEquals(this.values[1], shiftedHexResults[0]);
		assertEquals(this.values[2], shiftedHexResults[1]);
		assertEquals(this.values[3], shiftedHexResults[2]);
		assertEquals(this.values[4], shiftedHexResults[3]);
		assertEquals(this.values[5], shiftedHexResults[4]);
		assertEquals(this.values[6], shiftedHexResults[5]);
		assertEquals(this.values[7], shiftedHexResults[6]);
	}
}
