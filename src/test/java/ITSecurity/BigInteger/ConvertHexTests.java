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

	private String title;
	private String decStr;
	private String hexStr;
	private String octStr;

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
		this.decStr = d;
		this.hexStr = h;
		this.octStr = o;
	}

	@Test
	public void testDecimal() {
		// test the decimal. Use the hex value as reference.
		BigInt a = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		a.fromHexString(this.hexStr);
		BigInt b = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		b.fromDecString(this.decStr);
		assertEquals(a, b);
	}

	@Test
	public void testOctal() {
		// test the octal. Use the hex value as reference.
		BigInt a = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		a.fromHexString(this.hexStr);
		BigInt b = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		b.fromOctString(this.octStr);
		assertEquals(a, b);
	}

}
