package ITSecurity.BigInteger;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ConvertHexTests {
	private static String testFilePath = "src/test/java/ITSecurity/BigInteger/test_files/Convert-Hex-Tests.txt";
	private static int maxSize = 1360 / Cell.CELL_BASE;

	private String title;
	private int size;
	private String dec;
	private String hex;
	private String oct;

	@Parameterized.Parameters(name = "{0}")
	public static Collection<Object[]> data() {
		TestFileReader reader = new TestFileReader(testFilePath);
		List<ImportedTestCase> tests = reader.read();
		return convertTestMap(tests);
	}

	public static Collection<Object[]> convertTestMap(List<ImportedTestCase> tests) {
		List<Object[]> objects = new ArrayList<Object[]>();
		for (ImportedTestCase test : tests) {
			Object[] arr = { test.title, test.map.get("d"), test.map.get("h"), test.map.get("o") };
			objects.add(arr);
		}
		return objects;
	}

	public ConvertHexTests(String title, String d, String h, String o) {
		this.title = title;
		this.dec = d;
		this.hex = h;
		this.oct = o;
		this.size = maxSize;
	}

	@Test
	public void testDecimal() {
		// test the decimal. Use the hex value as reference.
		BigInt a = new BigInt(true, 0, this.size / Cell.CELL_BASE);
		a.fromHexString(this.hex);
		BigInt b = new BigInt(true, 0, this.size / Cell.CELL_BASE);
		b.fromDecString(this.dec);
		assertEquals(a, b);
	}

	@Test
	public void testOctal() {
		// test the octal. Use the hex value as reference.
		BigInt a = new BigInt(true, 0, this.size / Cell.CELL_BASE);
		a.fromHexString(this.hex);
		BigInt b = new BigInt(true, 0, this.size / Cell.CELL_BASE);
		b.fromOctString(this.oct);
		a.reduce();
		b.reduce();
		assertEquals(a, b);
	}

}
