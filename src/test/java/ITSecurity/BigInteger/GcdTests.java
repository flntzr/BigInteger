package ITSecurity.BigInteger;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class GcdTests {
	private static String testFilePath = "src/test/java/ITSecurity/BigInteger/test_files/gcd-Tests.txt";

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
			Object[] arr = { test.title, Integer.parseInt(test.map.get("s")), test.map.get("a"), test.map.get("A"),
					test.map.get("b"), test.map.get("B"), test.map.get("g"), test.map.get("G") };
			objects.add(arr);
		}
		return objects;
	}

	public GcdTests(String title, int size, String a, String decA, String b, String decB, String g, String decG) {
		this.title = title;
		this.size = size;
		this.hexVals = new String[] { a, b, g };
		this.decVals = new String[] { decA, decB, decG };
	}

	@Test
	public void testDecGcd() {
		BigInt a = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		a.fromDecString(this.decVals[0]);
		BigInt b = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		b.fromDecString(this.decVals[1]);
		BigInt g = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		g.fromDecString(this.decVals[2]);
		a.binGcd(b);
		assertEquals(g, a);
	}
	
	@Test
	public void testHexGcd() {
		BigInt a = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		a.fromHexString(this.hexVals[0]);
		BigInt b = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		b.fromHexString(this.hexVals[1]);
		BigInt g = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		g.fromHexString(this.hexVals[2]);
		a.binGcd(b);
		assertEquals(g, a);
	}

}
