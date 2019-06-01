package ITSecurity.BigInteger;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class PowerSmallTests {
	private static String testFilePath = "src/test/java/ITSecurity/BigInteger/test_files/Power-Small-Tests.txt";

	private String title;
	private int size;
	private String[] hexVals;
	private String[] decVals;

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
					test.map.get("h"), test.map.get("i"), test.map.get("A"), test.map.get("B"), test.map.get("C"),
					test.map.get("D"), test.map.get("E"), test.map.get("F"), test.map.get("G"), test.map.get("H"),
					test.map.get("I") };
			objects.add(arr);
		}
		return objects;
	}

	public PowerSmallTests(String title, int size, String a, String b, String c, String d, String e, String f, String g,
			String h, String i, String decA, String decB, String decC, String decD, String decE, String decF,
			String decG, String decH, String decI) {
		this.title = title;
		this.size = size;
		this.hexVals = new String[] { a, b, c, d, e, f, g, h, i };
		this.decVals = new String[] { decA, decB, decC, decD, decE, decF, decG, decH, decI };
	}

	private void testHexPowBy(int n) {
		BigInt a = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		BigInt bigN = new BigInt(true, n, BigInt.DEFAULT_BIG_INT_SIZE);
		a.fromHexString(this.hexVals[0]);
		a.pow(bigN);
		BigInt b = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		b.fromHexString(this.hexVals[n + 1]);
		assertEquals(b, a);
	}

	private void testDecPowBy(int n) {
		BigInt a = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		BigInt bigN = new BigInt(true, n, BigInt.DEFAULT_BIG_INT_SIZE);
		a.fromDecString(this.decVals[0]);
		a.pow(bigN);
		BigInt b = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		b.fromDecString(this.decVals[n + 1]);
		assertEquals(b, a);
	}

	@Test
	public void pow0() {
		this.testHexPowBy(0);
		this.testDecPowBy(0);
	}

	@Test
	public void pow1() {
		this.testHexPowBy(1);
		this.testDecPowBy(1);
	}

	@Test
	public void pow2() {
		this.testHexPowBy(2);
		this.testDecPowBy(2);
	}

	@Test
	public void pow3() {
		this.testHexPowBy(3);
		this.testDecPowBy(3);
	}

	@Test
	public void pow4() {
		this.testHexPowBy(4);
		this.testDecPowBy(4);
	}

	@Test
	public void pow5() {
		this.testHexPowBy(5);
		this.testDecPowBy(5);
	}

	@Test
	public void pow6() {
		this.testHexPowBy(6);
		this.testDecPowBy(6);
	}
	
	@Test
	public void pow7() {
		this.testHexPowBy(7);
		this.testDecPowBy(7);
	}
}
