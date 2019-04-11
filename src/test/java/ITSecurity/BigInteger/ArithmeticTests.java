package ITSecurity.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
	private final String[] operationsHex;
	private final String[] operationsDec;

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
			String minus, String D, String times, String E, String slash, String F, String percent, String G) {
		this.title = title;
		this.size = size;
		this.hexOperands = new String[] { a, b };
		this.decOperands = new String[] { A, B };
		this.operationsHex = new String[] { plus, minus, times, slash, percent };
		this.operationsDec = new String[] { C, D, E, F, G };
	}

	private BigInt hexToBigInt(String hexString) {
		BigInt a = new BigInt(true, 0, this.size / Cell.CELL_BASE);
		a.fromHexString(hexString);
		a.reduce();
		return a;
	}

	@Test
	public void addHex() {
		BigInt a = this.hexToBigInt(this.hexOperands[0]);
		BigInt b = this.hexToBigInt(this.hexOperands[1]);
	}
}