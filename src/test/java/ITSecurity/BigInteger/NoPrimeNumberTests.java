package ITSecurity.BigInteger;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class NoPrimeNumberTests {
	private static String testFilePath = "src/test/java/ITSecurity/BigInteger/test_files/NoPrimeNumber-Tests.txt";

	private String title;
	private int size;
	private String baseHex;
	private String baseDec;
	private String pseudoPrimeHex;
	private String pseudoPrimeDec;
	private boolean isPrimeFermat;
	private boolean isPrimeEuler;

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
					test.map.get("p"), test.map.get("P"), test.map.get("f").equals("true"),
					test.map.get("e").equals("true") };
			objects.add(arr);
		}
		return objects;
	}

	public NoPrimeNumberTests(String title, int size, String baseHex, String baseDec, String pseudoPrimeHex,
			String pseudoPrimeDec, boolean isPrimeFermat, boolean isPrimeEuler) {
		this.title = title;
		this.size = size;
		this.baseHex = baseHex;
		this.baseDec = baseDec;
		this.pseudoPrimeHex = pseudoPrimeHex;
		this.pseudoPrimeDec = pseudoPrimeDec;
		this.isPrimeFermat = isPrimeFermat;
		this.isPrimeEuler = isPrimeEuler;
	}

	private void testPrimeFermat(BigInt base, BigInt prime, boolean expected) {
		boolean result = prime.isPrimeFermat(base);
		assertEquals(expected, result);
	}
	
	private void testPrimeEuler(BigInt base, BigInt prime, boolean expected) {
		boolean result = prime.isPrimeEuler(base);
		assertEquals(expected, result);
	}
	
	@Test
	public void fermatHex() {
		BigInt base = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		BigInt pseudoPrime = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		base.fromHexString(this.baseHex);
		pseudoPrime.fromHexString(this.pseudoPrimeHex);
		this.testPrimeFermat(base, pseudoPrime, this.isPrimeFermat);
	}
	
	@Test
	public void fermatDec() {
		BigInt base = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		BigInt pseudoPrime = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		base.fromDecString(this.baseDec);
		pseudoPrime.fromDecString(this.pseudoPrimeDec);
		this.testPrimeFermat(base, pseudoPrime, this.isPrimeFermat);
	}

	@Test
	public void eulerHex() {
		BigInt base = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		BigInt pseudoPrime = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		base.fromHexString(this.baseHex);
		pseudoPrime.fromHexString(this.pseudoPrimeHex);
		this.testPrimeEuler(base, pseudoPrime, this.isPrimeEuler);
	}

	@Test
	public void eulerDec() {
		BigInt base = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		BigInt pseudoPrime = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		base.fromDecString(this.baseDec);
		pseudoPrime.fromDecString(this.pseudoPrimeDec);
		this.testPrimeEuler(base, pseudoPrime, this.isPrimeEuler);
	}

}
