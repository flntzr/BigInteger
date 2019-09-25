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
public class RsaTests2 {
	private static String testFilePath = "src/test/java/ITSecurity/BigInteger/test_files/RSA-Tests-2.txt";

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
					test.map.get("q"), test.map.get("Q"), test.map.get("e"), test.map.get("E"), test.map.get("d"),
					test.map.get("D"), test.map.get("g"), test.map.get("G"), test.map.get("h"), test.map.get("H"),
					test.map.get("i"), test.map.get("I"), test.map.get("j"), test.map.get("J"), test.map.get("k"),
					test.map.get("K"), test.map.get("l"), test.map.get("L") };
			objects.add(arr);
		}
		return objects;
	}

	public RsaTests2(String title, int size, String p, String decP, String q, String decQ, String e, String decE,
			String d, String decD, String g, String decG, String h, String decH, String i, String decI, String j,
			String decJ, String k, String decK, String l, String decL) {
		this.title = title;
		this.size = size;
		this.hexVals = new String[] { p, q, e, d, g, h, i, j, k, l };
		this.decVals = new String[] { decP, decQ, decE, decD, decG, decH, decI, decJ, decK, decL };
	}

	private void testEncryption(KeyPair keys, BigInt plainText, BigInt cipherText) {
		assertEquals(cipherText, RsaHandler.encrypt(keys.getE(), keys.getN(), plainText));
	}

	private void testDecryption(KeyPair keys, BigInt plainText, BigInt cipherText) {
		assertEquals(plainText, RsaHandler.decrypt(keys.getD(), keys.getN(), cipherText));
	}
	
	@Test 
	public void testEncrypt() {
		BigInt p = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		p.fromDecString(this.decVals[0]);
		BigInt q = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		q.fromDecString(this.decVals[1]);
		BigInt e = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		e.fromDecString(this.decVals[2]);
		BigInt d = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		d.fromDecString(this.decVals[3]);
		BigInt g = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		g.fromDecString(this.decVals[4]);
		BigInt h = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		h.fromDecString(this.decVals[5]);
		BigInt i = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		i.fromDecString(this.decVals[6]);
		BigInt j = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		j.fromDecString(this.decVals[7]);
		BigInt k = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		k.fromDecString(this.decVals[8]);
		BigInt l = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		l.fromDecString(this.decVals[9]);
		KeyPair keys = RsaHandler.generateRsaKeys(p, q, e);
		this.testEncryption(keys, g, h);
		this.testEncryption(keys, i, j);
		this.testEncryption(keys, k, l);
	}
	
	@Test 
	public void testDecrypt() {
		BigInt p = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		p.fromDecString(this.decVals[0]);
		BigInt q = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		q.fromDecString(this.decVals[1]);
		BigInt e = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		e.fromDecString(this.decVals[2]);
		BigInt d = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		d.fromDecString(this.decVals[3]);
		BigInt g = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		g.fromDecString(this.decVals[4]);
		BigInt h = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		h.fromDecString(this.decVals[5]);
		BigInt i = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		i.fromDecString(this.decVals[6]);
		BigInt j = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		j.fromDecString(this.decVals[7]);
		BigInt k = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		k.fromDecString(this.decVals[8]);
		BigInt l = new BigInt(true, 0, BigInt.DEFAULT_BIG_INT_SIZE);
		l.fromDecString(this.decVals[9]);
		KeyPair keys = RsaHandler.generateRsaKeys(p, q, e);
		this.testDecryption(keys, g, h);
		this.testDecryption(keys, i, j);
		this.testDecryption(keys, k, l);
	}

}
