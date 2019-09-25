package ITSecurity.BigInteger.RSA;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import ITSecurity.BigInteger.BigInt;
import ITSecurity.BigInteger.BigIntUtils;
import ITSecurity.BigInteger.Cell2;

public class RsaHandler {

	private static int MIN_KEY_LENGTH_BITS = 500;
	private static int KEY_SIZE_DELTA = 30;
	private static int[] PRIMES_UP_TO_100 = new int[] { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59,
			61, 67, 71, 73, 79, 83, 89, 97 };

	private static BigInt generatePrime(int base, int delta) {
		int rangeMin = 2 << (base - delta);
		int rangeMax = 2 << (base + delta);
		int randomBase = ThreadLocalRandom.current().nextInt(rangeMin, rangeMax + 1);
		BigInt p = BigIntUtils.getRandomOdd(randomBase);
		Cell2 summand = new Cell2(2);
		// TODO: Is this good enough to ensure real primes?
		while (!p.isPrimeMillerRabin(PRIMES_UP_TO_100)) {
			p.addCell2(0, summand);
		}
		return p;
	}

	private static BigInt generateE() {
		return generatePrime(17 + KEY_SIZE_DELTA, KEY_SIZE_DELTA); // at least 2^16+1
	}

	/**
	 * Generates a private and public key pair.
	 * 
	 * @return
	 */
	public static KeyPair generateRsaKeys(int keySize) {
		if (keySize <= MIN_KEY_LENGTH_BITS + KEY_SIZE_DELTA) {
			throw new RuntimeException("Key size must be >" + (MIN_KEY_LENGTH_BITS + KEY_SIZE_DELTA) + " bits long.");
		}
		int base = ThreadLocalRandom.current().nextInt(MIN_KEY_LENGTH_BITS + KEY_SIZE_DELTA, keySize - KEY_SIZE_DELTA);
		while (true) {
			BigInt p = generatePrime(base, KEY_SIZE_DELTA);
			BigInt q = generatePrime(base, KEY_SIZE_DELTA);
			while (p.equals(q)) {
				// ensure p != q
				q = generatePrime(base, KEY_SIZE_DELTA);
			}
			BigInt e = generateE();
			KeyPair keyPair = generateRsaKeys(p, q, e);
			if (keyPair != null) {
				return keyPair;
			}
		}
	}

	public static KeyPair generateRsaKeys(BigInt p, BigInt q, BigInt e) {
		BigInt one = new BigInt(true, 1, BigInt.DEFAULT_BIG_INT_SIZE);

		// phiN = (p-1)*(q-1)
		BigInt phiN = p.clone();
		phiN.sub(one, true);
		BigInt qClone = q.clone();
		qClone.sub(one, true);
		phiN.mul(qClone, true);

		BigInt gcd = e.clone();
		gcd.binGcd(phiN);
		if (!gcd.equals(one)) {
			return null;
		}

		BigInt d = BigIntUtils.inverse(e, phiN);
		BigInt n = p.clone();
		n.mul(q, true);
		KeyPair result = new KeyPair();
		result.setP(p);
		result.setQ(q);
		result.setN(n);
		result.setE(e);
		result.setD(d);
		return result;
	}

	/**
	 * Encrypts the plaintext. Returns the encrypted text.
	 * 
	 * @param plainText
	 * @param e
	 * @param n
	 * @return
	 */
	public static BigInt encrypt(BigInt e, BigInt n, BigInt plainText) {
		// Cipher = m^e mod n
		BigInt m = plainText.clone();
		m.powMod(e, n);
		return m;
	}
	
	public static BigInt decrypt(BigInt d, BigInt n, BigInt cipherText) {
		// Plain = c^d mod n
		BigInt c = cipherText.clone();
		c.powMod(d, n);
		return c;
	}
}
