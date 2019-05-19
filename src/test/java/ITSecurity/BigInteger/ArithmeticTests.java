package ITSecurity.BigInteger;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ArithmeticTests {
	private static String testFilePath = "src/test/java/ITSecurity/BigInteger/test_files/Arithmetic-Tests.txt";
	
	private String title;
	private int size;
	private final String[] hexOperands;
	private final String[] decOperands;
	private final String[] resultsHex;
	private final String[] resultsDec;

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
					test.map.get("b"), test.map.get("B"), test.map.get("+"), test.map.get("C"), test.map.get("-"),
					test.map.get("D"), test.map.get("*"), test.map.get("E"), test.map.get("/"), test.map.get("F"),
					test.map.get("%"), test.map.get("G") };
			objects.add(arr);
		}
		return objects;
	}

	public ArithmeticTests(String title, int size, String a, String A, String b, String B, String plus, String C,
			String minus, String D, String times, String E, String divide, String F, String percent, String G) {
		this.title = title;
		this.size = size;
		this.hexOperands = new String[] { a, b };
		this.decOperands = new String[] { A, B };
		this.resultsHex = new String[] { plus, minus, times, divide, percent };
		this.resultsDec = new String[] { C, D, E, F, G };
	}

	private BigInt hexToBigInt(String hexString) {
		BigInt a = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		a.fromHexString(hexString);
		a.reduce();
		return a;
	}
	
	private BigInt decToBigInt(String decString) {
		BigInt a = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		a.fromDecString(decString);
		a.reduce();
		return a;
	}

	@Test
	public void addHex() {
		BigInt a = this.hexToBigInt(this.hexOperands[0]);
		BigInt b = this.hexToBigInt(this.hexOperands[1]);
		BigInt expected = this.hexToBigInt(this.resultsHex[0]);
		BigInt result = BigIntUtils.add(a, b);
		assertEquals(expected, result);
	}

	@Test
	public void subHex() {
		BigInt a = this.hexToBigInt(this.hexOperands[0]);
		BigInt b = this.hexToBigInt(this.hexOperands[1]);
		BigInt expected = this.hexToBigInt(this.resultsHex[1]);
		BigInt result = BigIntUtils.sub(a, b);
		assertEquals(expected, result);
	}

	@Test
	public void mulHex() {
		BigInt a = this.hexToBigInt(this.hexOperands[0]);
		BigInt b = this.hexToBigInt(this.hexOperands[1]);
		BigInt expected = this.hexToBigInt(this.resultsHex[2]);
		a.mul(b, a.positive == b.positive);
		assertEquals(expected, a);
	}
	
	@Test
	public void addDec() {
		BigInt a = this.decToBigInt(this.decOperands[0]);
		BigInt b = this.decToBigInt(this.decOperands[1]);
		BigInt expected = this.decToBigInt(this.resultsDec[0]);
		BigInt result = BigIntUtils.add(a, b);
		assertEquals(expected, result);
	}

	@Test
	public void subDec() {
		BigInt a = this.decToBigInt(this.decOperands[0]);
		BigInt b = this.decToBigInt(this.decOperands[1]);
		BigInt expected = this.decToBigInt(this.resultsDec[1]);
		BigInt result = BigIntUtils.sub(a, b);
		assertEquals(expected, result);
	}

	@Test
	public void mulDec() {
		BigInt a = this.decToBigInt(this.decOperands[0]);
		BigInt b = this.decToBigInt(this.decOperands[1]);
		BigInt expected = this.decToBigInt(this.resultsDec[2]);
		a.mul(b, a.positive == b.positive);
		assertEquals(expected, a);
	}
	
	@Test
	public void divModDec() {
		BigInt a = this.decToBigInt(this.decOperands[0]);
		BigInt b = this.decToBigInt(this.decOperands[1]);
		BigInt expectedDiv = this.decToBigInt(this.resultsDec[3]);
		BigInt expectedR = this.decToBigInt(this.resultsDec[4]);
		try {
			BigInt r = BigIntUtils.divMod(a, b);			
			assertEquals(expectedDiv, a);
			assertEquals(r, expectedR);
		} catch (RuntimeException e) {
			assertEquals("Cannot divide by 0", e.getMessage());
			assertEquals(b.spart, 0);
		}
	}
	
	@Test
	public void divModHex() {
		BigInt a = this.hexToBigInt(this.hexOperands[0]);
		BigInt b = this.hexToBigInt(this.hexOperands[1]);
		BigInt expectedDiv = this.hexToBigInt(this.resultsHex[3]);
		BigInt expectedR = this.hexToBigInt(this.resultsHex[4]);
		try {
			BigInt r = BigIntUtils.divMod(a, b);			
			assertEquals(expectedDiv, a);
			assertEquals(r, expectedR);
		} catch (RuntimeException e) {
			assertEquals("Cannot divide by 0", e.getMessage());
			assertEquals(b.spart, 0);
		}
	}
}
