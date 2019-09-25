package ITSecurity.BigInteger;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class EgcdTests {
	private static String testFilePath = "src/test/java/ITSecurity/BigInteger/test_files/egcd-Tests.txt";

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
					test.map.get("b"), test.map.get("B"), test.map.get("g"), test.map.get("G"), test.map.get("u"),
					test.map.get("U"), test.map.get("v"), test.map.get("V") };
			objects.add(arr);
		}
		return objects;
	}

	public EgcdTests(String title, int size, String a, String decA, String b, String decB, String g, String decG,
			String u, String decU, String v, String decV) {
		this.title = title;
		this.size = size;
		this.hexVals = new String[] { a, b, g, u, v };
		this.decVals = new String[] { decA, decB, decG, decU, decV };
	}

	@Test
	public void testDecGcd() {
		BigInt a = new BigInt(true, 0, this.size);
		a.fromDecString(this.decVals[0]);
		BigInt b = new BigInt(true, 0, this.size);
		b.fromDecString(this.decVals[1]);
		BigInt g = new BigInt(true, 0, this.size);
		g.fromDecString(this.decVals[2]);
		BigInt u = new BigInt(true, 0, this.size);
		u.fromDecString(this.decVals[3]);
		BigInt v = new BigInt(true, 0, this.size);
		v.fromDecString(this.decVals[4]);
		BigInt[] egcd = BigIntUtils.egcd(a, b);
		assertEquals(g, egcd[0]);
		assertEquals(u, egcd[1]);
		assertEquals(v, egcd[2]);
	}

}
