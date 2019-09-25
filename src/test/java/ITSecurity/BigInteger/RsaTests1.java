package ITSecurity.BigInteger;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ITSecurity.BigInteger.RSA.KeyPair;
import ITSecurity.BigInteger.RSA.RsaHandler;

@RunWith(Parameterized.class)
public class RsaTests1 {
	private static String testFilePath = "src/test/java/ITSecurity/BigInteger/test_files/RSA-Tests-1.txt";

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
			Object[] arr = { test.title, Integer.parseInt(test.map.get("s")), test.map.get("p"), test.map.get("P"),
					test.map.get("q"), test.map.get("Q"), test.map.get("e"), test.map.get("E"), test.map.get("f"),
					test.map.get("F"), test.map.get("d"), test.map.get("D") };
			objects.add(arr);
		}
		return objects;
	}

	public RsaTests1(String title, int size, String p, String decP, String q, String decQ, String e, String decE,
			String f, String decF, String d, String decD) {
		this.title = title;
		this.size = size;
		this.hexVals = new String[] { p, q, e, f, d };
		this.decVals = new String[] { decP, decQ, decE, decF, decD };
	}

	@Test
	public void testRsaKeyGeneration() {
		BigInt p = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		p.fromDecString(this.decVals[0]);
		BigInt q = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		q.fromDecString(this.decVals[1]);
		BigInt e = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		e.fromDecString(this.decVals[2]);
		BigInt f = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		f.fromDecString(this.decVals[3]);
		BigInt d = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		d.fromDecString(this.decVals[4]);
		KeyPair keys = RsaHandler.generateRsaKeys(p, q, e);
		assertEquals(f, keys.getPhiN());
		assertEquals(d, keys.getD());
	}

}
