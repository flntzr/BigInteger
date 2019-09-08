package ITSecurity.BigInteger;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class PrimeNumberTests {
	private static String testFilePath = "src/test/java/ITSecurity/BigInteger/test_files/primeNumber-Tests.txt";

	private String title;
	private int size;
	private String[] baseHex;
	private String[] baseDec;
	private String primeHex;
	private String primeDec;
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
			String[] baseHexArr = { test.map.get("a"), test.map.get("a0"), test.map.get("a1"), test.map.get("a2"),
					test.map.get("a3"), test.map.get("a4"), test.map.get("a5"), test.map.get("a6"), test.map.get("a7"),
					test.map.get("a8"), test.map.get("a9"), test.map.get("a10") };
			String[] baseDecArr = { test.map.get("A"), test.map.get("A0"), test.map.get("A1"), test.map.get("A2"),
					test.map.get("A3"), test.map.get("A4"), test.map.get("A5"), test.map.get("A6"), test.map.get("A7"),
					test.map.get("A8"), test.map.get("A9"), test.map.get("A10") };
			Object[] arr = { test.title, Integer.parseInt(test.map.get("s")), baseHexArr, baseDecArr,
					test.map.get("p"), test.map.get("P"), test.map.get("f").equals("true"),
					test.map.get("e").equals("true") };
			objects.add(arr);
		}
		return objects;
	}

	public PrimeNumberTests(String title, int size, String[] baseHex, String[] baseDec, String primeHex,
			String primeDec, boolean isPrimeFermat, boolean isPrimeEuler) {
		this.title = title;
		this.size = size;
		this.baseHex = baseHex;
		this.baseDec = baseDec;
		this.primeHex = primeHex;
		this.primeDec = primeDec;
		this.isPrimeFermat = isPrimeFermat;
		this.isPrimeEuler = isPrimeEuler;
	}

	private void testPrimeFermat(BigInt[] bases, BigInt prime, boolean expected) {
		boolean result = prime.isPrimeFermat(bases);
		assertEquals(expected, result);
	}

	private void testPrimeEuler(BigInt[] bases, BigInt prime, boolean expected) {
		boolean result = prime.isPrimeEuler(bases);
		assertEquals(expected, result);
	}

	@Test
	public void fermatHex() {
		BigInt prime = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		prime.fromHexString(this.primeHex);
		BigInt[] bases = new BigInt[this.baseHex.length];
		for (int i = 0; i < bases.length; i++) {
			bases[i] = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
			bases[i].fromHexString(this.baseHex[i]);
		}
		this.testPrimeFermat(bases, prime, this.isPrimeFermat);
	}

	@Test
	public void fermatDec() {
		BigInt prime = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		prime.fromDecString(this.primeDec);
		BigInt[] bases = new BigInt[this.baseDec.length];
		for (int i = 0; i < bases.length; i++) {
			bases[i] = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
			bases[i].fromDecString(this.baseDec[i]);
		}
		this.testPrimeFermat(bases, prime, this.isPrimeFermat);
	}

	@Test
	public void eulerHex() {
		BigInt prime = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		prime.fromHexString(this.primeHex);
		BigInt[] bases = new BigInt[this.baseHex.length];
		for (int i = 0; i < bases.length; i++) {
			bases[i] = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
			bases[i].fromHexString(this.baseHex[i]);
		}
		this.testPrimeEuler(bases, prime, this.isPrimeEuler);
	}

	@Test
	public void eulerDec() {
		BigInt prime = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		prime.fromDecString(this.primeDec);
		BigInt[] bases = new BigInt[this.baseDec.length];
		for (int i = 0; i < bases.length; i++) {
			bases[i] = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
			bases[i].fromDecString(this.baseDec[i]);
		}
		this.testPrimeEuler(bases, prime, this.isPrimeEuler);
	}


}
