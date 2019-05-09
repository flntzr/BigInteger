package ITSecurity.BigInteger;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class CompareTests {
	private static String testFilePath = "src/test/java/ITSecurity/BigInteger/test_files/Compare-Tests.txt";
	private String title;
	private int size;
	private String hexStr1;
	private String decStr1;
	private String hexStr2;
	private String decStr2;
	private int comparisonResult1;
	private int comparisonResult2;

	@Parameterized.Parameters(name = "{0}")
	public static Collection<Object[]> data() {
		TestFileReader reader = new TestFileReader(testFilePath);
		List<ImportedTestCase> tests = reader.read();
		return convertTestMap(tests);
	}

	public static Collection<Object[]> convertTestMap(List<ImportedTestCase> tests) {
		List<Object[]> objects = new ArrayList<Object[]>();
		for (ImportedTestCase test : tests) {
			Object[] arr = { test.title, Integer.parseInt(test.map.get("s")), test.map.get("a"), test.map.get("A"),
					test.map.get("b"), test.map.get("B"), Integer.parseInt(test.map.get("c")),
					Integer.parseInt(test.map.get("d")) };
			objects.add(arr);
		}
		return objects;
	}

	public CompareTests(String title, int size, String h1, String d1, String h2, String d2, int cmp1, int cmp2) {
		this.title = title;
		this.size = size;
		this.hexStr1 = h1;
		this.decStr1 = d1;
		this.hexStr2 = h2;
		this.decStr2 = d2;
		this.comparisonResult1 = cmp1;
		this.comparisonResult2 = cmp2;
	}

	@Test
	public void compareHex1Hex2() {
		BigInt a = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		a.fromHexString(this.hexStr1);
		BigInt b = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		b.fromHexString(this.hexStr2);
		assertEquals(a.compareTo(b), comparisonResult1);
	}
	
	@Test
	public void compareHex2Hex1() {
		BigInt a = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		a.fromHexString(this.hexStr1);
		BigInt b = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		b.fromHexString(this.hexStr2);
		assertEquals(b.compareTo(a), comparisonResult2);
	}
	
	@Test
	public void compareDec1Dec2() {
		BigInt a = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		a.fromDecString(this.decStr1);
		BigInt b = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		b.fromDecString(this.decStr2);
		assertEquals(a.compareTo(b), comparisonResult1);
	}
	
	@Test
	public void compareDec2Dec1() {
		BigInt a = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		a.fromDecString(this.decStr1);
		BigInt b = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		b.fromDecString(this.decStr2);
		assertEquals(b.compareTo(a), comparisonResult2);
	}
}
