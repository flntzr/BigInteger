package ITSecurity.BigInteger;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class FermatPseudoPrimeTests {
	private static String testFilePath = "src/test/java/ITSecurity/BigInteger/test_files/FermatPseudoPrime-Tests.txt";

	private String title;
	private int size;
	private String baseHex;
	private String baseDec;
	private String pseudoPrimeHex;
	private String pseudoPrimeDec;

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
					test.map.get("p"), test.map.get("P") };
			objects.add(arr);
		}
		return objects;
	}

	public FermatPseudoPrimeTests(String title, int size, String baseHex, String baseDec, String pseudoPrimeHex,
			String pseudoPrimeDec) {
		this.title = title;
		this.size = size;
		this.baseHex = baseHex;
		this.baseDec = baseDec;
		this.pseudoPrimeHex = pseudoPrimeHex;
		this.pseudoPrimeDec = pseudoPrimeDec;
	}

	private void testPrimeFermat(BigInt base, BigInt prime, boolean expected) {
		boolean result = prime.isPrimeFermat(base);
		assertEquals(expected, result);
	}

	@Test
	public void fermatHex() {
		BigInt base = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		BigInt pseudoPrime = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		base.fromHexString(this.baseHex);
		pseudoPrime.fromHexString(this.pseudoPrimeHex);
		this.testPrimeFermat(base, pseudoPrime, true);
	}
	
	@Test
	public void fermatDec() {
		BigInt base = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		BigInt pseudoPrime = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		base.fromDecString(this.baseDec);
		pseudoPrime.fromDecString(this.pseudoPrimeDec);
		this.testPrimeFermat(base, pseudoPrime, true);
	}

}
