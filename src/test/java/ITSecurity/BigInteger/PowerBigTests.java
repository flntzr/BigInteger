package ITSecurity.BigInteger;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class PowerBigTests {
	private static String testFilePath = "src/test/java/ITSecurity/BigInteger/test_files/Power-Big-Tests.txt";

	private String title;
	private int size;
	private String baseHex;
	private String powerHex;
	private String resultHex;
	private String baseDec;
	private String powerDec;
	private String resultDec;

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
					test.map.get("c"), test.map.get("A"), test.map.get("B"), test.map.get("C") };
			objects.add(arr);
		}
		return objects;
	}

	public PowerBigTests(String title, int size, String baseHex, String powerHex, String resultHex, String baseDec,
			String powerDec, String resultDec) {
		this.title = title;
		this.size = size;
		this.baseHex = baseHex;
		this.powerHex = powerHex;
		this.resultHex = resultHex;
		this.baseDec = baseDec;
		this.powerDec = powerDec;
		this.resultDec = resultDec;
	}

	private void testPow(BigInt base, BigInt power, BigInt expected) {
		base.pow(power);
		assertEquals(expected, base);
	}

	@Test
	public void powHex() {
		BigInt base = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		BigInt power = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		BigInt expected = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		base.fromHexString(this.baseHex);
		power.fromHexString(this.powerHex);
		expected.fromHexString(this.resultHex);
		this.testPow(base, power, expected);
	}
	
	@Test
	public void powDec() {
		BigInt base = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		BigInt power = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		BigInt expected = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		base.fromDecString(this.baseDec);
		power.fromDecString(this.powerDec);
		expected.fromDecString(this.resultDec);
		this.testPow(base, power, expected);
	}

}
